package org.whsrobotics.commands;

import com.revrobotics.CANSparkMax.IdleMode;

import org.whsrobotics.robot.OI;
import org.whsrobotics.robot.Robot;
import org.whsrobotics.subsystems.Drivetrain;

import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.command.Command;
import org.whsrobotics.utils.XboxController;
import org.whsrobotics.utils.XboxController.Buttons;

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

        if (OI.getXboxControllerA().getRawButton(Buttons.LEFT_BUMPER)) {
            Drivetrain.setIdleMode(IdleMode.kBrake);
        }  else {
            Drivetrain.setIdleMode(IdleMode.kCoast);
        }

        // If XC has button held, use the FAST parameter and set "Drivetrain.setSparkMaxSmartCurrentLimit(80)"
        if (Robot.isTestRobot) {
            Drivetrain.arcadeDrive(Drivetrain.DrivetrainSpeedMode.FAST,
                    -OI.getXboxControllerA().getNormalizedAxis(XboxController.Axes.LEFT_Y),
                    -OI.getXboxControllerA().getNormalizedAxis(XboxController.Axes.RIGHT_X));      // Negative only for the Test Robot!!!
        } else {
            Drivetrain.arcadeDrive(Drivetrain.DrivetrainSpeedMode.FAST,
                    -OI.getXboxControllerA().getNormalizedAxis(XboxController.Axes.LEFT_Y),
                    OI.getXboxControllerA().getNormalizedAxis(XboxController.Axes.RIGHT_X));
        }


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