/**
   Controls WS2812 leds on a ring light.
   Uses the FastLED library: https://github.com/FastLED/FastLED.
   Tested with Arduino Leonardo++ (16Hertz) from FRC.

   Since there are two ringlights and one array of leds,
   configure the loops properly. 0-23 or 24-48.

   Controls Ultrasound Sensor SR04 from Elegoo
*/
#include <Wire.h>
#include <FastLED.h>          // Includes the FastLED library

// I2C Section
#define DEV_ADDRESS 0X4
#define FREQUENCY 400000  // Maximum I2C frequency

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
#define NUM_LED_UNITS     2
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
double distance;
char  led_command =  RingLEDsOff;
boolean do_led_command = false;
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

  Wire.begin(DEV_ADDRESS);       // join i2c bus with address #4
  //Wire.setClock(FREQUENCY);      // Set the wire frequency fast mode
  Wire.onReceive(receiveEvent);  // register callback to recieve events
  Wire.onRequest(requestEvent);  // register callback to request events
}

void loop() {
  // Read  Serial PORT to see if you received a command
  if (Serial.available()) {
    // read the byte
    led_command = Serial.read();
    do_led_command = true;
    
    switch (led_command) {
      case '0' : digitalWrite (13, LOW);
                 break;
      
      case '1' : digitalWrite(13, HIGH);
                 break;
                 
      default: break ; // do nothing
     
    }    
  }

  if(debug) {
    do_led_command = true;
    led_command = RingLEDsGreen;
  }

  // If we received an LED command event, then process the LED command.
  if (do_led_command) {
    ledCommands(led_command);
    do_led_command = false;
  }
  delay(1000);
}

// Listens to Wire for a request event and then reads the sensor distance value and sends it back on the I2C Wire.
void requestEvent() {
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
  led_command = LED[0];
    
  do_led_command = true;
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
        Serial.print("Invalid command: ");
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
