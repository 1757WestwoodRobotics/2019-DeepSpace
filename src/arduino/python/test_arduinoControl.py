"""
Test Driver program to test USB communication with Arduino. Use this as an example to communicate with Arduino in you Python code. Cummunication to Arduino using JSON and can be extened to do other things.
"""
import sys
import json
import arduinoControl

# If Teensy or Arduino is connected to Mac or Linux uncomment line below and change port
#arduino = arduinoControl.Arduino('/dev/cu.usbmodem52640001')

#Teensy or Arduino connectd to PC on COM Ports. Modify COM13 tot he right COM Port
arduino = arduinoControl.Arduino('COM18')

while 1:
	val = input("Enter a color 0 to 6 to control LED color, 8 - read lidar, 9 - exit):")
	sys.stdout.flush()

	if val == 9:
		arduino.close()
		exit()
	elif val == 8:
		command = '{ "sensor": "Lidar", "read": 1 }'
		print(command)
		cmd = json.loads(command)
		print(json.dumps(cmd))
		arduino.write(json.dumps(cmd))
		data = arduino.read()
		print(json.dumps(data))
	else:
		command = '{ "sensor": "ringLight", "color": 0 }'
		print(command)
		cmd = json.loads(command)
		cmd["color"] = val
		print(json.dumps(cmd))
		arduino.write(json.dumps(cmd))
