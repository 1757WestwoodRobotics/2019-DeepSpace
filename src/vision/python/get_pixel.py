import cv2
import numpy
from westwood_vision_tools import *

# take the picture
picture =take_picture(True, 1)
#picture = cv2.cvtColor(picture, cv2.COLOR_BGR2HSV)
picture =cv2.bilateralFilter(picture,10,150,150)
show_picture("Picture ",picture,1000)
#get_pixel_values(picture)
get_all_pixel_values(picture)