package org.whsrobotics.commands;

import org.whsrobotics.subsystems.Superstructure;
import org.whsrobotics.subsystems.PneumaticsBase.SingleSolenoidModes;
import org.whsrobotics.subsystems.PneumaticsBase;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.InstantCommand;

public class RampDeployment extends Command{

    public RampDeployment() {
        requires(Superstructure.getInstance());
    }

    @Override
    protected void initialize() {
        System.out.println("Ramp Deploying");
        PneumaticsBase.setSingleSolenoidPosition(Superstructure.getRampReleaseSolenoid(), SingleSolenoidModes.EXTENDED);
    }

    @Override
    protected void end() {
        PneumaticsBase.setSingleSolenoidPosition(Superstructure.getRampReleaseSolenoid(), SingleSolenoidModes.RETRACTED);
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}