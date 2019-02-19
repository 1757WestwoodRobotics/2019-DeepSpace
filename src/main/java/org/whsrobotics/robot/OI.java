package org.whsrobotics.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.whsrobotics.commands.Compress;
import org.whsrobotics.utils.XboxController;

public class OI {

    private static DriverStation.Alliance alliance;

    private static XboxController xboxController;

    static {
        xboxController = new XboxController(0);

        SmartDashboard.putData("Compress", new Compress());
    }

    public static XboxController getXboxController() {
        return xboxController;
    }

    public static DriverStation.Alliance getAlliance() {
        if (alliance == DriverStation.Alliance.Invalid) {
            try {
                alliance = DriverStation.getInstance().getAlliance();
            } catch (Exception e) {
                DriverStation.reportError("Error with getting the Alliance Data! " + e.getMessage(), false);
            }
        }

        return alliance;
    }

}