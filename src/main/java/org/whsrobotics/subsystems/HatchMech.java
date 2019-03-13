package org.whsrobotics.subsystems;

import com.ctre.phoenix.motorcontrol.*;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.whsrobotics.robot.OI;
import org.whsrobotics.utils.WolverinesSubsystem;

import javax.naming.OperationNotSupportedException;

import static org.whsrobotics.robot.Constants.SolenoidPorts.*;
import static org.whsrobotics.robot.Constants.SolenoidPorts.HATCH_DEPLOY;

public class HatchMech extends WolverinesSubsystem {

    private static final int BALL_SCREW_MAX_ERROR = 4096;
    private static final int BALL_SCREW_MAX_VELOCITY = 4162;
    private static final int BALL_SCREW_MAX_ACCEL = 8324;

    private static final int BALL_SCREW_FWD_LIMIT = 62_423;
    private static final int BALL_SCREW_REV_LIMIT = -62_423;
    private static final int BALL_SCREW_S_CURVE = 2;


    private static DoubleSolenoid hatchMechSliderSolenoid;
    private static DoubleSolenoid hatchDeploySolenoid;
    private static DoubleSolenoid dropArmsSolenoid;
    private static DoubleSolenoid floorHatchMechSolenoid;

    private static TalonSRX ballScrewTalon;
    
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
        hatchMechSliderSolenoid.setName("hatchMechSliderSolenoid");

        hatchDeploySolenoid = new DoubleSolenoid(HATCH_DEPLOY.module, HATCH_DEPLOY.a, HATCH_DEPLOY.b);
        hatchDeploySolenoid.setName("hatchDeploySolenoid");

        dropArmsSolenoid = new DoubleSolenoid(DROP_ARMS.module, DROP_ARMS.a, DROP_ARMS.b);
        dropArmsSolenoid.setName("dropArmsSolenoid");

        floorHatchMechSolenoid = new DoubleSolenoid(HATCH_FLOOR.module, HATCH_FLOOR.a, HATCH_FLOOR.b);
        floorHatchMechSolenoid.setName("floorHatchMechSolenoid");

        PneumaticsBase.registerDoubleSolenoid(hatchMechSliderSolenoid, hatchDeploySolenoid, dropArmsSolenoid, floorHatchMechSolenoid);


        ballScrewTalon = new TalonSRX(7);
        ballScrewTalon.configFactoryDefault();

        ballScrewTalon.setNeutralMode(NeutralMode.Brake);
        ballScrewTalon.setSensorPhase(false);

        ballScrewTalon.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute);
        ballScrewTalon.configForwardLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen);
        ballScrewTalon.configReverseLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen);

        ballScrewTalon.configPeakOutputForward(0.5);
        ballScrewTalon.configPeakOutputReverse(-0.5);

        ballScrewTalon.configClosedLoopPeakOutput(0, 0.5);

        ballScrewTalon.setStatusFramePeriod(StatusFrameEnhanced.Status_13_Base_PIDF0, 10);
        ballScrewTalon.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, 10);

        ballScrewTalon.selectProfileSlot(0, 0);
        ballScrewTalon.config_kF(0, 0.0);
        ballScrewTalon.config_kP(0, 0.1);
        ballScrewTalon.config_kI(0, 0);
        ballScrewTalon.config_kD(0, 0);

        ballScrewTalon.configForwardSoftLimitEnable(true);
        ballScrewTalon.configReverseSoftLimitEnable(true);

        ballScrewTalon.configForwardSoftLimitThreshold(BALL_SCREW_FWD_LIMIT);     // 2*3" of travel, 5 mm/rot, 4096 ticks/rot
        ballScrewTalon.configReverseSoftLimitThreshold(BALL_SCREW_REV_LIMIT);

        ballScrewTalon.configAllowableClosedloopError(0, BALL_SCREW_MAX_ERROR);

        ballScrewTalon.configMotionCruiseVelocity(BALL_SCREW_MAX_VELOCITY);    // Represents 2"/sec
        ballScrewTalon.configMotionAcceleration(BALL_SCREW_MAX_ACCEL);

        // "Nine levels (0 through 8), where 0 represents no smoothing (same as classic trapezoidal profiling) and 8 represents max smoothing." - CTRE
        ballScrewTalon.configMotionSCurveStrength(BALL_SCREW_S_CURVE);

    }

    @Override
    protected void reducedPeriodic() {
        OI.getRobotTable().getEntry("Hatch Mech Position").setNumber(ballScrewTalon.getSelectedSensorPosition());
        OI.getRobotTable().getEntry("Hatch Mech Velocity").setNumber(ballScrewTalon.getSelectedSensorVelocity());
    }

    @Override
    protected void initDefaultCommand() {

    }

    // –––––– SOLENOIDS –––––– //

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


    // –––––– BALL SCREW –––––– //

    public enum Units {
        INCH, CM, NATIVE_TICKS
    }

    public static void moveBallScrewMotionMagic(Units unit, double position) {
        switch (unit) {
            case INCH:
                ballScrewTalon.set(ControlMode.MotionMagic, position * 20807);  // ~20807 ticks per inch
                break;
            case CM:
                ballScrewTalon.set(ControlMode.MotionMagic, (position / 2.54) * 20807);
                break;
            case NATIVE_TICKS:
                ballScrewTalon.set(ControlMode.MotionMagic, position);
                break;
        }
    }

    // TODO: Sean, bind to a command/button (X on XboxControllerA or on the control system AND smartdashboard)
    public static void resetEncoder() {
        ballScrewTalon.setSelectedSensorPosition(0);
    }

    public static boolean ballScrewIsFinished() {
        return Math.abs(ballScrewTalon.getSelectedSensorPosition() - ballScrewTalon.getClosedLoopTarget()) < BALL_SCREW_MAX_ERROR;
    }

}
