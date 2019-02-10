package org.whsrobotics.subsystems;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;

public class Actuators {

    private static TalonSRX ballScrewTalon;

    public static void configureActuators() {

        ballScrewTalon = new TalonSRX(7);
        ballScrewTalon.configFactoryDefault();


    }

}
