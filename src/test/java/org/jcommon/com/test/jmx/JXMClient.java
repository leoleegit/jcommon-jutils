package org.jcommon.com.test.jmx;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

public class JXMClient {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String hostName = "192.168.2.104";  
        int portNum = 10042;  
          
        try {  
            JMXServiceURL u = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://"   
                    + hostName + ":" + portNum + "/jmxrmi");  
  
            Map<String, Object> auth = new HashMap<String, Object>();  
            auth.put(JMXConnector.CREDENTIALS, "admin");  
  
            JMXConnector c = JMXConnectorFactory.connect(u, auth);  
        } catch (MalformedURLException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
	}

}
