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
    #print(json.dumps(cmd))
    arduino.write(json.dumps(cmd))
    #print("Sent message to arduino...")
    
##############################################################################################################
# Sit in a loop reading the Network table for Slider
# and update Arduino via serial port to move the slider pot.
# Postion values are bound  to -512 to 512 by the arduino.
######################################################################
NetworkTables.initialize(server="127.0.0.1")
sd = NetworkTables.getTable('Slider')

while True:
    try:
        position = sd.getNumber('position', 0)
        print("position:", position)
        sendPosition(position)
        time.sleep(.1)
    except KeyboardInterrupt:
        arduino.close();
        exit()
