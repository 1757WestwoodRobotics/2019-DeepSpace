package org.whsrobotics.hardware;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.revrobotics.CANSparkMax;

import edu.wpi.first.wpilibj.DriverStation;
import org.whsrobotics.robot.Constants.canID;


import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.Solenoid;

import static com.revrobotics.CANSparkMaxLowLevel.MotorType.kBrushless;
import static org.whsrobotics.robot.Constants.*;

public class Actuators {

    public static class MotorControllers {

        public static TalonSRX ballScrewTalon;
        public static Servo topServo; //top
        public static Servo bottomServo; // bottom

        public static CANSparkMax leftA;
        public static CANSparkMax leftB;
        public static CANSparkMax leftC;
        public static CANSparkMax rightA;
        public static CANSparkMax rightB;
        public static CANSparkMax rightC;

    }

    public static class Pneumatics {

        public static Compressor compressor;

        public static DoubleSolenoid superstructureSolenoid;
        public static DoubleSolenoid hatchMechSliderSolenoid;
        public static DoubleSolenoid leftDropSolenoid;
        public static DoubleSolenoid rightDropSolenoid;
		public static void SetDoubleSolenoid(Solenoid solenoid, boolean state) {
		}


    }

    public static void configureActuators(boolean onTestRobot) {

        Pneumatics.superstructureSolenoid = new DoubleSolenoid(0,1);

        try {
            MotorControllers.leftA = new CANSparkMax(canID.leftA.id, kBrushless);
            MotorControllers.leftB = new CANSparkMax(canID.leftB.id, kBrushless);
            MotorControllers.leftC = new CANSparkMax(canID.leftC.id, kBrushless);
            MotorControllers.rightA = new CANSparkMax(canID.rightA.id, kBrushless);
            MotorControllers.rightB = new CANSparkMax(canID.rightB.id, kBrushless);
            MotorControllers.rightC = new CANSparkMax(canID.rightC.id, kBrushless);

            MotorControllers.rightA.setInverted(true);
            MotorControllers.rightB.setInverted(true);
            MotorControllers.rightC.setInverted(true);

        } catch (NullPointerException ex) {

        }

//        // TODO: Error handling and reporting (try/catch)
//
//        try {
//            MotorControllers.ballScrewTalon = new TalonSRX(7);
//            MotorControllers.ballScrewTalon.configFactoryDefault();
//            MotorControllers.ballScrewTalon.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute);
//        } catch (NullPointerException ex) {
//
//        }
//
//        MotorControllers.topServo = new Servo(0);
//        MotorControllers.bottomServo = new Servo(1);
//
//        Pneumatics.compressor = new Compressor(0);
//        Pneumatics.compressor.clearAllPCMStickyFaults();
//        Pneumatics.compressor.setClosedLoopControl(true);
//
//        Pneumatics.superstructureSolenoid = new DoubleSolenoid(0, 1, 1);

    }


}
