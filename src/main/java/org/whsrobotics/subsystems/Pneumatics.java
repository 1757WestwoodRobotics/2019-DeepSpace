package org.whsrobotics.subsystems;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Pneumatics extends Subsystem {

    Compressor compressor;


    public enum DoubleSolenoidModes {
        EXTENDED,
        RETRACTED,
    }

    @Override
    protected void initDefaultCommand() {

    }



}