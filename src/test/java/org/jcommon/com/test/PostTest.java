package org.jcommon.com.test;

import java.util.Locale;

import org.apache.log4j.xml.DOMConfigurator;
import org.jcommon.com.util.JsonUtils;
import org.jcommon.com.util.http.HttpListener;
import org.jcommon.com.util.http.HttpRequest;
import org.jcommon.com.util.thread.ThreadManager;

public class PostTest implements HttpListener{

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//DOMConfigurator.configure("log4j.xml");
//		HttpRequest post = new HttpRequest("http://192.168.2.72/ws/command?cmd=wakeup",new PostTest());
//		post.setAuthorization("root1", "password.com");
//		ThreadManager.instance().execute(post);
//		String xml = "<xml><ToUserName><![CDATA[gh_f49bb9a333b3]]></ToUserName><FromUserName><![CDATA[of-YetzJFYxGTltb4eCvgccHzHF02]]></FromUserName><CreateTime>1420623492</CreateTime><MsgType><![CDATA[text]]></MsgType><Content><![CDATA[Hi]]></Content><MsgId>6101531438271806065</MsgId></xml>";
//		String[] keys   = { "signature","timestamp","nonce" };
//		String[] values = { "aae2b6cda729304c12a81cecae5f484608884c82","1416817797","1256576892" };
//		String callback = "http://192.168.2.31/wss/callback";
//		//String callback = "http://192.168.2.104:8080/jcommon-wechat/callback";
//		String    url   = JsonUtils.toRequestURL(callback, keys, values);
//		
//		HttpRequest request = new HttpRequest(url,xml,"POST",new PostTest());
//		ThreadManager.instance().execute(request);
		//String url = "https://192.168.1.33/wss/FacebookPage";
		String url = args[0];
		System.out.println(url);
		//String url = "https://livechat6.pccw.com/chat/livechat.jsp";
		HttpRequest request = new HttpRequest(url,new PostTest());
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
