package org.whsrobotics.robot;

import edu.wpi.first.wpilibj.command.Scheduler;

import edu.wpi.first.wpilibj.TimedRobot;
import org.whsrobotics.commands.Drive;
import org.whsrobotics.hardware.Actuators;
import org.whsrobotics.hardware.Sensors;
import org.whsrobotics.subsystems.*;

import static org.whsrobotics.hardware.Actuators.*;

public class Robot extends TimedRobot {

    private boolean onTestRobot = false;

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    @Override
    public void robotInit() {

        Actuators.configureActuators(onTestRobot);  // If fails, don't initialize subsystem
        Sensors.configureSensors();

//        ElectronicsSystem.init();

        Drivetrain.init(MotorControllers.leftA, MotorControllers.leftB, MotorControllers.leftC,
                MotorControllers.rightA,MotorControllers.rightB, MotorControllers.rightC);

//        HatchMechScott.init(MotorControllers.topServo, MotorControllers.bottomServo, MotorControllers.ballScrewTalon);
//
        PneumaticsBase.init(Pneumatics.compressor, Sensors.pressureTransducer);
        Superstructure.init(Pneumatics.superstructureSolenoid);

    }

    /**
     * This function is called every robot packet, no matter the mode. Use
     * this for items like diagnostics that you want ran during disabled,
     * autonomous, teleoperated and test.
     *
     */
    @Override
    public void robotPeriodic() {
//        (new ElectronicsSystem()).periodic();
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
