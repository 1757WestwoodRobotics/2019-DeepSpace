package org.whsrobotics.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;

import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import org.whsrobotics.commands.Drive;
import org.whsrobotics.robot.Constants.Math;
import org.whsrobotics.utils.WolverinesSubsystem;

import static org.whsrobotics.hardware.Actuators.*;

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

    private static double rawEncoderPositions;
    private static double rawEncoderVelocities;

    private static Drivetrain instance;

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

        if (onTestRobot) {

            leftASpark = MotorControllers.leftA;
            leftBSpark = MotorControllers.leftB;
            rightASpark = MotorControllers.rightA;
            rightBSpark = MotorControllers.rightB;

            leftDrive = new SpeedControllerGroup(leftASpark, leftBSpark);
            rightDrive = new SpeedControllerGroup(rightASpark, rightBSpark);

        } else {

            leftASpark = MotorControllers.leftA;
            leftBSpark = MotorControllers.leftB;
            leftCSpark = MotorControllers.leftC;
            rightASpark = MotorControllers.rightA;
            rightBSpark = MotorControllers.rightB;
            rightCSpark = MotorControllers.rightC;

            leftDrive = new SpeedControllerGroup(leftASpark, leftBSpark, leftCSpark);
            rightDrive = new SpeedControllerGroup(rightASpark, rightBSpark, rightCSpark);

        }

        rightDrive.setInverted(true);

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

    public static void arcadeDrive(double xSpeed, double zRotation){
        differentialDrive.arcadeDrive(xSpeed, zRotation);
    }

    public static void tankDrive(double leftSpeed, double rightSpeed){
        differentialDrive.tankDrive(leftSpeed, rightSpeed);
    }

    public static void setCoastMode(){
        leftASpark.setIdleMode(IdleMode.kCoast);
        leftBSpark.setIdleMode(IdleMode.kCoast);
        leftCSpark.setIdleMode(IdleMode.kCoast);
        rightASpark.setIdleMode(IdleMode.kCoast);
        rightBSpark.setIdleMode(IdleMode.kCoast);
        rightCSpark.setIdleMode(IdleMode.kCoast);
    }

    public static void setBrakeMode(){
        leftASpark.setIdleMode(IdleMode.kBrake);
        leftBSpark.setIdleMode(IdleMode.kBrake);
        leftCSpark.setIdleMode(IdleMode.kBrake);
        rightASpark.setIdleMode(IdleMode.kBrake);
        rightBSpark.setIdleMode(IdleMode.kBrake);
        rightCSpark.setIdleMode(IdleMode.kBrake);
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
        //Find the vlaue above (not 1)
    
        return MPS;
    }

    @Override
    public void periodic() {

       // SmartDashboard.putNumber("Enocder Position in Meters", rawPositionsToMeters(rawEncoderPositions[0]));

        // System.out.println("Running Drivetrain periodic");

//        getEncoderTelemetry();
//        SmartDashboard.putNumberArray("Raw Encoder Positions", rawEncoderPositions);
//        SmartDashboard.putNumberArray("Raw Encoder Velocities", rawEncoderVelocities);
//
//        rawPositionsToMeters(rawEncoderPositions[0]);


        // TODO: Sean – report Spark Max currents and temperature (individually)

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