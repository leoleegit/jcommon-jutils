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
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URLClassLoader;
import java.util.*;

import javax.management.MBeanOperationInfo;
import javax.management.MBeanParameterInfo;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.jcommon.com.util.SortUtils;
import org.jcommon.com.util.health.StatusManager;
import org.jcommon.com.util.jmx.Monitor;
import org.jcommon.com.util.thread.ThreadManager;

/**
 * 
 * @author LeoLee<leo.li@protel.com.hk>
 *
 */
public class SystemManager extends Monitor implements ServletContextListener {
	private static Logger logger;
	
	private static SystemManager instance;
	private static URLClassLoader class_loader;
	private static List<Object> store      = new ArrayList<Object>();
	
	private static InputStream init_file_is = SystemManager.class.getResourceAsStream("/system.xml");
	
	private static final int STARTED  = 1;
	private static final int STARTING = 2;
	private static final int END      = 3;
	private static final int INIT     = 4;
	
	private static int status         = END;
	private CommandMonitor command;
	
	private static String HOME = System.getProperty("user.dir");
	private static String log_file = "log4j.xml";
	
	static {
		if(System.getProperty("jcommon.home")!=null)
			HOME = System.getProperty("jcommon.home");
		if(System.getProperty("jcommon.log")!=null)
			log_file = System.getProperty("jcommon.log");
		DOMConfigurator.configure(HOME+File.separator+log_file);
		logger = Logger.getLogger(SystemManager.class);
	}
	
	public static SystemManager instance(){
		if(instance == null){
			//instance = new SystemManager();
			logger.warn("SystemManager not Initialized");
		}
		return instance;
	}

	public SystemManager(){
		super("SystemManager");
		logger.info(this);
		instance = this;
	}
	
	public boolean isRunning(){
		return status == STARTED || status == STARTING;
	}
	
	public void addListener(SystemListener listener){
		ThreadManager.instance().execute(new ListenerTask(listener,true));
		synchronized(store){
			((ArrayList<Object>)store).add(listener);
		}
	}
	
	public boolean addListener(int index, SystemListener listener){
		ThreadManager.instance().execute(new ListenerTask(listener,true));
		synchronized(store){
			((ArrayList<Object>)store).add(index, listener);
		}
		return true;
	}
	
	public void removeListener(SystemListener listener){
		synchronized(store){
			((ArrayList<Object>)store).remove(listener);
		}
		super.removeProperties(String.valueOf(listener.getClass().hashCode()));
	}
	
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		stop();
		logger.info("SystemManager shutdown");
		status = END;
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		logger.info(status);
		if(status!=END)
			return;
		class_loader = new SystemClassLoader();
		Thread loader_thrad = new Thread(){
			public void run(){
				instance = SystemManager.this;
				logger.info("SystemManager initializing");
				SystemConfig config = SystemConfig.instance();
				config.setConfigUrl(init_file_is);
				config.reloadConfig();
					
				String ROOT       = System.getProperty("user.dir");
				String CONFIG_DIRECTORY = ROOT + File.separator + config.getLib_url();
				if(config.getLib_url()!=null){
					try {
						((SystemClassLoader)class_loader).addDirectory(new File(CONFIG_DIRECTORY));
					} catch (MalformedURLException e1) {
						// TODO Auto-generated catch block
						logger.error("", e1);
					}
				}
				logger.info("SystemManager running....");
				
				new org.jcommon.com.util.jmx.JmxManager().startup();
				new StatusManager().startup();
				
				init();
				initOperation();
				
				if(config.getCommand_port()>0){
					command = new CommandMonitor(config.getCommand_port(),config.getCommand_key());
				}
				registerMBean();
				SystemConfig.instance().registerMBean();
				SystemManager.this.start();
			}
		};
		loader_thrad.setName("class loader thread");
		loader_thrad.setContextClassLoader(class_loader);
		loader_thrad.start();
		
