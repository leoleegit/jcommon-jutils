package org.jcommon.com.test.jmx;

import java.util.Map;

import org.apache.log4j.xml.DOMConfigurator;
import org.jcommon.com.util.jmx.JmxManager;
import org.jcommon.com.util.jmx.RmiAdptor;
import org.jcommon.com.util.share.NotificationListener;
import org.jcommon.com.util.share.ShareHandle;
import org.jcommon.com.util.share.ShareListener;
import org.jcommon.com.util.share.ShareManager;
import org.jcommon.com.util.system.SystemConfig;
import org.jcommon.com.util.thread.ThreadManager;

public class ShareManagerTest implements Runnable, ShareListener, NotificationListener{

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DOMConfigurator.configure("log4j_fb.xml");
		ThreadManager.instance().execute(new ShareManagerTest());
	}

	static ShareHandle shareHandle;
	@Override
	public void run() {
		// TODO Auto-generated method stub
		//SystemConfig.instance().setRmiadapter("{name:rmiadapter,CREDENTIALS:ptsws,addr:192.168.2.170,port:10043}");
		//new JmxManager().startup();
//		
		//SystemConfig.instance().setShareObject("{rmi:rmiadapter,name:ShareObject,role:server,CREDENTIALS:ptsws,addr:127.0.0.1,port:10043}");
		
		//SystemConfig.instance().setShareObject("{rmi:rmiadapter,name:ShareObject,role:client,CREDENTIALS:ptsws,addr:192.168.2.72,port:10043}");
		new ShareManager().startup();
		//shareHandle = ShareManager.getShareHandle();
	   //((ShareManager)shareHandle).addNotificationListener("Script_Manager", this);
		//shareHandle.addObject(new ShareManagerTest(), "Script_Manager", "value_hello");
		//shareHandle.getObject(new ShareManagerTest(), "Script_Manager");
		//shareHandle.hasObject(new ShareManagerTest(), "Script_Manager");
		//shareHandle.removeObject("Script_Manager",new ShareManagerTest(), "Script_Manager");
	}


	@Override
	public void onUpdateObject(Object key, Object value) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onRemoveObject(Object key, Object value) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onAddObject(Object key, Object value) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onAddObject(String request, boolean sResult) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onHasObject(String request, boolean sResult) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onUpdateObject(String request, boolean sResult) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onGetObject(String request, Object sResult) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onRemoveObject(String request, boolean sResult) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onGetAllObject(String request, Map<Object, Object> all) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onException(String request, Exception e) {
		// TODO Auto-generated method stub
		
	}


}
