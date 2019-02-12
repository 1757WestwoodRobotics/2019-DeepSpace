package org.whsrobotics.subsystems;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.Servo;

public class Actuators {

    public static class MotorControllers {
        public static TalonSRX ballScrewTalon;
        public static Servo lS;
        public static Servo rS;
    }

    public static class Pneumatics {

    }

    public static void configureActuators() {

        // TODO: Error handling and reporting (try/catch)

        MotorControllers.ballScrewTalon = new TalonSRX(7);
        MotorControllers.ballScrewTalon.configFactoryDefault();
        MotorControllers.ballScrewTalon.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute);

    }

}
