package org.whsrobotics.robot;

public class Constants {

    public enum canID {
        LEFT_A(1), LEFT_B(2), LEFT_C(3),
        RIGHT_A(4), RIGHT_B(5), RIGHT_C(6),
        BALL_SCREW(7),
        PCM_A(10), PCM_B(11);

        public int id;

        canID(int id) {
            this.id = id;
        }
    }
    /**
     * Switches: A is leftmost, E is rightmost
     */
    public enum ControlSystemPort {
        TOP_LEFT(0), TOP_MIDDLE(0), TOP_RIGHT(7),
        BOTTOM_LEFT(10), BOTTOM_MIDDLE(9), BOTTOM_RIGHT(6),
        SWITCH_A(5), SWITCH_B(4), SWITCH_C(3), SWITCH_D(2), SWITCH_E(1),
        BRB(8), SLIDER_CONDUCTIVE(11);

        public int port;

        ControlSystemPort(int port) {
            this.port = port;
        }
    }

    public enum ComputerPort{
        XBOX_CONTROLLER(0), CONTROL_SYSTEM(1), XBOX_CONTROLLERB(2);

        public int port;

        ComputerPort(int port) {
            this.port = port;
        }
    }

    public enum Math {
        pi(3.141592653589793238462643383279502),
        circumference(12.5663706144),
        kConversionConstant(0.0452381);

        public double value;

        Math(double value){
            this.value = value;
        }
    }

    public enum SolenoidPorts {
        SUPERSTRUCTURE(canID.PCM_B, 0, 1),

        HATCH_MECH_SLIDER(canID.PCM_A, 0, 1),
        HATCH_MECH_ACTUATION(canID.PCM_A, 2, 3),

        RAMP_RELEASE(canID.PCM_B, 2, 2);

        public int module;
        public int a;
        public int b;

        SolenoidPorts(canID module, int a, int b) {
            this.module = module.id;
            this.a = a;
            this.b = b;
        }

    }

    public static final double MAX_SLOW_DRIVETRAIN = 0.5;
    public static final double ROTATION_FACTOR = 0.75;
    
}
