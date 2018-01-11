
package org.usfirst.frc.team1289.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import org.usfirst.frc.team1289.robot.RobotMap;
import org.usfirst.frc.team1289.robot.commands.*;
import org.usfirst.frc.team1289.robot.subsystems.*;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {

	private static RobotMap _ioMap = new RobotMap();
		
	private static TestMotor _testMotor;
	private static DriveTrain _driveTrain;
	private static Winch _winch;
	private static RangeFinder _rangeFinder;
	private static Gyroscope _gyro;
	private static Accelerometer _accelerometer;
	private static Switch _switch;
	
	private static Command _testCommand;
	private static Command _driveToDistanceCommand;
	private static Command _driveViaStickCommand;
	private static Command _winchRaiseCommand;
	
	public static OperatorInterface OperatorStation;

    Command autonomousCommand;
    Command teleopCommand;
    SendableChooser chooser;

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
    	// Must happen in this order
    	SubsystemInit();
    	CommandInit();
    	OperatorStation = new OperatorInterface(_winchRaiseCommand, _ioMap.IO_Joystick, _ioMap.IO_ButtonStation);
    	
    	SmartDashboard.putString("position", "C");
    	
    	chooser = new SendableChooser();
    //    chooser.addDefault("Default Auto", new ExampleCommand());
//        chooser.addObject("My Auto", new MyAutoCommand());
      //  SmartDashboard.putData("Auto mode", chooser);
    	
    }
	
    private void SubsystemInit()
    {
    	_testMotor = new TestMotor(_ioMap.PWM_Motor);
    	_driveTrain = new DriveTrain(_ioMap.PWM_leftFrontMotor, _ioMap.PWM_rightFrontMotor,
    								_ioMap.PWM_leftRearMotor, _ioMap.PWM_rightRearMotor,
    								_ioMap.DIO_leftEncoder, _ioMap.DIO_rightEncoder);
    	_winch = new Winch(_ioMap.PWM_winchMotor, _ioMap.DIO_limitSwitch);
    	_rangeFinder = new RangeFinder(_ioMap.AIO_RangeFinder);
    	_gyro = new Gyroscope(_ioMap.AIO_Gyroscope);
    	_accelerometer = new Accelerometer();
    	_switch = new Switch(_ioMap.DIO_Switch);
    }
    
    private void CommandInit()
    {
    	_testCommand = new TestCommand(_switch);
    	_winchRaiseCommand  = new WinchRaise(_winch);
    	_driveToDistanceCommand = new DriveToDistance(_driveTrain, 0.1, 130.0);
    	_driveViaStickCommand = new DriveViaStick(_driveTrain);
    	
    }
	/**
     * This function is called once each time the robot enters Disabled mode.
     * You can use it to reset any subsystem information you want to clear when
	 * the robot is disabled.
     */
    public void disabledInit(){

    }
	
	public void disabledPeriodic() {
		//Scheduler.getInstance().run();
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select between different autonomous modes
	 * using the dashboard. The sendable chooser code works with the Java SmartDashboard. If you prefer the LabVIEW
	 * Dashboard, remove all of the chooser code and uncomment the getString code to get the auto name from the text box
	 * below the Gyro
	 *
	 * You can add additional auto modes by adding additional commands to the chooser code above (like the commented example)
	 * or additional comparisons to the switch structure below with additional strings & commands.
	 */
    public void autonomousInit() {
        autonomousCommand = _testCommand;
        GetAutoModeCommand();
                
//        System.out.printf("%s %s\n", position, gameData);
        
		/* String autoSelected = SmartDashboard.getString("Auto Selector", "Default");
		switch(autoSelected) {
		case "My Auto":
			autonomousCommand = new MyAutoCommand();
			break;
		case "Default Auto":
		default:
			autonomousCommand = new ExampleCommand();
			break;
		} */
    	
    	// schedule the autonomous command (example)
        if (autonomousCommand != null) 
        	autonomousCommand.start();
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
        Scheduler.getInstance().run();
    }

    public void teleopInit() {
		// This makes sure that the autonomous stops running when
        // teleop starts running. If you want the autonomous to 
        // continue until interrupted by another command, remove
        // this line or comment it out.
        if (autonomousCommand != null) 
        	autonomousCommand.cancel();
        
        teleopCommand = _testCommand;
        teleopCommand.start();
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        Scheduler.getInstance().run();
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
        LiveWindow.run();
    }
    
    private void GetAutoModeCommand()
    {
    	String gameData;
        gameData = DriverStation.getInstance().getGameSpecificMessage();
        String position = SmartDashboard.getString("position", "C");
        switch (position)
        {
        case "C":
        case "c":
        	if (gameData.charAt(0) == 'L')
        		System.out.println("Center, left");
        	else
        		System.out.println("Center, right");
        	break;
        	
        case "L":
        case "l":
        	if (gameData.charAt(0) == 'L' && gameData.charAt(1) == 'L')
        		System.out.println("switch straight clw");
        	else if (gameData.charAt(0) == 'L' && gameData.charAt(1) == 'R')
        		System.out.println("switch straight clw");
        	else if (gameData.charAt(0) == 'R' && gameData.charAt(1) == 'L')
        		System.out.println("scale straight clw");
        	else if (gameData.charAt(0) == 'R' && gameData.charAt(1) == 'R')
        		System.out.println("wait for teleop");
        	else
        		System.out.printf("unknown set of gamedata %s %s \n", position, gameData);
        	break;
    	
        case "R": 
        case "r": 
        	if (gameData.charAt(0) == 'L' && gameData.charAt(1) == 'L')
        		System.out.println("wait for teleop");
        	else if (gameData.charAt(0) == 'L' && gameData.charAt(1) == 'R')
        		System.out.println("scale straight cclw");
        	else if (gameData.charAt(0) == 'R' && gameData.charAt(1) == 'L')
        		System.out.println("switch straight cclw");
        	else if (gameData.charAt(0) == 'R' && gameData.charAt(1) == 'R')
        		System.out.println("switch straight cclw");
        	else
        		System.out.printf("unknown set of gamedata %s %s \n", position, gameData);
        	break;
        	
        	default:
        		System.out.printf("unknown position %s \n", position);
        }

    }
}
