package org.whsrobotics.utils;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.buttons.Button;
import org.whsrobotics.robot.OI;

/**
 * Implementation of the WPILIB JoystickButton class. Provides similar functionality to the WPILIB JoystickButton class.
 * Gives the option to switch between different Joysticks while removing the DS warning messages.
 *
 * @version 1.0
 * @author Larry Tseng
 */
public class BetterJoystickButton extends Button {

    private final GenericHID m_joystick;
    private final int m_buttonNumber;

    /**
     * Create a joystick button for triggering commands.
     *
     * @param defaultJoystick     The GenericHID object that has the button (e.g. Joystick, KinectStick,
     *                     etc)
     * @param buttonNumber The button number (see {@link GenericHID#getRawButton(int) }
     */
    public BetterJoystickButton(GenericHID defaultJoystick, int buttonNumber) {
        m_joystick = defaultJoystick;
        m_buttonNumber = buttonNumber;
    }

    /**
     * Gets the value of the joystick button.
     *
     * @return The value of the joystick button
     */
    @Override
    public boolean get() {
        // Checks if the current joystick is the same as the defined joystick
        // If true, then poll the value of the button
        return (OI.getCurrentControlsJoystick() == m_joystick) && m_joystick.getRawButton(m_buttonNumber);
    }

}
