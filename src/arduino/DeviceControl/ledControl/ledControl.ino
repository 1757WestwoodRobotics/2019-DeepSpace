/**
   Controls WS2812 leds on a ring light.
   Uses the FastLED library: https://github.com/FastLED/FastLED.
   Tested with Arduino Leonardo++ (16Hertz) from FRC.
**/
#include <FastLED.h>          // Includes the FastLED library

// Device control commands
#define RingLEDsOff       '0'
#define RingLEDsRed       '1'
#define RingLEDsGreen     '2'
#define RingLEDsYellow    '3'
#define RingLEDsOrange    '4'
#define RingLEDsBlue      '5'
#define RingLEDsWhite     '6'


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

void setup() {

  // set up console baud rate.
  Serial.begin(9600);

  // Set up LED Control PIN
  pinMode (RING_LIGHT_PIN, OUTPUT);

  //For testing
  pinMode(13, OUTPUT);
 
  FastLED.delay(3000); // Sanity delay
  FastLED.addLeds<CHIPSET, RING_LIGHT_PIN, COLOR_ORDER>(leds, MAX_LEDS); // Initializes Ring leds

}

void loop() {
  // Read  Serial PORT to see if you received a command
  if (Serial.available()) {
    // read the byte
    ledCommands(Serial.read());
  }
  else {
    if(debug) {
     ledCommands(RingLEDsGreen);
     debug = false;
    }
  }
  delay(200);
}

// LED Control Section
void ledCommands(char cmd)
{
  if (debug) {
    Serial.print("Led Command -");
    Serial.println(cmd);
  }
  switch (cmd) {
    case RingLEDsOff:
      setRingLEDsColor(OFF);
      break;

    case RingLEDsRed:
      setRingLEDsColor(RED);
      break;

    case RingLEDsGreen:
      setRingLEDsColor(GREEN);
      break;

    case RingLEDsOrange:
      setRingLEDsColor(ORANGE);
      break;

    case RingLEDsYellow:
      setRingLEDsColor(YELLOW);
      break;

    case RingLEDsBlue:
      setRingLEDsColor(BLUE);
      break;

    case RingLEDsWhite:
      setRingLEDsColor(WHITE);
      break;

    default:
      // Do nothing for invalid commands
      if (debug) {
        Serial.print("Invalid LED command: ");
        Serial.println(cmd);
      }
      break;

  }
}


// Control Ring LED color
void setRingLEDsColor(CHSV color) {
  fill_solid(leds, MAX_LEDS, color);
  FastLED.show();
  FastLED.delay(30);
}
