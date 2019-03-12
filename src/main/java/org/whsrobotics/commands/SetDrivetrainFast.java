package org.whsrobotics.commands;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.TimedCommand;
import org.whsrobotics.subsystems.Drivetrain;

public class SetDrivetrainFast extends TimedCommand {

    @Override
    protected void initialize() {
        if (!Drivetrain.getInstance().ensureInit()) {
            cancel();
            DriverStation.reportError("**** ERROR: Cannot run SetDrivetrainFast command on an uninitialized Drivetrain! ****", false);
        } else {
            Drivetrain.setDrivetrainSpeedMode(Drivetrain.DrivetrainSpeedMode.FAST);
        }
    }

    @Override
    protected void end() {
        Drivetrain.setDrivetrainSpeedMode(Drivetrain.DrivetrainSpeedMode.SLOW);
    }

    public SetDrivetrainFast(double timeout) {
        super(timeout);
    }

}
