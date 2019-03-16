package org.whsrobotics.commands;

import org.whsrobotics.robot.OI;
import org.whsrobotics.subsystems.HatchMech;

import edu.wpi.first.wpilibj.command.Command;

public class SliderOverride extends Command{

    @Override
    protected void execute() {
        double value = OI.getSliderValue();
        (new MoveBallScrewToPosition(HatchMech.Units.NATIVE_TICKS, value * HatchMech.BALL_SCREW_FWD_LIMIT)).start();
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}