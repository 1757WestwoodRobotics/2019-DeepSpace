package org.whsrobotics.commands;

import org.whsrobotics.robot.OI;
import org.whsrobotics.subsystems.Drivetrain;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.command.Command;

public class Drive extends Command {

    @Override
    protected boolean isFinished() {
        return false;
    }

    @Override
    protected void execute() {

        Drivetrain.arcadeDrive(-OI.getXboxController().getY(Hand.kLeft), OI.getXboxController().getX(Hand.kRight));

    }

    @Override
    protected void initialize() {

        System.out.println("Initialized");

    }

    @Override
    protected void end() {
        super.end();
    }
    
}