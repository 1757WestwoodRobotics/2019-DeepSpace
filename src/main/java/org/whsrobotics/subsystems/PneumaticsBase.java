package org.whsrobotics.subsystems;

import edu.wpi.first.wpilibj.Compressor;
import org.whsrobotics.utils.WolverinesSubsystem;

public class PneumaticsBase extends WolverinesSubsystem {

    private static Compressor compressor;

    public enum DoubleSolenoidModes {
        EXTENDED,
        RETRACTED,
    }

    public static void init(Compressor comp) {
        compressor = comp;
    }

    @Override
    protected void initDefaultCommand() {

    }
}
