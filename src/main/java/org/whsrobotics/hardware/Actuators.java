package org.whsrobotics.hardware;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.Servo;

public class Actuators {

    public static class MotorControllers {
        public static TalonSRX ballScrewTalon;
        public static Servo topServo; //top
        public static Servo bottomServo; // bottom
    }

    public static class Pneumatics {

    }

    public static void configureActuators() {

        // TODO: Error handling and reporting (try/catch)

        try {
            MotorControllers.ballScrewTalon = new TalonSRX(7);
            MotorControllers.ballScrewTalon.configFactoryDefault();
            MotorControllers.ballScrewTalon.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute);
        } catch (NullPointerException ex) {

        }



        MotorControllers.topServo = new Servo(0);
        MotorControllers.bottomServo = new Servo(1);

    }

}
