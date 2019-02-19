package org.whsrobotics.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.InstantCommand;

import org.whsrobotics.subsystems.PneumaticsBase;

public class Compress extends Command {

    public Compress(){

    }

    @Override
    protected void initialize() {
        PneumaticsBase.startCompression(PneumaticsBase.getCompressor());
    }

    @Override
    protected boolean isFinished() {
        return ( !PneumaticsBase.getPressureSwitchState() );        // OR(||) read from DriverStation
    }
}

