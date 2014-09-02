package org.jcommon.com.test.jmx;
import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;  
import javax.management.ObjectName;  

import org.jcommon.com.util.jmx.PropertyManager;

import com.sun.jdmk.comm.HtmlAdaptorServer;  

public class HelloDynamicAgent {  
    public static void main(String[] args) throws Exception {  
        MBeanServer server = MBeanServerFactory.createMBeanServer();  
        ObjectName helloName = new ObjectName("chengang:name=HelloDynamic");  
      //  HelloDynamic hello = new HelloDynamic(); 
        PropertyTest hello =  new PropertyTest();
        server.registerMBean(hello, helloName);  
        ObjectName adapterName = new ObjectName("HelloAgent:name=htmladapter,port=8082");  
        HtmlAdaptorServer adapter = new HtmlAdaptorServer();  
        server.registerMBean(adapter, adapterName);  
        adapter.start();  
        System.out.println("start.....");  
    }  
}  
