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
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

import javax.management.JMX;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.apache.log4j.Logger;
import org.jcommon.com.util.thread.ThreadManager;

public class ShareClient implements ShareHandle {
	private Logger logger = Logger.getLogger(this.getClass());  
	
	private ObjectName adapterName;
	
	private JMXConnector conn;
	
	private boolean connected = false;
	
	public ShareClient(String addr, String CREDENTIALS, String name, String rmi){
		try {
			adapterName = new ObjectName("ShareObject:name="+name);
			HashMap<String,Object> prop = new HashMap<String, Object>();   
			if(CREDENTIALS!=null)
				prop.put(JMXConnector.CREDENTIALS, CREDENTIALS);    
			String url = "service:jmx:rmi:///jndi/rmi://"+addr+"/"+rmi;
			logger.info("url:"+url); 
			
	        conn = JMXConnectorFactory.newJMXConnector(new JMXServiceURL(url), prop);  
		} catch (MalformedObjectNameException e) {
			// TODO Auto-generated catch block
			logger.error(adapterName.toString(), e);
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			logger.error(adapterName.toString(), e);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			logger.error(adapterName.toString(), e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error(adapterName.toString(), e);
		}
		
	}
	
	public void close() throws IOException{
		if(conn!=null)
			conn.close();
	}
	
	@Override
	public void addObject(final String request, final ShareListener listener, final Object key, final Object value) {
		// TODO Auto-generated method stub
		if(conn==null)listener.onException(request,new NullPointerException("JMXConnector is null")) ;
	    Runnable r = new Runnable() {            
	        public void run() {    
	        	try { 
	        		ShareObjectMBean shareObject = getShareObject();
                	if(shareObject!=null && listener!=null)
        				listener.onAddObject(request,shareObject._addOne(key, value));
                } catch (IOException e) {    
                     //logger.error("", e); 
                     if(listener!=null)
         				listener.onException(request,e);
                }     
	       }
       };
       ThreadManager.instance().execute(r);
	}

	@Override
	public void hasObject(final String request, final ShareListener listener, final Object key) {
		// TODO Auto-generated method stub
		if(conn==null)listener.onException(request,new NullPointerException("JMXConnector is null")) ;
	    Runnable r = new Runnable() {            
	        public void run() {    
	        	try { 
	        		ShareObjectMBean shareObject = getShareObject();
                	if(shareObject!=null && listener!=null)
        				listener.onHasObject(request,shareObject._hasKey(key));
                } catch (Exception e) {    
                     //logger.error("", e); 
                     if(listener!=null)
         				listener.onException(request,e);
                }     
	       }
       };
       ThreadManager.instance().execute(r);
	}

	@Override
	public void updateObject(final String request, final ShareListener listener, final Object key, final Object value) {
		// TODO Auto-generated method stub
		if(conn==null)listener.onException(request,new NullPointerException("JMXConnector is null")) ;
	    Runnable r = new Runnable() {            
	        public void run() {    
	        	try { 
	        		ShareObjectMBean shareObject = getShareObject();
                	if(shareObject!=null && listener!=null)
        				listener.onUpdateObject(request,shareObject._updateOne(key, value));
                } catch (Exception e) {    
                     //logger.error("", e); 
                     if(listener!=null)
         				listener.onException(request,e);
                }     
	       }
       };
       ThreadManager.instance().execute(r);
	}

	@Override
	public void getObject(final String request, final ShareListener listener, final Object key) {
		// TODO Auto-generated method stub
		if(conn==null)listener.onException(request,new NullPointerException("JMXConnector is null")) ;
	    Runnable r = new Runnable() {            
	        public void run() {    
	        	try { 
	        		ShareObjectMBean shareObject = getShareObject();
                	if(shareObject!=null && listener!=null)
        				listener.onGetObject(request,shareObject._getOne(key));
                } catch (Exception e) {    
                     //logger.error("", e); 
                     if(listener!=null)
         				listener.onException(request,e);
                }     
	       }
       };
       ThreadManager.instance().execute(r);
	}

	@Override
	public void removeObject(final String request, final ShareListener listener, final Object key) {
		// TODO Auto-generated method stub
		if(conn==null)listener.onException(request,new NullPointerException("JMXConnector is null")) ;
	    Runnable r = new Runnable() {            
	        public void run() {    
	        	try { 
	        		ShareObjectMBean shareObject = getShareObject();
                	if(shareObject!=null && listener!=null)
        				listener.onRemoveObject(request,shareObject._removeOne(key)!=null);
                } catch (Exception e) {    
                    // logger.error("", e); 
                     if(listener!=null)
         				listener.onException(request,e);
                }     
	       }
       };
       ThreadManager.instance().execute(r);
	}

	@Override
	public void getAllObject(final String request, final ShareListener listener) {
		// TODO Auto-generated method stub
		if(conn==null)listener.onException(request,new NullPointerException("JMXConnector is null")) ;
	    Runnable r = new Runnable() {            
	        public void run() {    
	        	try { 
	        		ShareObjectMBean shareObject = getShareObject();
                	if(shareObject!=null && listener!=null)
        				listener.onGetAllObject(request,shareObject._getAll());
                } catch (Exception e) {    
                     //logger.error("", e); 
                     if(listener!=null)
         				listener.onException(request,e);
                }     
	       }
       };
       ThreadManager.instance().execute(r);
	}

	@Override
	public boolean addObject(Object key, Object value) {
		// TODO Auto-generated method stub
		try { 
			ShareObjectMBean shareObject = getShareObject();
        	if(shareObject!=null)
        		return shareObject._addOne(key, value);
        } catch (IOException e) {    
             logger.error("", e); 
             connected = false;
        }     
		return false;
	}

	@Override
	public boolean hasObject(Object key) {
		// TODO Auto-generated method stub
		try { 
			ShareObjectMBean shareObject = getShareObject();
        	if(shareObject!=null)
        		return shareObject._hasKey(key);
        } catch (Exception e) {    
            logger.error("", e); 
        }     
		return false;
	}

	@Override
	public boolean updateObject(Object key, Object value) {
		// TODO Auto-generated method stub
		try { 
			ShareObjectMBean shareObject = getShareObject();
        	if(shareObject!=null)
        		return shareObject._updateOne(key, value);
        } catch (Exception e) {    
            logger.error("", e); 
        }    
		return false;
	}

	@Override
	public Object getObject(Object key) {
		// TODO Auto-generated method stub
		try { 
			ShareObjectMBean shareObject = getShareObject();
        	if(shareObject!=null)
        		return shareObject._getOne(key);
        } catch (Exception e) {    
             logger.error("", e); 
        }     
		return null;
	}

	@Override
	public Object removeObject(Object key) {
		// TODO Auto-generated method stub
		try { 
			ShareObjectMBean shareObject = getShareObject();
        	if(shareObject!=null)
        		return shareObject._removeOne(key);
        } catch (Exception e) {    
            logger.error("", e); 
        } 
		return null;
	}

	@Override
	public Map<Object, Object> getAllObject() {
		// TODO Auto-generated method stub
		try { 
        	ShareObjectMBean shareObject = getShareObject();
        	if(shareObject!=null)
        		return shareObject._getAll();
        } catch (Exception e) {    
             logger.error("", e); 
        } 
		return null;
	}
	
	private ShareObjectMBean getShareObject() throws IOException{
		if(!connected){
    		conn.connect();
    		connected = true;
    	}     
    	ShareObjectMBean shareObject = JMX.newMBeanProxy(conn.getMBeanServerConnection(), 
    			adapterName, ShareObjectMBean.class);
    	return shareObject;
	}
}
