package org.whsrobotics.hardware;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.SPI;

public class Sensors {

    public static AHRS navX;

    public static AnalogPressureTransducer pressureSensor;

    public static void configureSensors() {
        navX = new AHRS(SPI.Port.kMXP);     // Use SPI because it's the fastest (see documentation)

        pressureSensor = new AnalogPressureTransducer(0);
    }

}
