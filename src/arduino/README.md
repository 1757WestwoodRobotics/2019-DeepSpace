# Folder contains code to control LED lights and Lidars connected to Arduino. Communication is via I2C

Ring Lights: Used only for Vision - Directory ledControl
--------------------------------------------------------
We plan to use 3 Ring lights daisly chained via Data in and Data Out of the Ring lights. There are 24 LEDs in each ring and hence 72 LEDs in total.

|Arduino     | Ring Light   |
|-------     | ----------   |
|Data Pin 6  |   Data In    |
|VCC 5V      |   VCC        |
|GND         |   GND        |


Lidars: Used to detect objects around the Robot - Directories fronBackLidarControl & leftRightLidarControl
----------------------------------------------------------------------------------------------------------

Front & Back Lidars:
* Front Lidar is GARMIN Lidar Lite V3
* Rear Lidar is a VL53L0X

Front LIDAR Connections:

|  Lidar-Lite      | Arduino 1 |
|------------------|-----------|
| 5v dc (red)      |     5v    |
| I2C SCL (green)  |     SCL   |
| I2C SDA (blue)   |     SDA   |
| Ground (black)   |     GND   |
 
Back LIDAR Connections:

|  LOX Back  | Arduino 1 |
|------------|-----------|
|    5v      |     5v    |
|    GND     |     GND   |
|    SCL     |     SCL   |
|    SDA     |     SDA   |

Left & Right Lidars:
* Shutdown PINS for each of the VL53L0X to set up I2C Address. Make sure to connect Digital Out Pins of Arduino appropriately to the LOX XSHUT Pins.

|  LOX Left  | Arduino 2 |
|------------|-----------|
|    5v      |     5v    |
|    GND     |     GND   |
|    SCL     |     SCL   |
|    SDA     |     SDA   |
|    XSHUT   |     D7    |

| LOX Right  | Arduino 2 |
|------------|-----------|
|    5v      |     5v    |
|    GND     |     GND   |
|    SCL     |     SCL   |
|    SDA     |     SDA   |
|    XSHUT   |     D8    |

  

* I2C is used to communicate between Arduinos and Lidar - Only between LIDAR and Arduino. The Front and Back LIDARs use default I2C Addressed. The Left and Right VL53L0X use specfic I2C addressed as detailed in the table below.

| LOX  | I2C Address |
|------|-------------|
| Left |   0x32      |
| Right|   0x33      |



