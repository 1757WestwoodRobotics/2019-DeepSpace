package org.whsrobotics.subsystems;

import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.whsrobotics.robot.OI;
import org.whsrobotics.utils.WolverinesSubsystem;

public class ElectronicsSystem extends WolverinesSubsystem {

    private static PowerDistributionPanel pdp;

    private static double[] pdpCurrents;

    private static ElectronicsSystem instance;

    public static ElectronicsSystem getInstance() {
        if (instance == null) {
            instance = new ElectronicsSystem();
        }
        return instance;
    }

    private ElectronicsSystem() {
        super(false);
    }

    @Override
    protected void init(boolean onTestRobot) {
        pdp = new PowerDistributionPanel();
        pdpCurrents = new double[16];
    }

    @Override
    public void reducedPeriodic() {

        OI.getRobotTable().getEntry("PDP Voltage").setNumber(pdp.getVoltage());

        for (int i = 0; i <= 15; i++) {
            pdpCurrents[i] = pdp.getCurrent(i);
        }

        OI.getRobotTable().getEntry("PDP Currents").setDoubleArray(pdpCurrents);

        OI.getRobotTable().getEntry("time").setDouble(OI.getMatchTime());
    }

    @Override
    protected void initDefaultCommand() {

    }

}
