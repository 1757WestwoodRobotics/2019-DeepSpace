package org.whsrobotics.subsystems;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import org.whsrobotics.utils.WolverinesSubsystem;

import static org.whsrobotics.robot.Constants.SolenoidPorts.*;
import static org.whsrobotics.robot.Constants.SolenoidPorts.RIGHT_DROP;

public class HatchMechJack extends WolverinesSubsystem {

    public static DoubleSolenoid hatchMechSliderSolenoid;
    public static DoubleSolenoid dropArmsSolenoid;
    public static DoubleSolenoid floorHatchMechSolenoid;

    public static TalonSRX ballScrewTalon;
    
    public static HatchMechJack instance;

    private HatchMechJack() {
        super(false);
    }

    public static void init() {
        instance = new HatchMechJack();
    }

    @Override
    protected void initDefaultCommand() {

    }

    @Override
    protected void init(boolean onTestRobot) {
        hatchMechSliderSolenoid = new DoubleSolenoid(HATCH_MECH_SLIDER.module, HATCH_MECH_SLIDER.a, HATCH_MECH_SLIDER.b);
        dropArmsSolenoid = new DoubleSolenoid(LEFT_DROP.module, LEFT_DROP.a, LEFT_DROP.b);
        floorHatchMechSolenoid = new DoubleSolenoid(RIGHT_DROP.module, RIGHT_DROP.a, RIGHT_DROP.b);

        ballScrewTalon = new TalonSRX(7);
        ballScrewTalon.configFactoryDefault();
        ballScrewTalon.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute);

    }

    public static DoubleSolenoid getHatchMechSliderSolenoid() {
        return hatchMechSliderSolenoid;
    }

    public static DoubleSolenoid getDropArmsSolenoid() {
        return dropArmsSolenoid;
    }

    public static DoubleSolenoid getFloorHatchMechSolenoid() {
        return floorHatchMechSolenoid;
    }

}
