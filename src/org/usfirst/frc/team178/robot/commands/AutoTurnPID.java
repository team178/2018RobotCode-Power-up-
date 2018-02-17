package org.usfirst.frc.team178.robot.commands;

import org.usfirst.frc.team178.robot.OI;
import org.usfirst.frc.team178.robot.Robot;
import org.usfirst.frc.team178.robot.subsystems.Drivetrain;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class AutoTurnPID extends Command {
	OI oi;
	Drivetrain drivetrain;
	double lspeed, rspeed, targetAngle, actualAngle;
	double angleSetpoint, angleIntegral, previousAngle, angleDerivative, aP= 1, aI= 0.0, aD = 0.0;
	final double minSpeed = .1;
	int counter = 0;
	double lastSpeedL, lastSpeedR;
	
	
    public AutoTurnPID(double tAngle, double speed) {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    requires(Robot.drivetrain);
    targetAngle = tAngle;
    lspeed = speed;
    rspeed = speed;
    lastSpeedL = speed;
    lastSpeedR = speed;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	oi = Robot.oi;
    	drivetrain = Robot.drivetrain;
    	drivetrain.resetGyro();
    	lastSpeedL = lspeed;
    	lastSpeedR = rspeed;
    	
    	
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	double currentAngle = drivetrain.getAngle();
    	System.out.println("Current Angle: " + currentAngle);	
    	setAngleSetpoint(targetAngle);
    	
    	double valuePID = turnPID(currentAngle);
    	System.out.println("PID Value: " + valuePID + "\n");
    	
    	if (currentAngle < targetAngle) {
    		drivetrain.drive(lastSpeedL, lastSpeedL);
    		lastSpeedL *= valuePID;
    	} //else {
    	else if(currentAngle > targetAngle)
    	{
    		drivetrain.drive(-lastSpeedR, -lastSpeedR);
    		lastSpeedR *= valuePID;
    	}
    		//drivetrain.drive(0, 0);
    	//}
    /*	if (valuePID >=0) {
    		drivetrain.drive((lspeed * (1-valuePID)), minSpeed);
    	} else if (valuePID < 0){
    		drivetrain.drive(-minSpeed, rspeed * (1-valuePID));
    	}
    	*/
    	
    }
    

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	if (Math.abs(drivetrain.getAngle() - targetAngle) < 1) {
    		return true;
    	}
    	else {
    		return false;
    	}
    }

    // Called once after isFinished returns true
    protected void end() {
    	drivetrain.drive(0, 0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    //	drivetrain.drive(0,0);
    	//yay
    }
    
    public double turnPID(double currentAngle)
	{
    	if(targetAngle > 0)
    	{
    		aP = .5;
    	}
    	else
    		aP = .9;
		//How far the Robot is from it's target distance
		double angleError = Math.abs(angleSetpoint - currentAngle);  //inverse of difference between current distance and target distance 
	//	double angleError = drivetrain.getAngle() - angleSetpoint;
		angleIntegral += (angleError * .02);
		double angleDerivative = (angleError - previousAngle)/.02;
		previousAngle = angleError;
		double output = 1- 1/(aP * angleError + aI * angleIntegral + aD * angleDerivative); //inverse of output
	
		/*
		double newSpeed = previousAngle * (1-output);
		previousAngle = previousAngle * (1-output);
		*/
		return output;
	}
    
    public void setAngleSetpoint(double target)
	{
		this.angleSetpoint = target;
	}
}