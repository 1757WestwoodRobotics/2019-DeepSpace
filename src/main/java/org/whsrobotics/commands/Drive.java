package org.whsrobotics.commands;

import com.revrobotics.CANSparkMax.IdleMode;

import edu.wpi.first.wpilibj.DriverStation;
import org.whsrobotics.robot.OI;
import org.whsrobotics.robot.Robot;
import org.whsrobotics.subsystems.Drivetrain;

import edu.wpi.first.wpilibj.command.Command;
import org.whsrobotics.utils.XboxController;
import org.whsrobotics.utils.XboxController.Buttons;

public class Drive extends Command {

    private int currentLimit = 80;

    public Drive() {
        requires(Drivetrain.getInstance());
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    @Override
    protected void execute() {

        // Bind Left Bumper to brake mode while held
        if (OI.getXboxControllerA().getRawButton(Buttons.LEFT_BUMPER)) {
            Drivetrain.setIdleMode(IdleMode.kBrake);
        }  else {
            Drivetrain.setIdleMode(IdleMode.kCoast);
        }

        if (Robot.isTestRobot) {
            Drivetrain.arcadeDrive(
                    -OI.getXboxControllerA().getNormalizedAxis(XboxController.Axes.LEFT_Y),
                    -OI.getXboxControllerA().getNormalizedAxis(XboxController.Axes.RIGHT_X));      // Negative only for the Test Robot!!!
        } else {
            Drivetrain.arcadeDrive(
                    -OI.getXboxControllerA().getNormalizedAxis(XboxController.Axes.LEFT_Y),
                    OI.getXboxControllerA().getNormalizedAxis(XboxController.Axes.RIGHT_X));
        }

    }

    @Override
    protected void initialize() {
        if (!Drivetrain.getInstance().ensureInit()) {
            cancel();
            DriverStation.reportError("**** ERROR: Cannot run Drive command on an uninitialized Drivetrain! ****", false);
        }
    }

    @Override
    protected void end() {
        super.end();
    }
    
}