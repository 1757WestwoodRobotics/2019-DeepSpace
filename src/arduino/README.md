# Folder contains code to control Driver Station, LED lights and Lidars connected to Arduino. Communication is via I2C for Lidars.

DriverStation:
--------------
Emulates a Joystcik and controls the slider pot and 7 Buttons and 5 Switches. DriverStation uses a Arduino Leonardo++. This code only works on certain Arduino's so make sure you have atleast a Leonardo. The DriverStaion appears as a Joystick device on the Laptop and talks directly to the FRCDriverStation.


ArduinoController: 
------------------
Ring light for Vision - 1 Ring light controlled via Data in and Data Out of the Ring lights. There are 24 LEDs in each ring.

|Teensy      | Ring Light   |   5V Buck |
|-------     | ----------   |---------- |
|D11 (Yellow)|   Data In    | -         |
|-           |   VCC (Red)  | V out +   |
|GND (Black) |   GND (Black)| V out GND |


Lidars: Used to detect objects around the Robot -  Controls Lidars(Front, Back, Left and Right)
* Front Lidar is GARMIN Lidar Lite V3
* Rear, Left & Right Lidars is a VL53L0X

Front LIDAR Connections:

|  Lidar-Lite      | Teensy3.2 |   5V Buck |
|------------------|-----------|-----------|
| 5v dc (red)      |     -     | V out +   |
| Ground (black)   |     GND   | V out GND |
| I2C SCL (blue)   |     SCL   | -         |
| I2C SDA (green)  |     SDA   | -         |
|
 
Back, Left, Right LIDAR Connections:

|  LOX Back   | LOX Laeft  | LOX Right   | Teensy3.2 |   5V Buck |
|-------------|------------|-------------|-----------|-----------|
|    5v       |     5v     |     5v      |    -      | V out +   |
|    GND      |     GND    |     GND     |     GND   | V out GND |  
|  SCL (blue) |     SCL    |     SCL     |     SCL   |    -      |
|  SDA (green)|     SDA    |     SDA     |     SCL   |    -      |
|  XSHUT(grey)|     -      |     -       |     D7    |    -      |
|  -          |XSHUT(brown)|     -       |     D8    |    -      |
|  -          |     -      |XSHUT(orange)|     D9    |    -      |


* I2C is used to communicate between Arduinos and Lidar - Only between LIDAR and Arduino. The Front and Back LIDARs use default I2C Addressed. The Left and Right VL53L0X use specfic I2C addressed as detailed in the table below.

| LOX  | I2C Address    |
|------|----------------|
| Front|   Default(0x62)|
| Back |   0x31         |
| Left |   0x32         |
| Right|   0x33         |



