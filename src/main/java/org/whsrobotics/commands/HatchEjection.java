package org.whsrobotics.commands;

import org.whsrobotics.subsystems.HatchMech;
import org.whsrobotics.subsystems.PneumaticsBase;
import org.whsrobotics.subsystems.PneumaticsBase.DoubleSolenoidModes;

import edu.wpi.first.wpilibj.command.TimedCommand;

public class HatchEjection extends TimedCommand {

    public HatchEjection() {
        //Was .35 seconds
        super(3.0);
    }

    @Override
        protected void initialize() {
            PneumaticsBase.setDoubleSolenoidPosition(HatchMech.getHatchDeploySolenoid(), DoubleSolenoidModes.EXTENDED);
        }

        @Override
        protected void end() {
            PneumaticsBase.setDoubleSolenoidPosition(HatchMech.getHatchDeploySolenoid(), DoubleSolenoidModes.RETRACTED);
        }
}