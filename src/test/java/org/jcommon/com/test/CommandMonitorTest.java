package org.jcommon.com.test;

import org.apache.log4j.xml.DOMConfigurator;
import org.jcommon.com.util.system.CommandMonitor;

public class CommandMonitorTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DOMConfigurator.configure("log4j.xml");
		// TODO Auto-generated method stub
		CommandMonitor cm = new CommandMonitor(34,"");
		cm.copyCommand("192.168.2.72", 9088, "SPOTLIGHT", "active");
	}

}
