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
    private static final double PERIODIC_TIME = 0.25;        // seconds

    // Collections for the subsystem references
    private static volatile HashSet<WolverinesSubsystem> subsystems;
    private static volatile HashSet<WolverinesSubsystem> failedSubsystems;

    // The class that feeds the loop and keeps track of the time between loops
    private static Notifier notifier;

    private static boolean isTestRobot;

    private boolean isMissionCritical;
    private boolean hasSuccessfulInit = false;

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
    public static void initSubsystems(boolean isTestRobotHardware, WolverinesSubsystem... subsystems) {
        isTestRobot = isTestRobotHardware;
        _initSubsystems(subsystems);
    }

    /**
     *
     * @param subsystem
     */
    public static void reInitSubsystem(WolverinesSubsystem subsystem) {
        failedSubsystems.remove(subsystem);

        if (subsystem.getCurrentCommand() != null)
            subsystem.getCurrentCommand().cancel();     // TODO: Does this actually cancel the non-interruptible command?

        _initSubsystems(subsystem);

        // ENABLE ONLY IF the notifier breaks
//        notifier.stop();
//        notifier.setHandler(WolverinesSubsystem::defineReducedPeriodic);
//        beginReducedPeriodic();
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
                ws.hasSuccessfulInit = true;

            } catch (Exception ex) {

                failedSubsystems.add(ws);

                // Sets an un-interruptible command that requires(subsystem) -> locks the Scheduler

                (new Command() {

                    {
                        requires(ws);
                        setInterruptible(false);    // TODO: Check if this actually locks the subsystem
                        setRunWhenDisabled(true);
                        setName(ws.getName() + "-BLOCKED");
                    }

                    @Override
                    protected boolean isFinished() {
                        return false;
                    }

                }).start();

                if (ws.isMissionCritical)
                    throw new RuntimeException("**** ERROR: Unable to initialize mission-critical " + ws.getName() + ex.getMessage() + " ****");
                else
                    DriverStation.reportWarning("* WARNING: Unable to initialize non-mission-critical " + ws.getName() + ex.getMessage() + " *", false);

            }
        }

        if (notifier == null) {
            notifier = new Notifier(WolverinesSubsystem::defineReducedPeriodic);
            notifier.setHandler(WolverinesSubsystem::defineReducedPeriodic);
            beginReducedPeriodic();
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
        for (WolverinesSubsystem ws : subsystems) {
            try {
                ws.reducedPeriodic();
            } catch (Exception ex) {

                ws.hasSuccessfulInit = false;
                subsystems.remove(ws);
                failedSubsystems.add(ws);

                if (ws.isMissionCritical)
                    throw new RuntimeException("**** ERROR: The mission-critical " + ws.getName() + " subsystem has stopped working! ****");
                else
                    DriverStation.reportWarning("* WARNING: The non-mission-critical " + ws.getName() + " subsystem has stopped working! *", true);

                // TODO: Push an update to Dashboard (for the option to reinit)
                // TODO: Test if removing a subsystem still calls its reducedPeriodic() method

            }
        }
    }

    /**
     *
     */
    protected void reducedPeriodic() {
        // Override me!
    }

    public boolean ensureInit() {
        return hasSuccessfulInit;
    }

}
