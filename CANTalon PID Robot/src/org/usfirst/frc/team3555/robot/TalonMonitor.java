package org.usfirst.frc.team3555.robot;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.networktables.NetworkTable;

public class TalonMonitor {
	private CANTalon talon;
	
	private String name;
	private int id;
	private double p, i, d, f;
	private double setPoint; 
	
	public TalonMonitor(CANTalon talon) {
		this.talon = talon;
		
		id = talon.getDeviceID();
	}
	
	public TalonMonitor(String name, CANTalon talon) {
		this(talon);
		this.name = name;
	}
	
	public void update(NetworkTable table) {
		table.putString(id + " Name", name);
		table.putNumber(id + " Velocity", talon.getSpeed());
		table.putNumber(id + " Position", talon.getPosition());
		table.putNumber(id + " Voltage", talon.getBusVoltage());
		table.putNumber(id + " Current", talon.getOutputCurrent());
		table.putNumber(id + " Temperature", talon.getTemperature());
		table.putNumber(id + " BatteryVoltage", DriverStation.getInstance().getBatteryVoltage());
		
		if(table.getBoolean(id + " Enabled", false) && !talon.isEnabled()) {
			talon.enable();
			
			talon.setP(p);
			talon.setI(i);
			talon.setD(d);
			talon.setF(f);
			talon.setSetpoint(setPoint);
		}
    		
    	else if(!table.getBoolean(id + " Enabled", false) && talon.isEnabled())
    		talon.disable();
		
		if(table.getBoolean(id + " ResetPosition", false)) {
			talon.setPosition(0);
			table.putBoolean(id + " ResetPosition", false);
		}
		
		checkPIDF(table);
		
		if(table.getNumber(id + " Mode", 0) != talon.getControlMode().value) 
			talon.changeControlMode(CANTalon.TalonControlMode.valueOf((int) table.getNumber(id + " Mode", 0)));
	}
	
	public void checkPIDF(NetworkTable table) {
		double tempP = table.getNumber(id + " P", 0), tempI = table.getNumber(id + " I", 0), 
				tempD = table.getNumber(id + " D", 0), tempF = table.getNumber(id + " F", 0),
				tempSetPoint = table.getNumber(id + " SetPoint", 0);
		
		if(tempP != talon.getP()) {
			talon.setP(tempP);
			p = tempP;
		}
		
		if(tempI != talon.getI()) {
			talon.setI(tempI);
			i = tempI;
		}
		
		if(tempD != talon.getD()) {
			talon.setD(tempD);
			d = tempD;
		}
		
		if(tempF != talon.getF()) {
			talon.setF(tempF);
			f = tempF;
		}
		
		if(tempSetPoint != talon.getSetpoint()) {
			talon.setSetpoint(tempSetPoint);
			setPoint = tempSetPoint;
		}
	}
}