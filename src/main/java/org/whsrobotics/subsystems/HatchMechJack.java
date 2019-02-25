package org.whsrobotics.subsystems;

import org.whsrobotics.utils.WolverinesSubsystem;

public class HatchMechJack extends WolverinesSubsystem {
    
    public static HatchMechJack instance;

    private HatchMechJack() {
        super(true);
    }

    public static void init() {
        instance = new HatchMechJack();
    }

    @Override
    protected void initDefaultCommand() {

    }

    @Override
    protected void init(boolean onTestRobot) {

    }
}
