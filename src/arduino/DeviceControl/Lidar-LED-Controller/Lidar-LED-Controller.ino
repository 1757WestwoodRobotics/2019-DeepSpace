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
#define NUM_LED_UNITS     1
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


// LOX Disabled - If Setup Fails, this flag is use to disable the lox during measurements
boolean lidar_f_disabled = false;


// Array to store distances measured
double obj_distance = -1.0;

// Data Out PINS used for LED and Shutdown PINS for each of the VL53L0X

#define RING_LIGHT_PIN     6    // Ring Light control

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

  // Setup LIDARS
  setupLidar();
}


void loop() {
  // Read  Serial PORT to see if you received a command in JSON
  if (Serial.available()) {

    DynamicJsonDocument read_doc(512);
    DeserializationError error =  deserializeJson(read_doc, Serial);

    // Only if JSON Parse succeds do something or ignore the command
    if (!error) {
      const char *sensor = read_doc["sensor"]; // Look up which sensor
      if (String(sensor) == "ringLight") {
        ledCommands(read_doc["color"]);
      }
      else if(String(sensor) == "Lidar") {
        if(read_doc["read"])
          writeLidar();
      }
      else if(String(sensor) == "Lidar") {
        if(read_doc["restart"])
          setupLidar();
      }
    }
  }
  // keep reading Lidars in the background
  readLidar();
  delay(100);
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


void setupLidar() {

  // Instantiate and configure LIDARS

  // Setup LIDARLite
  frontLidar.begin(0, true); // Set configuration to default and I2C to 400 kHz
  frontLidar.configure(LL_CFG_DEF);
  if (frontLidar.status() != lidarState::Ok) {
    //Serial.println(F("Error: Failed to boot front LidarLite"));
    lidar_f_disabled = true;
  }
  else {
    obj_distance = 0.0;
  }
}


void readLidar() {
  // Read front LIDAR and update global obj_distance.
  if (!lidar_f_disabled) {
    obj_distance = frontLidar.distance();;
  }
  else {
    obj_distance = -1.0;
  }
}


// Sends JSON output to serial port:
void writeLidar() {
  DynamicJsonDocument root(512);
  root["sensor"] = "Lidar";
  root["time"] = millis(); // current Arduino time elapsed since reboot.
  root["front"] = obj_distance;
  
  serializeJson(root, Serial);
  Serial.println(); // Always send a CR at the end so reciever does not block.
  Serial.flush(); // Empty the buffer..
}
