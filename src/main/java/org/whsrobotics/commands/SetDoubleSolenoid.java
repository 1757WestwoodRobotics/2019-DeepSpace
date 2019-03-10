package org.whsrobotics.commands;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.InstantCommand;
import edu.wpi.first.wpilibj.command.Subsystem;

import org.whsrobotics.subsystems.PneumaticsBase;

/**
 * @param subsystem the subsystem that is being manipulated
 * @param solenoid the solenoid that is being set
 * @param mode the mode of the solenoid: extended, neutral, retracted
 */
public class SetDoubleSolenoid extends InstantCommand{

    private DoubleSolenoid solenoid;
    private PneumaticsBase.DoubleSolenoidModes mode;
    
    public SetDoubleSolenoid(Subsystem subsystem, DoubleSolenoid solenoid, PneumaticsBase.DoubleSolenoidModes mode) {
        requires(subsystem);
        this.solenoid = solenoid;
        this.mode = mode;
    }
    
    @Override
    protected void initialize() {
        System.out.println("Moving " + solenoid.toString() + " to " + mode.toString());
        PneumaticsBase.setSolenoidPosition(solenoid, mode);
    }
    
    @Override
    protected boolean isFinished() {
        return true;
    }
}
