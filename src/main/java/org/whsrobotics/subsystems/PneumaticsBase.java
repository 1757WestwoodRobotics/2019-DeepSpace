package org.whsrobotics.subsystems;

import edu.wpi.first.wpilibj.Compressor;
import org.whsrobotics.hardware.AnalogPressureTransducer;
import org.whsrobotics.utils.WolverinesSubsystem;

public class PneumaticsBase extends WolverinesSubsystem {

    private static Compressor compressor;
    private static AnalogPressureTransducer pressureTransducer;

    public enum DoubleSolenoidModes {
        EXTENDED,
        RETRACTED,
    }

    public static void init(Compressor comp, AnalogPressureTransducer pressureTransducerSensor) {
        compressor = comp;
        pressureTransducer = pressureTransducerSensor;
    }

    @Override
    protected void initDefaultCommand() {

    }

    public static boolean getCompressorState() {
        return compressor.enabled();
    }

    public static double getCompressorCurrent() {
        return compressor.getCompressorCurrent();
    }

    public static void startCompression() {
        compressor.start();
    }

    public static void stopCompression() {
        compressor.stop();
    }

    public static boolean getPressureSwitchState() {
        return compressor.getPressureSwitchValue();
    }

    public static double getPressureSensor() {
        return pressureTransducer.getPSI();
    }
}
