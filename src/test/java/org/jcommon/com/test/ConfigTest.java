package org.jcommon.com.test;

import java.io.FileNotFoundException;
import java.util.*;

import org.apache.log4j.xml.DOMConfigurator;
import org.jcommon.com.util.DateUtils;
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
//		String office_hour="00901000";
//		String start_time = office_hour.substring(0, 4);
//		String end_time   = office_hour.substring(4);
//		System.out.println(start_time+"\n"+end_time);
//		System.out.println("a\tb\tc\t");
		
//		String wechat_key = "gh_e6e86fdce3b9-fegeh";
//		String mywechat_id = wechat_key!=null && wechat_key.indexOf("-")!=-1?wechat_key.substring(0, wechat_key.lastIndexOf("-")):wechat_key;
//		System.out.println(mywechat_id);
//		
//		
//		
//		System.out.println(DateUtils.getNowSinceYear(new Date(1420635667402l)));
//		
		String str = "https%3A%2F%2Flivechat10.pccw.com%2Fchat%2Findex.jsp%3Fc_t%3DNTK%26c_id%3DGuest%26m_n%3DLGIF_PA_LGIF%26l%3Den%26n_n%3DGuest%26e_s%3Dcs.pccw.com";
		System.out.println(org.jcommon.com.util.CoderUtils.decode(str));
		
		
	}
}
