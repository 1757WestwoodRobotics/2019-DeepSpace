package org.whsrobotics.subsystems;

import com.ctre.phoenix.motorcontrol.*;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.TalonSRXPIDSetConfiguration;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import org.whsrobotics.utils.WolverinesSubsystem;

import static org.whsrobotics.robot.Constants.SolenoidPorts.*;
import static org.whsrobotics.robot.Constants.SolenoidPorts.HATCH_DEPLOY;

public class HatchMech extends WolverinesSubsystem {

    public static DoubleSolenoid hatchMechSliderSolenoid;
    public static DoubleSolenoid hatchDeploySolenoid;
    public static DoubleSolenoid dropArmsSolenoid;
    public static DoubleSolenoid floorHatchMechSolenoid;

    public static TalonSRX ballScrewTalon;
    
    public static HatchMech instance;

    public static HatchMech getInstance() {
        if (instance == null) {
            instance = new HatchMech();
        }
        return instance;
    }

    private HatchMech() {
        super(false);
    }

    @Override
    protected void init(boolean onTestRobot) {
        hatchMechSliderSolenoid = new DoubleSolenoid(HATCH_MECH_SLIDER.module, HATCH_MECH_SLIDER.a, HATCH_MECH_SLIDER.b);
        hatchDeploySolenoid = new DoubleSolenoid(HATCH_DEPLOY.module, HATCH_DEPLOY.a, HATCH_DEPLOY.b);
        dropArmsSolenoid = new DoubleSolenoid(DROP_ARMS.module, DROP_ARMS.a, DROP_ARMS.b);
        floorHatchMechSolenoid = new DoubleSolenoid(HATCH_FLOOR.module, HATCH_FLOOR.a, HATCH_FLOOR.b);

        ballScrewTalon = new TalonSRX(7);
        ballScrewTalon.configFactoryDefault();

        ballScrewTalon.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute);
        ballScrewTalon.configForwardLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyClosed);
        ballScrewTalon.configReverseLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyClosed);

        ballScrewTalon.configPeakOutputForward(0.5);
        ballScrewTalon.configPeakOutputReverse(0.5);

        ballScrewTalon.configClosedLoopPeakOutput(0, 0.5);

        ballScrewTalon.setStatusFramePeriod(StatusFrameEnhanced.Status_2_Feedback0, 10);
        ballScrewTalon.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, 10);

        ballScrewTalon.selectProfileSlot(0, 0);
        ballScrewTalon.config_kF(0, 0);
        ballScrewTalon.config_kP(0, 0);
        ballScrewTalon.config_kI(0, 0);
        ballScrewTalon.config_kD(0, 0);

        ballScrewTalon.configMotionCruiseVelocity(0);
        ballScrewTalon.configMotionAcceleration(0);
        ballScrewTalon.configMotionSCurveStrength(0);


    }

    @Override
    protected void initDefaultCommand() {

    }

    public static DoubleSolenoid getHatchMechSliderSolenoid() {
        return hatchMechSliderSolenoid;
    }

    public static DoubleSolenoid getHatchDeploySolenoid() {
        return hatchDeploySolenoid;
    }

    public static DoubleSolenoid getDropArmsSolenoid() {
        return dropArmsSolenoid;
    }

    public static DoubleSolenoid getFloorHatchMechSolenoid() {
        return floorHatchMechSolenoid;
    }

    // Slide ballScrewTalon to position
    public static void moveLinear() { }
    public static void moveRotation() { }
    public static double convertRotationToLinear(double degrees) { return 0.0; }
    public static double convertLinearToRotations(double rotation) { return 0.0; }


}
