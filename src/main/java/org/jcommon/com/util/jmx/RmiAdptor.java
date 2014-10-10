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

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Enumeration;
import java.util.HashMap;

import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;
import javax.management.remote.JMXAuthenticator;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXConnectorServerFactory;
import javax.management.remote.JMXServiceURL;
import javax.security.auth.Subject;

import org.apache.log4j.Logger;
import org.jcommon.com.util.JsonUtils;
import org.json.JSONException;
import org.json.JSONObject;

public class RmiAdptor {
	protected static Logger logger = Logger.getLogger(RmiAdptor.class);
	
	private JMXConnectorServer cserver;
	
	private JMXAuthenticator authenticator;
	
	private ObjectName objectName;
	
	private String addr;
	
	private int port;
	
	private String name;
	
	private String user;
	private String CREDENTIALS;
	
	private java.rmi.registry.Registry registry;
	
	public RmiAdptor(){
		
	}
	
	public RmiAdptor(String data){
		init(data);
	}
	
	public RmiAdptor(String name,String addr,String user,String CREDENTIALS,int port){
		JSONObject json = new JSONObject();
		try {
			json.accumulate("user", user);
			json.accumulate("name", name);
			json.accumulate("CREDENTIALS", CREDENTIALS);
			json.accumulate("addr", addr);
			json.accumulate("port", port);		
			init(json.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			logger.error("", e);
		} 
	}

	public void init(String data) {
		// TODO Auto-generated method stub
		if(data==null){
			logger.info("html adaptor have not Descriptions data");
			return;
		}
		try {
			user = JsonUtils.getValueByKey(data, "user");
			name = JsonUtils.getValueByKey(data, "name");
			CREDENTIALS = JsonUtils.getValueByKey(data, "CREDENTIALS");
			addr = JsonUtils.getValueByKey(data, "addr");
			//addr = resetAddr(addr);
			String p = JsonUtils.getValueByKey(data, "port");
			if(p!=null)port = Integer.valueOf(p);
			logger.info("user:"+name+"	addr:"+addr+":"+port);
			
			objectName = new ObjectName("RmiAdaptor:name="+name);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			logger.error("", e);
		} catch (MalformedObjectNameException e) {
			// TODO Auto-generated catch block
			logger.error("", e);
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			logger.error("", e);
		}
	}

	public void start(){
		if(JmxManager.instance()==null){
			logger.warn("JmxManager is null");
			return;
		}
		ObjectName adapterName = getObjectName();
		//cserver = getCserver();
		try {
			setCserver(JmxManager.instance().getServer());
			JmxManager.instance().registerMBean(cserver, adapterName);
			cserver.start();
		} catch (InstanceAlreadyExistsException e) {
			// TODO Auto-generated catch block
			logger.error("", e);
		} catch (MBeanRegistrationException e) {
			// TODO Auto-generated catch block
			logger.error("", e);
		} catch (NotCompliantMBeanException e) {
			// TODO Auto-generated catch block
			logger.error("", e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("", e);
		}   
		
	}
	
	public void stop(){
		try {
			getCserver().stop();
			unexportObject();
		} catch (NoSuchObjectException e) {
			// TODO Auto-generated catch block
			logger.error("", e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("", e);
		}
		if(JmxManager.instance()==null){
			logger.warn("JmxManager is null");
			return;
		}
		ObjectName adapterName = getObjectName();
		try {
			JmxManager.instance().unRigisterMBean(adapterName);
		} catch (MBeanRegistrationException e) {
			// TODO Auto-generated catch block
			logger.error("", e);
		} catch (InstanceNotFoundException e) {
			// TODO Auto-generated catch block
			logger.error("", e);
		}
	}
	
	public static String resetAddr(String addr2) {
		// TODO Auto-generated method stub
		if("127.0.0.1".equals(addr2) || "localhost".equals(addr2)){
			return getLocalHostAddress();
		}
		return addr2;
	}

	public void setObjectName(ObjectName objectName) {
		this.objectName = objectName;
	}

	public ObjectName getObjectName() {
		return objectName;
	}

	public void unexportObject() throws NoSuchObjectException{
		if(registry!=null)
			UnicastRemoteObject.unexportObject(registry, true);
	}
	
	public void setCserver(MBeanServer server) throws MalformedURLException, IOException {
		if(port==0 || name==null || addr==null){
			throw new NullPointerException("data not be ready");
		}
		try{
			registry = LocateRegistry.createRegistry(port); 
		}catch(RemoteException e){
			
		} 
		HashMap<String, Object> prop = new HashMap<String, Object>();  
		if(CREDENTIALS!=null){
			authenticator = new JMXAuthenticator() {    
			    
	            public Subject authenticate(Object credentials) {  
	            	logger.info(credentials.getClass().getName() + " is trying connect...");
	                if (credentials instanceof String) {    
	                    if (credentials.equals(CREDENTIALS)) {    
	                        return new Subject();    
	                    }    
	                }else if (credentials instanceof String []) {  
	                	String[] copy = (String[]) credentials;
	                	String username = copy.length>0?copy[0]:null;
	                	String passwd   = copy.length>1?copy[1]:null;
	                	logger.info(username + " is trying connect...");
	                    if (passwd.equals(CREDENTIALS) && username.equals(user)) {    
	                        return new Subject();    
	                    }    
	                }     
	                throw new SecurityException("not authicated");    
	            }    
	        };
	        
	        prop.put(JMXConnectorServer.AUTHENTICATOR,authenticator);    
		}
        String url = "service:jmx:rmi:///jndi/rmi://"+addr+":"+port+"/"+name;
        this.cserver =    
                JMXConnectorServerFactory.newJMXConnectorServer(    
                new JMXServiceURL(url), prop, server);   
	}

	public JMXConnectorServer getCserver() {
		return cserver;
	}
	
    /** Detects the default IP address of this host. */
    public static String getLocalHostAddress()
    {  try
      {  
	      InetAddress inet_addr = getIPv4InetAddress();
	   	  return inet_addr!=null?inet_addr.getHostAddress():"127.0.0.1";
      }
      catch (java.net.UnknownHostException e)
      {  return "127.0.0.1";
      } catch (SocketException e) {
		// TODO Auto-generated catch block
    	  return "127.0.0.1";
      }
    }
   
    static private InetAddress getIPv4InetAddress() throws SocketException, UnknownHostException {

	    String os = System.getProperty("os.name").toLowerCase();

	    if(os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0) {   
	        NetworkInterface ni = NetworkInterface.getByName("eth0");

	        Enumeration<InetAddress> ias = ni.getInetAddresses();

	        InetAddress iaddress;
	        do {
	            iaddress = ias.nextElement();
	        } while(!(iaddress instanceof Inet4Address));

	        return iaddress;
	    }

	    return InetAddress.getLocalHost();  // for Windows and OS X it should work well
	}
}
