/**
   Controls WS2812 leds on a ring light.
   Uses the FastLED library: https://github.com/FastLED/FastLED.
   Tested with Arduino Leonardo++ (16Hertz) from FRC.

   Since there are two ringlights and one array of leds,
   configure the loops properly. 0-23 or 24-48.

   Controls Ultrasound Sensor SR04 from Elegoo
*/
#include <Wire.h>
#include <SR04.h>
#include <FastLED.h>          // Includes the FastLED library

// I2C Section
#define DEV_ADDRESS 0X4
#define FREQUENCY 400000  // Maximum I2C frequency

// Device control commands
#define AllLEDsOff  0
#define RingLEDsRed 1
#define RingLEDsGreen 2
#define RingLEDsYellow 3
#define RingLEDsOrange 4
#define RingLEDsBlue 5
#define RingLEDsWhite 6
#define RingLEDsOff 7

#define StripLEDs20vHigh 8
#define StripLEDs20vMed 9
#define StripLEDs20vLow 10
#define StripLEDs20vOff 11

#define StripLEDsRed 12
#define StripLEDsGreen 13
#define StripLEDsYellow 14
#define StripLEDsOrange 15
#define StripLEDsBlue 16
#define StripLEDsWhite 17
#define StripLEDsOff 18

#define RingLEDsFade 19
#define StripLEDsFade 20


#define TRIG_PIN          12    // SR04 Ultrasound Sensor
#define ECHO_PIN          11    // SR04 Ultrasound Sensor
#define RING_LIGHT_PIN     6    // Ring Light control
#define STRIP_LIGHT_PIN1    5    // Strip Light control
#define STRIP_LIGHT_PIN2    4    // Strip Light control
#define STRIP_LIGHT_20V    9    // Strip Light 20V Lighting control PWM pin

// Ultrasonic sensor
SR04 sr04 = SR04(ECHO_PIN, TRIG_PIN);

// LED Light section

#define COLOR_ORDER GRB
#define CHIPSET     WS2812B   // WS2812B has 4 pins/LED, WS2812 has 6 pins/LED
#define NUM_LED_UNITS        3
#define NUM_RING_LEDS        24
#define NUM_STRIP_LEDS1       24
#define NUM_STRIP_LEDS2       24
#define MAX_LEDS             24+24+24

CRGB leds[NUM_LED_UNITS][MAX_LEDS];

// Preset values
const CHSV GREEN(100, 255, 255);
const CHSV RED(0, 255, 255);
const CHSV BLUE(160, 255, 255);
const CHSV YELLOW(64, 255, 255);
const CHSV WHITE(0, 0, 255);
const CHSV ORANGE(32, 255, 255);
const CHSV OFF(0, 0, 0);


// Strip Light Intensity
#define INTENSITY_HIGH 0
#define INTENSITY_MED  85
#define INTENSITY_LOW  170
#define INTENSITY_OFF  255

// Global Variables hold object distance as seen by the ultrasonic sensor, led commands etc.
double distance;
int led_command = AllLEDsOff;
int last_strip_command = StripLEDsWhite;
boolean do_led_command = false;
boolean debug = false;

void setup() {

  // set up console baud rate.
  Serial.begin(9600);

  // Set up LED Control PIN
  pinMode (RING_LIGHT_PIN, OUTPUT);
  pinMode (STRIP_LIGHT_PIN1, OUTPUT);
   pinMode (STRIP_LIGHT_PIN2, OUTPUT);
  pinMode (STRIP_LIGHT_20V, OUTPUT);

  FastLED.delay(3000); // Sanity delay
  FastLED.addLeds<CHIPSET, RING_LIGHT_PIN, COLOR_ORDER>(leds[0], NUM_RING_LEDS); // Initializes Ring leds
  FastLED.addLeds<CHIPSET, STRIP_LIGHT_PIN1, COLOR_ORDER>(leds[1], NUM_STRIP_LEDS1); // Initializes Strip leds
  FastLED.addLeds<CHIPSET, STRIP_LIGHT_PIN2, COLOR_ORDER>(leds[2], NUM_STRIP_LEDS2);
  // Turn off all LEDs
  ledCommands(AllLEDsOff);

  // setup Ulrasonitsensor pins
  pinMode(TRIG_PIN, OUTPUT);     // Sets the trigPin as an Output
  pinMode(ECHO_PIN, INPUT);      // Sets the echoPin as an Input

  Wire.begin(DEV_ADDRESS);       // join i2c bus with address #4
  Wire.setClock(FREQUENCY);      // Set the wire frequency fast mode
  Wire.onReceive(receiveEvent);  // register callback to recieve events
  Wire.onRequest(requestEvent);  // register callback to request events
}

