package org.whsrobotics.robot;

public class RobotMap{

    public enum xboxButtons{

        A(1), B(2), X(3), Y(4),
        LB(5), RB(6),
        Select(7), Start(8),
        LS_Button(9), RS_Button(10);

        public int button;

        xboxButtons(int button) {
            this.button = button;
        }
    }

    public enum xboxAxes{

        LS_X(0), LS_Y(1),
        LeftTrigger(2), RightTrigger(3),
        RS_X(4), RS_Y(5);

        public int axis;

        xboxAxes(int axis) {
            this.axis = axis;
        }
    }
}