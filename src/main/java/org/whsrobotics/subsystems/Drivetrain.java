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

import static com.revrobotics.CANSparkMaxLowLevel.MotorType.kBrushless;

public class Drivetrain extends WolverinesSubsystem {

    private static CANSparkMax leftASpark;
    private static CANSparkMax leftBSpark;
    private static CANSparkMax leftCSpark;  // not on the test robot
    private static CANSparkMax rightASpark;
    private static CANSparkMax rightBSpark;
    private static CANSparkMax rightCSpark; // not on the test robot

    private static SpeedControllerGroup leftDrive;
    private static SpeedControllerGroup rightDrive;

    private static DifferentialDrive differentialDrive;

    private static AHRS navX;

    private static double rawEncoderPositions;
    private static double rawEncoderVelocities;

    private static Drivetrain instance;
    private static boolean testRobot;

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

        testRobot = onTestRobot;

        navX = new AHRS(SPI.Port.kMXP);     // Use SPI because it's the fastest (see documentation)
        navX.zeroYaw();

        leftASpark = new CANSparkMax(Constants.canID.LEFT_A.id, kBrushless);
        leftBSpark = new CANSparkMax(Constants.canID.LEFT_B.id, kBrushless);
        rightASpark = new CANSparkMax(Constants.canID.RIGHT_A.id, kBrushless);
        rightBSpark = new CANSparkMax(Constants.canID.RIGHT_B.id, kBrushless);

        if (onTestRobot) {
            leftDrive = new SpeedControllerGroup(leftASpark, leftBSpark);
            rightDrive = new SpeedControllerGroup(rightASpark, rightBSpark);

            leftASpark.setInverted(true);
            leftBSpark.setInverted(true);
            rightASpark.setInverted(true);
            rightBSpark.setInverted(true);

        } else {
            leftCSpark = new CANSparkMax(Constants.canID.LEFT_C.id, kBrushless);
            rightCSpark = new CANSparkMax(Constants.canID.RIGHT_C.id, kBrushless);

            leftDrive = new SpeedControllerGroup(leftASpark, leftBSpark, leftCSpark);
            rightDrive = new SpeedControllerGroup(rightASpark, rightBSpark, rightCSpark);

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

    public static void setIdleMode(IdleMode idleMode){
        leftASpark.setIdleMode(idleMode);
        leftBSpark.setIdleMode(idleMode);
        rightASpark.setIdleMode(idleMode);
        rightBSpark.setIdleMode(idleMode);

        if (!testRobot) {
            leftCSpark.setIdleMode(idleMode);
            rightCSpark.setIdleMode(idleMode);
        }

    }

    public static void setSparkMaxSmartCurrentLimit(int amps) {
        leftASpark.setSmartCurrentLimit(amps);
        leftBSpark.setSmartCurrentLimit(amps);
        rightASpark.setSmartCurrentLimit(amps);
        rightBSpark.setSmartCurrentLimit(amps);

        if (!testRobot) {
            leftCSpark.setSmartCurrentLimit(amps);
            rightCSpark.setSmartCurrentLimit(amps);
        }

    }

    public static void getEncoderTelemetry() {
        rawEncoderPositions = leftASpark.getEncoder().getPosition();
        rawEncoderVelocities = leftASpark.getEncoder().getVelocity();
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

       // SmartDashboard.putNumber("Encoder Position in Meters", rawPositionsToMeters(rawEncoderPositions[0]));

        // System.out.println("Running Drivetrain periodic");

//        getEncoderTelemetry();
//        SmartDashboard.putNumberArray("Raw Encoder Positions", rawEncoderPositions);
//        SmartDashboard.putNumberArray("Raw Encoder Velocities", rawEncoderVelocities);
//
//        rawPositionsToMeters(rawEncoderPositions[0]);


        // TODO: Sean – report Spark Max currents and temperature (individually)


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