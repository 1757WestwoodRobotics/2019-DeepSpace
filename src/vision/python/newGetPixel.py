import cv2
import numpy
from westwood_vision_tools import *


###################################################################################################
# given a USB camera number, this configures the camera

def configure_camera(camera_number):

    cap = cv2.VideoCapture(camera_number)

    cap.set(cv2.CAP_PROP_SETTINGS, 1)  # to fix things
    cap.set(cv2.CAP_PROP_BRIGHTNESS, 30)
    cap.set(cv2.CAP_PROP_EXPOSURE, -7)
    cap.set(cv2.CAP_PROP_CONTRAST, 5)
    cap.set(cv2.CAP_PROP_SATURATION, 83)
    cap.set(cv2.CAP_PROP_FRAME_WIDTH, 240)
    cap.set(cv2.CAP_PROP_FRAME_HEIGHT, 480)

    return cap

###################################################################################################

cap = configure_camera(1)

picture =take_picture2(cap)
picture = cv2.cvtColor(picture, cv2.COLOR_BGR2HSV)
picture =cv2.bilateralFilter(picture,5,50,50)
show_picture("Picture ",picture,1000)
get_pixel_values(picture)