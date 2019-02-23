package org.whsrobotics.commands;

import edu.wpi.first.wpilibj.command.Command;

import org.whsrobotics.subsystems.PneumaticsBase;

public class Compress extends Command{

    public Compress(){
        requires(PneumaticsBase.instance);
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
        return ( !PneumaticsBase.getPressureSwitchState() );        // OR(||) read from DriverStation
    }
}

