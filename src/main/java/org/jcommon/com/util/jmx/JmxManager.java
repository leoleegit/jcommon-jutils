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
import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectInstance;
import javax.management.ObjectName;

import org.apache.log4j.Logger;
import org.jcommon.com.util.system.SystemConfig;
import org.jcommon.com.util.system.SystemListener;

import com.sun.jdmk.comm.HtmlAdaptorServer;
/**
 * 
 * @author LeoLee<leo.li@protel.com.hk>
 *
 */
public class JmxManager implements SystemListener {
	private static Logger logger = Logger.getLogger(JmxManager.class);
	private static JmxManager instance;
	
	private MBeanServer server;
	
	private HtmlAdaptorServer htmlAdaptor;
	
	private RmiAdptor rmiAdptor;
	
	public MBeanServer getServer() {
		return server;
	}

	public JmxManager(){
		JmxManager.instance = this;
	}
	
	public static JmxManager instance(){
		if(instance == null)
			logger.warn("JmxManager not Initialized");
		return instance;
	}
	
	public boolean registerMBean(Object object, ObjectName name) throws InstanceAlreadyExistsException, MBeanRegistrationException, NotCompliantMBeanException{
		if(server!=null){ 
			server.registerMBean(object, name); 
			return true;
		}
		return false;
	}
	
	public boolean unRigisterMBean(ObjectName name) throws MBeanRegistrationException, InstanceNotFoundException{
		if(server!=null){ 
			server.unregisterMBean(name);
			return true;
		}
		return false;
	}
	
	public ObjectInstance getObjectInstance(ObjectName name) throws InstanceNotFoundException{
		if(server!=null){ 
			return server.getObjectInstance(name);
		}
		return null;
	}
	
	@Override
	public void shutdown() {
		// TODO Auto-generated method stub
		if(htmlAdaptor!=null)
			htmlAdaptor.stop();
		if(rmiAdptor!=null)
			rmiAdptor.stop();
		instance = null;
	}

	@Override
	public void startup() {
		// TODO Auto-generated method stub
		if(JmxManager.instance == null)
			JmxManager.instance = this;
		
		logger.info("JmxManager running...");
		server = MBeanServerFactory.createMBeanServer(SystemConfig.instance().getMbean_server());  
		if(SystemConfig.instance().getHtmladapter()!=null)
			try {
				htmlAdaptor = new HtmlAdaptor(SystemConfig.instance().getHtmladapter());
				ObjectName adapterName = ((HtmlAdaptor) htmlAdaptor).getObjectName_();
				server.registerMBean(htmlAdaptor, adapterName);   
				htmlAdaptor.start();
				logger.info("htmlAdaptor running...");
			}catch (NullPointerException e) {
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
			}
		if(SystemConfig.instance().getRmiadapter()!=null)
			try {
				rmiAdptor = new RmiAdptor(SystemConfig.instance().getRmiadapter());
				rmiAdptor.start();
				logger.info("rmiAdptor "+rmiAdptor.getCserver().getAddress()+" running...");
			}catch (NullPointerException e) {
				// TODO Auto-generated catch block
				logger.error("", e);
			} 
	}

	public void setHtmlAdaptor(HtmlAdaptorServer htmlAdaptor) {
		this.htmlAdaptor = htmlAdaptor;
	}

	public HtmlAdaptorServer getHtmlAdaptor() {
		return htmlAdaptor;
	}

	public void setRmiAdptor(RmiAdptor rmiAdptor) {
		this.rmiAdptor = rmiAdptor;
	}

	public RmiAdptor getRmiAdptor() {
		return rmiAdptor;
	}

	@Override
	public boolean isSynchronized() {
		// TODO Auto-generated method stub
		return true;
	}

}
