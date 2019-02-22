package org.whsrobotics.utils;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Subsystem;

import java.util.HashSet;

public abstract class WolverinesSubsystem extends Subsystem {

    private static final double PERIODIC_TIME = 0.5;

    private static HashSet<WolverinesSubsystem> subsystems;
    private static HashSet<WolverinesSubsystem> failedSubsystems;
    private static Notifier notifier;
    private boolean isMissionCritical;

    static {
        subsystems = new HashSet<>();
        failedSubsystems = new HashSet<>();
    }

    protected WolverinesSubsystem(boolean isMissionCritical) {
        this.isMissionCritical = isMissionCritical;
    }

    protected abstract void init(boolean onTestRobot);

    public static void initSubsystems(boolean onTestRobot, WolverinesSubsystem... subsystems) {
        _initSubsystems(onTestRobot, subsystems);

        if (notifier == null)
            notifier = new Notifier(WolverinesSubsystem::defineReducedPeriodic);
    }

    public static void reInitSubsystem(boolean onTestRobot, WolverinesSubsystem subsystem) {
        failedSubsystems.remove(subsystem);

        if (subsystem.getCurrentCommand() != null)
            subsystem.getCurrentCommand().cancel();     // TODO: Does this actually cancel the command?

        _initSubsystems(onTestRobot, subsystem);

        notifier.stop();
        notifier = new Notifier(WolverinesSubsystem::defineReducedPeriodic);
        beginReducedPeriodic();
    }

    private static void _initSubsystems(boolean onTestRobot, WolverinesSubsystem... subsystems) {

        for (WolverinesSubsystem ws : subsystems) {
            try {
                ws.init(onTestRobot);
                WolverinesSubsystem.subsystems.add(ws);

            } catch (Exception ex) {

                failedSubsystems.add(ws);

                // Removes capabilities and sets an un-interruptable command that requires(subsystem) -> locks the Scheduler

                (new Command() {

                    {
                        requires(ws);
                        setInterruptible(false);    // TODO: Check if this actually locks the subsystem
                        setRunWhenDisabled(true);
                    }

                    @Override
                    protected boolean isFinished() {
                        return false;
                    }

                }).start();

                if (ws.isMissionCritical)
                    throw new RuntimeException("**** ERROR: Unable to initialize mission-critical " + ws.getName() + " ****");
                else
                    DriverStation.reportError("* WARNING: Unable to initialize non-mission-critical " + ws.getName() + " *", false);

            }
        }

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
            // TODO: Remove from HashSet and report an error. Give the option to reinitialize
        }
    }

    protected void reducedPeriodic() {
        // Override me!
    }

}
