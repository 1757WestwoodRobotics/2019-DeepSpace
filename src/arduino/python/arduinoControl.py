import time
import serial
import json

# make sure to install PySerial using the command pip install pyserial

'''
Sample JSON commands to send to Arduino

Example Lidar JSON:

{
  "sensor": "Lidar",
  "command": 0
}


Example Lidar Ring LED:

{
  "sensor": "ringLight",
  "command": 0 # values from ringColor()
}

'''

# Commands for controlling Ring LED Colors
class ringColor():
        # Device control commands
        Off     = 0
        Red     = 1
        Green   = 2
        Yellow  = 3
        Orange  = 4
        Blue    = 5
        White   = 6

# Arduino controls via Serial Port
class arduino():
        # Set up the serial port and ensure it is non-blocking
        def __init__(self, port, baud):
                self._port=serial.Serial(port, baud=9800, timeout=0.5)

        # returns number of bytes sent over serial
        def write(self, buffer):
                return self._port.write(buffer)

        # returns a JSON object read from the serial port
        def read():
                return json.dumps(self._port.readline())


                
