/**
   Controls WS2812 leds on a ring light.
   Uses the FastLED library: https://github.com/FastLED/FastLED.
   Tested with Arduino Leonardo++ (16Hertz) from FRC.
**/
#include <FastLED.h>          // Includes the FastLED library
#include <ArduinoJson.h>

// Device control commands
#define RingLEDsOff       0
#define RingLEDsRed       1
#define RingLEDsGreen     2
#define RingLEDsYellow    3
#define RingLEDsOrange    4
#define RingLEDsBlue      5
#define RingLEDsWhite     6


#define RING_LIGHT_PIN     6    // Ring Light control


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

  //For testing
  pinMode(13, OUTPUT);

  FastLED.delay(3000); // Sanity delay
  FastLED.addLeds<CHIPSET, RING_LIGHT_PIN, COLOR_ORDER>(leds, MAX_LEDS); // Initializes Ring leds

  if (debug) {
    ledCommands(RingLEDsGreen);
  }

  // wait until serial port opens for native USB devices
  while (! Serial) {
    delay(1);
  }
}

void loop() {

  // Read  Serial PORT to see if you received a command
  if (Serial.available()) {
    JsonObject& cmdObj = jsonBuffer.parse(Serial);

    // Only if JSON Parse succeds do something or ignore the command
    if (cmdObj.success()) {
      const char *sensor = cmdObj["sensor"];

      if (String(sensor) == "ringLight") {
        ledCommands(cmdObj["color"]);
      }
      else {
        writeSerial();
      }
    }
    delay(200);
  }
}

// LED Control Section
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

// Sends JSON output to serial port: For testing only
void writeSerial() {
  JsonObject& root = jsonBuffer.createObject();
  root["sensor"] = "lidar";
  root["time"] = millis();

  JsonArray& objects = root.createNestedArray("objects");
  objects.add(0);
  objects.add(0);
  objects.add(0);
  objects.add(0);
  root.printTo(Serial);
  Serial.println(); // Always send a CR at the end so reciever does not block.
}
