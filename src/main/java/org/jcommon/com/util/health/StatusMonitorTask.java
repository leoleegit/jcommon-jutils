package org.jcommon.com.util.health;

import java.util.Timer;
import java.util.TimerTask;

import org.jcommon.com.util.thread.TimerTaskManger;

public abstract class StatusMonitorTask extends TimerTask{
	
	private Timer timer;
	private Status status;
	
	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public StatusMonitorTask(String name, long firstTime, long period){
		status = new Status(name);
		timer = TimerTaskManger.instance().schedule("StatusMonitorTask-"+name, this, firstTime, period);
	}
	
	public abstract void run();
	
	public Timer getTimer(){
		return timer;
	}
	
	public void setStatus(String status){
		this.status.setStatus(status);
		StatusManager.instance().addStatus(this.status);
	}
	
	public boolean cancel(){
		try{
			timer.cancel();
			timer = null;
		}catch(Exception e){}
		return super.cancel();
	}
}
