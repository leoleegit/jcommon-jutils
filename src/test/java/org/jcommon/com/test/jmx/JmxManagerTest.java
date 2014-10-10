package org.jcommon.com.test.jmx;

import org.apache.log4j.xml.DOMConfigurator;
import org.jcommon.com.util.jmx.JmxManager;
import org.jcommon.com.util.system.SystemConfig;
import org.jcommon.com.util.system.SystemManager;

public class JmxManagerTest {
	 public static void main(String[] args) throws Exception {
		 DOMConfigurator.configure("log4j.xml");
		 //SystemConfig.instance().setHtmladapter("{user:admin,password:admin,port:10042}");
		 SystemConfig.instance().setRmiadapter("{name:jmxrmi,user:admin,CREDENTIALS:admin,addr:192.168.2.104,port:10042}");
		 SystemManager.instance().contextInitialized(null);
	 }
}
