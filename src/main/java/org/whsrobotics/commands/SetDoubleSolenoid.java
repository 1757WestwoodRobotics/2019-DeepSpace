package org.whsrobotics.commands;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.InstantCommand;

import org.whsrobotics.hardware.Actuators.Pneumatics;
import org.whsrobotics.subsystems.PneumaticsBase;
import org.whsrobotics.subsystems.Superstructure;

public class SetDoubleSolenoid extends InstantCommand{

    private Solenoid solenoid;
    private boolean state;
    
    public SetDoubleSolenoid(Solenoid solenoid, boolean state) {
        //requires();
        this.solenoid = solenoid;
        this.state = state;
    }
    
    @Override
    protected void initialize() {
        Pneumatics.SetDoubleSolenoid(solenoid, state);
    }
    
    @Override
    protected boolean isFinished() {
        return super.isFinished();
    }
    
    
}
