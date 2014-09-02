package org.jcommon.com.util.jmx;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;

import javax.management.JMX;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.apache.log4j.Logger;

public abstract class RmiClient {
	protected Logger logger = Logger.getLogger(this.getClass());  
	
	private ObjectName adapterName;
	
	private JMXConnector conn;
	
	private boolean connected = false;
	
	public RmiClient(String addr, String CREDENTIALS,String rmi, String name, String domain){
		this(addr,CREDENTIALS,rmi,null);
		try {
			adapterName = new ObjectName(domain+":name="+name);
		} catch (MalformedObjectNameException e) {
			// TODO Auto-generated catch block
			logger.error("", e);
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			logger.error("", e);
		}
	}
	
	public RmiClient(String addr, String CREDENTIALS,String rmi, ObjectName adapterName){
		try {
			this.adapterName = adapterName;
			HashMap<String,Object> prop = new HashMap<String, Object>();   
			if(CREDENTIALS!=null)
				prop.put(JMXConnector.CREDENTIALS, CREDENTIALS);    
			String url = "service:jmx:rmi:///jndi/rmi://"+addr+"/"+rmi;
			logger.info("url:"+url); 
			
	        conn = JMXConnectorFactory.newJMXConnector(new JMXServiceURL(url), prop);  
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			logger.error("", e);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			logger.error("", e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("", e);
		}
	}
	
	public void close() throws IOException{
		if(conn!=null && connected)
			conn.close();
	}
	
	public <T> T getMBean(Class<T> interfaceClass) throws IOException{
		if(!connected){
    		conn.connect();
    		connected = true;
    	}     
    	return JMX.newMBeanProxy(conn.getMBeanServerConnection(), 
    			adapterName, interfaceClass);
	}
}
