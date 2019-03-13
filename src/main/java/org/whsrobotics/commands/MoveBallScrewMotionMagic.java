package org.whsrobotics.commands;

import org.whsrobotics.subsystems.HatchMech.Units;
import org.whsrobotics.subsystems.HatchMech;

import edu.wpi.first.wpilibj.command.Command;

public class MoveBallScrewMotionMagic extends Command{

    public static void MoveBallScrewMotionMagic(Units unit, double position) {
        // switch (unit) {
        //     case INCH:
        //         ballScrewTalon.set(ControlMode.MotionMagic, position * 20807);  // ~20807 ticks per inch
        //         break;
        //     case CM:
        //         ballScrewTalon.set(ControlMode.MotionMagic, (position / 2.54) * 20807);
        //         break;
        //     case NATIVE_TICKS:
        //         ballScrewTalon.set(ControlMode.MotionMagic, position);
        //         break;
        // }
    }

    @Override
    protected void initialize() {
        
    }

    @Override
    protected void end() {
        super.end();
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}