package org.whsrobotics.robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import edu.wpi.first.wpilibj.TimedRobot;
import org.whsrobotics.hardware.Actuators;
import org.whsrobotics.subsystems.HatchMech;

import static org.whsrobotics.hardware.Actuators.*;

/**
 *
 */
public class Robot extends TimedRobot {

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    @Override
    public void robotInit() {

        Actuators.configureActuators();

        HatchMech.init(MotorControllers.topServo, MotorControllers.bottomServo, MotorControllers.ballScrewTalon);

        SmartDashboard.putNumber("LS", 0.0);
        SmartDashboard.putNumber("RS", 0.0);


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
//
//        new Drive().start();

        HatchMech.getTopServo().set(SmartDashboard.getNumber("LS", 0.0));
        HatchMech.getBottomServo().set(SmartDashboard.getNumber("RS", 0.0));

        // Scheduler.getInstance().run();

    }

    /**
     * This function is called periodically during test mode.
     */
    @Override
    public void testPeriodic() {
    }
}
