package org.whsrobotics.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.whsrobotics.commands.Compress;
import org.whsrobotics.commands.CompressStop;
import org.whsrobotics.commands.SetDoubleSolenoid;
import org.whsrobotics.subsystems.HatchMechJack;
import org.whsrobotics.subsystems.Superstructure;
import org.whsrobotics.subsystems.PneumaticsBase.DoubleSolenoidModes;
import org.whsrobotics.utils.XboxController;

public class OI {

    private static DriverStation.Alliance alliance;

    private static XboxController xboxController;

    public static void init() {

        xboxController = new XboxController(0);

        SmartDashboard.putData("Compress", new Compress());
        SmartDashboard.putData("Stop Compress", new CompressStop());

        SmartDashboard.putData("Superstructure Extended", new SetDoubleSolenoid(
            Superstructure.instance, Superstructure.getSuperstructureSolenoid(), DoubleSolenoidModes.EXTENDED));

        SmartDashboard.putData("Superstructure Neutral", new SetDoubleSolenoid(
            Superstructure.instance, Superstructure.getSuperstructureSolenoid(), DoubleSolenoidModes.NEUTRAL));

        SmartDashboard.putData("Superstructure Retracted", new SetDoubleSolenoid(
            Superstructure.instance, Superstructure.getSuperstructureSolenoid(), DoubleSolenoidModes.RETRACTED));

        SmartDashboard.putData("Hatch Mech Extended", new SetDoubleSolenoid(
            HatchMechJack.instance, HatchMechJack.getHatchMechSliderSolenoid(), DoubleSolenoidModes.EXTENDED));

        SmartDashboard.putData("Hatch Mech Neutral", new SetDoubleSolenoid(
            HatchMechJack.instance, HatchMechJack.getHatchMechSliderSolenoid(), DoubleSolenoidModes.NEUTRAL));

        SmartDashboard.putData("Hatch Mech Retracted", new SetDoubleSolenoid(
            HatchMechJack.instance, HatchMechJack.getHatchMechSliderSolenoid(), DoubleSolenoidModes.RETRACTED));

        SmartDashboard.putData("Drop Arms Extended", new SetDoubleSolenoid(
            HatchMechJack.instance, HatchMechJack.getDropArmsSolenoid(), DoubleSolenoidModes.EXTENDED));

        SmartDashboard.putData("Drop Arms Neutral", new SetDoubleSolenoid(
            HatchMechJack.instance, HatchMechJack.getDropArmsSolenoid(), DoubleSolenoidModes.NEUTRAL));

        SmartDashboard.putData("Drop Arms Retracted", new SetDoubleSolenoid(
            HatchMechJack.instance, HatchMechJack.getDropArmsSolenoid(), DoubleSolenoidModes.RETRACTED));

        SmartDashboard.putData("Floor Drop Extended", new SetDoubleSolenoid(
            HatchMechJack.instance, HatchMechJack.getFloorHatchMechSolenoid(), DoubleSolenoidModes.EXTENDED));

        SmartDashboard.putData("Floor Drop Neutral", new SetDoubleSolenoid(
            HatchMechJack.instance, HatchMechJack.getFloorHatchMechSolenoid(), DoubleSolenoidModes.NEUTRAL));

        SmartDashboard.putData("Floor Drop Retracted", new SetDoubleSolenoid(
            HatchMechJack.instance, HatchMechJack.getFloorHatchMechSolenoid(), DoubleSolenoidModes.RETRACTED));

        SmartDashboard.putData("Hatch Deploy Extended", new SetDoubleSolenoid(
            HatchMechJack.instance, HatchMechJack.getHatchDeploySolenoid(), DoubleSolenoidModes.EXTENDED));

        SmartDashboard.putData("Hatch Deploy Neutral", new SetDoubleSolenoid(
            HatchMechJack.instance, HatchMechJack.getHatchDeploySolenoid(), DoubleSolenoidModes.NEUTRAL));

        SmartDashboard.putData("Hatch Deploy Retracted", new SetDoubleSolenoid(
            HatchMechJack.instance, HatchMechJack.getHatchDeploySolenoid(), DoubleSolenoidModes.RETRACTED));

    }

    public static XboxController getXboxController() {
        return xboxController;
    }

    XboxController.getButton();

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