package org.whsrobotics.commands;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.InstantCommand;
import edu.wpi.first.wpilibj.command.Subsystem;

import org.whsrobotics.hardware.Actuators.Pneumatics;
import org.whsrobotics.subsystems.PneumaticsBase;
import org.whsrobotics.subsystems.Superstructure;

public class SetDoubleSolenoid extends InstantCommand{

    private Solenoid solenoid;
    private PneumaticsBase.DoubleSolenoidModes mode;
    
    public SetDoubleSolenoid(Subsystem subsystem, Solenoid solenoid, PneumaticsBase.DoubleSolenoidModes mode) {
        requires(subsystem);
        this.solenoid = solenoid;
        this.mode = mode;
    }
    
    @Override
    protected void initialize() {
        PneumaticsBase(solenoid, mode);
    }
    
    @Override
    protected boolean isFinished() {
        return super.isFinished();
    }
    
    
}
