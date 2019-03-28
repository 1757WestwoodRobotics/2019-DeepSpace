package org.whsrobotics.robot;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.whsrobotics.commands.*;
import org.whsrobotics.subsystems.HatchMech;
import org.whsrobotics.subsystems.Superstructure;
import org.whsrobotics.subsystems.PneumaticsBase.DoubleSolenoidModes;
import org.whsrobotics.subsystems.PneumaticsBase.SingleSolenoidModes;
import org.whsrobotics.utils.BetterJoystickButton;
import org.whsrobotics.utils.XboxController;

import static org.whsrobotics.robot.Constants.ComputerPort;
import static org.whsrobotics.robot.Constants.ControlSystemPort;
import static org.whsrobotics.utils.XboxController.*;

public class OI {

    private static DriverStation.Alliance alliance;

    private static XboxController xboxControllerA;
    private static XboxController xboxControllerB;
    /**
     * Input is -1 to 1
     * Output is -512 to 512
     */
    private static Joystick controlSystem;

    private static Joystick currentControlsJoystick;

    private static Command currentControlsSwitcher;

    private static NetworkTable robotTable;

    public static void init() {

        xboxControllerA = new XboxController(ComputerPort.XBOX_CONTROLLER.port);
        xboxControllerB = new XboxController(ComputerPort.XBOX_CONTROLLER_B.port);
        controlSystem = new Joystick(ComputerPort.CONTROL_SYSTEM.port);

        try {
            setCurrentControlsJoystick(controlSystem);
        } catch (NullPointerException ex) {
            try {
                setCurrentControlsJoystick(xboxControllerB);
            } catch (NullPointerException e) {
                DriverStation.reportError("**** ERROR: NO CONTROLS CONTROLLERS DETECTED! PRESS START ON MAIN CONTROLLER OR SMARTDASHBOARD! ****", false);
            }
        }

        // |-------- Switches --------|

        //When switch is off, compression stops (compression is automatically on)
        (new BetterJoystickButton(controlSystem, ControlSystemPort.SWITCH_E.port)).whileHeld(
            new CompressStop());  
        //When switch is on, the hatch mechanism is moved to its extended position
        (new BetterJoystickButton(controlSystem, ControlSystemPort.SWITCH_A.port)).whileHeld(
            new SetDoubleSolenoidLoop(HatchMech.instance, HatchMech.getHatchMechSliderSolenoid())); 
        //When switch is on, the superstructure is moved to its extended position
        (new BetterJoystickButton(controlSystem, ControlSystemPort.SWITCH_B.port)).whileHeld(
            new SetDoubleSolenoidLoop(Superstructure.instance, Superstructure.getSuperstructureSolenoid()));
        //When switch is on, uses manual input over vision alignment for slider
        (new BetterJoystickButton(controlSystem, ControlSystemPort.SWITCH_C.port)).whileHeld(new SliderOverride());
        

        // |-------- Buttons --------|

        //When button is pressed, hatch mechanism toggles between being actuated and being folded in
        (new BetterJoystickButton(controlSystem, ControlSystemPort.BOTTOM_RIGHT.port)).toggleWhenPressed(
            new SetDoubleSolenoidLoop(HatchMech.instance, HatchMech.getHatchMechActuationSolenoid()));
        
        // |-------- Big Red Button --------|
        
        //Deploys ramp
        (new BetterJoystickButton(controlSystem, ControlSystemPort.BRB.port)).toggleWhenPressed(new RampDeployment());

        // |-------- Slider --------|

        (new BetterJoystickButton(controlSystem, ControlSystemPort.SLIDER_CONDUCTIVE.port))
                .whileHeld(new SliderOverride());

        // |-------- Xbox Buttons --------|

        //Compress
        (new BetterJoystickButton(xboxControllerB, Buttons.A.value)).toggleWhenPressed(
            new CompressStop());
        //Superstructure Extended
        (new BetterJoystickButton(xboxControllerB, Buttons.B.value)).toggleWhenPressed(
            new SetDoubleSolenoidLoop(Superstructure.instance, Superstructure.getSuperstructureSolenoid()));
        //Hatch Mech Actuation
        (new BetterJoystickButton(xboxControllerB, Buttons.Y.value)).toggleWhenPressed(
            new SetDoubleSolenoidLoop(HatchMech.instance, HatchMech.getHatchMechActuationSolenoid()));
        //Hatch Extend
        (new BetterJoystickButton(xboxControllerB, Buttons.BACK.value)).toggleWhenPressed(
            new SetDoubleSolenoidLoop(HatchMech.instance, HatchMech.getHatchMechSliderSolenoid()));
        //Ramp Deployment
        (new BetterJoystickButton(xboxControllerB, Buttons.START.value)).toggleWhenPressed(
            new RampDeployment());

        /*  
        XboxController A
        Right Bumper - While Held - Fast Mode
        */
        (new JoystickButton(xboxControllerA, Buttons.RIGHT_BUMPER.value)).whileHeld(
            new SetDrivetrainFast(5));


        currentControlsSwitcher = new Command() {

            @Override
            protected void initialize() {
                setCurrentControlsJoystick(xboxControllerB);
            }

            @Override
            protected boolean isFinished() {
                return false;
            }

            @Override
            protected void end() {
                setCurrentControlsJoystick(controlSystem);
            }

        };

        (new JoystickButton(xboxControllerA, Buttons.START.value)).toggleWhenPressed(currentControlsSwitcher);


        // NETWORK TABLES STUFF

        robotTable = NetworkTableInstance.getDefault().getTable("/Robot");

        SmartDashboard.putData("Controls Joystick Switcher", currentControlsSwitcher);
        
        SmartDashboard.putData("Wing Retract", new RampDeployment());

        //Returns Wing Retract to Normal Position (If needed, shoudld only press once ever)
        SmartDashboard.putData("Return Wing", new SetSingleSolenoid(
            Superstructure.instance, Superstructure.getRampReleaseSolenoid(), SingleSolenoidModes.RETRACTED));

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

    }

    // On button press, get Slider value
    public static double getSliderValue(){
        return controlSystem.getRawAxis(0);
    }

    public static void setSliderPosition(double position) {
        getRobotTable().getSubTable("Slider").getEntry("position").setNumber(position);
    }

    public static XboxController getXboxControllerA() {
        return xboxControllerA;
    }

    public static XboxController getXboxControllerB() {
        return xboxControllerB;
    }

    public static Joystick getControlSystem() {
        return controlSystem;
    }

    /////

    public static Joystick getCurrentControlsJoystick() {
        return currentControlsJoystick;
    }

    private static void setCurrentControlsJoystick(Joystick currentControlsJoystick) {
        getRobotTable().getEntry("ControlsJoystick").setString(currentControlsJoystick.getName());
        OI.currentControlsJoystick = currentControlsJoystick;
    }

    /////

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

    public static double getMatchTime() {
        return DriverStation.getInstance().getMatchTime();
    }

    public static NetworkTable getRobotTable() {
        return robotTable;
    }

}
