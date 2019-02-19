import ledControl

# Teensy or Arduino is connect to the following port on my Mac
led = ledControl.ringLight('/dev/cu.usbmodem52640001', 9800)

while 1:
	val = raw_input("Enter a color 0 to 6 to control LED color ( 0 - off):");
	led.color(val);
