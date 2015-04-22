// ========================================================================
// Copyright 2012 leolee<workspaceleo@gmail.com>
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//     http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
// ========================================================================
package org.jcommon.com.util.system;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanRegistrationException;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.jcommon.com.util.config.ConfigLoader;
import org.jcommon.com.util.health.Status;
import org.jcommon.com.util.jmx.JmxManager;
/**
 * 
 * @author LeoLee<leo.li@protel.com.hk>
 *
 */
public class SystemConfig implements SystemConfigMBean {
	private static final SystemConfig instance = new SystemConfig();
	private static Logger logger = Logger.getLogger(SystemConfig.class);
	
	public static final String SPOTLIGHTHOME = "spotlightHome";
	public static final String HOME = System.getProperty(SPOTLIGHTHOME, System.getProperty("user.dir"));
	
	private long version;
	
	private Map<String,String> system_object;
	private Map<String,String> system_group; //group_id,server URL
	
	private int init_threads = 60;
	private String group_id  = "default";
	
	private String htmladapter;
	
	private String rmiadapter;
	
	private String mbean_server = "default";
	
	private List<String> ShareObject;
	
	private InputStream init_file_is;
	
	private String lib_url;
	
	private String deploy_name;
	
	private String log_path;
	
	private boolean auth;
	
	private String command_key;
	
	private int command_port;
	
	private long boot_time;
	
	private Map<String,String> system_user;
	
	public void registerMBean(){
		ObjectName adapterName;
		try {
			adapterName = new ObjectName("SystemManagerAdaptor:name=SystemConfig");
			if(JmxManager.instance()!=null)
				JmxManager.instance().registerMBean(SystemConfig.instance(), adapterName);
		} catch (MalformedObjectNameException e) {
			// TODO Auto-generated catch block
			logger.error("", e);
		} catch (InstanceAlreadyExistsException e) {
			// TODO Auto-generated catch block
			logger.error("", e);
		} catch (MBeanRegistrationException e) {
			// TODO Auto-generated catch block
			logger.error("", e);
		} catch (NotCompliantMBeanException e) {
			// TODO Auto-generated catch block
			logger.error("", e);
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			logger.error("", e);
		}	
	}
	
	public static SystemConfig instance(){	
		return  instance ;
	}
	
	public void setSystem_object(Map<String,String> system_object) {
		this.system_object = system_object;
	}

	public Map<String,String> getSystem_object() {
		return system_object;
	}

	public void setInit_threads(int init_threads) {
		this.init_threads = init_threads;
	}

	public int getInit_threads() {
		return init_threads;
	}

	public void setConfigUrl(InputStream init_file_is) {
		this.init_file_is = init_file_is; 
	}
	
	@Override
	public void reloadConfig() {
		// TODO Auto-generated method stub
		if(init_file_is==null){
			logger.info("input Stream can not be null");
			return;
		}
		try {
			ConfigLoader.loadConf4xml(init_file_is, this);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			logger.error("",e1);
			return;
		}
	}

	public void setHtmladapter(String htmladapter) {
		this.htmladapter = htmladapter;
	}

	public String getHtmladapter() {
		return htmladapter;
	}

	@Override
	public void saveConfig() {
		// TODO Auto-generated method stub
		if(init_file_is==null){
			logger.info("input Stream can not be null");
			return;
		}
	}

	public void setMbean_server(String mbean_server) {
		this.mbean_server = mbean_server;
	}

	public String getMbean_server() {
		return mbean_server;
	}

	public void setRmiadapter(String rmiadapter) {
		this.rmiadapter = rmiadapter;
	}

	public String getRmiadapter() {
		return rmiadapter;
	}

	public void setShareObject(List<String> ShareObject) {
		this.ShareObject = ShareObject;
	}

	public List<String> getShareObject() {
		return ShareObject;
	}
	
	public void setVersion(long version) {
		this.version = version;
	}

	public long getVersion() {
		return version;
	}

	public void setLib_url(String lib_url) {
		this.lib_url = lib_url;
	}

	public String getLib_url() {
		return lib_url;
	}

	public void setDeploy_name(String deploy_name) {
		this.deploy_name = deploy_name;
	}

	public String getDeploy_name() {
		return deploy_name;
	}

	public void setLog_path(String log_path) {
		this.log_path = log_path;
		try{
			DOMConfigurator.configure(HOME+File.separator+log_path);
		}catch(Exception e){e.printStackTrace();}
	}

	public String getLog_path() {
		return log_path;
	}

	public boolean isStandby() {
		return Status.StandbyMode.equals(System.getProperty("MODE"));
	}

	public void setStandby(boolean standby) {
		setBoot_time(new Date().getTime());
		System.setProperty("MODE", standby?Status.StandbyMode:Status.ActiveMode);
	}

	public void setAuth(boolean auth) {
		this.auth = auth;
	}

	public boolean isAuth() {
		return auth;
	}

	public void setSystem_user(Map<String,String> system_user) {
		this.system_user = system_user;
	}

	public Map<String,String> getSystem_user() {
		return system_user;
	}

	public void setSystem_group(Map<String,String> system_group) {
		this.system_group = system_group;
	}

	public Map<String,String> getSystem_group() {
		return system_group;
	}

	public void setGroup_id(String group_id) {
		this.group_id = group_id;
	}

	public String getGroup_id() {
		return group_id;
	}

	public void setCommand_port(int command_port) {
		this.command_port = command_port;
	}

	public int getCommand_port() {
		return command_port;
	}

	public void setCommand_key(String command_key) {
		this.command_key = command_key;
	}

	public String getCommand_key() {
		return command_key;
	}

	public void setBoot_time(long boot_time) {
		this.boot_time = boot_time;
	}

	public long getBoot_time() {
		return boot_time;
	}
}
