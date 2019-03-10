package org.whsrobotics.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.whsrobotics.commands.Compress;
import org.whsrobotics.commands.CompressStop;
import org.whsrobotics.commands.HatchEjection;
import org.whsrobotics.commands.SetDoubleSolenoid;
import org.whsrobotics.commands.SetDoubleSolenoidLoop;
import org.whsrobotics.subsystems.HatchMech;
import org.whsrobotics.subsystems.PneumaticsBase;
import org.whsrobotics.subsystems.Superstructure;
import org.whsrobotics.subsystems.PneumaticsBase.DoubleSolenoidModes;
import org.whsrobotics.utils.XboxController;
import org.whsrobotics.utils.XboxController.Buttons;

import static org.whsrobotics.robot.Constants.ComputerPort;
import static org.whsrobotics.robot.Constants.ControlSystemPort;
import static org.whsrobotics.subsystems.PneumaticsBase.startCompression;

public class OI {

    private static DriverStation.Alliance alliance;

    private static XboxController xboxController;
    private static XboxController xboxControllerB;

    private static Joystick controlSystem;

    public static void init() {

        xboxController = new XboxController(ComputerPort.XBOX_CONTROLLER.port);
        xboxControllerB = new XboxController(ComputerPort.XBOX_CONTROLLERB.port);
        controlSystem = new Joystick(ComputerPort.CONTROL_SYSTEM.port);

        // |-------- Switches --------|

        //When swtich is off, compression stops (compression is automatically on)
        (new JoystickButton(controlSystem, ControlSystemPort.SWITCH_E.port)).whenInactive(
            new CompressStop());
        //When switch is on, the hatch mechanism is moved to its extended position
        (new JoystickButton(controlSystem, ControlSystemPort.SWITCH_A.port)).whileHeld(
            new SetDoubleSolenoidLoop(HatchMech.instance, HatchMech.getHatchDeploySolenoid()));
        //When switch is on, the superstructure is moved to its extended position
        (new JoystickButton(controlSystem, 2)).whileHeld(
            new SetDoubleSolenoidLoop(Superstructure.instance, Superstructure.getSuperstructureSolenoid()));
        

        // |-------- Buttons --------|

        //When button is held, the hatch mechanism decends to the floor for hatch pickup
        (new JoystickButton(controlSystem, ControlSystemPort.BOTTOM_MIDDLE.port)).whileHeld(
            new SetDoubleSolenoidLoop(HatchMech.instance, HatchMech.getFloorHatchMechSolenoid()));
        //When button is pressed, hatch is shot off the hatch mechanism
        (new JoystickButton(controlSystem, ControlSystemPort.BOTTOM_RIGHT.port)).whenPressed(
            new HatchEjection());


        // |-------- Xbox Buttons --------|

        //Compress
        (new JoystickButton(xboxControllerB, XboxController.Buttons.A.value)).toggleWhenPressed(
            new CompressStop());
        //Superstructure Extended
        (new JoystickButton(xboxControllerB, XboxController.Buttons.B.value)).toggleWhenPressed(
            new SetDoubleSolenoidLoop(Superstructure.instance, Superstructure.getSuperstructureSolenoid()));
        //Hatch Floor Grab
        (new JoystickButton(xboxControllerB, XboxController.Buttons.X.value)).whileHeld(
            new SetDoubleSolenoidLoop(HatchMech.instance, HatchMech.getFloorHatchMechSolenoid()));
        //Hatch Extend
        (new JoystickButton(xboxControllerB, XboxController.Buttons.START.value)).whileHeld(
            new SetDoubleSolenoidLoop(HatchMech.instance, HatchMech.getHatchDeploySolenoid()));
        //Hatch Eject
        (new JoystickButton(xboxControllerB, XboxController.Buttons.Y.value)).whenPressed(
            new HatchEjection());


        

        
        
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

    public static XboxController getXboxControllerA() {
        return xboxController;
    }

    public static XboxController getXboxControllerB() {
        return xboxControllerB;
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