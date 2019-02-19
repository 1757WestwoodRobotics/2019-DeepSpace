package org.whsrobotics.subsystems;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import org.whsrobotics.hardware.AnalogPressureTransducer;
import org.whsrobotics.utils.WolverinesSubsystem;

import static edu.wpi.first.wpilibj.DoubleSolenoid.Value.*;

public class PneumaticsBase extends WolverinesSubsystem {

    private static Compressor compressor;
    private static AnalogPressureTransducer pressureTransducer;

    private static DoubleSolenoid[] doubleSolenoids;

    public enum DoubleSolenoidModes {
        EXTENDED(kForward),
        RETRACTED(kReverse),
        NEUTRAL(kOff);

        private DoubleSolenoid.Value value;

        DoubleSolenoidModes(DoubleSolenoid.Value value) {

            this.value = value;
        }
    }

    public static void init(Compressor comp, AnalogPressureTransducer pressureTransducerSensor, DoubleSolenoid... solenoids) {
        compressor = comp;
        pressureTransducer = pressureTransducerSensor;

        doubleSolenoids = solenoids;
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
