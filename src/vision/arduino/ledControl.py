import time
import serial

ser=serial.Serial('/dev/ttyACM0', 9600)
while 1:
	val = raw_input("Enter 0 to 6 to controll LED color ( 0 - off):");
	ser.write(val);
