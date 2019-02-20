package org.whsrobotics.commands;

import edu.wpi.first.wpilibj.command.Command;
<<<<<<< HEAD

import org.whsrobotics.subsystems.PneumaticsBase;

public class Compress extends Command{
=======
import edu.wpi.first.wpilibj.command.InstantCommand;

import org.whsrobotics.subsystems.PneumaticsBase;

public class Compress extends Command {
>>>>>>> 9260683e5ab0a6ec909a8a2e77ccb76b1349b41b

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
        return ( !PneumaticsBase.getPressureSwitchState() );        // OR(||) read from DriverStation
    }
}

