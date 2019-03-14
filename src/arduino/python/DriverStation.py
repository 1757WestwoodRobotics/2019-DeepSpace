import time
import serial
import json

# make sure to install PySerial using the command pip install pyserial

'''
Sample JSON commands to send to Arduino

Example DriverStation JSON: postion can range form -512 to 512

{
  "sensor": "sliderPot",
  "position": 0
}
'''


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


                
