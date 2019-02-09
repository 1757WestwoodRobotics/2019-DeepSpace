/**
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

  IMPORTANT ***** Make sure all are on the Same GND  Arduino, Robot, LIDAR Lite and LOX ****

*/
#include <Wire.h>
#include <LIDARLite.h>
#include "Adafruit_VL53L0X.h"


// Global Variables hold object distance as seen by the ultrasonic sensor, led commands etc.

boolean debug = true;

#define NUM_LOXS 1 // Number of LOX used Back, Left & Right
#define NUM_LIDAR_LITES 1 // Front LIDAR

#define NUM_LIDARS        (NUM_LOXS + NUM_LIDAR_LITES)

// GARMIN Lidar Lite is the Front Lidar
LIDARLite frontLidar;

// VL53L0X for Back, Left and Right Sensors
Adafruit_VL53L0X lox_b =  Adafruit_VL53L0X(); ;
VL53L0X_RangingMeasurementData_t lox_measure[3]; // holds measurement for Lox Lidars

// Shutdown PINS for each of the VL53L0X
#define S_LOX_B  6
#define S_LOX_L  7
#define S_LOX_R  8


// Array to store distances measured
int distances[NUM_LIDARS];

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
#define I2C_FRONT 0x30
#define I2C_BACK  0x31
#define I2C_LEFT  0x32
#define I2C_RIGHT 0x33

void setupVL53L0X() {

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

  /*
    // activating Left
    digitalWrite(S_LOX_L, HIGH);
    delay(10);

    // init Left
    if (!lox[1].begin(I2C_LEFT)) {
      Serial.println(F("Failed to boot left VL53L0X"));
      while (1);
    }
    delay(10);

    // activating Right
    digitalWrite(S_LOX_R, HIGH);
    delay(10);

    // init Right
    if (!lox[2].begin(I2C_RIGHT)) {
      Serial.println(F("Failed to boot right VL53L0X"));
      while (1);
    }
    delay(10);
  */
}


void setup() {

  // set up console baud rate.
  Serial.begin(115200);
 
  // wait until serial port opens for native USB devices
  while (! Serial) { delay(1); }


  // Instantiate and configure LIDARS
 // frontLidar.begin(0, true, I2C_FRONT); // Set configuration to default and I2C to 400 kHz
  //frontLidar.configure(LL_CFG_DEF);

  // Set LOX
  setupVL53L0X();
}


void readLidars() {
  /*
    // Read front LIDAR
    distances[0] = frontLidar.distance();
    // Serial.print("Front: "); Serial.print(frontLidar.distance()); Serial.println();
    Serial.print("Front: "); Serial.print(distances[0]); Serial.println();
  */
  // Read LOX Radars
  for (int i = 0; i < NUM_LOXS; i++) {
    // pass in 'true' to get debug data printout!
    lox_b.rangingTest(&lox_measure[i], false);

    Serial.print("LOX ("); Serial.print(i); Serial.print("): ");
    if (lox_measure[i].RangeStatus != 4) {    // if not out of range
      distances[i + 1] = lox_measure[i].RangeMilliMeter;
      Serial.print(lox_measure[i].RangeMilliMeter);
    } else {
      Serial.print("Out of range");
      distances[i + 1] = -1; // No objects in range
    }
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
        readLidars();
        break;

      default: break ; // do nothing

    }
  }
  readLidars();
  delay(1000);
}
