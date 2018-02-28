package org.usfirst.frc.team1289.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team1289.robot.subsystems.Retractor;
import edu.wpi.first.wpilibj.Timer;

/**
 *
 */
public class ActuateRetractor extends Command 
{
	private Retractor _retractor;
	private Timer _timer;
	private boolean _isDone = false;
	private RetractorDirection _direction;
	
    public ActuateRetractor(Retractor retractor, RetractorDirection direction) 
    {
    	_retractor = retractor;
    	_direction = direction;
    	_timer = new Timer();
    }

    // Called just before this Command runs the first time
    protected void initialize() 
    {
    	_retractor.Stop();
    	_timer.reset();
    	_timer.start();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() 
    {
    	double speed = 0.7;
    	if (_direction == RetractorDirection.UP)
    		speed = speed;
    	else 
    		speed = - speed;
    	
    	_retractor.Move(speed);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() 
    {
    	if (_direction == RetractorDirection.UP && _retractor.IsAtLimit())
			return true;
    	else
    		if (_direction == RetractorDirection.DOWN && _timer.get() > 1.0)
    			return true;
    	
        return false;
    }

    // Called once after isFinished returns true
    protected void end() 
    {
    	_retractor.Stop();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}