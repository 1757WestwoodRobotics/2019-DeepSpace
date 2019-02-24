import time
import serial
import json

# make sure to install PySerial using the command pip install pyserial

'''
Sample JSON commands to send to Arduino

Example Lidar JSON:

{
  "sensor": "Lidar",
  "read": 1
}


Example Lidar Ring LED:

{
  "sensor": "ringLight",
  "color": 0 
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
class Arduino():
    # Set up the serial port and ensure it is non-blocking
	def __init__(self, port):
		self._port=serial.Serial(port, baudrate=115200, timeout=1)

    # returns number of bytes sent over serial
	def write(self, cmd):
		return self._port.write(cmd)

    # returns a JSON object read from the serial port
	def read(self):
		return json.loads(self._port.readline())
				
	def close(self):
		self._port.close()


                
