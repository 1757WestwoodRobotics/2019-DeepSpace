package org.whsrobotics.robot;

public class Constants {

    public enum canID {
        leftA(1), leftB(2), leftC(3),
        rightA(4), rightB(5), rightC(6);

        public int id;

        canID(int id) {
            this.id = id;
        }
    }

    public enum Math {
        pi(3.1415926535897932384626502),
        circumference(12.5663706144);


        public double value;

        Math(double value){
            this.value = value;
        }
    }
}