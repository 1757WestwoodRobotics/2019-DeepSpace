package org.whsrobotics.subsystems;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.revrobotics.CANSparkMax;

import edu.wpi.first.wpilibj.Servo;

public class Actuators {

    public static class MotorControllers {
        public static TalonSRX ballScrewTalon;
        public static Servo lS;
        public static Servo rS;

        public static CANSparkMax leftA;
        public static CANSparkMax leftB;
        public static CANSparkMax leftC;
        public static CANSparkMax rightA;
        public static CANSparkMax rightB;
        public static CANSparkMax rightC;
    }

    public static class Pneumatics {

    }

    public static void configureActuators() {

        MotorControllers.ballScrewTalon = new TalonSRX(7);
        MotorControllers.ballScrewTalon.configFactoryDefault();


    }

}
