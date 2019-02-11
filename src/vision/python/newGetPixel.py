import cv2
import numpy
from westwood_vision_tools import *




###################################################################################################

cap = configure_camera(1)

picture =take_picture2(cap)
picture = cv2.cvtColor(picture, cv2.COLOR_BGR2HSV)
picture =cv2.bilateralFilter(picture,5,50,50)
#show_picture("Picture ",picture,1000)
get_pixel_values(picture)