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
-------------------
* Front Lidar is GARMIN Lidar Lite V3
* Rear Lidar is a VL53L0X

Front Connections:
  LIDAR-Lite 5 Vdc (red) to Arduino 5v
  LIDAR-Lite I2C SCL (green) to Arduino SCL
  LIDAR-Lite I2C SDA (blue) to Arduino SDA
  LIDAR-Lite Ground (black) to Arduino GND

  Back Connections:
  LOX 5 VDC to Arduino 5v
  LOX GND to Arduino GND
  LOX I2C SCL to Arduino SCL
  LOX I2C SDA to Ardunio SDA

Left & Right Lidars:
-------------------
  Left & Right Connections:
  LOX 5 VDC to Arduino 5v
  LOX GND to Arduino GND
  LOX I2C SCL to Arduino SCL
  LOX I2C SDA to Ardunio SDA
  
Shutdown PINS for each of the VL53L0X to set up I2C Address. Make sure to connect Digital Out Pins of Arduino appropriately to the LOX XSHUT Pins.

| LOX XSHUT | Arduino |
|-----------|---------|
|  Left     |    D7   |
|  Right    |    D8   |


I2C is used to communicate between Arduinos and Lidar - Only between LIDAR and Arduino. The Front and Back LIDARs use default I2C Addressed. The Left and Right VL53L0X use specfic I2C addressed as detailed in the table below.

| LOX  | I2C Address |
|------|-------------|
| Left |   0x32      |
| Right|   0x33      |



