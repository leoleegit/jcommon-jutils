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
package org.jcommon.com.util.share;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanRegistrationException;
import javax.management.NotCompliantMBeanException;

import org.apache.log4j.Logger;
import org.jcommon.com.util.JsonUtils;
import org.jcommon.com.util.jmx.JmxManager;
import org.jcommon.com.util.system.SystemConfig;
import org.jcommon.com.util.system.SystemListener;
import org.json.JSONException;

public class ShareManager implements SystemListener{
	private Logger logger = Logger.getLogger(this.getClass());  
	
	private static Map<String, ShareClient> clientMap = new HashMap<String, ShareClient>();
	private static Map<String, ShareServer> serverMap = new HashMap<String, ShareServer>();
	
	private static ShareManager instance;
	
	public static ShareManager instance(){return instance;}
	
	public ShareManager(){
		instance = this;
	}

	public ShareServer getShareServer(String key){
		if(serverMap.containsKey(key))
			return serverMap.get(key);
		return null;
	}
	
	public ShareHandle getShareHandle(String key){
		if(clientMap.containsKey(key))
			return clientMap.get(key);
		return null;
	}
	
	private void init() {
		// TODO Auto-generated method stub
		List<String> ShareObject = SystemConfig.instance().getShareObject();
		if(ShareObject==null){
			logger.info("html adaptor have not Descriptions data");
			return;
		}
		for(String data : ShareObject){
			initShare(data);
		}
	}
	
	public void addShareServer(ShareServer share_server){
		if(JmxManager.instance()!=null){
			try {
				JmxManager.instance().registerMBean(share_server.getShareObject(), share_server.getAdapterName());
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
		}
		serverMap.put(share_server.getName(), share_server);
	}
	
	public void removeShareServer(ShareServer share_server){
		if(JmxManager.instance()!=null){
			try {
				JmxManager.instance().unRigisterMBean(share_server.getAdapterName());
			}catch (MBeanRegistrationException e) {
				// TODO Auto-generated catch block
				logger.error("", e);
			} catch (InstanceNotFoundException e) {
				// TODO Auto-generated catch block
				logger.error("", e);
			}
		}
		serverMap.remove(share_server.getName());
	}

	private void initShare(String data){
		try {
			String name = JsonUtils.getValueByKey(data, "name");
			String role = JsonUtils.getValueByKey(data, "role");
			String CREDENTIALS = JsonUtils.getValueByKey(data, "CREDENTIALS");
			String addr = JsonUtils.getValueByKey(data, "addr");
			//addr = RmiAdptor.resetAddr(addr);
			String rmi = JsonUtils.getValueByKey(data, "rmi");
			String p = JsonUtils.getValueByKey(data, "port");
			addr = addr + ":" + p;
	
			if(ShareObject.SERVER.equalsIgnoreCase(role)){
				addShareServer(new ShareServer(name));
			}else{
				ShareClient shareClient = new ShareClient(addr, CREDENTIALS, name, rmi);
				clientMap.put(name, shareClient);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			logger.error("", e);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("", e);
		}
	}
	
	public void shutdown() {
		// TODO Auto-generated method stub
		for(ShareServer ss : serverMap.values()){
			if(JmxManager.instance()!=null){
				try {
					JmxManager.instance().unRigisterMBean(ss.getAdapterName());
				} catch (MBeanRegistrationException e) {
					// TODO Auto-generated catch block
					logger.error("", e);
					continue;
				} catch (InstanceNotFoundException e) {
					// TODO Auto-generated catch block
					logger.error("", e);
					continue;
				}
			}
		}
		serverMap.clear();
		
		for(ShareClient sh : clientMap.values()){
			try {
				sh.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				logger.error("", e);
				continue;
			}
		}
		clientMap.clear();
	}

	public void startup() {
		// TODO Auto-generated method stub
		init();
	}

	@Override
	public boolean isSynchronized() {
		// TODO Auto-generated method stub
		return true;
	}
}
