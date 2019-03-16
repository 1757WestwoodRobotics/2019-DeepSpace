package org.whsrobotics.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import org.whsrobotics.utils.WolverinesSubsystem;

import static org.whsrobotics.robot.Constants.SolenoidPorts.WING_DELPOY;
import static org.whsrobotics.subsystems.PneumaticsBase.*;

public class WingDeploy extends WolverinesSubsystem {

    public static WingDeploy instance;

    private static DoubleSolenoid wingDeploySolenoid;

    private WingDeploy() {
        super(false);
    }

    public static WingDeploy getInstance() {
        if (instance == null) {
            instance = new WingDeploy();
        }
        return instance;
    }

    @Override
    protected void init(boolean onTestRobot) {
        wingDeploySolenoid = new DoubleSolenoid(WING_DELPOY.module, WING_DELPOY.a, WING_DELPOY.b);
        wingDeploySolenoid.setName("wingDeploySolenoid");

        PneumaticsBase.registerDoubleSolenoid(wingDeploySolenoid);

    }

    @Override
    protected void initDefaultCommand() {

    }

    public static DoubleSolenoidModes getWingDeployPosition() {
        return DoubleSolenoidModes.lookup(wingDeploySolenoid.get());
    }

    public static DoubleSolenoid getWingDeploySolenoid() {
        return wingDeploySolenoid;
    }

}
