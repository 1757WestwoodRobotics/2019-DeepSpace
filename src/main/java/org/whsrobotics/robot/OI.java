package org.whsrobotics.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.whsrobotics.commands.Compress;
import org.whsrobotics.commands.CompressStop;
import org.whsrobotics.commands.SetDoubleSolenoid;
import org.whsrobotics.subsystems.HatchMech;
import org.whsrobotics.subsystems.Superstructure;
import org.whsrobotics.subsystems.PneumaticsBase.DoubleSolenoidModes;
import org.whsrobotics.utils.XboxController;

import static org.whsrobotics.subsystems.PneumaticsBase.startCompression;

public class OI {

    private static DriverStation.Alliance alliance;

    private static XboxController xboxController;

    private static Joystick controlSystem;

    public static void init() {

        xboxController = new XboxController(0);
        // |-------- Switches --------|
        
        //When swtich is turned on, compression starts, when it's turned off, compression stops
        (new JoystickButton(controlSystem, 0)).whenPressed(new Compress());
        (new JoystickButton(controlSystem, 0)).whenReleased(new CompressStop());
        //When switch is on, the hatch mechanism is put in forward mode
        (new JoystickButton(controlSystem, 1)).whileHeld(new SetDoubleSolenoid(
            HatchMech.instance, HatchMech.getHatchDeploySolenoid(), DoubleSolenoidModes.EXTENDED));
        

        // |-------- Buttons --------|

        //When button is held, the hatch mechanism decends to the floor
        (new JoystickButton(controlSystem, 2)).whileHeld(new SetDoubleSolenoid(
            HatchMech.instance, HatchMech.floorHatchMechSolenoid, DoubleSolenoidModes.EXTENDED));
        
        
        
        
            SmartDashboard.putData("Compress", new Compress());

        SmartDashboard.putData("Stop Compress", new CompressStop());

        SmartDashboard.putData("Superstructure Extended", new SetDoubleSolenoid(
            Superstructure.instance, Superstructure.getSuperstructureSolenoid(), DoubleSolenoidModes.EXTENDED));

        SmartDashboard.putData("Superstructure Neutral", new SetDoubleSolenoid(
            Superstructure.instance, Superstructure.getSuperstructureSolenoid(), DoubleSolenoidModes.NEUTRAL));

        SmartDashboard.putData("Superstructure Retracted", new SetDoubleSolenoid(
            Superstructure.instance, Superstructure.getSuperstructureSolenoid(), DoubleSolenoidModes.RETRACTED));

        SmartDashboard.putData("Hatch Mech Extended", new SetDoubleSolenoid(
            HatchMech.instance, HatchMech.getHatchMechSliderSolenoid(), DoubleSolenoidModes.EXTENDED));

        SmartDashboard.putData("Hatch Mech Neutral", new SetDoubleSolenoid(
            HatchMech.instance, HatchMech.getHatchMechSliderSolenoid(), DoubleSolenoidModes.NEUTRAL));

        SmartDashboard.putData("Hatch Mech Retracted", new SetDoubleSolenoid(
            HatchMech.instance, HatchMech.getHatchMechSliderSolenoid(), DoubleSolenoidModes.RETRACTED));

        SmartDashboard.putData("Drop Arms Extended", new SetDoubleSolenoid(
            HatchMech.instance, HatchMech.getDropArmsSolenoid(), DoubleSolenoidModes.EXTENDED));

        SmartDashboard.putData("Drop Arms Neutral", new SetDoubleSolenoid(
            HatchMech.instance, HatchMech.getDropArmsSolenoid(), DoubleSolenoidModes.NEUTRAL));

        SmartDashboard.putData("Drop Arms Retracted", new SetDoubleSolenoid(
            HatchMech.instance, HatchMech.getDropArmsSolenoid(), DoubleSolenoidModes.RETRACTED));

        SmartDashboard.putData("Floor Drop Extended", new SetDoubleSolenoid(
            HatchMech.instance, HatchMech.getFloorHatchMechSolenoid(), DoubleSolenoidModes.EXTENDED));

        SmartDashboard.putData("Floor Drop Neutral", new SetDoubleSolenoid(
            HatchMech.instance, HatchMech.getFloorHatchMechSolenoid(), DoubleSolenoidModes.NEUTRAL));

        SmartDashboard.putData("Floor Drop Retracted", new SetDoubleSolenoid(
            HatchMech.instance, HatchMech.getFloorHatchMechSolenoid(), DoubleSolenoidModes.RETRACTED));

        SmartDashboard.putData("Hatch Deploy Extended", new SetDoubleSolenoid(
            HatchMech.instance, HatchMech.getHatchDeploySolenoid(), DoubleSolenoidModes.EXTENDED));

        SmartDashboard.putData("Hatch Deploy Neutral", new SetDoubleSolenoid(
            HatchMech.instance, HatchMech.getHatchDeploySolenoid(), DoubleSolenoidModes.NEUTRAL));

        SmartDashboard.putData("Hatch Deploy Retracted", new SetDoubleSolenoid(
            HatchMech.instance, HatchMech.getHatchDeploySolenoid(), DoubleSolenoidModes.RETRACTED));

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