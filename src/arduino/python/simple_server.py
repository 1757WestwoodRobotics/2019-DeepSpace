import sys
import time
from networktables import NetworkTables

# To see messages from networktables, you must setup logging
import logging

logging.basicConfig(level=logging.DEBUG)

NetworkTables.initialize(server="192.168.1.218")

sd = NetworkTables.getTable("Slider")

i = 0
while True:
   
    sd.putNumber("position", i)
    print("Slider Postion:", sd.getNumber("position", ""))
    time.sleep(1)
    i += 1
