package org.whsrobotics.commands;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.InstantCommand;
import edu.wpi.first.wpilibj.command.Subsystem;

import org.whsrobotics.hardware.Actuators.Pneumatics;
import org.whsrobotics.subsystems.PneumaticsBase;
import org.whsrobotics.subsystems.Superstructure;

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
        PneumaticsBase.setSolenoidPosition(solenoid, mode);
    }
    
    @Override
    protected boolean isFinished() {
        return super.isFinished();
    }
    
    
}
