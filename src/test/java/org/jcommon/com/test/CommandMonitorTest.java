package org.jcommon.com.test;

import java.io.IOException;

import org.apache.log4j.xml.DOMConfigurator;
import org.jcommon.com.util.system.CommandMonitor;

public class CommandMonitorTest {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		DOMConfigurator.configure("log4j.xml");
		// TODO Auto-generated method stub
		CommandMonitor cm = new CommandMonitor(-1,"spotlight");
		
		cm.copyCommand("192.168.2.71", 9898, "spotlight", "cap:only");
	}

}
