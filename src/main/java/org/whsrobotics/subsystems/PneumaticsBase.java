package org.whsrobotics.subsystems;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.whsrobotics.commands.Compress;
import org.whsrobotics.hardware.AnalogPressureTransducer;
import org.whsrobotics.robot.Constants;
import org.whsrobotics.robot.OI;
import org.whsrobotics.utils.WolverinesSubsystem;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import static edu.wpi.first.wpilibj.DoubleSolenoid.Value.*;

public class PneumaticsBase extends WolverinesSubsystem {

    private static Compressor compressor;
    private static AnalogPressureTransducer pressureTransducer;
    private static HashSet<DoubleSolenoid> doubleSolenoids;

    public static PneumaticsBase instance;

    public static PneumaticsBase getInstance() {
        if (instance == null) {
            instance = new PneumaticsBase();
        }
        return instance;
    }

    private PneumaticsBase() {
        super(true);
    }

    @Override
    protected void init(boolean onTestRobot) {
        compressor = new Compressor(Constants.canID.PCM_B.id);
        compressor.clearAllPCMStickyFaults();
        compressor.setClosedLoopControl(true);

        pressureTransducer = new AnalogPressureTransducer(0);

        doubleSolenoids = new HashSet<>();
    }

    public enum DoubleSolenoidModes {
        EXTENDED(kForward),
        RETRACTED(kReverse),
        NEUTRAL(kOff);

        public DoubleSolenoid.Value value;

        DoubleSolenoidModes(DoubleSolenoid.Value value) {
            this.value = value;
        }

        private static final HashMap<DoubleSolenoid.Value, DoubleSolenoidModes> table = new HashMap<>();

        static {
            Arrays.stream(DoubleSolenoidModes.values()).forEach(dsm -> table.put(dsm.value, dsm));
        }

        public static DoubleSolenoidModes lookup(DoubleSolenoid.Value value) {
            return table.get(value);
        }

    }

    public enum SingleSolenoidModes {
        EXTENDED(true),
        RETRACTED(false);

        public boolean value;

        SingleSolenoidModes(boolean value) {
            this.value = value;
        }

    }
    

    @Override
    protected void reducedPeriodic() {
        SmartDashboard.putNumber("Pressure (psi)", getPressure());
        SmartDashboard.putBoolean("Pressure Switch", getPressureSwitchState());

        OI.getRobotTable().getEntry("compressor").setBoolean(getCompressorState());
        OI.getRobotTable().getEntry("compressor_current").setDouble(getCompressorCurrent());

        // Update NetworkTables status for each DoubleSolenoid
        doubleSolenoids.forEach(doubleSolenoid ->
                OI.getRobotTable().getEntry(doubleSolenoid.getName())
                        .setString(DoubleSolenoidModes.lookup(doubleSolenoid.get()).toString()));

    }

    public static Compressor getCompressor(){
        return compressor;
    }

    @Override
    protected void initDefaultCommand() {
        setDefaultCommand(new Compress());
    }

    public static void setDoubleSolenoidPosition(DoubleSolenoid solenoid, DoubleSolenoidModes mode) {
        solenoid.set(mode.value);
    }

    public static void setSingleSolenoidPosition(Solenoid solenoid, SingleSolenoidModes mode) {
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

    public static double getPressure() {
        return pressureTransducer.getPSI();
    }

    public static void registerDoubleSolenoid(DoubleSolenoid... ds) {
        doubleSolenoids.addAll(Arrays.asList(ds));
    }

}