
#include <ArduinoJson.h>
#include <CapacitiveSensor.h>

#define POT_PIN         A0    // Analog pot readings via this pin.
#define MIN_POT_VALUE   0   // Minimum analog read value read from A0
#define MAX_POT_VALUE   1023 // Maximum analog read value read from A0
#define MIN_ANGLE       0
#define MAX_ANGLE       180
#define POT_SPEED       150 // How fast to move the POT
#define POT_FWD_PIN     9
#define POT_REV_PIN     10
#define TOUCH_THRESHOLD 200  // Will have to tune this appropriately

// Global Variables hold object distance as seen by the ultrasonic sensor, led commands etc.
boolean debug = true;
boolean stp = false;

/*
    JSON Section for creating JSON output to Serial.
*/

const size_t capacity = JSON_ARRAY_SIZE(4) + JSON_OBJECT_SIZE(3) + 90;
DynamicJsonBuffer readBuffer(capacity);
DynamicJsonBuffer writeBuffer(capacity);


// Testing Capacitive Touch

CapacitiveSensor   cs_4_2 = CapacitiveSensor(4, 2);


void setup() {

  cs_4_2.set_CS_AutocaL_Millis(0xFFFFFFFF);

  // set up console baud rate.
  Serial.begin(115200);

  // Built in LED pin 13 as output
  pinMode (LED_BUILTIN, OUTPUT);

  // Setup POT Motor PINS
  pinMode(POT_FWD_PIN, OUTPUT);
  pinMode(POT_REV_PIN, OUTPUT);

  // wait until serial port opens for native USB devices
  while (! Serial) {
    delay(1);
  }
}


void loop() {

  // Read  Serial PORT to see if you received a command
  if (Serial.available()) {
    JsonObject& cmdObj = readBuffer.parse(Serial);

    // Only if JSON Parse succeds do something or ignore the command
    if (cmdObj.success()) {
      const char *sensor = cmdObj["sensor"];

      if (String(sensor) == "sliderPot") {
        movePot(cmdObj["position"]);
      }
    }
  }
  else {
    JsonObject &writeObj = writeBuffer.createObject();

    readPot(writeObj);
    writeSerial(writeObj);
    writeBuffer.clear();
  }
  stp = touch();
  if (!stp) {
    movePot(90);
    stp = true;
  }
  delay(1000);
}


// Chekc if the slider was touched and return true if touched
boolean touch() {
  long touch_val =  cs_4_2.capacitiveSensor(30);

  return (touch_val > TOUCH_THRESHOLD);
}

// Port Command Processor for moving Pot by a certain position.
void movePot(int angle)
{
  // Check for Bounds
  if (angle > MAX_ANGLE)
    angle = MAX_ANGLE;
  else  {
    if (angle < MIN_ANGLE)
      angle = MIN_ANGLE;
  }
  // Translate Angle to Resistnace Values
  int new_pos = map(angle, MIN_ANGLE, MAX_ANGLE, MIN_POT_VALUE, MAX_POT_VALUE);

  // Check which direction to move
  int cur_pos = analogRead(POT_PIN);

  if (debug) {
    Serial.print("Current pos =");
    Serial.print (cur_pos);
    Serial.print("  New pos =");
    Serial.print (new_pos);
    Serial.println();
  }
  // Move the motor if the postions is not the same
  while (new_pos != cur_pos) {
    // Power the Motor PINS
    // turn Motor # 2 in one direction
    if (new_pos > cur_pos) {
      analogWrite(POT_FWD_PIN, POT_SPEED);
      analogWrite(POT_REV_PIN, 0);
    }
    else {
      if ( new_pos < cur_pos) {
        analogWrite(POT_FWD_PIN, 0);
        analogWrite(POT_REV_PIN, POT_SPEED);
      }
    }
    // read the currtent postion
    cur_pos = analogRead(POT_PIN);
    if (debug) {
      Serial.print("Current pos =");
      Serial.print (cur_pos);
      Serial.print("  New pos =");
      Serial.print (new_pos);
      Serial.println();
    }
  }
  // Stop the Pot Motor
  analogWrite(POT_FWD_PIN, 0);
  analogWrite(POT_REV_PIN, 0);
  delay(100);
}


// Retunr a reading from 0 toi 180 degrees for driving Motor
int readPot(JsonObject& root)
{
  int val = analogRead(POT_PIN);

  root["sensor"] = "sliderPot";
  root["time"] = millis();
  root["touch"] = touch();  // Will toggle with tocuh of the slider
  root["degrees"] = map(val, MIN_POT_VALUE, MAX_POT_VALUE, MIN_ANGLE, MAX_ANGLE); // Postition will be in degrees

  return val;
}

// Sends JSON output to serial port: For testing only
void writeSerial(JsonObject& root) {
  root.printTo(Serial);
  Serial.println(); // Always send a CR at the end so reciever does not block.
}
