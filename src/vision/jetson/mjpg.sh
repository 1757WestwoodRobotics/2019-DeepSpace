export LD_LIBRARY_PATH=/home/nvidia/camera/mjpg-streamer/mjpg-streamer-experimental
./mjpg-streamer/mjpg-streamer-experimental/mjpg_streamer -i "input_file.so -f ./cam0 -r" -o "output_http.so -w ./mjpg-streamer/mjpg-streamer-experimental/www"
