package org.whsrobotics.subsystems;

import com.revrobotics.CANSparkMax;

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

    public static void init(CANSparkMax leftA, CANSparkMax leftB, CANSparkMax leftC,
                     CANSparkMax rightA, CANSparkMax rightB, CANSparkMax rightC) {

        leftDrive = new SpeedControllerGroup(leftA, leftB, leftC);
        rightDrive = new SpeedControllerGroup(rightA, rightB, rightC);

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

    public static void tankDrive (double leftSpeed, double rightSpeed){
        differentialDrive.tankDrive(leftSpeed, rightSpeed);
    }

    @Override
    protected void initDefaultCommand() {
        
        //setDefaultCommand(new Drive());

    }
 
    public static void stopDrive() {
        
    }

}