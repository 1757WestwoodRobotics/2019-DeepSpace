import cv2
import numpy
from westwood_vision_tools import *

cap = cv2.VideoCapture(0)
time.sleep(5)
picture =take_picture2(cap)
#picture = cv2.cvtColor(picture, cv2.COLOR_BGR2HSV)
picture =cv2.bilateralFilter(picture,10,150,150)
show_picture("Picture ",picture,1000)
get_pixel_values(picture)