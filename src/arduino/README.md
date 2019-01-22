# Folder contains code to control LED lights and Ultrasound Sensor connected to Arduino. Communication is via I2C

PIN Connections on Arduino (Digital Connector Side)

I2C Connectivity - Make sure to connect GND. If not communication will not work.

|Arduino     |    RoboRio   |
|-------     |    -------   |
|(I2C)SDA    |    I2C SDA   |
|(I2C)SCL    |    I2C SCL   |
|GND         |    I2C GND   |

|Arduino     | Ring Light   |
|-------     | ----------   |
|Data Pin 6  |   Data In    |
|VCC 5V      |   VCC        |
|GND         |   GND        |


Strip Lighting has 2 kinds of light. One can be programmed and the other is just white light 20V. 
The 20V is controlled by a variable power supply.

|Arduino     | Strip Light  |
|-------     | ----------   |
|Data Pin 5  |   DI         |
|Data Pin 9  |   0-5v Controller |
|VCC 5V      |   VCC        |
|GND         |   GND        |

|Arduino     |    Ultrasound Sensor |
|-------     |    ----------------- |
| Pin 12     |    Trigger           |
| Pin 11     |    Echo              |
| 5 V        |    VCC               |
| GND        |    GND               |



