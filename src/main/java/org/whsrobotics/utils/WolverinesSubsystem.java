package org.whsrobotics.utils;

import edu.wpi.first.wpilibj.command.Subsystem;

public abstract class WolverinesSubsystem extends Subsystem {

    private boolean isMissionCritical = false;

    public WolverinesSubsystem() {

    }

    protected boolean testSubsystem() {
        // Override me!
        return true;
    }



}