package org.whsrobotics.robot;

import edu.wpi.first.wpilibj.XboxController;

public class OI {

    private static XboxController xboxController;

    static {
        xboxController = new XboxController(0);
    }

    public static XboxController getXboxController() {
        return xboxController;
    }

}