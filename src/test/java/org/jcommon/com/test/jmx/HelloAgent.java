package org.jcommon.com.test.jmx;

import javax.management.MBeanServer;   
import javax.management.MBeanServerFactory;   
import javax.management.ObjectName;   

import org.jcommon.com.util.share.ShareObject;

import com.sun.jdmk.comm.AuthInfo;
import com.sun.jdmk.comm.HtmlAdaptorServer;   
   
public class HelloAgent {   
   
    public static void main(String[] args) throws Exception {   
        MBeanServer server = MBeanServerFactory.createMBeanServer();   
   
//        ObjectName helloName = new ObjectName("chengang:name=HelloWorld");   
//        server.registerMBean(new Hello(), helloName);   
   
        ObjectName helloName = new ObjectName("chengang:name=HelloWorld");   
        server.registerMBean(new ShareObject(), helloName);  
        
        ObjectName adapterName = new ObjectName("HelloAgent:name=htmladapter,port=8082");   
        HtmlAdaptorServer adapter = new HtmlAdaptorServer();  
        AuthInfo authInfo = new AuthInfo();
        authInfo.setLogin("leo");
        authInfo.setPassword("leo");
        adapter.addUserAuthenticationInfo(authInfo);
        server.registerMBean(adapter, adapterName);   
        adapter.start();   
        System.out.println("start.....");   
   
    }   
}   