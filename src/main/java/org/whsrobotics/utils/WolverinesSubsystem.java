package org.whsrobotics.utils;

import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.command.Subsystem;

import java.util.HashSet;

public abstract class WolverinesSubsystem extends Subsystem {

    private static final double PERIODIC_TIME = 0.5;

    private static HashSet<WolverinesSubsystem> subsystems;
    private static Notifier notifier;
    private boolean isMissionCritical;

    static {
        subsystems = new HashSet<>();
    }

    protected WolverinesSubsystem(boolean isMissionCritical) {
        this.isMissionCritical = isMissionCritical;
        subsystems.add(this);
    }

    protected abstract void init(boolean onTestRobot);

    public static void initSubsystems(boolean onTestRobot, WolverinesSubsystem... subsystems) {

        for (WolverinesSubsystem ws : subsystems) {
            try {
                ws.init(onTestRobot);
            } catch (Exception ex) {
                // TODO: Report that the subsystem was not initializable, if mission critical -> stop the robot
                // Remove capabilities and commands?
                if (ws.isMissionCritical)
                    throw new RuntimeException("**** ERROR: Unable to initialize " + ws.getName() + " ****");
            }
        }

        notifier = new Notifier(WolverinesSubsystem::defineReducedPeriodic);

    }

    protected boolean testSubsystem() {
        // Override me!
        return true;
    }

    public static void beginReducedPeriodic() {
        notifier.startPeriodic(PERIODIC_TIME);
    }

    private static void defineReducedPeriodic() {
        try {
            for (WolverinesSubsystem ws : subsystems) {
                ws.reducedPeriodic();
            }
        } catch (Exception ex) {
            // TODO: Just report an error.
        }
    }

    protected void reducedPeriodic() {
        // Override me!
    }

}
