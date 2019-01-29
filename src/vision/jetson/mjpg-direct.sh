export LD_LIBRARY_PATH=/home/nvidia/camera/mjpg-streamer/mjpg-streamer-experimental
./mjpg-streamer/mjpg-streamer-experimental/mjpg_streamer -i "input_uvc.so -f 15 -r 640x480 /dev/video0" -o "output_http.so -w ./mjpg-streamer/mjpg-streamer-experimental/www"
