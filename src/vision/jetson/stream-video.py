import numpy as np
import cv2
import os
from time import time

# Adjustable - for multiple cameras, change below value
cameras = 1

# Don't touch anything below this line
cam = []
time = time()

for i in range(cameras):
    cam.append(cv2.VideoCapture(i))
    try:
        os.mkdir('cam' + str(i))
    except OSError:
        pass

fourcc = cv2.cv.CV_FOURCC(*'XVID')

while True:
    for i in range(len(cam)):
	os.chdir('./cam' + str(i))
	out = cv2.VideoWriter(str(time) + '.avi', fourcc, 20.0, (640,480))
        ret, frame = cam[i].read()
        if ret==True:
            frame = cv2.flip(frame, 1)
            out.write(frame)
        os.chdir('../')

    if cv2.waitKey(1) & 0xFF == ord('q'):
        break

for i in range(len(cam)):
    cam[i].release()
out.release()
cv2.destroyAllWindows()
