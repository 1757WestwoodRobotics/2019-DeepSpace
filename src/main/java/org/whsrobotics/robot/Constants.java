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
}