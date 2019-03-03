package org.whsrobotics.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import org.whsrobotics.utils.WolverinesSubsystem;

import static org.whsrobotics.robot.Constants.SolenoidPorts.SUPERSTRUCTURE;
import static org.whsrobotics.subsystems.PneumaticsBase.*;

public class Superstructure extends WolverinesSubsystem {

    public static Superstructure instance;

    private static DoubleSolenoid superstructureSolenoid;

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
        superstructureSolenoid.setName("Superstructure Solenoid");

    }

    public static void init(DoubleSolenoid solenoid) {
        superstructureSolenoid = solenoid;
        instance = new Superstructure();
    }

    @Override
    protected void initDefaultCommand() {

    }

    public static DoubleSolenoidModes getSuperstructurePosition() {
        return null;    // TODO: Return based on state of the solenoid... either sensor or local variable
    }

    public static DoubleSolenoid getSuperstructureSolenoid() {
        return superstructureSolenoid;
    }

}
