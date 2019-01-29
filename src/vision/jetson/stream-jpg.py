import numpy as np
import cv2
import os
from time import time

# Adjustable - for multiple cameras, change below value
cameras = 1
cam = []

for i in range(cameras):
    cam.append(cv2.VideoCapture(i))
    try:
        os.mkdir('cam' + str(i))
    except OSError:
        pass

fourcc = cv2.cv.CV_FOURCC(*'MJPG')

while True:
    for i in range(len(cam)):
	out = cv2.VideoWriter('frame' + str(time()) + '.jpg', fourcc, 20.0, (640,480))
        ret, frame = cam[i].read()
        if ret==True:
            frame = cv2.flip(frame, 1)
            os.chdir('./cam' + str(i))
            out.write(frame)
            os.chdir('../')

    if cv2.waitKey(1) & 0xFF == ord('q'):
        break

for i in range(len(cam)):
    cam[i].release()
out.release()
cv2.destroyAllWindows()
