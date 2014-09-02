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
package org.jcommon.com.util;

import java.lang.reflect.Method;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public abstract class JsonUtils {
	private static Logger logger = Logger.getLogger(JsonUtils.class);  
	
	public static String getValueFromJsonStr(String data, String key){
		JSONObject jsonObj = getJSONObject(data);
		if(jsonObj==null)return null;
		if(jsonObj.has(key))
			try
			{
				return jsonObj.getString(key);
			} catch (JSONException e)
			{
				// TODO Auto-generated catch block
				logger.error("", e);
			}
		return null;
	}
	
	public static String toJson(String[] keys, String[] values){		
		return toJson(keys, values, true);
	}
	
	public static String toJson(String[] keys, String[] values, boolean encode){
		if(keys==null || values==null){
			logger.warn("json key or vlaue is null");
			return null;
		}
		if(keys.length != values.length){
			logger.warn("key' length must be same with vlaue");
			return null;
		}
		
		StringBuilder sb = new StringBuilder();
		try{
			sb.append("{");
			for(int i=0; i<keys.length; i++){
				if(values[i]==null || keys[i]==null)continue;
				if(encode)
					sb.append("\""+CoderUtils.encode(keys[i])+"\""    + ":\""   + CoderUtils.encode(values[i]) +"\"");	
				else
					sb.append("\""+keys[i]+"\""    + ":\""   + values[i] +"\"");	
				sb.append(",");
			}
			if(sb.lastIndexOf(",") == sb.length()-1)
				sb.deleteCharAt(sb.length()-1);
			sb.append("}");
		}catch(Exception e){
			logger.error("", e);
		}
		
		return sb.toString();
	}
	
	public static String toRequestURL(String url, String[] keys, String[] values){
		String par = toParameter(keys, values);
		if(url.indexOf("?")!=-1 && par!=null){
			par  = par.substring(1);
			par  = "&"+par;
		}
		return url + par;
	}
	
	public static String toParameter(String[] keys, String[] values){
		if(keys==null || values==null){
			logger.warn("json key or vlaue is null");
			return null;
		}
		if(keys.length != values.length){
			logger.warn("key' length must be same with vlaue");
			return null;
		}
		
		StringBuilder sb = new StringBuilder();
		try{
			sb.append("?");
			for(int i=0; i<keys.length; i++){
				if(values[i]==null || keys[i]==null)continue;
				
				sb.append(CoderUtils.encode(keys[i])    + "="   + CoderUtils.encode(values[i]));	
				sb.append("&");
			}
			if(sb.lastIndexOf("&") == sb.length()-1)
				sb.deleteCharAt(sb.length()-1);
		}catch(Exception e){
			logger.error("", e);
		}
		
		return sb.toString();
	}
	
	public static String toJson(Object o){
		return toJson(o,false);
	}
	
	public static String toJson(Object o, boolean encode){
		StringBuilder sb = new StringBuilder();
		Class<?> type = null;
		sb.append("{");
		try{
			java.lang.reflect.Field[] fs = o.getClass().getDeclaredFields();
			String name, value;
			for(java.lang.reflect.Field f : fs){
				value=null;
				name = f.getName();
				type = f.getType();
				java.lang.reflect.Method m = getMethod(o.getClass(),"get"+name);
				if(m==null)
					m = getMethod(o.getClass(),"is"+name);
				if(m!=null){
					if(String.class == type){
						value = (String) m.invoke(o);
					}else if(java.lang.Integer.class == type || Integer.TYPE == type){
						value = String.valueOf((Integer)m.invoke(o));
					}else if(java.lang.Boolean.class == type || Boolean.TYPE == type){
						value = String.valueOf((Boolean)m.invoke(o));
					}else if(java.lang.Long.class == type || Long.TYPE==type){
						value = String.valueOf((Long)m.invoke(o));
					}else if(java.lang.Float.class == type || Float.TYPE==type){
						value = String.valueOf((Float)m.invoke(o));
					}else if(java.lang.Long.class == type || Long.TYPE==type){
						logger.info("not map Class:"+type);
					}
				}
				if(value!=null){
					if(encode)
						sb.append("\""+CoderUtils.encode(name)+"\""    + ":\""   + CoderUtils.encode(value) +"\"");	
					else	
						sb.append("\""+name+"\""    + ":\""   + value +"\"");	
					sb.append(",");
				}
			}
		}catch(Throwable t){
			logger.error("", t);
		}
		if(sb.lastIndexOf(",") == sb.length()-1)
			sb.deleteCharAt(sb.length()-1);
		sb.append("}");
		return sb.toString();
	}
	
	public static java.lang.reflect.Method getMethod(Class<?> c, String name){
		Method   _method  = null;
		for(Method m:c.getMethods()){
			if(m.getName().equalsIgnoreCase(name)){
				_method = m;
				break;
			}
		}
		return _method;
	}
	
	public static JSONObject getJSONObjectByKey(String data, String key, String value) throws JSONException{
		JSONObject jsonObj = getJSONObject(data);
		JSONObject jsonO   = getJSONObjectByKey(jsonObj, key, value);
		return jsonO;
	}
	
	public static JSONObject getJSONObjectByKey(String data, String key, long value, long limt) throws JSONException{
		JSONObject jsonObj = getJSONObject(data);
		JSONObject jsonO   = getJSONObjectByKey(jsonObj, key, value, limt);
		return jsonO;
	}
	
	public static JSONObject getJSONObject(String str) {
        if (str == null || (str!=null && str.trim().length() == 0)) {
            return null;
        }
        if(!str.startsWith("{")){
        	logger.warn(" A JSONObject text must begin with '{' at character 1 :\n"+str);
        	return null;
        }
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(str);
        } catch (JSONException e) {
            logger.warn(str, e);
            return null;
        }
        return jsonObject;
    }
	
	public static JSONArray getJSONArray(String str) {
        if (str == null || (str!=null && str.trim().length() == 0)) {
            return null;
        }
        if(!str.startsWith("[")){
        	logger.warn(" A JSONArray text must begin with '[' at character 1 :\n"+str);
        	return null;
        }
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(str);
        } catch (JSONException e) {
            logger.warn(str, e);
            return null;
        }
        return jsonArray;
    }
	
	public static String getValueByKey(String str, String key) throws JSONException{
		JSONObject jsonObj = getJSONObject(str);
		if(jsonObj!=null && jsonObj.has(key))
			return jsonObj.getString(key);
		return null;
	}
	
	public static String getSonValueByKey(JSONObject rootObj, String sonObj, String key) throws JSONException{
		JSONObject jsonObj = rootObj.has(sonObj)?rootObj.getJSONObject(sonObj):null;
		if(jsonObj!=null && jsonObj.has(key))
			return jsonObj.getString(key);
		return null;
	}
	
	@SuppressWarnings("unchecked")
	private static JSONObject getJSONObjectByKey(JSONObject jsonObj, String key, long value, long limt) throws JSONException{
		JSONObject jsonO   = null;
		if(jsonObj==null || key==null) return null;
		
		if(isLimt(jsonObj,key,value,limt)){
			return jsonObj;
		}
		
		java.util.Iterator<String> it  = jsonObj.keys();
		for(;it.hasNext();){
			String k = it.next();
			Object o = jsonObj.get(k);
			if(o instanceof JSONObject){
				jsonO = getJSONObjectByKey((JSONObject)o, key, value, limt);
				if(jsonO!=null)
					return jsonO;
			}else if(o instanceof JSONArray){
				JSONArray arr = (JSONArray)o;
				jsonO = getJSONObjectFromArray(arr, key, value, limt);
				if(jsonO!=null)
					return jsonO;
			}
		}
		return null;
	}
	
	private static JSONObject getJSONObjectFromArray(JSONArray arr, String key, long value, long limt) throws JSONException{
		Object o         = null;
		JSONObject jsonO = null;
		for(int i=0 ;i<arr.length();i++){
			o = arr.get(i);
			if(o instanceof JSONObject){
				jsonO = (JSONObject)o;
				if(isLimt(jsonO,key,value,limt))
					return jsonO;
				JSONObject re =  getJSONObjectByKey(jsonO, key, value, limt);
				if(re!=null)
					return re;
			}else if(o instanceof JSONArray){
				o = getJSONObjectFromArray((JSONArray)o, key, value, limt);
				if(o!=null){
					jsonO = (JSONObject)o;
					if(isLimt(jsonO,key,value,limt))
						return jsonO;
					JSONObject re =  getJSONObjectByKey(jsonO, key, value, limt);
					if(re!=null)
						return re;
				}
			}
		}
		return null;
	}
	
	private static boolean isLimt(JSONObject jsonObj, String key, long value, long limt) throws JSONException{
		if(jsonObj.has(key)){
			long temp = jsonObj.getLong(key);
			if(java.lang.Math.abs(value-temp)<limt){
				return true;
			}
		}
		return false;
	}
	
	@SuppressWarnings("unchecked")
	private static JSONObject getJSONObjectByKey(JSONObject jsonObj, String key, String value) throws JSONException{
		JSONObject jsonO   = null;
		if(jsonObj==null || key==null || value==null) return null;
		
		if(value.equals("*") && jsonObj.has(key))
			return jsonObj;
		else if(jsonObj.has(key) && value.equals(jsonObj.getString(key)))
			return jsonObj;
		
		java.util.Iterator<String> it  = jsonObj.keys();
		for(;it.hasNext();){
			String k = it.next();
			Object o = jsonObj.get(k);
			if(o instanceof JSONObject){
				jsonO = getJSONObjectByKey((JSONObject)o, key, value);
				if(jsonO!=null)
					return jsonO;
			}else if(o instanceof JSONArray){
				JSONArray arr = (JSONArray)o;
				jsonO = getJSONObjectFromArray(arr, key, value);
				if(jsonO!=null)
					return jsonO;
			}
		}
		return null;
	}
	
	@SuppressWarnings({ "unchecked", "unused" })
	private static JSONArray getJSONArrayByKey(JSONObject jsonObj, String key) throws JSONException{
		if(jsonObj==null || key==null) return null;
		
		if(jsonObj.has(key))
			return jsonObj.getJSONArray(key);
		
		java.util.Iterator<String> it  = jsonObj.keys();
		for(;it.hasNext();){
			String k = it.next();
			Object o = jsonObj.get(k);
			if(o instanceof JSONObject){
				return getJSONArrayByKey((JSONObject)o, key);
			}else if(o instanceof JSONArray){
				if(k.equals(key))
					return jsonObj.getJSONArray(k);
			}
		}
		return null;
	}
	
	private static JSONObject getJSONObjectFromArray(JSONArray arr, String key, String value) throws JSONException{
		Object o         = null;
		JSONObject jsonO = null;
		for(int i=0 ;i<arr.length();i++){
			o = arr.get(i);
			if(o instanceof JSONObject){
				jsonO = (JSONObject)o;
				if(value.equals("*") && jsonO.has(key))
					return jsonO;				
				else if(jsonO.has(key) && value.equals(jsonO.getString(key)))
					return jsonO;
				JSONObject re =  getJSONObjectByKey(jsonO, key, value);
				if(re!=null)
					return re;
			}else if(o instanceof JSONArray){
				o = getJSONObjectFromArray((JSONArray)o, key, value);
				if(o!=null){
					jsonO = (JSONObject)o;
					if(value.equals("*") && jsonO.has(key))
						return jsonO;
					if(jsonO.has(key) && value.equals(jsonO.getString(key)))
						return jsonO;
					JSONObject re =  getJSONObjectByKey(jsonO, key, value);
					if(re!=null)
						return re;
				}
			}
		}
		return null;
	}

}
