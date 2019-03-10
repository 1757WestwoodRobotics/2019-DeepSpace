package org.whsrobotics.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.InstantCommand;

public class RampDeployment extends InstantCommand{

    @Override
    protected void initialize() {
        System.out.println("Ramp Deploying");
    }

    @Override
    protected void end() {
        
    }
}