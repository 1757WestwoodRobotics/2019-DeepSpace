package org.whsrobotics.commands;

import org.whsrobotics.robot.OI;

import edu.wpi.first.wpilibj.command.Command;


public class ControlSwitch extends Command{

    @Override
            protected void initialize() {
                this.setRunWhenDisabled(true);
                OI.setCurrentControlsJoystick(OI.getXboxControllerB());
            }

            @Override
            protected boolean isFinished() {
                return false;
            }

            @Override
            protected void end() {
                OI.setCurrentControlsJoystick(OI.getControlSystem());
            }

}