package org.whsrobotics.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import org.whsrobotics.utils.WolverinesSubsystem;

import static org.whsrobotics.subsystems.PneumaticsBase.*;

public class Superstructure extends WolverinesSubsystem {

    private static DoubleSolenoid extensionSolenoid;

    public static void init(DoubleSolenoid solenoid) {
        extensionSolenoid = solenoid;
    }

    @Override
    protected void initDefaultCommand() {

    }

    public static void setSuperstructurePosition(DoubleSolenoidModes mode) {
        extensionSolenoid.set(mode.value);
    }

    public static DoubleSolenoidModes getSuperstructurePosition() {
        return null;    // TODO: Return based on state of the solenoid... either sensor or local variable
    }

}
