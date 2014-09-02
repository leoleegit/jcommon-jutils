package org.jcommon.com.test;

import org.apache.log4j.xml.DOMConfigurator;
import org.jcommon.com.util.system.SystemConfig;
import org.jcommon.com.util.system.SystemListener;
import org.jcommon.com.util.system.SystemManager;
import org.jcommon.com.util.thread.ThreadManager;

public class SystemManagerTest implements SystemListener{
	public static void main(String[] args){
		DOMConfigurator.configure("log4j_fb.xml");
		SystemManager.instance().addListener(new SystemManagerTest());
		SystemManager s = new SystemManager();
		SystemManager.instance().addListener(new SystemManagerTest());
		s.contextInitialized(null);
		
		SystemConfig sc = new SystemConfig();
		ThreadManager tm = new ThreadManager();
		System.out.println(sc.instance());
		System.out.println(tm.instance());
	}

	@Override
	public void shutdown() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void startup() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isSynchronized() {
		// TODO Auto-generated method stub
		return false;
	}
}
