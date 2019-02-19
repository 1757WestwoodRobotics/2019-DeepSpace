package org.whsrobotics.commands;

import edu.wpi.first.wpilibj.command.InstantCommand;

import org.whsrobotics.subsystems.PneumaticsBase;

public class Compress extends InstantCommand{

    public Compress(){

    }

    @Override
    protected void initialize() {
        PneumaticsBase.startCompression(PneumaticsBase.getCompressor());
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}

