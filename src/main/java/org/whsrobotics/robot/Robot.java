package org.whsrobotics.robot;

import edu.wpi.first.wpilibj.command.Scheduler;

import edu.wpi.first.wpilibj.TimedRobot;
import org.whsrobotics.hardware.Actuators;
import org.whsrobotics.hardware.Sensors;
import org.whsrobotics.subsystems.*;
import org.whsrobotics.utils.WolverinesSubsystem;

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

        PneumaticsBase.loadHardwareReferences(Pneumatics.compressor, Sensors.pressureTransducer,
                Pneumatics.superstructureSolenoid,
                Pneumatics.hatchMechSliderSolenoid,
                Pneumatics.leftDropSolenoid,
                Pneumatics.rightDropSolenoid);


        WolverinesSubsystem.initSubsystems(onTestRobot,
                ElectronicsSystem.getInstance(),
                Drivetrain.getInstance(),
                PneumaticsBase.getInstance());

//        HatchMechScott.init(MotorControllers.topServo, MotorControllers.bottomServo, MotorControllers.ballScrewTalon);
//

        HatchMechJack.init();

        Superstructure.init(Pneumatics.superstructureSolenoid);

        OI.init();

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
