package org.whsrobotics.subsystems;

import org.whsrobotics.utils.WolverinesSubsystem;

public class HatchMechJack extends WolverinesSubsystem {
    
    public static HatchMechJack instance;

    private HatchMechJack() {
    }

    public static void init() {
        instance = new HatchMechJack();
    }

    @Override
    protected void initDefaultCommand() {

    }
}
