package org.jcommon.com.util.thread;

import java.util.*;

public class TimerTaskManger {
	private static final TimerTaskManger instance = new TimerTaskManger();
	private Map<Object,Timer> timers = new HashMap<Object,Timer>();
	
	public static TimerTaskManger instance(){
		return instance;
	}
	
	public Timer schedule(String name, TimerTask task, long delay){
		Timer timer = new Timer(name==null?this.getClass().getName():name);
		timer.schedule(task, delay);
		return timer;
	}
	
	public Timer schedule(String name, TimerTask task, long firstTime, long period){
		Timer timer = new Timer(name==null?this.getClass().getName():name);
		timer.schedule(task, firstTime, period);
		return timer;
	}
	
	public void putTimer(Object key, Timer timer){
		if(timers.containsKey(key)){
			Timer t = timers.remove(key);
			try{
				t.cancel();
				t = null;
			}catch(Exception e){}	
		}
		timers.put(key, timer);		
	}
	
	public Timer getTimer(Object key){
		if(timers.containsKey(key))
			return timers.get(key);
		return null;
	}
	
	public Timer removeTimer(Object key){
		if(timers.containsKey(key))
			return timers.remove(key);
		return null;
	}
}
