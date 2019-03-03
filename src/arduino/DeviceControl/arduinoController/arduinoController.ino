/**

   LIDARS:
   ------
   Reads LIDARS fron, back laft and rights
   Front - Garmin LIDAR-Lite V3
   Back, Left and Right - VL53L0X

  Front Connections:
  LIDAR-Lite 5 Vdc (red) to Arduino 5v
  LIDAR-Lite I2C SCL (green) to Arduino SCL
  LIDAR-Lite I2C SDA (blue) to Arduino SDA
  LIDAR-Lite Ground (black) to Arduino GND

  Back, Left & Right Connections:
  LOX 5 VDC to Arduino 5v
  LOX GND to Arduino GND
  LOX I2C SCL to Arduino SCL
  LOX I2C SDA to Ardunio SDA

   Ring LEDs:
   ----------
   Controls WS2812 leds on a ring light.
   Uses the FastLED library: https://github.com/FastLED/FastLED.
   Tested with Arduino Leonardo++ (16Hertz) from FRC.

  IMPORTANT ***** Make sure all are on the Same GND  Arduino, Robot, LIDAR Lite and LOX ****

*/
#include <Wire.h>
#include <FastLED.h>          // Includes the FastLED library
#include <LIDARLite.h>
#include <ArduinoJson.h>
#include "Adafruit_VL53L0X.h"


// Device control commands
#define RingLEDsOff       0
#define RingLEDsRed       1
#define RingLEDsGreen     2
#define RingLEDsYellow    3
#define RingLEDsOrange    4
#define RingLEDsBlue      5
#define RingLEDsWhite     6




// LED Light section
#define COLOR_ORDER       GRB
#define CHIPSET           WS2812B   // WS2812B has 4 pins/LED, WS2812 has 6 pins/LED
#define NUM_LED_UNITS     3
#define NUM_RING_LEDS     24
#define MAX_LEDS          NUM_LED_UNITS *  NUM_RING_LEDS

CRGB leds[MAX_LEDS];

// Preset values
const CHSV GREEN(100, 255, 255);
const CHSV RED(0, 255, 255);
const CHSV BLUE(160, 255, 255);
const CHSV YELLOW(64, 255, 255);
const CHSV WHITE(0, 0, 255);
const CHSV ORANGE(32, 255, 255);
const CHSV OFF(0, 0, 0);


// Global Variables hold object distance as seen by the ultrasonic sensor, led commands etc.

boolean debug = true;

// GARMIN Lidar Lite is the Front Lidar
LIDARLite frontLidar;

// VL53L0X for Back
Adafruit_VL53L0X lox_b =  Adafruit_VL53L0X();
Adafruit_VL53L0X lox_l =  Adafruit_VL53L0X();
Adafruit_VL53L0X lox_r =  Adafruit_VL53L0X();

// LOX Disabled - If Setup Fails, this flag is use to disable the lox during measurements
boolean lidar_f_disabled = false;
boolean lidar_b_disabled = false;
boolean lidar_l_disabled = false;
boolean lidar_r_disabled = false;

// holds measurement for Lox Lidars
VL53L0X_RangingMeasurementData_t lox_measure;


// We have 4 Lidards on the Robot
#define NUM_LIDARS    4

// Array to store distances measured
double distances[NUM_LIDARS];

// Data Out PINS used for LED and Shutdown PINS for each of the VL53L0X

#define RING_LIGHT_PIN     6    // Ring Light control
#define S_LOX_B            7    // LOX Back
#define S_LOX_L            8    // LOX Left
#define S_LOX_R            9    // LOX Right

// I2C Address for LIDARs
#define I2C_BACK  0x31
#define I2C_LEFT  0x32
#define I2C_RIGHT 0x33

