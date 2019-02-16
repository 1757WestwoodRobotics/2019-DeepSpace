package org.whsrobotics.utils;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

/**
 * Implementation of the WPILIB Joystick class. Provides similar functionality to the WPILIB XboxController class.
 *
 * @author Larry Tseng 
 *
 */
public class XboxController extends Joystick {

	private final static double DEADBAND = 0.1;

	public enum Buttons {
		A(1), B(2), X(3), Y(4), LEFT_BUMPER(5), RIGHT_BUMPER(6), 
		BACK(7), START(8), LEFT_STICK_BUTTON(9), RIGHT_STICK_BUTTON(10);
		
		private int value;
		
		Buttons(int value) {
			this.value = value;
		}
		
		public int getValue() {
			return value;
		}
		
	}
	
	/**
	 * Enum of XboxController D-Pad buttons.
	 */
	public enum DirectionalPad {
		D_TOP(0), D_RIGHT(90), D_BOTTOM(180), D_LEFT(270);
		
		private int value;
		
		DirectionalPad(int value) {
			this.value = value;
		}
		
		public int getValue() {
			return value;
		}
		
	}

	public interface XboxControllerAxes {
        int getValue();
    }
	
	/**
	 * Enum of XboxController axes.
	 */
	public enum Axes implements XboxControllerAxes {
		LEFT_X(0), LEFT_Y(1), LEFT_TRIGGER(2), RIGHT_TRIGGER(3), RIGHT_X(4), RIGHT_Y(5);
		
		private int value;
		
		Axes(int value) {
			this.value = value;
		}
		
		public int getValue() {
			return value;
		}
		
	}

    /**
     * Enum of QDriverStation Mapping of XboxController axes.
	 *
	 * @deprecated
     */
	public enum QDriverStationAxes implements XboxControllerAxes {
        LEFT_X(0), LEFT_Y(1), LEFT_TRIGGER(4), RIGHT_TRIGGER(5), RIGHT_X(2), RIGHT_Y(3);

        private int value;

        QDriverStationAxes(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

	/**
	 * Constructs a XboxController instance.
	 * 
	 * @param port The Driver Station port the Xbox controller is plugged into.
	 */
	public XboxController(int port) {
		super(port);

	}
	
	/**
	 * Get the JoystickButton associated with a Buttons button.
     * Used for calling Commands!
	 * 
	 * @param button The button of the desired JoystickButton.
	 * @return The JoystickButton of the desired button.
	 */
	public JoystickButton getButton(Buttons button) {
		return new JoystickButton(this, button.getValue());
	}
	
	/**
	 * Get the value of the button.
	 * 
	 * @param button The button to read.
	 * @return The value of the button.
	 */
	public boolean getRawButton(Buttons button) {
		return super.getRawButton(button.getValue());
		
	}
	
	/**
	 * Get the value of the axis.
	 *
	 * @param axis The axis to read.
	 * @return The value of the axis.
	 */
	public double getRawAxis(XboxControllerAxes axis) {
	  return super.getRawAxis(axis.getValue());

	}

    /**
     *
     * @param pov The DirectionalPad button to read.
     * @return The value of the POV button.
     */
    public boolean getPOV(DirectionalPad pov) {
        return super.getPOV(pov.getValue()) == pov.getValue();

    }

	/**
	 * Sets the vibration motors of the Xbox controller.
	 * 
	 * @param type	The motor to vibrate. Left motor is rougher than right motor. Uses a RumbleType constant.
	 * @param value	The strength at which the motor vibrates in range [0, 1]
	 */
	public void setRumble(RumbleType type, double value) {
		super.setRumble(type, value);
		
	}

}
