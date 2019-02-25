package org.whsrobotics.hardware;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.revrobotics.CANSparkMax;

import org.whsrobotics.robot.Constants.canID;


import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Servo;

import static com.revrobotics.CANSparkMaxLowLevel.MotorType.kBrushless;
import static org.whsrobotics.robot.Constants.SolenoidPorts.*;

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
        public static DoubleSolenoid dropArmsSolenoid;
        public static DoubleSolenoid floorHatchMechSolenoid;

    }

    public static void configureActuators(boolean onTestRobot) {

        Pneumatics.superstructureSolenoid = new DoubleSolenoid(SUPERSTRUCTURE.module, SUPERSTRUCTURE.a, SUPERSTRUCTURE.b);
        Pneumatics.superstructureSolenoid.setName("Superstructure Solenoid");

        Pneumatics.hatchMechSliderSolenoid = new DoubleSolenoid(HATCH_MECH_SLIDER.module, HATCH_MECH_SLIDER.a, HATCH_MECH_SLIDER.b);
        Pneumatics.dropArmsSolenoid = new DoubleSolenoid(LEFT_DROP.module, LEFT_DROP.a, LEFT_DROP.b);
        Pneumatics.floorHatchMechSolenoid = new DoubleSolenoid(RIGHT_DROP.module, RIGHT_DROP.a, RIGHT_DROP.b);

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
        Pneumatics.compressor = new Compressor(11);
        Pneumatics.compressor.clearAllPCMStickyFaults();
        Pneumatics.compressor.setClosedLoopControl(true);

    }


}
