package org.whsrobotics.commands;

import org.whsrobotics.subsystems.HatchMech;
import org.whsrobotics.subsystems.PneumaticsBase;
import org.whsrobotics.subsystems.PneumaticsBase.DoubleSolenoidModes;

import edu.wpi.first.wpilibj.command.TimedCommand;

public class HatchEjection extends TimedCommand {

    public HatchEjection() {
        super(0.35);
    }
    
    @Override
        protected void initialize() {
            PneumaticsBase.setSolenoidPosition(HatchMech.getHatchDeploySolenoid(), DoubleSolenoidModes.EXTENDED);
        }
        
        @Override
        protected void end() {
            PneumaticsBase.setSolenoidPosition(HatchMech.getHatchDeploySolenoid(), DoubleSolenoidModes.RETRACTED);
        }
}