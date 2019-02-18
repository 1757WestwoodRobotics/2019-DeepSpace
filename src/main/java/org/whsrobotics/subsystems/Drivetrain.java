package org.whsrobotics.subsystems;

import com.revrobotics.CANSparkMax;

import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.whsrobotics.commands.Drive;
import org.whsrobotics.utils.WolverinesSubsystem;

public class Drivetrain extends WolverinesSubsystem {

    private static CANSparkMax leftASpark;
    private static CANSparkMax leftBSpark;
    private static CANSparkMax leftCSpark;
    private static CANSparkMax rightASpark;
    private static CANSparkMax rightBSpark;
    private static CANSparkMax rightCSpark;

    private static SpeedControllerGroup leftDrive;
    private static SpeedControllerGroup rightDrive;

    private static DifferentialDrive differentialDrive;

    private static double[] rawEncoderPositions;
    private static double[] rawEncoderVelocities;

    public static Drivetrain instance;
    

    private Drivetrain() {

    }

    public static void init(CANSparkMax leftA, CANSparkMax leftB, CANSparkMax leftC,
                     CANSparkMax rightA, CANSparkMax rightB, CANSparkMax rightC) {

        leftASpark = leftA;
        leftBSpark = leftB;
        leftCSpark = leftC;
        rightASpark = rightA;
        rightBSpark = rightB;
        rightCSpark = rightC;

        leftDrive = new SpeedControllerGroup(leftASpark, leftBSpark, leftCSpark);
        rightDrive = new SpeedControllerGroup(rightASpark, rightBSpark, rightCSpark);

        differentialDrive = new DifferentialDrive(leftDrive, rightDrive);

        rawEncoderPositions = new double[6];
        rawEncoderVelocities = new double[6];

        instance = new Drivetrain();

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

    public static void getEncoderTelemetry() {

        rawEncoderPositions[0] = leftASpark.getEncoder().getPosition();
        rawEncoderPositions[1] = leftBSpark.getEncoder().getPosition();
        rawEncoderPositions[2] = leftCSpark.getEncoder().getPosition();
        rawEncoderPositions[3] = rightASpark.getEncoder().getPosition();
        rawEncoderPositions[4] = rightBSpark.getEncoder().getPosition();
        rawEncoderPositions[5] = rightCSpark.getEncoder().getPosition();

        rawEncoderVelocities[0] = leftASpark.getEncoder().getVelocity();
        rawEncoderVelocities[1] = leftBSpark.getEncoder().getVelocity();
        rawEncoderVelocities[2] = leftCSpark.getEncoder().getVelocity();
        rawEncoderVelocities[3] = rightASpark.getEncoder().getVelocity();
        rawEncoderVelocities[4] = rightBSpark.getEncoder().getVelocity();
        rawEncoderVelocities[5] = rightCSpark.getEncoder().getVelocity();
    }

    // Unit Conversion from rawEncoderPositions to meters

    public static double rawPositionsToMeters(double rawEncoderPosition) {

        double meters = (rawEncoderPosition / 1.9);

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

        getEncoderTelemetry();
        SmartDashboard.putNumberArray("Raw Encoder Positions", rawEncoderPositions);
        SmartDashboard.putNumberArray("Raw Encoder Velocities", rawEncoderVelocities);

        rawPositionsToMeters(rawEncoderPositions[0]);
    }

    @Override
    protected void initDefaultCommand() {
        
        setDefaultCommand(new Drive());

    }
 
    public static void stopDrive() {
        differentialDrive.stopMotor();
    }

}