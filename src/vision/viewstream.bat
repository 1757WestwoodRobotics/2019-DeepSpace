gst-launch-1.0 udpsrc port=554 ! "application/x-rtp, payload=127" ! rtph264depay ! avdec_h264 ! glimagesink
