package org.whsrobotics.subsystems;

import java.lang.module.ModuleDescriptor.Requires;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import com.revrobotics.CANSparkMaxLowLevel.ConfigParameter;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.sun.javadoc.Type;

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

    public static void init() {

        //TODO: Tune Spark IDs
        LeftA = new CANSparkMax(2, CANSparkMaxLowLevel.MotorType.kBrushless);
        LeftB = new CANSparkMax(4, CANSparkMaxLowLevel.MotorType.kBrushless);
        LeftC = new CANSparkMax(6, CANSparkMaxLowLevel.MotorType.kBrushless);
        RightA = new CANSparkMax(1, CANSparkMaxLowLevel.MotorType.kBrushless);
        RightB = new CANSparkMax(3, CANSparkMaxLowLevel.MotorType.kBrushless);
        RightC = new CANSparkMax(5, CANSparkMaxLowLevel.MotorType.kBrushless);

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