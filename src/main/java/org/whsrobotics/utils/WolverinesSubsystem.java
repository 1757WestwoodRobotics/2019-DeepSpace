package org.whsrobotics.utils;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Subsystem;

import java.util.HashSet;

public abstract class WolverinesSubsystem extends Subsystem {

    /**
     * The reduced periodic loop time in seconds. Reduces processing and network congestion.
     */
    private static final double PERIODIC_TIME = 0.5;        // seconds

    // Collections for the subsystem references
    private static HashSet<WolverinesSubsystem> subsystems;
    private static HashSet<WolverinesSubsystem> failedSubsystems;

    // The class that feeds the loop and keeps track of the time between loops
    private static Notifier notifier;

    private static boolean isTestRobot;

    private boolean isMissionCritical;

    // Initialize the collections with empty HashSets
    static {
        subsystems = new HashSet<>();
        failedSubsystems = new HashSet<>();
    }

    /**
     * Default constructor for a WolverinesSubsystem subclass. Only sets the flag for whether a subsystem
     * is mission critical or not.
     *
     * @param isMissionCritical
     */
    protected WolverinesSubsystem(boolean isMissionCritical) {
        this.isMissionCritical = isMissionCritical;
    }

    /**
     * Abstract method that is defined per each subsystem. Connect/define all hardware references here!
     *
     * @param onTestRobot
     */
    protected abstract void init(boolean onTestRobot);

    /**
     *
     * @param isTestRobotHardware
     * @param subsystems
     */
    public static void initSubsystems(boolean isTestRobotHardware, WolverinesSubsystem... subsystems)
    {
        isTestRobot = isTestRobotHardware;
        _initSubsystems(subsystems);

        if (notifier == null)
            notifier = new Notifier(WolverinesSubsystem::defineReducedPeriodic);
    }

    /**
     *
     * @param subsystem
     */
    public static void reInitSubsystem(WolverinesSubsystem subsystem) {
        failedSubsystems.remove(subsystem);

        if (subsystem.getCurrentCommand() != null)
            subsystem.getCurrentCommand().cancel();     // TODO: Does this actually cancel the command?

        _initSubsystems(subsystem);

        notifier.stop();
        notifier = new Notifier(WolverinesSubsystem::defineReducedPeriodic);
        beginReducedPeriodic();
    }

    /**
     *
     * @param subsystems
     */
    private static void _initSubsystems(WolverinesSubsystem... subsystems) {

        for (WolverinesSubsystem ws : subsystems) {
            try {
                ws.init(isTestRobot);
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

    /**
     *
     * @return
     */
    protected boolean testSubsystem() {
        // Override me!
        return true;
    }

    /**
     *
     */
    public static void beginReducedPeriodic() {
        notifier.startPeriodic(PERIODIC_TIME);
    }

    /**
     *
     */
    private static void defineReducedPeriodic() {
        try {
            for (WolverinesSubsystem ws : subsystems) {
                ws.reducedPeriodic();
            }
        } catch (Exception ex) {
            // TODO: Remove from HashSet and report an error. Give the option to reinitialize
        }
    }

    /**
     *
     */
    protected void reducedPeriodic() {
        // Override me!
    }

}
