package org.whsrobotics.robot;

import edu.wpi.first.wpilibj.command.Scheduler;

import edu.wpi.first.wpilibj.TimedRobot;
import org.whsrobotics.hardware.Sensors;
import org.whsrobotics.subsystems.*;
import org.whsrobotics.utils.WolverinesSubsystem;

public class Robot extends TimedRobot {

    public static final boolean isTestRobot = true;

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    @Override
    public void robotInit() {

        Sensors.configureSensors();

        WolverinesSubsystem.initSubsystems(isTestRobot,
                ElectronicsSystem.getInstance(),
                Drivetrain.getInstance(),
                PneumaticsBase.getInstance(),
                Superstructure.getInstance(),
                HatchMechJack.getInstance());

        OI.init();

        WolverinesSubsystem.beginReducedPeriodic();

    }

    /**
     * This function is called every robot packet, no matter the mode. Use
     * this for items like diagnostics that you want ran during disabled,
     * autonomous, teleoperated and test.
     *
     */
    @Override
    public void robotPeriodic() {

    }

    /**
     *
     */
    @Override
    public void autonomousInit() {

    }

    /**
     * This function is called periodically during autonomous.
     */
    @Override
    public void autonomousPeriodic() {
        Scheduler.getInstance().run();
    }

    public void teleopInit() {

    }

    /**
     * This function is called periodically during operator control.
     */
    @Override
    public void teleopPeriodic() {
        Scheduler.getInstance().run();
    }

    /**
     * This function is called periodically during test mode.
     */
    @Override
    public void testPeriodic() {

    }
}
