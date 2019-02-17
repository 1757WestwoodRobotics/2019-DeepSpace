package org.whsrobotics.utils;

import edu.wpi.first.wpilibj.command.Subsystem;

public abstract class WolverinesSubsystem extends Subsystem {

    private boolean isMissionCritical = false;

    protected boolean testSubsystem() {
        // Override me!
        return true;
    }

    protected static void init() {
        // Override me!
    }

}
