package org.whsrobotics.subsystems;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.revrobotics.CANSparkMax;

import com.revrobotics.CANSparkMaxLowLevel;
import edu.wpi.first.wpilibj.Servo;
import org.whsrobotics.robot.Constants;

import static com.revrobotics.CANSparkMaxLowLevel.MotorType.*;
import static org.whsrobotics.robot.Constants.*;

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

        MotorControllers.leftA = new CANSparkMax(leftAPort, kBrushless);
        MotorControllers.leftB = new CANSparkMax(leftBPort, kBrushless);
        MotorControllers.leftC = new CANSparkMax(leftCPort, kBrushless);
        MotorControllers.rightA = new CANSparkMax(rightAPort, kBrushless);
        MotorControllers.rightB = new CANSparkMax(rightBPort, kBrushless);
        MotorControllers.rightC = new CANSparkMax(rightCPort, kBrushless);

        MotorControllers.ballScrewTalon = new TalonSRX(7);
        MotorControllers.ballScrewTalon.configFactoryDefault();


    }

}
