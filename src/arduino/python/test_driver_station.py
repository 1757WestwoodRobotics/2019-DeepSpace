"""
Test Driver program to test USB communication with Arduino. Use this as an example to communicate with Arduino in you Python code. Cummunication to Arduino using JSON and can be extened to do other things.
"""
import sys
import json
import DriverStation

# If Teensy or Arduino is connected to Mac or Linux uncomment line below and change port
arduino = DriverStaion.driverStation('/dev/cu.usbmodem52640001')

#Teensy or Arduino connectd to PC on COM Ports. Modify COM13 tot he right COM Port
#arduino = DriverStaion.driverStation('COM13')

while 1:
	val = input("Enter a slier postion from -512 to 512 OR  9999 - exit):")
	sys.stdout.flush()

	if val == 9999:
		arduino.close()
		exit()
	else:
		command = '{ "sensor": "sliderPot", "position": 0 }'
		print(command)
		cmd = json.loads(command)
		cmd["position"] = val
		print(json.dumps(cmd))
		arduino.write(json.dumps(cmd))
