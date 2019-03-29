"""
Lidar Proxy: Bridges communication between the Arduino controlling the LIDAR and
RoboRio. Uses Network table "Lidar" and Key "distance" to send measurements.
"""
import sys
import json
import time
from networktables import NetworkTables
import arduinoControl

# If Teensy or Arduino is connected to Mac or Linux uncomment line below
# and change port to the right on one. On Mac usually /dev/cu.* and on Linux
# /dev/serial1 or /dev/serial/*

#arduino = arduinoControl.Arduino('/dev/cu.usbmodem52640001')

#Teensy or Arduino connectd to PC on COM Ports. Modify COM13 tot he right COM Port
arduino = arduinoControl.Arduino('COM22')

# Turn on the Green Light on the LED.
command = '{ "sensor": "ringLight", "color": 0 }'
print(command)
cmd = json.loads(command)
cmd["color"] = 2
print(json.dumps(cmd))
arduino.write(json.dumps(cmd))

######################################################################
# Sit in a loop sending Lidar measurements to RoboRio
######################################################################
#NetworkTables.initialize(server="127.0.0.1")
NetworkTables.initialize(server='10.17.57.2')
ld = NetworkTables.getTable('Lidar')

while True:
    try:
        command = '{ "sensor": "Lidar", "read": 1 }'
        cmd = json.loads(command)
        # Ask Arduino to send the current Lidar reading
        arduino.write(json.dumps(cmd))
        # Read the Lidar Value
        data = arduino.read()
        if (data != ""):
            json_data = json.loads(data)
            distance = json_data["front"]
            ld.putNumber("distance", distance)
        else:
            ld.putNumber("distance", -10.0)
        time.sleep(0.2)
    except KeyboardInterrupt:
        arduino.close();
        ld.putNumber("distance", -10.0)
        exit()