void loop() {

  distance = readUltrasonicSensor(); // Read the ultrsound sensor to see any objects nearby and store in global variable.

  // if disstance is 0.0 send back -1; if dist is > 0 and <= 30 we have an object proably a cube.
  if ((distance > 0) && (distance <= 30)) {
    do_led_command = StripLEDsOrange;
  }
  else {
     // do_led_command = StripLEDsGreen;
   do_led_command = last_strip_command;
  }

  if(debug) {
     do_led_command = StripLEDsGreen;
  }

  // If we received an LED command event, then process the LED command.
  if (do_led_command) {
    ledCommands(led_command);
    do_led_command = false;
  }
  // LED Test section.
  if (debug) {
    // Test Ring Light Leds
    //ledCommands(RingLEDsGreen);

    // Test Strip Lights 20V
   // analogWrite(STRIP_LIGHT_20V, INTENSITY_HIGH);
   // delay(1000);
   // analogWrite(STRIP_LIGHT_20V, INTENSITY_MED);
   // delay(1000);
   // analogWrite(STRIP_LIGHT_20V, INTENSITY_LOW);
  }

  delay(100);
}

// Listens to Wire for a request event and then reads the sensor distance value and sends it back on the I2C Wire.
void requestEvent() {
  String data;

  if (debug) {
    Serial.print("Distance = ");
    Serial.println(distance);
  }
  data = String(distance, 2);

  // Write to the wire.
  Wire.write(data.c_str());
}

// Function to read ultrasonic sensor value to measure distance in cm.
double readUltrasonicSensor() {

  double dist = sr04.Distance(); // Distance read is in cm.

  if (!dist) {
    dist = -1;
  }

  if (debug) {
   // Serial.print(dist);
   // Serial.println(" - Cms");
  }
  return dist;
}


// Wire callback will be called when event is recieved on the I2C BUS
void receiveEvent(int howMany)
{
  String LED = "";
  int bytes_to_read = howMany;

  if (debug) {
    Serial.print("Received - ");
    Serial.println(howMany);
  }

  while ( bytes_to_read > 0 )
  {
    char n = (char)Wire.read(); // Roborio is sending character commands
    LED += n;
    bytes_to_read --; // decrement byte counter
  }
  if (debug) {
    Serial.print("Value = ");
    Serial.println(LED.toInt());
  }
  // Save the command and set the do_led_command flag to true and get out of the interrupt
  led_command = LED.toInt();
  
  // Save last Strip LED Command
  if ((led_command >= StripLEDsRed) && (led_command <= StripLEDsOff))
    last_strip_command = led_command;
    
  do_led_command = true;
}

// LED Control Section

void ledCommands(int cmd)
{
  if (debug) {
    Serial.print("Led Command -");
    Serial.println(cmd);
  }
  switch (cmd) {
    case AllLEDsOff:
      setRingLEDsColor(OFF);
      setStripLEDsColor(OFF);
      analogWrite(STRIP_LIGHT_20V, INTENSITY_OFF);
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

    case RingLEDsOff:
      setRingLEDsColor(OFF);
      break;

    case StripLEDsRed:
      setStripLEDsColor(RED);
      break;

    case StripLEDsGreen:
      setStripLEDsColor(GREEN);
      break;

    case StripLEDsOrange:
      setStripLEDsColor(ORANGE);
      break;

    case StripLEDsYellow:
      setStripLEDsColor(YELLOW);
      break;

    case StripLEDsBlue:
      setStripLEDsColor(BLUE);
      break;

    case StripLEDsWhite:
      setStripLEDsColor(WHITE);
      break;

    case StripLEDsOff:
      setStripLEDsColor(OFF);
      break;

    case StripLEDs20vHigh:
      analogWrite(STRIP_LIGHT_20V, INTENSITY_HIGH);
      break;

    case StripLEDs20vMed:
      analogWrite(STRIP_LIGHT_20V, INTENSITY_MED);
      break;

    case StripLEDs20vLow:
      analogWrite(STRIP_LIGHT_20V, INTENSITY_LOW);
      break;

    case StripLEDs20vOff:
      analogWrite(STRIP_LIGHT_20V, INTENSITY_OFF);
      break;

    case RingLEDsFade: // Fade String LEDs
      break;

    case StripLEDsFade: // Fade strip LEDs
      fadeStripLEDs();
      break;

    default:
      // Do nothing for invalid commands
      if (debug) {
        Serial.print("Invalid command: ");
        Serial.println(cmd);
      }
      break;

  }
}


// All LEDs display functions

void updateLEDs() {
  FastLED.show();
  FastLED.delay(30);
}

// Control Ring LED color
void setRingLEDsColor(CHSV color) {
  fill_solid(leds[0], NUM_RING_LEDS, color);
  updateLEDs();
}

// Control Strip LED Color
void setStripLEDsColor(CHSV color) {
  fill_solid(leds[1], NUM_STRIP_LEDS1, color);
  fill_solid(leds[2], NUM_STRIP_LEDS2, color);
  updateLEDs();
}

// Function to fade Strip LEDs
void fadeStripLEDs() {
  
}


