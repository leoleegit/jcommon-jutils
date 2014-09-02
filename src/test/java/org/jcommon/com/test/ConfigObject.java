package org.jcommon.com.test;

import java.util.List;
import java.util.Map;

import org.jcommon.com.util.config.BaseConfigMBean;

public class ConfigObject implements BaseConfigMBean {
	private List<String> list_object;
	
	private Map<String,String> system_object;

	private String htmladapter;
	
	public void setSystem_object(Map<String,String> system_object) {
		this.system_object = system_object;
	}

	public Map<String,String> getSystem_object() {
		return system_object;
	}

	public void setList_object(List<String> list_object) {
		this.list_object = list_object;
	}

	public List<String> getList_object() {
		return list_object;
	}

	@Override
	public void reloadConfig() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void saveConfig() {
		// TODO Auto-generated method stub
		
	}

	public void setHtmladapter(String htmladapter) {
		this.htmladapter = htmladapter;
	}

	public String getHtmladapter() {
		return htmladapter;
	}
}
