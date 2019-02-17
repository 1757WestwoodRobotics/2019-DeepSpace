package org.whsrobotics.hardware;

import edu.wpi.first.wpilibj.AnalogInput;

/**
 * Class to access data from an analog Pressure Transducer (sensor) (SSI Technologies - P51-200-G-A-I36-4.5V-000-000)
 * Uses WPILib's AnalogInput to access sensor data.
 *
 * @version 1.1 - Fixed the conversion between Volts and PSI.
 *
 * @author Larry Tseng
 */
public class AnalogPressureTransducer {

    private static final double MIN_VOLT = 0.5;
    private static final double MAX_VOLT = 4.5;

    private AnalogInput sensor;

    public AnalogPressureTransducer(int channel) {
        this.sensor = new AnalogInput(channel);
    }

    public double getPSI() {

        double voltage = getVoltage();

        if (voltage != -1.0) {

            if (voltage > MIN_VOLT && voltage < MAX_VOLT) {

                // Test assertions to make sure scaling function works
                assert scaleFn(MIN_VOLT) == 20;
                assert scaleFn(MAX_VOLT) == 200;

                return scaleFn(voltage);
            }

        }

        return -1.0;
    }

    public int getRaw() {
        return sensor != null ? sensor.getValue() : -1;
    }

    public double getVoltage() {
        return sensor != null ? sensor.getVoltage() : -1.0;
    }

    private static double scaleFn(double voltage) {
        // Equation is y = 50(v-0.5)
        return 50 * (voltage - 0.5);
    }

}