/*
        Lidar Lite Configuration
        0: Default mode, balanced performance.
        1: Short range, high speed. Uses 0x1d maximum acquisition count.
        2: Default range, higher speed short range. Turns on quick termination
        detection for faster measurements at short range (with decreased accuracy)
        3: Maximum range. Uses 0xff maximum acquisition count.
        4: High sensitivity detection. Overrides default valid measurement detection
        algorithm, and uses a threshold value for high sensitivity and noise.
        5: Low sensitivity detection. Overrides default valid measurement detection
        algorithm, and uses a threshold value for low sensitivity and noise.
        lidarliteAddress: Default 0x62. Fill in new address here if changed. See
        operating manual for instructions.
*/

#define LL_CFG_DEF               0
#define LL_CFG_SR_HS             1
#define LL_CFG_DR_HS             2
#define LL_CGG_MAX_RANGE         3
#define LL_CFG_HIGH_SENS         4
#define LL_CFG_LOW_SENS          5


/*
    JSON Section for creating JSON output to Serial.
*/

const size_t capacity = JSON_ARRAY_SIZE(4) + JSON_OBJECT_SIZE(3) + 90;
DynamicJsonBuffer jsonBuffer(capacity);



void setup() {

  // set up console baud rate.
  Serial.begin(115200);

  
  // Built in LED pin 13 as output
  pinMode (LED_BUILTIN, OUTPUT);

  // Set up LED Control PIN
  pinMode (RING_LIGHT_PIN, OUTPUT);

  FastLED.delay(3000); // Sanity delay
  FastLED.addLeds<CHIPSET, RING_LIGHT_PIN, COLOR_ORDER>(leds, MAX_LEDS); // Initializes Ring leds

  if (debug) {
    ledCommands(RingLEDsGreen);
  }

  // wait until serial port opens for native USB devices
  while (! Serial) {
    delay(1);
  }

  // Setup LIDARS
  setupLidars();
}


void loop() {
  // Read  Serial PORT to see if you received a command in JSON
  if (Serial.available()) {

    JsonObject& cmdObj = jsonBuffer.parse(Serial);

    // Only if JSON Parse succeds do something or ignore the command
    if (cmdObj.success()) {
      const char *sensor = cmdObj["sensor"]; // Look up which sensor
      if (String(sensor) == "ringLight") {
        ledCommands(cmdObj["color"]);
      }
      else if (String(sensor) == "Lidar") {
        // Process Command for LIDARs
        writeLidar();
      }
    }
  }
  // keep reading Lidars in the background
  readLidars();
  jsonBuffer.clear(); // free up buffer space.
  delay(1);
}




/*
   LED Control Section

*/
void ledCommands(int cmd)
{
  switch (cmd) {
    case RingLEDsOff:
      setRingLEDsColor(OFF);
      digitalWrite(LED_BUILTIN, LOW);
      break;

    case RingLEDsRed:
      setRingLEDsColor(RED);
      digitalWrite(LED_BUILTIN, HIGH);
      break;

    case RingLEDsGreen:
      setRingLEDsColor(GREEN);
      digitalWrite(LED_BUILTIN, HIGH);
      break;

    case RingLEDsOrange:
      setRingLEDsColor(ORANGE);
      digitalWrite(LED_BUILTIN, HIGH);
      break;

    case RingLEDsYellow:
      setRingLEDsColor(YELLOW);
      digitalWrite(LED_BUILTIN, HIGH);
      break;

    case RingLEDsBlue:
      setRingLEDsColor(BLUE);
      digitalWrite(LED_BUILTIN, HIGH);
      break;

    case RingLEDsWhite:
      setRingLEDsColor(WHITE);
      digitalWrite(LED_BUILTIN, HIGH);
      break;

    default:
      // Do nothing for invalid commands
      if (debug) {
        Serial.print("Invalid LED command: ");
        Serial.println(cmd);
      }
      digitalWrite(LED_BUILTIN, LOW);
      break;

  }
}


// Control Ring LED color
void setRingLEDsColor(CHSV color) {
  fill_solid(leds, MAX_LEDS, color);
  FastLED.show();
  FastLED.delay(30);
}

