package org.usfirst.frc.team178.robot;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.usfirst.frc.team178.robot.autocommandgroups.*;
import org.usfirst.frc.team178.robot.subsystems.*;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {

	public static OI oi;
	public static Drivetrain drivetrain;
	public static CubeIntake cubeintake;
	public static Ramp ramp;
	public static Climber climber;
	public static Pneumatics pneumatics;
	public static AnalogGyro gyro;
	public static LightsSubsystem lights;
	


	Command autonomousCommand;
	
	public static SendableChooser<String> botLocation = new SendableChooser<>();
	public static SendableChooser<String> switchChooser = new SendableChooser<>();
	public static SendableChooser<String> scaleChooser = new SendableChooser<>();
	public static SendableChooser<String> goForward = new SendableChooser<>();
	public static SendableChooser<String> pickUpSecondBlock = new SendableChooser<>();
	public static SendableChooser<String> vault = new SendableChooser<>();
	public static SendableChooser<Command> autoChooser = new SendableChooser<>();
	
	public static boolean goForwardChoice;
	public static boolean vaultChooser;
	public static boolean switchChoice;
	public static boolean scaleChoice;
	public static boolean secondBlock;
	public static boolean isOnSide;
	
	public static String gameData;
	public static char switchSide;
	public static char scaleSide;
	public static String position;

	SendableChooser<Command> chooser = new SendableChooser<>();


	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	
	
	public void robotInit() {
		drivetrain = new Drivetrain();
		cubeintake = new CubeIntake();
		ramp = new Ramp();
		climber = new Climber();
		pneumatics = new Pneumatics();
		oi = new OI();
		
	/*	UsbCamera camera1 = CameraServer.getInstance().startAutomaticCapture(0);
		camera1.setResolution(320, 240);
		camera1.setFPS(25);
		
		UsbCamera camera2 = CameraServer.getInstance().startAutomaticCapture(1);
		camera2.setResolution(320, 240);
		camera2.setFPS(25); */
	
		autoChooser.addObject("Testing", new Testing());
		SmartDashboard.putData(autoChooser);
		
		botLocation.addObject("Left", "Left");
		botLocation.addObject("Middle", "Middle");
		botLocation.addObject("Right", "Right");
		
		SmartDashboard.putData("AutoLocation", botLocation);
		chooser.addObject("Testing", new Testing());
		SmartDashboard.putData("Auto mode", chooser);
		
		switchChooser.addObject("Yes", "Yes");
		switchChooser.addObject("No", "No");
		switchChooser.addDefault("No", "No");

		scaleChooser.addObject("Yes", "Yes");
		scaleChooser.addObject("No", "No");
		scaleChooser.addDefault("No", "No");
		
		goForward.addObject("Yes", "Yes");
		goForward.addObject("No", "No");
		goForward.addDefault("No", "No");
		SmartDashboard.putData("Drive Forward?", goForward);
		
		pickUpSecondBlock.addDefault("No", "No");
		pickUpSecondBlock.addObject("Yes", "Yes");
		

		vault.addObject("Yes", "Yes");
		vault.addObject("No", "No");
		vault.addDefault("No", "No");

		SmartDashboard.putData("Go for vault?", vault);
		SmartDashboard.putData("Go for switch?", switchChooser);
		SmartDashboard.putData("Go for scale?", scaleChooser);
		SmartDashboard.putData("Second block?", pickUpSecondBlock);
		
		System.out.println("Hello");
	}

		
	

	public static String returnSelection() {
		return botLocation.getSelected();
		
	}
	/**
	 * This function is called once each time the robot enters Disabled mode.
	 * You can use it to reset any subsystem information you want to clear when
	 * the robot is disabled.
	 */
	@Override
	public void disabledInit() {
			}

	@Override
	public void disabledPeriodic() {
		Scheduler.getInstance().run();
	}
	
	boolean[] userChoice = new boolean[5];
	char[] fieldConfig = new char[3];
	
	public void getAutoSelections() {
		//gameData = DriverStation.getInstance().getGameSpecificMessage();\
		gameData = "RLR"; //for testing purposes
		
		//Array of the choices driver can make before auto
		userChoice[0] = (goForward.getSelected()).equals("Yes");
		userChoice[1] = (vault.getSelected()).equals("Yes");
		userChoice[2] = ((switchChooser.getSelected()).equals("Yes"));
		userChoice[3] = (scaleChooser.getSelected()).equals("Yes");
		userChoice[4] = (pickUpSecondBlock.getSelected()).equals("Yes");
		
		//Predetermined field positions/aspects
		fieldConfig[0] = gameData.charAt(0); //this is switch 
		fieldConfig[1] = gameData.charAt(1);  //this is scale
    	fieldConfig[2] = returnSelection().charAt(0); //starting position

		
	}
	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString code to get the auto name from the text box below the Gyro
	 *
	 * You can add additional auto modes by adding additional commands to the
	 * chooser code above (like the commented example) or additional comparisons
	 * to the switch structure below with additional strings & commands.
	 */
	@Override
	public void autonomousInit() {
		/*
		 * String autoSelected = SmartDashboard.getString("Auto Selector",
		 * "Default"); switch(autoSelected) { case "My Auto": autonomousCommand
		 * = new MyAutoCommand(); break; case "Default Auto": default:
		 * autonomousCommand = new ExampleCommand(); break; }
		 */
		getAutoSelections();
		autonomousCommand = new AutoDecisions(userChoice, fieldConfig);
		
		if (autonomousCommand != null)
			autonomousCommand.start();

		// schedule the autonomous command (example)
		
	}

	/**
	 * This function is called periodically during autonomous
	 */
	@Override
	public void autonomousPeriodic() {
		Scheduler.getInstance().run();
	}

	@Override
	public void teleopInit() {
		// This makes sure that the autonomous stops running when
		// teleop starts running. If you want the autonomous to
		// continue until interrupted by another command, remove
		// this line or comment it out.
		if(autonomousCommand != null)
			autonomousCommand.cancel();
		//add something else
		
	}

	/**
	 * This function is called periodically during operator control
	 */
	@Override
	public void teleopPeriodic() {
		Scheduler.getInstance().run();
	}

	/**
	 * This function is called periodically during test mode
	 */
	@SuppressWarnings("deprecation")
	@Override
	public void testPeriodic() {
		LiveWindow.run();
	}
}