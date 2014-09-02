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
		String xml = "<xml><ToUserName><![CDATA[gh_f49bb9a333b3]]></ToUserName><FromUserName><![CDATA[of-YetzJFYxGTltb4eCvgccHzHF0]]></FromUserName><CreateTime>1393401623</CreateTime><MsgType><![CDATA[image]]></MsgType><PicUrl><![CDATA[http://mmbiz.qpic.cn/mmbiz/KicH5B1sk30ea2upvicicXCicmiciblthdQAfnVVdjfSvzJJXVmCmq6bB20Nwmzao101Rym8v29nPdqBrw7X3fV14vUw/0]]></PicUrl><MsgId>5984614401178375290</MsgId><MediaId><![CDATA[AEFjcYxnuX6sdMi-78tCwQEZX5AUM-aPdg_lR1gitHsjufDCDs8ZlcpmrYtp3phq]]></MediaId></xml>";
		HttpRequest request = new HttpRequest("http://192.168.3.105:8080/scoialmm/callback",xml,"POST",new PostTest());
		ThreadManager.instance().execute(request);
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
