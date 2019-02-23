package org.whsrobotics.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.whsrobotics.subsystems.PneumaticsBase;

public class CompressStop extends Command {

    public CompressStop(){
        requires(PneumaticsBase.instance);
    }

    @Override
    protected void initialize() {
        PneumaticsBase.stopCompression(PneumaticsBase.getCompressor());
    }

    @Override
    protected void end() {

    }

    @Override
    protected boolean isFinished() {
        return false;        // OR(||) read from DriverStation
    }
}

