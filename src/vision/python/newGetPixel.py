import cv2
import numpy
from westwood_vision_tools import *




###################################################################################################

robot_execution=True

cap = configure_camera(0,robot_execution)

picture =take_picture2(cap)

if (robot_execution):
        picture = cv2.cvtColor(picture, cv2.cv.CV_BGR2HSV)
else:
	picture = cv2.cvtColor(picture, cv2.COLOR_BGR2HSV)

picture =cv2.bilateralFilter(picture,5,50,50)


if robot_execution:
	get_pixel_values_unbuntu(picture)
else:
	get_pixel_values(picture)
