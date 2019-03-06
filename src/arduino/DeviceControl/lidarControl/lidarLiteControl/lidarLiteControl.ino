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

// VL53L0X for Back, Left and Right Sensors
Adafruit_VL53L0X lox_b =  Adafruit_VL53L0X();
Adafruit_VL53L0X lox_l =  Adafruit_VL53L0X();
Adafruit_VL53L0X lox_r =  Adafruit_VL53L0X();

// holds measurement for Lox Lidars
VL53L0X_RangingMeasurementData_t lox_measure;

// Shutdown PINS for each of the VL53L0X
#define S_LOX_B  6
#define S_LOX_L  7
#define S_LOX_R  8

// We have 4 Lidards on the Robot
#define NUM_LIDARS    3

/*
    Object Seen truth table:

    000 - No objects in sight
    001 - Object seen to the Right
    010 - Object seen at the back
    100 - Object seen to the Left
    011 - Object Right & Back
    110 - Object Left & Back
    111 - Object see Laft, Back and Right

*/

#define O_NONE   0b0000
#define O_LEFT   0b0100
#define O_BACK   0b0010
#define O_RIGHT  0b0001

byte object_seen = O_NONE;

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

// I2C Address for LIDARs
#define I2C_BACK  0x31
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
    Serial.println(F("Failed to boot rear VL53L0X"));
    while (1);
  }
  delay(10);

  // activating Left
  digitalWrite(S_LOX_L, HIGH);
  delay(10);

  // init Left
  if (!lox_l.begin(I2C_LEFT)) {
    Serial.println(F("Failed to boot left VL53L0X"));
    while (1);
  }
  delay(10);

  // activating Right
  digitalWrite(S_LOX_R, HIGH);
  delay(10);

  /*
    // init Right
    if (!lox_r.begin(I2C_RIGHT)) {
      Serial.println(F("Failed to boot right VL53L0X"));
      while (1);
    }
    delay(10);
  */
}


void readLox() {

  // Read LOX Radars

  // Front
  lox_b.rangingTest(&lox_measure, false);  // pass in 'true' to get debug data printout!

  object_seen = O_NONE;

  if (lox_measure.RangeStatus != 4) {    // if not out of range
    object_seen |= O_BACK;
  }

  // Left
  lox_l.rangingTest(&lox_measure, false);
  if (lox_measure.RangeStatus != 4) {    // if not out of range
    object_seen |= O_LEFT;
  }

  /*
    // Right
    lox_r.rangingTest(&lox_measure, false);
    if (lox_measure.RangeStatus != 4) {    // if not out of range
    object_seen |= O_RIGHT;
    }
  */
  if (debug) {
    Serial.print(F("Object hit: "));
    Serial.print(object_seen, BIN);
    Serial.println();
  }
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
