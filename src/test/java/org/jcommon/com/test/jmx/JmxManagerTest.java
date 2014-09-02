package org.jcommon.com.test.jmx;

import org.apache.log4j.xml.DOMConfigurator;
import org.jcommon.com.util.jmx.JmxManager;
import org.jcommon.com.util.system.SystemConfig;

public class JmxManagerTest {
	 public static void main(String[] args) throws Exception {
		 DOMConfigurator.configure("log4j_fb.xml");
		 SystemConfig.instance().setHtmladapter("{user:admin,password:admin,port:10042}");
		 new JmxManager().startup();
	 }
}
