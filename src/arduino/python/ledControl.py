import time
import serial

# make sure to install PySerial using the command pip install pyserial

# Commands for controlling Ring LED Colors
class ringColor():
        # Device control commands
        Off     = '0'
        Red     = '1'
        Green   = '2'
        Yellow  = '3'
        Orange  = '4'
        Blue    = '5'
        White   = '6'

# Arduino controls via Serial Port
class ringLight():

        def __init__(self, port, baud):
                self._port=serial.Serial(port, baud)
      
        def color(self, ringColor color):
	        self.port.write(color)

