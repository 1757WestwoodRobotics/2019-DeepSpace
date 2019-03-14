
#include <ArduinoJson.h>  // Make sure you use ArduinoJson 6 Libs.
#include <CapacitiveSensor.h>
#include <Joystick.h>




#define POT_R_PIN      A0    // Analog pot readings via this pin.
#define POT_M_PIN      A1    // Analog pot readings via this pin.

#define MIN_POT_VALUE   0   // Minimum analog read value read from POT
#define MAX_POT_VALUE   1023 // Maximum analog read value read from POT
#define POT_SPEED       150 // How fast to move the POT
#define POT_FWD_PIN     10
#define POT_REV_PIN     11
#define TOUCH_PIN_WRITE 12
#define TOUCH_PIN_READ  13
#define TOUCH_THRESHOLD 200  // Will have to tune this appropriately

// Various Joy Stick Buttons for the Driver station and their Ardunio Pin outs
#define MIN_RANGE -512
#define MAX_RANGE  512
#define DS_BUTTON_COUNT 12   // We have 7 button and 5 toggle switches on the Driver Station

// Create Joystick
Joystick_ Joystick(JOYSTICK_DEFAULT_REPORT_ID,
                   JOYSTICK_TYPE_JOYSTICK,
                   DS_BUTTON_COUNT,
                   0,  // No HAT switch
                   true,  // X- Axis only
                   false,
                   false,
                   false,
                   false,
                   false,
                   false,
                   false,
                   false,
                   false,
                   false);

// Arduino Pins the buttons are connected to
const int buttons[DS_BUTTON_COUNT] = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, A4, A5};

// Global Variables hold object distance as seen by the ultrasonic sensor, led commands etc.
boolean debug = true;
boolean touched = true;
const bool autoSendMode = true;


// Testing Capacitive Touch

CapacitiveSensor   cs = CapacitiveSensor(TOUCH_PIN_WRITE, TOUCH_PIN_READ);


void setup() {

  cs.set_CS_AutocaL_Millis(0xFFFFFFFF);

  // set up console baud rate.
  Serial.begin(115200);
  // wait until serial port opens for native USB devices
  while (! Serial) {
    delay(1);
  }

  // Built in LED pin 13 as output
  pinMode (LED_BUILTIN, OUTPUT);

  // Configure JS buttons

  // Setup POT Motor PINS
  pinMode(POT_FWD_PIN, OUTPUT);
  pinMode(POT_REV_PIN, OUTPUT);

  //  Configure All buttons. Since we run out of Digital Pins we will use 2 Analog pins to serve as digital pins.
  for (int pin = 0; pin < 10; pin++)
    pinMode(pin, OUTPUT);

  // Joystick initialization
  // Set Range Values
  Joystick.setXAxisRange(MIN_RANGE, MAX_RANGE);
  Joystick.begin(autoSendMode);
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
        if (!touch()) { // Ignore movePot if slider is being touched.
          int position = read_doc["position"];

          if (debug) {
            Serial.print("Recieved poistion :");
            Serial.print (position);
            Serial.println();
          }
          movePot(position);
        }
      }
    }
  }
  if (touch()) {
    // as long as slider is being tocuhed and moved send position back to robot.
    DynamicJsonDocument write_doc(512);

    write_doc["sensor"] = "sliderPot";
    write_doc["position"] = map(analogRead(POT_R_PIN), MIN_POT_VALUE, MAX_POT_VALUE, MIN_RANGE, MAX_RANGE);
    write_doc["manual_control"] = touch();
    writeSerial(write_doc);
  }

  // Process Joystick
  processAxis();
  processButtons();
  delay(100);
}


// Chekc if the slider was touched and return true if touched
boolean touch() {

  long touch_val =  cs.capacitiveSensor(30);
  boolean touched = (touch_val > TOUCH_THRESHOLD);

  if (touched)
    digitalWrite(LED_BUILTIN, HIGH);
  else
    digitalWrite(LED_BUILTIN, LOW);

  return (touched);


}

// Servo control of the POT
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
  int cur_pos = map(analogRead(POT_M_PIN), MIN_POT_VALUE, MAX_POT_VALUE, MIN_RANGE, MAX_RANGE);

  if (debug) {
    Serial.print("New Pos = ");
    Serial.print(new_pos);
    Serial.print(" Current Pos = ");
    Serial.print(cur_pos);
    Serial.println();
  }
  // Move the motor if the postions is not the same

  int breakloop = 0; // this is to braak out of the loop in case we never able to reach the intended postion
  while ((new_pos != cur_pos) && (breakloop < MAX_POT_VALUE)) {
    if (debug) {
      Serial.print("New Pos = ");
      Serial.print(new_pos);
      Serial.print(" Current Pos = ");
      Serial.print(cur_pos);
      Serial.println();
    }
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
    cur_pos = map(analogRead(POT_M_PIN), MIN_POT_VALUE, MAX_POT_VALUE, MIN_RANGE, MAX_RANGE);
    breakloop++;
  }
  // Stop the Pot Motor
  analogWrite(POT_FWD_PIN, 0);
  analogWrite(POT_REV_PIN, 0);

}

// Sends JSON output to serial port: For testing only
void writeSerial(DynamicJsonDocument root) {
  serializeJson(root, Serial);
  Serial.println(); // Always send a CR at the end so reciever does not block.
  Serial.flush(); // Empty the buffer..
}


// Joystick Functions

// Read the pot value for Joystick X Axis
void processAxis()
{
  int val = map(analogRead(POT_R_PIN), MIN_POT_VALUE, MAX_POT_VALUE, MIN_RANGE, MAX_RANGE);
  Joystick.setXAxis(val);
  if (debug) {
    Serial.print("X Axis value - ");
    Serial.print(val);
    Serial.println();
  }
}


void processButtons()
{
  boolean pressed = false;

  for (int i = 0; i < DS_BUTTON_COUNT; i++) {
    if (buttons[i] == A4 || buttons[i] == A5) {
      // process Analog pins differentl
      pressed = analogRead(buttons[i]) / 1023;  // 1K = 5V max ADC value
    }
    else {
      pressed = digitalRead(buttons[i]);
    }
    // We are processing digial I/O pins
    if (debug) {
      Serial.print("Button [");
      Serial.print(i);
      Serial.print("] - ");
      Serial.print(pressed);
      Serial.println();
    }
    if (pressed) {
      Joystick.pressButton(i);
    }
    else {
      Joystick.releaseButton(i);
    }
  }

}
