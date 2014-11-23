package org.jcommon.com.test;

import java.util.Locale;

import org.apache.log4j.xml.DOMConfigurator;
import org.jcommon.com.util.http.HttpListener;
import org.jcommon.com.util.http.HttpRequest;
import org.jcommon.com.util.thread.ThreadManager;

public class PostTest implements HttpListener{

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DOMConfigurator.configure("log4j.xml");
//		HttpRequest post = new HttpRequest("http://192.168.2.72/ws/command?cmd=wakeup",new PostTest());
//		post.setAuthorization("root1", "password.com");
//		ThreadManager.instance().execute(post);
		String xml = "<xml><ToUserName><![CDATA[gh_e6e86fdce3b9]]></TooB5EKt7ogUYtzdMbrzVXXA6icGxE]]></FromUserName><CreateTime>1416553641</CreateTime><MsgType><![CDATA[text]]></MsgType><Content>>6084051561327333659</MsgId></xml>"; 
		HttpRequest request = new HttpRequest("http://192.168.2.31/wss/callback",xml,"POST",new PostTest());
		ThreadManager.instance().execute(request);
		
//		String ss = "C:\\Users\\Administrator\\Desktop\\cipango-distribution-2.0.0\\media\\NhRCvGU8tapYzrstTBLrAVmQclXi9tIYV_dA0VmnS9J_elwvqw44HS7jWxBca8it.jpg";
	}

	@Override
	public void onSuccessful(HttpRequest reqeust, StringBuilder sResult) {
		// TODO Auto-generated method stub
		System.out.println("onSuccessful:"+sResult);
	}

	@Override
	public void onFailure(HttpRequest reqeust, StringBuilder sResult) {
		// TODO Auto-generated method stub
		System.out.println("onFailure:"+sResult);
	}

	@Override
	public void onTimeout(HttpRequest reqeust) {
		// TODO Auto-generated method stub
		System.out.println("timeout");
	}

	@Override
	public void onException(HttpRequest reqeust, Exception e) {
		// TODO Auto-generated method stub
		e.printStackTrace();
	}

}
