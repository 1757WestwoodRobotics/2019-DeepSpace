package org.whsrobotics.robot;

public class Constants {

    public static final int leftAPort = 0;
    public static final int leftBPort = 1;
    public static final int leftCPort = 2;
    public static final int rightAPort = 3;
    public static final int rightBPort = 4;
    public static final int rightCPort = 5;

    // !!! These should really go inside its own XboxController class.
    public enum XboxButtons {

        A(1), B(2), X(3), Y(4),
        LB(5), RB(6),
        Select(7), Start(8),
        LS_Button(9), RS_Button(10);

        public final int button;

        XboxButtons(int button) {
            this.button = button;
        }

    }

    public enum XboxAxes {

        LS_X(0), LS_Y(1),
        LeftTrigger(2), RightTrigger(3),
        RS_X(4), RS_Y(5);

        public final int axis;

        XboxAxes(int axis) {
            this.axis = axis;
        }

    }
}