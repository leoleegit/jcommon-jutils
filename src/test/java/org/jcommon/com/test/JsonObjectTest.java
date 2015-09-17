package org.jcommon.com.test;

import org.apache.log4j.xml.DOMConfigurator;
import org.jcommon.com.util.JsonObject;

public class JsonObjectTest extends JsonObject{

	private String name;
	private String url;
	private int code;
	private boolean isobj;
	
	private JsonObjectTest json_test;
	
	public JsonObjectTest(String json){
		super(json,true);
	}
	
	public JsonObjectTest(String json, boolean encode){
		super(json,encode);
	}
	
	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public boolean isIsobj() {
		return isobj;
	}

	public void setIsobj(boolean isobj) {
		this.isobj = isobj;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DOMConfigurator.configure("log4j.xml");
		JsonObjectTest jt = new JsonObjectTest(null);
		jt.setName("中文");
		jt.setUrl("https://hello.com/test");
		
		
		JsonObjectTest jt1 = new JsonObjectTest(jt.toJson());
		jt.setJson_test(jt1);
		
		jt = new JsonObjectTest(jt.toJson());
		System.out.println(jt.toJson());
		
		System.out.println(jt.getJson_test().getUrl());
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

	public JsonObjectTest getJson_test() {
		return json_test;
	}

	public void setJson_test(JsonObjectTest json_test) {
		this.json_test = json_test;
	}

}
