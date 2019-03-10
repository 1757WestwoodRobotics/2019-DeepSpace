package org.whsrobotics.commands.commandgroups;

import edu.wpi.first.wpilibj.command.CommandGroup;

import org.whsrobotics.commands.RampDeployment;
import org.whsrobotics.commands.SetDoubleSolenoid;
import org.whsrobotics.subsystems.HatchMech;
import org.whsrobotics.subsystems.Superstructure;
import org.whsrobotics.subsystems.PneumaticsBase.DoubleSolenoidModes;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.InstantCommand;

public class Endgame extends CommandGroup {

    public Endgame() {

        addSequential(new SetDoubleSolenoid(
            Superstructure.instance, Superstructure.getSuperstructureSolenoid(), DoubleSolenoidModes.EXTENDED));
        addSequential(new SetDoubleSolenoid(
            HatchMech.instance, HatchMech.getDropArmsSolenoid(), DoubleSolenoidModes.RETRACTED));
        addSequential(new RampDeployment()); 
             
    }
}