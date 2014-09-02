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
package org.jcommon.com.util.jmx;

import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanRegistrationException;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;

import org.apache.log4j.Logger;
import org.jcommon.com.util.jmx.JmxManager;
import org.jcommon.com.util.jmx.PropertyManager;
import org.jcommon.com.util.system.SystemListener;

public class Monitor extends PropertyManager implements SystemListener {

	protected static final Logger logger = Logger.getLogger(Monitor.class);
	
	private String monitor_name;
	private boolean initialized;

	public Monitor(String monitor_name){
		super(true,false);	
		this.monitor_name = monitor_name;
	}
	
	public Monitor(String monitor_name,boolean isReadable, boolean isWritable){
		super(isReadable,isWritable);	
		this.monitor_name = monitor_name;
	}
	
	public void registerMBean(){
		ObjectName adapterName;
		logger.info(monitor_name+" init...");
		try {
			adapterName = new ObjectName(String.format("MonitorAdaptor:name=%s",monitor_name));
			if(JmxManager.instance()!=null)
				JmxManager.instance().registerMBean(this, adapterName);
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
	
	public void unRigisterMBean(){
		ObjectName adapterName;
		logger.info(monitor_name+" unRigister...");
		try {
			adapterName = new ObjectName(String.format("MonitorAdaptor:name=%s",monitor_name));
			if(JmxManager.instance()!=null)
				JmxManager.instance().unRigisterMBean(adapterName);
		} catch (MalformedObjectNameException e) {
			// TODO Auto-generated catch block
			logger.error("", e);
		} catch (MBeanRegistrationException e) {
			// TODO Auto-generated catch block
			logger.error("", e);
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			logger.error("", e);
		} catch (InstanceNotFoundException e) {
			// TODO Auto-generated catch block
			logger.error("", e);
		}
	}
	
	public void addProperties(String key, String value){
		try{
			super.addProperties(key, value);
		}catch(Exception e){logger.error("",e);}
	}
	
	public void removeProperties(String key){
		try{
			super.removeProperties(key);
		}catch(Exception e){logger.error("",e);}
	}

	@Override
	public void shutdown() {
		// TODO Auto-generated method stub
		unRigisterMBean();
	}

	@Override
	public void startup() {
		// TODO Auto-generated method stub
		if(!initialized)
			initOperation();
		registerMBean();
	}

	public void initOperation() {
		// TODO Auto-generated method stub
		initialized = true;
	}

	@Override
	public boolean isSynchronized() {
		// TODO Auto-generated method stub
		return false;
	}

}
