import sys
import json
import DriverStation
from networktables import NetworkTables

##############################################################################################################

def init_network_tables():
    NetworkTables.initialize(server='10.17.57.2')    # Use 127.0.0.1 for local testing, 10.17.57.2 for roboRIO
    #global table
    parent_table = NetworkTables.getTable('Slider')
    settings_sub_table=parent_table.getSubTable("settings")

    return parent_table, settings_sub_table


##############################################################################################################

def read_network_value(key, table):

    return table.getEntry(key)


##############################################################################################################
# from command prompt enter the "outline viewer" directory type "gradlew run"

def publish_network_value(key, value, table):
    table.putValue(key, value)



#Teensy or Arduino connectd to PC on COM Ports. Modify COM13 tot he right COM Port

arduino = DriverStaion.driverStation('COM13')

##############################################################################################################
# Sit in a loop reading the Network table for Slider
# and update Arduino via serial port to move the slider pot.
# Postion values are bount  to -512 to 512 by the arduino.
######################################################################
while True:
    table = init_network_tables()
    position = read_network_value('position', table)
    command = '{ "sensor": "sliderPot", "position": 0 }'
    cmd = json.loads(command)
    cmd["position"] = val
    print(json.dumps(cmd))
    arduino.write(json.dumps(cmd))
#     publish_network_value('ABC', 104.5, table)


