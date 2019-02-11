package org.whsrobotics.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import org.whsrobotics.commands.Drive;

import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

public class Drivetrain extends Subsystem {

    private static CANSparkMax LeftA;
    private static CANSparkMax LeftB;
    private static CANSparkMax LeftC;
    private static CANSparkMax RightA;
    private static CANSparkMax RightB;
    private static CANSparkMax RightC;

    private static SpeedControllerGroup leftDrive;
    private static SpeedControllerGroup rightDrive;

    private static DifferentialDrive differentialDrive;

    public Drivetrain(CANSparkMax LeftA, CANSparkMax LeftB, CANSparkMax LeftC, CANSparkMax rightA, CANSparkMax rightB, CANSparkMax rightC) {

        //leftA = new CANSparkMax(1, MotorType.kBrushless);

        leftDrive = new SpeedControllerGroup(LeftA, LeftB, LeftC);
        rightDrive = new SpeedControllerGroup(RightA, RightB, RightC);

        differentialDrive = new DifferentialDrive(leftDrive, rightDrive);

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
        
        setDefaultCommand(new Drive());

    }
 
    public static void stopDrive() {
        
    }

}