package org.whsrobotics.robot;

import org.whsrobotics.commands.Drive;
import org.whsrobotics.subsystems.Drivetrain;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Scheduler;
import org.whsrobotics.subsystems.HatchMech;

import static org.whsrobotics.subsystems.Actuators.*;

/**
 *
 */
public class Robot extends TimedRobot {

    HatchMech hatchMech;
    Drivetrain drivetrain;

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    @Override
    public void robotInit() {

        hatchMech = new HatchMech(MotorControllers.lS, MotorControllers.rS, MotorControllers.ballScrewTalon);
        drivetrain = new Drivetrain(MotorControllers.leftA, MotorControllers.leftB, MotorControllers.leftC, MotorControllers.rightA, MotorControllers.rightB, MotorControllers.rightB);

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
     * This autonomous (along with the chooser code above) shows how to select
     * between different autonomous modes using the dashboard. The sendable
     * chooser code works with the Java SmartDashboard. If you prefer the
     * LabVIEW Dashboard, remove all of the chooser code and uncomment the
     * getString line to get the auto name from the text box below the Gyro
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

    }

    /**
     * This function is called periodically during operator control.
     */
    @Override
    public void teleopPeriodic() {

        new Drive().start();

        Scheduler.getInstance().run();

    }

    /**
     * This function is called periodically during test mode.
     */
    @Override
    public void testPeriodic() {
    }
}
