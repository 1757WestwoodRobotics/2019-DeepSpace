
#include <ArduinoJson.h>  // Make sure you use ArduinoJson 6 Libs.
#include <CapacitiveSensor.h>

#define POT_PIN         A0    // Analog pot readings via this pin.
#define MIN_POT_VALUE   0   // Minimum analog read value read from A0
#define MAX_POT_VALUE   1023 // Maximum analog read value read from A0
#define MIN_RANGE       0
#define MAX_RANGE       1023
#define POT_SPEED       150 // How fast to move the POT
#define POT_FWD_PIN     9
#define POT_REV_PIN     10
#define TOUCH_PIN_WRITE 3
#define TOUCH_PIN_READ  2
#define TOUCH_THRESHOLD 200  // Will have to tune this appropriately

// Global Variables hold object distance as seen by the ultrasonic sensor, led commands etc.
boolean debug = false;
boolean touched = true;

// Testing Capacitive Touch

CapacitiveSensor   cs = CapacitiveSensor(TOUCH_PIN_WRITE, TOUCH_PIN_READ);


void setup() {

  cs.set_CS_AutocaL_Millis(0xFFFFFFFF);

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
  int how_many = 0;

  // Read  Serial PORT to see if you received a command
  if (how_many = Serial.available()) {

    DynamicJsonDocument read_doc(512);
    DeserializationError error =  deserializeJson(read_doc, Serial);


    // Only if JSON Parse succeds do something or ignore the command
    if (!error) {
      const char *sensor = read_doc["sensor"];

      if (String(sensor) == "sliderPot") {
        movePot(read_doc["position"]);
        //touched = true;
      }
    }
  }
  else {
    if (Serial.availableForWrite()) {
      // Write only if slider is touched else we are in auto mode.
      DynamicJsonDocument write_doc(512);
      write_doc = readPot();

      if (touch()) {
        writeSerial(write_doc);
        touched = true; // previous state
      }
      else if (touched) {
        writeSerial(write_doc);
        touched = false;
      }
    }
  }
  delay(100);
}


// Chekc if the slider was touched and return true if touched
int touch() {

  long touch_val =  cs.capacitiveSensor(30);
  boolean touched = (touch_val > TOUCH_THRESHOLD);

  if (touched)
    digitalWrite(LED_BUILTIN, HIGH);
  else
    digitalWrite(LED_BUILTIN, LOW);

  return (touched);


}

// Port Command Processor for moving Pot by a certain position.
void movePot(int pos)
{
  // Check for Bounds
  if (pos >= MAX_RANGE)
    pos = MAX_RANGE;
  else  {
    if (pos <= MIN_RANGE)
      pos = MIN_RANGE;
  }
  // Translate Angle to Resistnace Values
  int new_pos = pos;

  // Check which direction to move
  int cur_pos = map(analogRead(POT_PIN), MIN_POT_VALUE, MAX_POT_VALUE, MIN_RANGE, MAX_RANGE);

  // Move the motor if the postions is not the same
  while (new_pos != cur_pos) {
    // Power the Motor PINS
    // turn Motor # 1 in one direction
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
    cur_pos = map(analogRead(POT_PIN), MIN_POT_VALUE, MAX_POT_VALUE, MIN_RANGE, MAX_RANGE);
  }
  // Stop the Pot Motor
  analogWrite(POT_FWD_PIN, 0);
  analogWrite(POT_REV_PIN, 0);

}


// Retunr a reading from 0 toi 180 degrees for driving Motor
DynamicJsonDocument readPot()
{
  DynamicJsonDocument root(512);

  int val = analogRead(POT_PIN);

  root["sensor"] = "sliderPot";
  root["time"] = millis();
  root["manual"] = touch();  // Will toggle with tocuh of the slider
  root["position"] = map(val, MIN_POT_VALUE, MAX_POT_VALUE, MIN_RANGE, MAX_RANGE); // Postition will be in degrees

  return root;
}

// Sends JSON output to serial port: For testing only
void writeSerial(DynamicJsonDocument root) {
  serializeJson(root, Serial);
  Serial.println(); // Always send a CR at the end so reciever does not block.
  Serial.flush(); // Empty the buffer..
}
