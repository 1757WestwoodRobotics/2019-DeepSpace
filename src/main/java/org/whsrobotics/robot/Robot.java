package org.whsrobotics.robot;

import edu.wpi.first.wpilibj.command.Scheduler;

import edu.wpi.first.wpilibj.TimedRobot;
import org.whsrobotics.commands.Drive;
import org.whsrobotics.hardware.Actuators;
import org.whsrobotics.hardware.Sensors;
import org.whsrobotics.subsystems.Drivetrain;
import org.whsrobotics.subsystems.HatchMechScott;
import org.whsrobotics.subsystems.PneumaticsBase;

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

        Drivetrain.init(MotorControllers.leftA, MotorControllers.leftB, MotorControllers.leftC,
                MotorControllers.rightA,MotorControllers.rightB, MotorControllers.rightC);

//        HatchMechScott.init(MotorControllers.topServo, MotorControllers.bottomServo, MotorControllers.ballScrewTalon);
//
//        PneumaticsBase.init(Pneumatics.compressor, Sensors.pressureTransducer);

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

    }

    public void teleopInit() {
        (new Drive()).start();
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
