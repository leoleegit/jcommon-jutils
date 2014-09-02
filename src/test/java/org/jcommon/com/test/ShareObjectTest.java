package org.jcommon.com.test;

import org.apache.log4j.xml.DOMConfigurator;
import org.jcommon.com.util.share.NotificationListener;
import org.jcommon.com.util.share.ShareClient;
import org.jcommon.com.util.share.ShareHandle;
import org.jcommon.com.util.share.ShareManager;
import org.jcommon.com.util.share.ShareServer;
import org.jcommon.com.util.system.SystemListener;
import org.jcommon.com.util.system.SystemManager;

public class ShareObjectTest implements org.jcommon.com.util.share.NotificationListener, SystemListener{
    private static NotificationListener listener = new ShareObjectTest();
	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException { 
		// TODO Auto-generated method stub
		System.out.println(System.getProperty("user.dir"));
		DOMConfigurator.configure("log4j.xml");
		SystemManager s = new SystemManager();
		s.contextInitialized(null);
		
		Thread.sleep(3000);
		s.addListener((ShareObjectTest)listener);
	}

	@Override
	public void onAddObject(Object key, Object value) {
		// TODO Auto-generated method stub
		System.out.println(String.format("key:%s;value:%s", key,value));
	}

	@Override
	public void onUpdateObject(Object key, Object value) {
		// TODO Auto-generated method stub
		System.out.println(String.format("key:%s;value:%s", key,value));
	}

	@Override
	public void onRemoveObject(Object key, Object value) {
		// TODO Auto-generated method stub
		System.out.println(String.format("key:%s;value:%s", key,value));
	}

	@Override
	public void shutdown() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void startup() {
		// TODO Auto-generated method stub
		ShareClient client = (ShareClient) ShareManager.instance().getShareHandle("mg_share_resource");
		
		System.out.println(client.getObject("test1"));
	}

	@Override
	public boolean isSynchronized() {
		// TODO Auto-generated method stub
		return false;
	}

}
