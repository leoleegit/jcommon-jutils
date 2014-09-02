package org.jcommon.com.test;

import java.io.FileNotFoundException;
import java.util.*;

import org.apache.log4j.xml.DOMConfigurator;
import org.jcommon.com.util.SortUtils;
import org.jcommon.com.util.config.BaseConfigMBean;
import org.jcommon.com.util.config.ConfigLoader;
import org.jcommon.com.util.system.SystemConfig;

public class ConfigTest {
	public static void main(String[] args) throws FileNotFoundException{
//		DOMConfigurator.configure("log4j_fb.xml");
//		ConfigObject config = new ConfigObject();
//	    ConfigLoader.loadConf4xml(ConfigTest.class.getResourceAsStream("/system.xml"), config);
//	    
//	    // System.out.println(config.getHtmladapter());
//	    String[] ss = SortUtils.sortKey(config.getSystem_object().keySet());
//	    for(String s: ss)
//	    	System.out.println(s);
//	    
//	    config.setHtmladapter("{\"user\":\"admin\",\"password\":\"ptsws\",\"port\":\"10042\"}");
//	    ConfigLoader.saveConf2xml(ConfigTest.class.getResource("/system.xml"), config);
		String office_hour="00901000";
		String start_time = office_hour.substring(0, 4);
		String end_time   = office_hour.substring(4);
		System.out.println(start_time+"\n"+end_time);
		System.out.println("a\tb\tc\t");
	}
}
