package org.whsrobotics.robot;

public class Constants {

    public enum canID {
        leftA(1), leftB(2), leftC(3),
        rightA(4), rightB(5), rightC(6),
        ballScrew(7),
        pcmA(10), pcmB(11);    // TODO: Make all caps! Ex: LEFT_A

        public int id;

        canID(int id) {
            this.id = id;
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

    // TODO: Ensure accuracy of Solenoid ports
    public enum SolenoidPorts {
        SUPERSTRUCTURE(canID.pcmB, 0, 1),


        HATCH_MECH_SLIDER(canID.pcmA, 0, 1),
        DROP_ARMS(canID.pcmA, 6, 7),
        HATCH_DEPLOY(canID.pcmA, 4, 5),
        HATCH_FLOOR(canID.pcmA, 2, 3);

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