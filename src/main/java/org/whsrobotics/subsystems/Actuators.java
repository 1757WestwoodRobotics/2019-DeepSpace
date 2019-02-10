package org.whsrobotics.subsystems;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.Servo;

public class Actuators {

    public static class MotorControllers {
        public static TalonSRX ballScrewTalon;
    }

    public static class Servos {
        public static Servo lS;
        public static Servo rS;
    }

    public static void configureActuators() {

        MotorControllers.ballScrewTalon = new TalonSRX(7);
        MotorControllers.ballScrewTalon.configFactoryDefault();


    }

}
