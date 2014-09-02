package org.jcommon.com.test;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

public class JsonTest {
     static String data = "{\"ScriptID\":\"busy\",\"Language\":\"en\",\"ScriptName\":\"Busy Handling\",\"Scripts\":\"Thank you for using PCCW CS Live Chat. We apologize that our Live Chat is very busy at the momry agank you!"+ 

"\n\n hahaha\",\"CreatedDate\":\"2012-05-03 12:57:14.0\"}";
     
     public static void main(String[] args) throws Exception{
    	// JSONObject jsonObj = new JSONObject(URLEncoder.encode(data, "UTF-8"));
    	 JSONObject jsonObj;
    	 org.json.JSONArray array = new org.json.JSONArray();
    	 jsonObj = new JSONObject();
    	 jsonObj.accumulate("from", "agent2");
    	 jsonObj.accumulate("msg", "agent2");
    	 array.put(jsonObj);
    	 jsonObj = new JSONObject();
    	 jsonObj.accumulate("from", "agent1");
    	 jsonObj.accumulate("msg", "agent1");
    	 array.put(jsonObj);
    	 
    	 jsonObj = new JSONObject();
    	 jsonObj.accumulate("from", "age nt3");
    	 jsonObj.accumulate("msg", "agent3");
    	 jsonObj.accumulate("history", array);
    	 System.out.println(jsonObj.toString());
    	 String c = "+++=";
    	// c =org.jcommon.com.util.CoderUtils.encodeToFlex("+++=");
    	 System.out.println(c);
    	 System.out.println(org.jcommon.com.util.CoderUtils.decode(c));

     }
}
