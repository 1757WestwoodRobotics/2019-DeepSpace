######################################################################
# Simple Server simulates RoboRio to test communication betwee Arduino
# & RoboRio. Server operates in 2 modes. Mode 0 is consumer mode  to
# recieve data from Lidar and Mode 1 is producer mode to move the
# slider potentiameter on the Driver Station
######################################################################
import sys
import time
import numpy as np
from networktables import NetworkTables
from numpy import interp

# To see messages from networktables, you must setup logging
import logging

logging.basicConfig(level=logging.DEBUG)

# No parameters to go into server mode.
NetworkTables.initialize()

sd = NetworkTables.getTable("Slider")
ld = NetworkTables.getTable("Lidar")

val = input("Enter server mode, 0 - consumer, 1 - producer :")
print("Entering mode: ", val)
i = 0
while True:
    try:
        if val == 0:
            print("Object Distance:", ld.getNumber("distance", -1.0))
            time.sleep(1)
        elif val == 1:
            sd.putNumber("position", np.int(interp(i,[0, 1023], [-512, 512])))
            time.sleep(1)
            i+=1
            if(i > 1023):
                i = 0
    except KeyboardInterrupt:
        exit()
