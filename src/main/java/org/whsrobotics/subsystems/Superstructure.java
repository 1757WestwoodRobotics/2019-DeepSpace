package org.whsrobotics.subsystems;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Solenoid;

import org.whsrobotics.utils.WolverinesSubsystem;

import static org.whsrobotics.robot.Constants.SolenoidPorts.*;
import static org.whsrobotics.subsystems.PneumaticsBase.*;

import org.whsrobotics.robot.OI;

public class Superstructure extends WolverinesSubsystem {

    public static Superstructure instance;

    private static DoubleSolenoid superstructureSolenoid;
    private static Solenoid rampReleaseSolenoid;
    

    private Superstructure() {
        super(false);
    }

    public static Superstructure getInstance() {
        if (instance == null) {
            instance = new Superstructure();
        }
        return instance;
    }

    @Override
    protected void init(boolean onTestRobot) {

        superstructureSolenoid = new DoubleSolenoid(SUPERSTRUCTURE.module, SUPERSTRUCTURE.a, SUPERSTRUCTURE.b);
        superstructureSolenoid.setName("superstructureSolenoid");

        rampReleaseSolenoid = new Solenoid(RAMP_RELEASE.module, RAMP_RELEASE.a);
        rampReleaseSolenoid.setName("rampReleaseSolenoid");

        PneumaticsBase.registerDoubleSolenoid(superstructureSolenoid);

        // ai = new AnalogInput(1);

    }

    @Override
    protected void reducedPeriodic() {
        OI.getRobotTable().getEntry("rampReleaseSolenoid").setBoolean(rampReleaseSolenoid.get());
        // OI.getRobotTable().getEntry("limitSwitch").setBoolean(!limitSwitch.get());
        
    }

    @Override
    public void periodic() {
        // System.out.println(ai.getVoltage());
    }

    @Override
    protected void initDefaultCommand() {

    }

    public static DoubleSolenoidModes getSuperstructurePosition() {
        return DoubleSolenoidModes.lookup(superstructureSolenoid.get());
    }

    public static DoubleSolenoid getSuperstructureSolenoid() {
        return superstructureSolenoid;
    }

    public static Solenoid getRampReleaseSolenoid() {
        return rampReleaseSolenoid;
    }

}
