package org.whsrobotics.commands;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.InstantCommand;

import org.whsrobotics.subsystems.PneumaticsBase;
import org.whsrobotics.utils.WolverinesSubsystem;

public class SetSingleSolenoid extends InstantCommand{

    private WolverinesSubsystem subsystem;
    private Solenoid solenoid;
    private PneumaticsBase.SingleSolenoidModes mode;

    /**
     * @param subsystem the subsystem that is being manipulated
     * @param solenoid the solenoid that is being set
     * @param mode the mode of the solenoid: extended, neutral, retracted
     */
    public SetSingleSolenoid(WolverinesSubsystem subsystem, Solenoid solenoid, PneumaticsBase.SingleSolenoidModes mode) {
        requires(subsystem);
        this.subsystem = subsystem;
        this.solenoid = solenoid;
        this.mode = mode;
    }
    
    @Override
    protected void initialize() {
        if (subsystem.ensureInit()) {
            System.out.println("Moving " + solenoid.toString() + " to " + mode.toString());
            PneumaticsBase.setSingleSolenoidPosition(solenoid, mode);
        } else {
            DriverStation.reportError("**** ERROR: Cannot move " + solenoid.toString() + " because the subsystem has failed initialization", false);
        }
    }
    
    @Override
    protected boolean isFinished() {
        return true;
    }
}
