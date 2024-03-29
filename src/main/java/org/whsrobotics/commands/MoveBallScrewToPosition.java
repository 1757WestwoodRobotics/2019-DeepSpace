package org.whsrobotics.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.whsrobotics.subsystems.HatchMech;

public class MoveBallScrewToPosition extends Command {

    private double position;
    private HatchMech.Units unit;

    /**
     * @param unit unit of measurement
     * @param position the position that the ball screw is moving to
     */
    public MoveBallScrewToPosition(HatchMech.Units unit, double position) {
        this.unit = unit;
        this.position = position;
    }

    @Override
    protected void initialize() {
        HatchMech.moveBallScrewMotionMagic(unit, position);
    }

    @Override
    protected boolean isFinished() {
        return HatchMech.ballScrewIsFinished();
    }

}
