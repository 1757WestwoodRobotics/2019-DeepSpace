package org.whsrobotics.subsystems;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.Servo;
import org.whsrobotics.utils.WolverinesSubsystem;

public class HatchMech extends WolverinesSubsystem {

    private static Servo topServo;
    private static Servo bottomServo;
    private static TalonSRX ballScrewTalon;

    public static void init(Servo top, Servo bottom, TalonSRX talon) {
        topServo = top;
        bottomServo = bottom;
        ballScrewTalon = talon;

    }

    @Override
    protected void initDefaultCommand() {

    }

    public enum HatchMechState {
        LOCKED,
        FREE
    }

    public enum BallScrewMode {
        MANUAL,
        SETPOINT,
        AUTO
    }

    public static boolean verify() {
        return false;   //TODO
    }

    // Slide ballScrewTalon to position
    public static void moveLinear() { }
    public static void moveRotation() { }
    public static double convertRotationToLinear(double degrees) { return 0.0; }
    public static double convertLinearToRotations(double rotation) { return 0.0; }

    // Move servos to position
    public static void turnLeftServo() { }
    public static void turnRightServo() { }
    public static double negateServoDegrees(double degrees) { return 170 - degrees; }

    // Get hardware objects
    public static Servo getTopServo() { return topServo; }
    public static Servo getBottomServo() { return bottomServo; }
    public static TalonSRX getBallScrewTalon() { return ballScrewTalon; }


}
