import sys
import time
import numpy as np
from networktables import NetworkTables
from numpy import interp

# To see messages from networktables, you must setup logging
import logging

logging.basicConfig(level=logging.DEBUG)

NetworkTables.initialize()

sd = NetworkTables.getTable("Slider")

i = 0
while True:
   
    sd.putNumber("position", np.int(interp(i,[0, 1023], [-512, 512])))
    print("Slider Postion:", sd.getNumber("position", ""))
    time.sleep(1)
    i+=1
    if(i > 1023):
        i = 0