		status = INIT;
	}

	private void init() {
		// TODO Auto-generated method stub	
		SystemConfig config = SystemConfig.instance();
		Map<String,String> listener = config.getSystem_object();
		if(listener!=null){
			String keys[] = SortUtils.sortKey(listener.keySet());
			for(String key: keys){
				addModule(null, listener.get(key));
			}
		}
	}

	public void start(){
		logger.info("start ...");
		if(store==null)return;
		status = STARTING;
		synchronized(store){
			for(Object o : store.toArray()){
				logger.info(o+" start in mode"+(SystemConfig.instance().isStandby()?":STANDBY":":ACTIVE"));
				SystemListener listener = (SystemListener)o;
				if(listener.isSynchronized()){
					try{
						if(!SystemConfig.instance().isStandby())
							listener.startup();
						SystemManager.this.addProperties(String.valueOf(listener.getClass().hashCode()), 
								listener.getClass().getName()+(SystemConfig.instance().isStandby()?":STANDBY":":ACTIVE"));
					}catch(Exception e){
						SystemManager.this.addProperties(String.valueOf(listener.getClass().hashCode()), listener.getClass().getName()+":Exception");
						logger.error("", e);
					}
				}
				else
					ThreadManager.instance().execute(new ListenerTask((SystemListener)o,true));
			}
		}
		status = STARTED;
	}
	
	public void stop(){
		logger.info("down ...");
		if(store==null)return;
		synchronized(store){
			for(int i=store.size()-1;i>=0;i--){
				Object o = store.get(i);
				logger.info(o+" down ...");
				SystemListener listener = (SystemListener)o;
				if(listener.isSynchronized()){
					try{
						if(!SystemConfig.instance().isStandby())
							listener.shutdown();
						SystemManager.this.addProperties(String.valueOf(listener.getClass().hashCode()), listener.getClass().getName()+":STOPED");
					}catch(Exception e){
						SystemManager.this.addProperties(String.valueOf(listener.getClass().hashCode()), listener.getClass().getName()+":STOPED:Exception");
						logger.error("", e);
					}
				}else
					ThreadManager.instance().execute(new ListenerTask(listener,false));
			}
		}
		status = INIT;
	}
	
	public void restartSystem(){
		logger.info("restart ...");
		stop();
		start();
	}
	
	public void removeModule(String module_id){
		module_id = module_id!=null?module_id.trim():null;
		SystemListener listener = null;
		try{
			int hashcode = Integer.valueOf(module_id);
			synchronized(store){
				for(Object o : store){
					if(o.getClass().hashCode()==hashcode){
						listener = (SystemListener) o;
						listener.shutdown();
						break;
					}
				}
			}
		}catch(Exception e){
			logger.error("module id:"+module_id, e);
		}
		store.remove(listener);
		super.removeProperties(module_id);
	}
	
	public void restartModule(String module_id){
		stopModule(module_id);
		try{
			Thread.sleep(5000);
		}catch(Exception e){}
		startModule(module_id);
	}
	
	public void startModule(String module_id){
		SystemListener listener = getModule(module_id);
		if(listener!=null)
			ThreadManager.instance().execute(new ListenerTask(listener,true));
	}
	
	public void stopModule(String module_id){
		SystemListener listener = getModule(module_id);
		if(listener==null)
			return;
		ThreadManager.instance().execute(new ListenerTask(listener,false));
	}
	
	private SystemListener getModule(String module_id){
		module_id = module_id!=null?module_id.trim():null;
		SystemListener listener = null;
		try{
			int hashcode = Integer.valueOf(module_id);
			synchronized(store){
				for(Object o : store){
					if(o.getClass().hashCode()==hashcode){
						listener = (SystemListener) o;
						break;
					}
				}
			}
		}catch(Exception e){
			logger.error("module id:"+module_id, e);
		}
		return listener;
	}
	
	
	
	class ListenerTask implements Runnable{
		SystemListener listener;
		boolean start;
		
		public ListenerTask(SystemListener listener,boolean start){
			this.listener = listener;
			this.start    = start;
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			if(listener==null)return;
			if(start){
				if(!SystemConfig.instance().isStandby() && isRunning())
					listener.startup();
				SystemManager.this.addProperties(String.valueOf(listener.getClass().hashCode()), 
						listener.getClass().getName()+(SystemConfig.instance().isStandby()?":STANDBY":":ACTIVE"));
			}
			else{
				if(!SystemConfig.instance().isStandby())
					listener.shutdown();
				SystemManager.this.addProperties(String.valueOf(listener.getClass().hashCode()), listener.getClass().getName()+":STOPED");
			}
		}
	}
	
	public void addModule(String jar_path, final String class_name){
		
		try {
			if(jar_path!=null && !"".equals(jar_path)){
				if(!jar_path.startsWith("file://"))
					jar_path = System.getProperty("user.dir") + File.separator + jar_path;
				try {
					((SystemClassLoader)class_loader).addDirectory(new File(jar_path));
				} catch (MalformedURLException e1) {
					// TODO Auto-generated catch block
					logger.error("", e1);
				}
			}
			
			Class<?> c = class_loader.loadClass(class_name);
			SystemListener sl = (SystemListener) c.newInstance();
			addListener(sl);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("",e);
		} 
	}
	
	public void setSystemProperty(String key, String value){
		logger.info(String.format("key:%s;value:%s", key,value));
		System.setProperty(key, value);
	}
	
	@Override
	public void initOperation(){
		addOperation(new MBeanOperationInfo(
                "setSystemProperty",
                "set System Property",
                new MBeanParameterInfo[]{new MBeanParameterInfo(
                		"key","java.lang.String","key"),
                		new MBeanParameterInfo(
                        "value","java.lang.String","value")},   // no parameters
                "void",
                MBeanOperationInfo.ACTION));
		addOperation(new MBeanOperationInfo(
                "start",
                "start all modules",
                new MBeanParameterInfo[]{},   // no parameters
                "void",
                MBeanOperationInfo.ACTION));
		addOperation(new MBeanOperationInfo(
                "stop",
                "stop all modules",
                new MBeanParameterInfo[]{},   // no parameters
                "void",
                MBeanOperationInfo.ACTION));
		addOperation(new MBeanOperationInfo(
                "restartModule",
                "restart one module by id",
                new MBeanParameterInfo[]{new MBeanParameterInfo(
                		"module_id","java.lang.String","choose a module above list")},   // no parameters
                "void",
                MBeanOperationInfo.ACTION));
		addOperation(new MBeanOperationInfo(
                "stopModule",
                "stop one module by id",
                new MBeanParameterInfo[]{new MBeanParameterInfo(
                		"module_id","java.lang.String","choose a module above list")},   // no parameters
                "void",
                MBeanOperationInfo.ACTION));
		addOperation(new MBeanOperationInfo(
                "startModule",
                "start one module by id",
                new MBeanParameterInfo[]{new MBeanParameterInfo(
                		"module_id","java.lang.String","choose a module above list")},   // no parameters
                "void",
                MBeanOperationInfo.ACTION));
		addOperation(new MBeanOperationInfo(
                "removeModule",
                "remove one module by id",
                new MBeanParameterInfo[]{new MBeanParameterInfo(
                		"module_id","java.lang.String","choose a module above list")},   // no parameters
                "void",
                MBeanOperationInfo.ACTION));
		addOperation(new MBeanOperationInfo(
                "addModule",
                "add one module by class",
                new MBeanParameterInfo[]{new MBeanParameterInfo(
                		"jar_path","java.lang.String","module jar path"),
                		new MBeanParameterInfo(
                        "class_name","java.lang.String","module class name.eg:org.jcommon.com.Newmodule")},   // no parameters
                "void",
                MBeanOperationInfo.ACTION));
		super.initOperation();
	}

	public void setCommand(CommandMonitor command) {
		this.command = command;
	}

	public CommandMonitor getCommand() {
		return command;
	}
}
