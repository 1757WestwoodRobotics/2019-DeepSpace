package org.whsrobotics.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.whsrobotics.robot.Robot;
import org.whsrobotics.subsystems.PneumaticsBase;

public class Compress extends Command{

    public Compress(){
        requires(PneumaticsBase.instance);
    }

    @Override
    protected void initialize() {
        if (PneumaticsBase.getInstance().ensureInit() && !Robot.isTestRobot) {
            PneumaticsBase.startCompression(PneumaticsBase.getCompressor());
        } else {
            cancel();
        }
    }

    @Override
    protected void end() {
    
    }

    @Override
    protected boolean isFinished() {
        return ( Robot.isTestRobot || !PneumaticsBase.getPressureSwitchState() );
    }
}

