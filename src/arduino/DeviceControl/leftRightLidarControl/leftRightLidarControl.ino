/**
  Reads LIDARS  back left and right
  Back, Left and Right - VL53L0X

  Back, Left & Right Connections:
  LOX 5 VDC to Arduino 5v
  LOX GND to Arduino GND
  LOX I2C SCL to Arduino SCL
  LOX I2C SDA to Ardunio SDA

  IMPORTANT ***** Make sure all are on the Same GND  Arduino, Robot, LIDAR Lite and LOX ****

*/
#include "Adafruit_VL53L0X.h"


// Global Variables hold object distance as seen by the ultrasonic sensor, led commands etc.

boolean debug = true;

// VL53L0X for Left and Right Sensors
Adafruit_VL53L0X lox_l =  Adafruit_VL53L0X();
Adafruit_VL53L0X lox_r =  Adafruit_VL53L0X();

// holds measurement for Lox Lidars
VL53L0X_RangingMeasurementData_t lox_measure;

// Shutdown PINS for each of the VL53L0X
#define S_LOX_L  7
#define S_LOX_R  8

// We have 4 Lidards on the Robot
#define NUM_LIDARS    2

// Array to store distances measured
int distances[NUM_LIDARS];

// I2C Address for LIDARs
#define I2C_LEFT  0x32
#define I2C_RIGHT 0x33



void setup() {

  // set up console baud rate.
  Serial.begin(115200);

  // wait until serial port opens for native USB devices
  while (! Serial) {
    delay(1);
  }

  // Set LOX

  // all reset
  digitalWrite(S_LOX_L, LOW);
  digitalWrite(S_LOX_R, LOW);
  delay(10);

  // all unreset
  digitalWrite(S_LOX_L, HIGH);
  digitalWrite(S_LOX_R, HIGH);
  delay(10);


  // activatie one LOX at a time
  digitalWrite(S_LOX_L, HIGH);
  digitalWrite(S_LOX_R, LOW);

  // init Left
  if (!lox_l.begin(I2C_LEFT)) {
    Serial.println(F("Failed to boot left VL53L0X"));
    while (1);
  }
  delay(10);

  // activating Right
  digitalWrite(S_LOX_R, HIGH);
  delay(10);

  // init Right
  if (!lox_r.begin(I2C_RIGHT)) {
    Serial.println(F("Failed to boot right VL53L0X"));
    while (1);
  }
  delay(10);

}


void readLox() {

  // Read LOX Radars

  // Left
  lox_l.rangingTest(&lox_measure, false);  // pass in 'true' to get debug data printout!

  Serial.print("LOX (Back): ");
  if (lox_measure.RangeStatus != 4) {    // if not out of range
    distances[0] = lox_measure.RangeMilliMeter;
    Serial.print(lox_measure.RangeMilliMeter);
  } else {
    Serial.print("Out of range");
    distances[0] = -1; // No objects in range
  }
  Serial.println();

  // Right
  lox_r.rangingTest(&lox_measure, false);  // pass in 'true' to get debug data printout!

  Serial.print("LOX (Back): ");
  if (lox_measure.RangeStatus != 4) {    // if not out of range
    distances[1] = lox_measure.RangeMilliMeter;
    Serial.print(lox_measure.RangeMilliMeter);
  } else {
    Serial.print("Out of range");
    distances[1] = -1; // No objects in range
  }
  Serial.println();
}

void loop() {
  // Read  Serial PORT to see if you received a command
  if (Serial.available()) {
    switch (Serial.read()) {
      case '0' : digitalWrite (13, LOW);
        break;

      case '1' : digitalWrite(13, HIGH);
        readLox();
        break;

      default: break ; // do nothing

    }
  }
  if (debug) {
    readLox();
  }
  delay(1000);
}
