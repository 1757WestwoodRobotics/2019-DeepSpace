package org.whsrobotics.subsystems;

import com.kauailabs.navx.frc.AHRS;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import org.whsrobotics.commands.Drive;
import org.whsrobotics.robot.Constants;
import org.whsrobotics.robot.Constants.Math;
import org.whsrobotics.robot.OI;
import org.whsrobotics.utils.WolverinesSubsystem;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.revrobotics.CANSparkMaxLowLevel.MotorType.kBrushless;

public class Drivetrain extends WolverinesSubsystem {

    private static CANSparkMax[] leftSet;
    private static CANSparkMax[] rightSet;

    private static SpeedControllerGroup leftDrive;
    private static SpeedControllerGroup rightDrive;

    private static DifferentialDrive differentialDrive;

    private static AHRS navX;

    private static double leftEncoderPosition;
    private static double leftEncoderVelocity;
    private static double rightEncoderPosition;
    private static double rightEncoderVelocity;

    private static Drivetrain instance;

    private static DrivetrainSpeedMode drivetrainSpeedMode = DrivetrainSpeedMode.SLOW;

    public static Drivetrain getInstance() {
        if (instance == null) {
            instance = new Drivetrain();
        }
        return instance;
    }

    private Drivetrain() {
        super(true);
    }

    public void init(boolean onTestRobot) {

        navX = new AHRS(SPI.Port.kMXP);     // Use SPI because it's the fastest (see documentation)
        navX.zeroYaw();

        leftSet = new CANSparkMax[3];
        rightSet = new CANSparkMax[3];

        leftSet[0] = new CANSparkMax(Constants.canID.LEFT_A.id, kBrushless);
        leftSet[1] = new CANSparkMax(Constants.canID.LEFT_B.id, kBrushless);
        rightSet[0] = new CANSparkMax(Constants.canID.RIGHT_A.id, kBrushless);
        rightSet[1] = new CANSparkMax(Constants.canID.RIGHT_B.id, kBrushless);

        if (onTestRobot) {
            leftDrive = new SpeedControllerGroup(leftSet[0], leftSet[1]);
            rightDrive = new SpeedControllerGroup(rightSet[0], rightSet[1]);

            leftSet[0].setInverted(true);
            leftSet[1].setInverted(true);
            rightSet[0].setInverted(true);
            rightSet[1].setInverted(true);

        } else {
            leftSet[2] = new CANSparkMax(Constants.canID.LEFT_C.id, kBrushless);
            rightSet[2] = new CANSparkMax(Constants.canID.RIGHT_C.id, kBrushless);

            leftDrive = new SpeedControllerGroup(leftSet[0], leftSet[1], leftSet[2]);
            rightDrive = new SpeedControllerGroup(rightSet[0], rightSet[1], rightSet[2]);

        }

        differentialDrive = new DifferentialDrive(leftDrive, rightDrive);

    }

    public enum DrivetrainAutoState {
        MANUAL,
        SEMIAUTO,
        FULLAUTO
    }

    public enum DrivetrainSpeedMode {
        SLOW,
        FAST
    }

    // -----Drivetrain Methods----- //

    public static void arcadeDrive(double xSpeed, double zRotation, boolean squaredInputs) {
        differentialDrive.arcadeDrive(xSpeed, zRotation, squaredInputs);
    }

    public static void arcadeDrive(double xSpeed, double zRotation) {
        if (drivetrainSpeedMode == DrivetrainSpeedMode.FAST)
            arcadeDrive(xSpeed,
                    zRotation * Constants.ROTATION_FACTOR,
                    false);
        else if (drivetrainSpeedMode == DrivetrainSpeedMode.SLOW) {
            arcadeDrive(xSpeed * Constants.MAX_SLOW_DRIVETRAIN,
                    zRotation * Constants.ROTATION_FACTOR * Constants.MAX_SLOW_DRIVETRAIN,
                    false);
        }
    }

    public static void setDrivetrainSpeedMode(DrivetrainSpeedMode mode) {
        drivetrainSpeedMode = mode;

        if (mode == DrivetrainSpeedMode.FAST) {
            setSparkMaxSmartCurrentLimit(80);
        } else if (mode == DrivetrainSpeedMode.SLOW) {
            setSparkMaxSmartCurrentLimit(60);
        }

    }

//    public static void tankDrive(double leftSpeed, double rightSpeed) {
//        differentialDrive.tankDrive(leftSpeed, rightSpeed);
//    }

    // Contains nulls or 0.0!!!
    public static double[] getMotorCurrents() {
        return Stream.of(leftSet, rightSet)
                .flatMap(Stream::of)
                .mapToDouble(CANSparkMax::getOutputCurrent)
                .toArray();
    }

    // Contains nulls!!!
    public static double[] getMotorTemperatures() {
        return Stream.of(leftSet, rightSet)
                .flatMap(Stream::of)
                .mapToDouble(CANSparkMax::getMotorTemperature)
                .toArray();
    }

    public static void setIdleMode(IdleMode idleMode) {
        Stream.of(leftSet, rightSet)
                .flatMap(Stream::of)
                .filter(Objects::nonNull)
                .forEach(c -> c.setIdleMode(idleMode));
    }

    public static void setSparkMaxSmartCurrentLimit(int amps) {
         Stream.of(leftSet, rightSet)
                .flatMap(Stream::of)
                .filter(Objects::nonNull)
                .forEach(c -> c.setSmartCurrentLimit(amps));
    }

    public static void getEncoderTelemetry() {
        Arrays.stream(leftSet)
                .filter(Objects::nonNull)
                .findFirst()
                .ifPresent(c -> {
            leftEncoderPosition = c.getEncoder().getPosition();
            leftEncoderVelocity = c.getEncoder().getVelocity();
        });

        Arrays.stream(rightSet)
                .filter(Objects::nonNull)
                .findFirst()
                .ifPresent(c -> {
            rightEncoderPosition = c.getEncoder().getPosition();
            rightEncoderVelocity = c.getEncoder().getVelocity();
        });
    }

    /*
    Unit conversion from rawEncoderPositions to meters
    y = 1.9x/42
    y = meters, x = # of ticks
    1.9/42 = kConversionConstant (see Constants.java)
    */

    public static double rawPositionsToMeters(double rawEncoderPosition) {

        double meters = (rawEncoderPosition * Math.kConversionConstant.value);

        return meters;
    }
    
    
    // Unit conversion from rawEncoderVelocities to meters/second

    public static double rawVelocitiesToMetersPerSec(double rawEncoderVelocities) {
        
        double MPS = (rawEncoderVelocities / 1);
        //Find the value above (not 1)
    
        return MPS;
    }

    @Override
    public void reducedPeriodic() {

        double[] currents = getMotorCurrents();
        double[] temperatures = getMotorTemperatures();

        for (int i = 0; i < currents.length; i++) {

            OI.getRobotTable().getSubTable("SparkMax").getSubTable(String.valueOf(i))
                    .getEntry("current").setDouble(currents[i]);

            OI.getRobotTable().getSubTable("SparkMax").getSubTable(String.valueOf(i))
                    .getEntry("temperature").setDouble(temperatures[i]);

        }

        OI.getRobotTable().getEntry("angle").setDouble(navX.getAngle());        // Navx yaw angle (Z axis)
    }

    @Override
    protected void initDefaultCommand() {
        setDefaultCommand(new Drive());
    }
 
    public static void stopDrive() {
        differentialDrive.stopMotor();
    }

    // ------------ ENCODER PID ------------- //



}