package org.whsrobotics.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import com.revrobotics.CANSparkMaxLowLevel.ConfigParameter;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.command.Scheduler;
import org.whsrobotics.commands.Drive;

import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

public class Drivetrain extends Subsystem {

    private static CANSparkMax LeftA;
    private static CANSparkMax LeftB;
    private static CANSparkMax RightA;
    private static CANSparkMax RightB;

    private static SpeedControllerGroup leftDrive;
    private static SpeedControllerGroup rightDrive;

    private static DifferentialDrive differentialDrive;

    private static Drivetrain instance;

    public static void init() {

        instance = new Drivetrain();    // Used to invoke the superconstructor

        //TODO: Tune Spark IDs
        LeftA = new CANSparkMax(2, CANSparkMaxLowLevel.MotorType.kBrushless);
        LeftB = new CANSparkMax(4, CANSparkMaxLowLevel.MotorType.kBrushless);
        RightA = new CANSparkMax(1, CANSparkMaxLowLevel.MotorType.kBrushless);
        RightB = new CANSparkMax(3, CANSparkMaxLowLevel.MotorType.kBrushless);

        leftDrive = new SpeedControllerGroup(LeftA, LeftB);
        rightDrive = new SpeedControllerGroup(RightA, RightB);

        differentialDrive = new DifferentialDrive(leftDrive, rightDrive);

    }

    private Drivetrain() {

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