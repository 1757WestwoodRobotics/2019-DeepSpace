package org.whsrobotics.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import org.whsrobotics.utils.WolverinesSubsystem;

import static org.whsrobotics.subsystems.PneumaticsBase.*;

public class Superstructure extends WolverinesSubsystem {

    public static Superstructure instance;

    private static DoubleSolenoid extensionSolenoid;

    private Superstructure() {
    }

    public static void init(DoubleSolenoid solenoid) {
        extensionSolenoid = solenoid;
        instance = new Superstructure();
    }

    @Override
    protected void initDefaultCommand() {

    }

    public static DoubleSolenoidModes getSuperstructurePosition() {
        return null;    // TODO: Return based on state of the solenoid... either sensor or local variable
    }

}
