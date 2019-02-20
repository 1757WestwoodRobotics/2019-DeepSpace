package org.whsrobotics.commands;

import edu.wpi.first.wpilibj.command.Command;

import org.whsrobotics.subsystems.PneumaticsBase;

public class Compress extends Command{

    public Compress(){
        requires(new PneumaticsBase());
    }

    @Override
    protected void initialize() {
        PneumaticsBase.startCompression(PneumaticsBase.getCompressor());
    }

    @Override
    protected void end() {
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}

