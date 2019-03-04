package org.whsrobotics.commands;

import org.whsrobotics.robot.OI;
import org.whsrobotics.subsystems.Drivetrain;

import edu.wpi.first.wpilibj.command.Command;
import org.whsrobotics.utils.XboxController;

public class Drive extends Command {

    public Drive() {
        requires(Drivetrain.getInstance());
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    @Override
    protected void execute() {

        // TODO: Sean

        // Set Brake mode based on button hold

        // If XC has button held, use the FAST parameter and set "Drivetrain.setSparkMaxSmartCurrentLimit(80)"
        Drivetrain.arcadeDrive(Drivetrain.DrivetrainSpeedMode.FAST,
                -OI.getXboxController().getNormalizedAxis(XboxController.Axes.LEFT_Y),
                -OI.getXboxController().getNormalizedAxis(XboxController.Axes.RIGHT_X));    // TODO: Negative only for the Test Robot!!!

        // Otherwise, use SLOW and set "Drivetrain.setSparkMaxSmartCurrentLimit(60)"

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