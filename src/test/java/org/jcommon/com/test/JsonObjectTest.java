package org.jcommon.com.test;

import org.jcommon.com.util.JsonObject;

public class JsonObjectTest extends JsonObject{

	private String name;
	private String url;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		JsonObjectTest jt = new JsonObjectTest();
		jt.setName("中文");
		jt.setUrl("https://hello.com/test");
		System.out.println(jt.toJson());
	}
	
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}

}
