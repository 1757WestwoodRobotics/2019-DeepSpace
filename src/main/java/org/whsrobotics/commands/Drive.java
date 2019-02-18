package org.whsrobotics.commands;

import org.whsrobotics.robot.OI;
import org.whsrobotics.subsystems.Drivetrain;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.command.Command;
import org.whsrobotics.utils.XboxController;

public class Drive extends Command {

    public Drive() {
        requires(Drivetrain.instance);
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    @Override
    protected void execute() {

//        Drivetrain.tankDrive(-OI.getXboxController().getRawAxis(XboxController.Axes.LEFT_Y),
//                -OI.getXboxController().getRawAxis(XboxController.Axes.RIGHT_Y));

        Drivetrain.arcadeDrive(-OI.getXboxController().getRawAxis(XboxController.Axes.LEFT_Y),
                OI.getXboxController().getRawAxis(XboxController.Axes.RIGHT_X));

    }

    @Override
    protected void initialize() {

        System.out.println("Initialized Drive");

    }

    @Override
    protected void end() {
        super.end();
    }
    
}