/*
    LIDAR Control Section
*/


void setupLidars() {

  // Instantiate and configure LIDARS

  // Setup LIDARLite
  frontLidar.begin(0, true); // Set configuration to default and I2C to 400 kHz
  frontLidar.configure(LL_CFG_DEF);
  if (frontLidar.status() != lidarState::Ok) {
    Serial.println(F("Error: Failed to boot front LidarLite"));
    lidar_f_disabled = true;
  }

  // Setup LOX

  // all reset
  digitalWrite(S_LOX_B, LOW);
  digitalWrite(S_LOX_L, LOW);
  digitalWrite(S_LOX_R, LOW);
  delay(10);
  // all unreset
  digitalWrite(S_LOX_B, HIGH);
  digitalWrite(S_LOX_L, HIGH);
  digitalWrite(S_LOX_R, HIGH);
  delay(10);


  // activatie one LOX at a time
  digitalWrite(S_LOX_B, HIGH);
  digitalWrite(S_LOX_L, LOW);
  digitalWrite(S_LOX_R, LOW);

  // init Back LIDAR
  if (!lox_b.begin()) {
    Serial.println(F("Error: Failed to boot rear VL53L0X"));
    lidar_b_disabled = true;
  }
  delay(10);

  // activating Left
  digitalWrite(S_LOX_L, HIGH);
  delay(10);

  // init Left
  if (!lox_l.begin(I2C_LEFT)) {
    Serial.println(F("Error: Failed to boot left VL53L0X"));
    lidar_l_disabled = true;
  }
  delay(10);

  // activating Right
  digitalWrite(S_LOX_R, HIGH);
  delay(10);

  // init Right
  if (!lox_r.begin(I2C_RIGHT)) {
    Serial.println(F("Error: Failed to boot right VL53L0X"));
    lidar_r_disabled = true;
  }
  delay(10);
}


void readLidarLite() {
  // Read front LIDAR
  if (!lidar_f_disabled) {
    distances[0] = frontLidar.distance();;
  }
  else {
    distances[0] = 0;
  }

}

void readLox() {

  // Read LOX Radars

  // Back
  if (!lidar_b_disabled) {
    lox_b.rangingTest(&lox_measure, false);  // pass in 'true' to get debug data printout!
    if (lox_measure.RangeStatus != 4) {    // if not out of range
      distances[1] = lox_measure.RangeMilliMeter;
    } else {
      distances[1] = -1; // No objects in range
    }
  }

  // Left
  if (!lidar_l_disabled) {
    lox_l.rangingTest(&lox_measure, false);  // pass in 'true' to get debug data printout!
    if (lox_measure.RangeStatus != 4) {    // if not out of range
      distances[2] = lox_measure.RangeMilliMeter;
    } else {
      distances[2] = -1; // No objects in range
    }
  }

  // Right
  if (!lidar_r_disabled) {
    lox_r.rangingTest(&lox_measure, false);  // pass in 'true' to get debug data printout!
    if (lox_measure.RangeStatus != 4) {    // if not out of range
      distances[3] = lox_measure.RangeMilliMeter;
    } else {
      distances[3] = -1; // No objects in range
    }
  }

}

// Read all lidars Fron, Back, Lef & Right

void readLidars() {
  readLidarLite();
  readLox();
}


// Sends JSON output to serial port:
void writeLidar() {
  JsonObject& root = jsonBuffer.createObject();
  root["sensor"] = "lidar";
  root["time"] = millis(); // current Arduino time elapsed since reboot.

  JsonArray& objects = root.createNestedArray("objects");
  objects.add(distances[0]);
  objects.add(distances[1]);
  objects.add(distances[2]);
  objects.add(distances[3]);
  root.printTo(Serial);
  Serial.println(); // Always send a CR at the end so reciever does not block.
  jsonBuffer.clear()// Free up any objectes we have created
  
  
}
