package org.jcommon.com.test;

import org.jcommon.com.util.JsonUtils;

public class JsonUtilsTest {
	private String test = "aa fff ddd";

	public void setTest(String test) {
		this.test = test;
	}

	public String getTest() {
		return test;
	}
	
	 public String toJson(){
		  return JsonUtils.toJson(this,true);
		 }
	 
	 public static void main(String[] args) throws Exception{
		 
		 System.out.println(new JsonUtilsTest().toJson());
	 }
}
