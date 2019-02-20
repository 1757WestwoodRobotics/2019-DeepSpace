package org.whsrobotics.subsystems;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Command;

import org.whsrobotics.commands.Compress;
import org.whsrobotics.hardware.AnalogPressureTransducer;
import org.whsrobotics.utils.WolverinesSubsystem;

import static edu.wpi.first.wpilibj.DoubleSolenoid.Value.*;

public class PneumaticsBase extends WolverinesSubsystem {

    private static Compressor compressor;
    private static AnalogPressureTransducer pressureTransducer;

    private static DoubleSolenoid[] doubleSolenoids;

    public static PneumaticsBase instance;

    private PneumaticsBase() {
    }

    public enum DoubleSolenoidModes {
        EXTENDED(kForward),
        RETRACTED(kReverse),
        NEUTRAL(kOff);

        public DoubleSolenoid.Value value;

        DoubleSolenoidModes(DoubleSolenoid.Value value) {

            this.value = value;
        }
    }

    public static Compressor getCompressor(){
        return compressor;
    }

    public static void init(Compressor comp, AnalogPressureTransducer pressureTransducerSensor, DoubleSolenoid... solenoids) {
        compressor = comp;
        pressureTransducer = pressureTransducerSensor;

        doubleSolenoids = solenoids;

        instance = new PneumaticsBase();
    }

    @Override
    protected void initDefaultCommand() {
        setDefaultCommand(new Compress());
    }

    public static void setSolenoidPosition(DoubleSolenoid solenoid, DoubleSolenoidModes mode) {
        solenoid.set(mode.value);
    }

    public static boolean getCompressorState() {
        return compressor.enabled();
    }

    public static double getCompressorCurrent() {
        return compressor.getCompressorCurrent();
    }

    public static void startCompression(Compressor compressor) {
        compressor.start();
    }

    public static void stopCompression(Compressor compressor) {
        compressor.stop();
    }

    public static boolean getPressureSwitchState() {
        return compressor.getPressureSwitchValue();
    }

    public static double getPressureTransducer() {
        return pressureTransducer.getPSI();
    }
   
}