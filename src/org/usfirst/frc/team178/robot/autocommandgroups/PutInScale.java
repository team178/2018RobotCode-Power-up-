package org.usfirst.frc.team178.robot.autocommandgroups;

import org.usfirst.frc.team178.robot.Robot;
import org.usfirst.frc.team178.robot.commands.AutoTurn;
import org.usfirst.frc.team178.robot.commands.DriveForward;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class PutInScale extends CommandGroup {

    public PutInScale() {
    	addSequential(new DriveForward(300, .25));
    	
    	if(Robot.fieldConfig[2] == 'R')
    		addSequential(new AutoTurn(135, .25));
    	else if (Robot.fieldConfig[2] == 'L')
    		addSequential(new AutoTurn(-135, .25));
    	
    	addSequential(new AutoShootSequence());
        // Add Commands here:
        // e.g. addSequential(new Command1());
        //      addSequential(new Command2());
        // these will run in order.

        // To run multiple commands at the same time,
        // use addParallel()
        // e.g. addParallel(new Command1());
        //      addSequential(new Command2());
        // Command1 and Command2 will run in parallel.

        // A command group will require all of the subsystems that each member
        // would require.
        // e.g. if Command1 requires chassis, and Command2 requires arm,
        // a CommandGroup containing them would require both the chassis and the
        // arm.
    	
    	//turn to face thing
    	/*
    	if (isRight) {
    		addSequential(new AutoTurn(-90, 0.5));
    		//addSequential (insert shoot command)
    	} else {
    		addSequential(new AutoTurn(90, 0.5));
    		//addSequential (insert shoot command)
    	}
    	*/
    	
    	
    }
}
