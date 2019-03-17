package org.whsrobotics.commands;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Subsystem;

import org.whsrobotics.subsystems.PneumaticsBase;
import org.whsrobotics.subsystems.PneumaticsBase.DoubleSolenoidModes;

/**
 * This command allows a subsystem to be extended while it's active (using while held or while active),
 * and then returns to its retracted state when it's no longer active
 */
public class SetDoubleSolenoidLoop extends Command {

    private DoubleSolenoid solenoid;

    /**
     * @param subsystem the subsystem that's being manipulated
     * @param solenoid  the solenoid that's being extended and retracted
     */
    public SetDoubleSolenoidLoop(Subsystem subsystem, DoubleSolenoid solenoid) {
        this.solenoid = solenoid;
    }
    
    @Override
    protected void initialize() {
        PneumaticsBase.setDoubleSolenoidPosition(solenoid, DoubleSolenoidModes.EXTENDED);
    }
    
    @Override
    protected boolean isFinished() {
        return false;
    }

    @Override
    protected void end() {
        PneumaticsBase.setDoubleSolenoidPosition(solenoid, DoubleSolenoidModes.RETRACTED);
    }
}