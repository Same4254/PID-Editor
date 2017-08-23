package org.usfirst.frc.team3555.robot.Components;

import org.usfirst.frc.team3555.robot.Handler;

public class SquareWaveMonitor extends Updatable {
	private Handler handler;
	
	private long lastTime;
	private double frequency;
	
	private double[] setPoints;
	private int index;
	
	private boolean monitoring;
	private int id;
	
	public SquareWaveMonitor(Handler handler, int id) {
		this.handler = handler;
		this.id = id;
		
		setPoints = new double[2];
		
		active = true;
	}
	
	@Override
	public void update() {
		if(monitoring) {
			if(System.currentTimeMillis() > lastTime + frequency) {
				index++;
				
				if(index >= setPoints.length)
					index = 0;
				
				handler.getDeviceInfoManager().setDouble("SetPoint", setPoints[index], id);
				
				lastTime = System.currentTimeMillis();
			}
		}
	}
	
	public boolean start() {
		if(frequency == 0)
			return false;
		if(setPoints[0] == 0 && setPoints[1] == 0)
			return false;
		monitoring = true;
		return true;
	}
	
	public void end() {
		monitoring = false;
	}

	public boolean isMonitoring(){return monitoring;}
	public double[] getSetPoints(){return setPoints;}

	public void setFrequency(double frequency){this.frequency = frequency;}
}
