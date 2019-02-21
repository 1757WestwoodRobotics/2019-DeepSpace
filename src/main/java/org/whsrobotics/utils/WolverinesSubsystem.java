package org.whsrobotics.utils;

import edu.wpi.first.wpilibj.command.Subsystem;

public abstract class WolverinesSubsystem extends Subsystem {

    private boolean isMissionCritical;

    protected WolverinesSubsystem(boolean isMissionCritical) {
        this.isMissionCritical = isMissionCritical;
    }

    protected abstract void init(boolean onTestRobot);

    protected boolean testSubsystem() {
        // Override me!
        return true;
    }

    public static void initSubsystems(boolean onTestRobot, WolverinesSubsystem... subsystems) {
        try {
            subsystems[0].init(onTestRobot);
        } catch (Exception ex) {

        }
    }

}
