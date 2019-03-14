import sys
import time
import json
import DriverStation
from networktables import NetworkTables

#Teensy or Arduino connectd to PC on COM Ports. Modify COM13 tot he right COM Port

arduino = DriverStation.Arduino('COM17')

##############################################################################################################

def sendPosition(value):
    command = '{ "sensor": "sliderPot", "position": 0 }'
    cmd = json.loads(command)
    cmd["position"] = value
    print(json.dumps(cmd))
    arduino.write(json.dumps(cmd))
    
##############################################################################################################
# Sit in a loop reading the Network table for Slider
# and update Arduino via serial port to move the slider pot.
# Postion values are bount  to -512 to 512 by the arduino.
######################################################################
NetworkTables.initialize(server="192.168.1.218")
sd = NetworkTables.getTable('Slider')

while True:
    position = sd.getNumber('position', "N/A")
    print("position:", position)
    sendPosition(position)
    time.sleep(1)
