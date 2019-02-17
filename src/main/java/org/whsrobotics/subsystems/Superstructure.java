package org.whsrobotics.subsystems;

import edu.wpi.first.wpilibj.Solenoid;
import org.whsrobotics.utils.WolverinesSubsystem;

public class Superstructure extends WolverinesSubsystem {

    private static Solenoid extensionSolenoid;

    public Superstructure(Solenoid solenoid) {
        extensionSolenoid = solenoid;
    }

    @Override
    protected void initDefaultCommand() {

    }

    public enum SuperstructureState {
        EXTENDED,
        RETRACTED
    }

    public static void setSuperstructurePosition(SuperstructureState state) {

        switch (state) {
            case EXTENDED:
                setExtensionSolenoid(true);
                break;
            case RETRACTED:
                setExtensionSolenoid(false);
                break;
        }
    }

    private static void setExtensionSolenoid(boolean on) {
        extensionSolenoid.set(on);
    }

    public static SuperstructureState getSuperstructurePosition() {
        return null;    // TODO: Return based on state of the solenoid... either sensor or local variable
    }

}
