package org.whsrobotics.subsystems;

import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
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

        SmartDashboard.putNumber("PDP Voltage", pdp.getVoltage());
        SmartDashboard.putNumber("PDP Total Current", pdp.getTotalCurrent());

        for (int i = 0; i <= 15; i++) {
            pdpCurrents[i] = pdp.getCurrent(i);
        }

        SmartDashboard.putNumberArray("PDP Individual Current", pdpCurrents);

    }

    @Override
    protected void initDefaultCommand() {

    }

}
