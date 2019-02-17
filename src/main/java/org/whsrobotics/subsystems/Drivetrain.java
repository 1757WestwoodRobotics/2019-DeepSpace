package org.whsrobotics.subsystems;

import com.revrobotics.CANSparkMax;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import org.whsrobotics.utils.WolverinesSubsystem;

public class Drivetrain extends WolverinesSubsystem {

    private static CANSparkMax leftA;
    private static CANSparkMax leftB;
    private static CANSparkMax leftC;
    private static CANSparkMax rightA;
    private static CANSparkMax rightB;
    private static CANSparkMax rightC;

    private static SpeedControllerGroup leftDrive;
    private static SpeedControllerGroup rightDrive;

    private static DifferentialDrive differentialDrive;

    private static double[] encoderPosition;
    private static double[] encoderVelocity;

    public static void init(CANSparkMax leftA, CANSparkMax leftB, CANSparkMax leftC,
                     CANSparkMax rightA, CANSparkMax rightB, CANSparkMax rightC) {

        leftDrive = new SpeedControllerGroup(leftA, leftB, leftC);
        rightDrive = new SpeedControllerGroup(rightA, rightB, rightC);

        differentialDrive = new DifferentialDrive(leftDrive, rightDrive);

        encoderPosition = new double[6];
        encoderVelocity = new double[6];

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

    public static void tankDrive (double leftSpeed, double rightSpeed){
        differentialDrive.tankDrive(leftSpeed, rightSpeed);
    }

    // method that goes through all the CANSparkMaxs and adds the encoder positions and velocities to the proper array
    // void function header
    //      leftA.getEncoderPosition()
    //      leftB....
    //      ...
    //      rightC....
    //      leftA.getEncoderVelocity()
    //      ...
    //      rightC.getEncoderVelocity()
    //

    @Override
    protected void initDefaultCommand() {
        
        //setDefaultCommand(new Drive());

    }
 
    public static void stopDrive() {
        
    }

}