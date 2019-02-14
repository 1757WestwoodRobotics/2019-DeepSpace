package org.whsrobotics.subsystems;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.Servo;

public class HatchMech {

    private static Servo leftServo;
    private static Servo rightServo;
    private static TalonSRX ballScrewTalon;

    public static void init(Servo left, Servo right, TalonSRX talon) {
        leftServo = left;
        rightServo = right;
        ballScrewTalon = talon;

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
        return false;   // TODO
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
    public static Servo getLeftServo() { return leftServo; }
    public static Servo getRightServo() { return rightServo; }
    public static TalonSRX getBallScrewTalon() { return ballScrewTalon; }


}
