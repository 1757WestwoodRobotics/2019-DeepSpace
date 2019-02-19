package org.whsrobotics.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import org.whsrobotics.utils.WolverinesSubsystem;

public class Superstructure extends WolverinesSubsystem {

    private static DoubleSolenoid extensionSolenoid;

    public static void init(DoubleSolenoid solenoid) {
        extensionSolenoid = solenoid;
    }

    @Override
    protected void initDefaultCommand() {

    }

    public enum SuperstructureState {
        EXTENDED,
        RETRACTED,
        NEUTRAL
    }

    public static void setSuperstructurePosition(SuperstructureState state) {

        switch (state) {
            case EXTENDED:
                extensionSolenoid.set(DoubleSolenoid.Value.kForward);
                break;
            case RETRACTED:
                extensionSolenoid.set(DoubleSolenoid.Value.kReverse);
                break;
            case NEUTRAL:
                extensionSolenoid.set(DoubleSolenoid.Value.kOff);
                break;
        }

    }

    public static SuperstructureState getSuperstructurePosition() {
        return null;    // TODO: Return based on state of the solenoid... either sensor or local variable
    }

}
