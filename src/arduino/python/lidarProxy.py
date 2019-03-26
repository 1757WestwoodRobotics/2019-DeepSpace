"""
Test Driver program to test USB communication with Arduino. Use this
as an example to communicate with Arduino in you Python code.
Cummunication to Arduino using JSON and can be extened to do other
things.
"""
import sys
import json
import time
from networktables import NetworkTables
import arduinoControl

# If Teensy or Arduino is connected to Mac or Linux uncomment line below and change port
#arduino = arduinoControl.Arduino('/dev/cu.usbmodem52640001')

#Teensy or Arduino connectd to PC on COM Ports. Modify COM13 tot he right COM Port
arduino = arduinoControl.Arduino('COM7')

command = '{ "sensor": "ringLight", "color": 0 }'
print(command)
cmd = json.loads(command)
cmd["color"] = 2
print(json.dumps(cmd))
arduino.write(json.dumps(cmd))

######################################################################
# Sit in a loop reading the Network table for Slider
# and update Arduino via serial port to move the slider pot.
# Postion values are bound  to -512 to 512 by the arduino.
######################################################################
NetworkTables.initialize(server="127.0.0.1")
ld = NetworkTables.getTable('Lidar')
i = 0
while True:
    try:
        command = '{ "sensor": "Lidar", "read": 1 }'
        cmd = json.loads(command)
        print(json.dumps(cmd))
        # Ask Arduino to send the current Lidar reading
        arduino.write(json.dumps(cmd))
        # Read the Lidar Value
        data = arduino.read()
        if (data != ""):
            json_data = json.loads(data)
            distance = json_data["front"]
            ld.putNumber("distance", distance+i)
            i = i+1
        else:
            ld.putNumber("distance", -10.0)
        time.sleep(0.2)
    except KeyboardInterrupt:
        arduino.close();
        exit()