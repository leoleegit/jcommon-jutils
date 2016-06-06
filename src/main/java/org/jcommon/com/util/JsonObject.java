package org.jcommon.com.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.jcommon.com.util.CoderUtils;
import org.jcommon.com.util.JsonUtils;
import org.jcommon.com.util.config.ConfigLoader;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public abstract class JsonObject{
  protected static Logger logger = Logger.getLogger(JsonObject.class);
  private String json;
  
  private boolean encode_ = true;
  private boolean decode_ = false;

  public JsonObject(String json){
    this.json = json;
    json2Object();
  }
  
  public JsonObject(String json, boolean decode){
	    this.json = json;
	    this.decode_ = decode;
	    json2Object();
  }
  
  public JsonObject() {
  }

  private void json2Object() {
    if (this.json == null) return;
    json2Object(this, this.json);
  }

  public void json2Object(Object object, String data) {
    JSONObject jsonO = JsonUtils.getJSONObject(data);
    json2Object(object, jsonO);
  }
  
  public static List<Object> json2Objects(Class<?> class_, String data) {
    JSONArray arr = JsonUtils.getJSONArray(data);
    if ((arr == null) && 
      (data != null)) {
      arr = new JSONArray();
      JSONObject o = JsonUtils.getJSONObject(data);
      if (o != null) {
        arr.put(o);
      }
    }
    return json2Objects(class_, arr);
  }
  
  public static List<Object> json2Objects(Class<?> class_, JSONArray arr) {
    if ((class_ == null) || (arr == null)) return null;
    List<Object> list = new ArrayList<Object>();

    for (int index = 0; index < arr.length(); index++) {
      try {
        JSONObject jsonO = arr.getJSONObject(index);
        Object o = newInstance(class_, new Object[]{ jsonO.toString() });
        list.add(o);
      }
      catch (JSONException e) {
        logger.error("", e);
      }
    }
    return list;
  }

  public Object json2Object(Object object, JSONObject json) {
    if (object == null) return null;
    Object o = object;
    if (json == null) return o;
    for (Class<?> clazz = o.getClass(); 
      clazz != Object.class; clazz = clazz.getSuperclass()) {
      Field[] fs = clazz.getDeclaredFields();
      Class<?> type = null;
      for (Field f : fs) {
        String name = f.getName();
        Method m = ConfigLoader.getMethod(o.getClass(), new StringBuilder().append("set").append(name).toString());
        if (m == null)
          m = ConfigLoader.getMethod(o.getClass(), new StringBuilder().append("is").append(name).toString());
        if ((m != null) && (json.has(name))) {
          try {
            String value = json.getString(name);
            if (notNull(value)) {
              type = f.getType();
              Object args = null;
              if (String.class == type){
            	  if(isDecode()){
            		  value = CoderUtils.decode(value);
            	  }
            	  args = value;
              }
              else if ((Integer.class == type) || (Integer.TYPE == type))
                args = isNumeric(value)?Integer.valueOf(value):null;
              else if ((Boolean.class == type) || (Boolean.TYPE == type))
                args = Boolean.valueOf(Boolean.parseBoolean(value));
              else if ((Long.class == type) || (Long.TYPE == type))
                args = isNumeric(value)?Long.valueOf(value):null;
              else if ((Float.class == type) || (Float.TYPE == type))
                args = Float.valueOf(value);
              else if (Timestamp.class == type)
                args = isNumeric(value)?new Timestamp(Long.valueOf(value)):null;
              else if (JsonObject.class.isAssignableFrom(type)) {
                try {
                  args = newInstance(type, !isDecode()?new Object[]{value}:new Object[]{value,isDecode()});
                }
                catch (SecurityException e) {
                  logger.warn(e);
                  continue;
                }
                catch (IllegalArgumentException e) {
                  logger.warn(e);
                  continue;
                }
              }else if (Collection.class.isAssignableFrom(type)) {
//            	  if(isDecode()){
//            		  value = CoderUtils.decode(value);
//            	  }
            	  args = value;
            	  setListObject(name, args);
            	  continue;
              }
              
              try
              {
                if (args != null)
                  m.invoke(o, new Object[] { args });
              } catch (Exception e) {
                logger.warn(e);
              }
            }
          }
          catch (JSONException e) {
            logger.error("", e);
          }
        }
      }
    }
    return o;
  }
  
  public static boolean isNumeric(String str){ 
		if(str==null)return false;
		if(str.startsWith("-"))
			str = str.substring(1);
	    Pattern pattern = Pattern.compile("[0-9]*"); 
	    Matcher isNum = pattern.matcher(str);
	    if( !isNum.matches() ){
	        return false; 
	    } 
	    return true; 
  }

  public void setListObject(Object args, Object args2){};

  public String toJson() {
    return toJson(this);
  }

  public static String list2Json(List<?> list) {
    if (list == null) return null;
    StringBuilder sb = new StringBuilder();
    sb.append("[");
    for (Iterator<?> i$ = list.iterator(); i$.hasNext(); ) { Object o = i$.next();
      sb.append(((JsonObject)o).toJson()).append(",");
    }

    if (sb.lastIndexOf(",") == sb.length() - 1)
      sb.deleteCharAt(sb.length() - 1);
    sb.append("]");
    return sb.toString();
  }

  @SuppressWarnings("unchecked")
  private  String toJson(JsonObject o)
  {
    if (o == null) return null;
    StringBuilder sb = new StringBuilder();
    Class<?> type = null;
    sb.append("{");
    try {
      for (Class<?> clazz = o.getClass(); 
        clazz != Object.class; clazz = clazz.getSuperclass()) {
        Field[] fs = clazz.getDeclaredFields();

        for (Field f : fs) {
          if(Modifier.isStatic(f.getModifiers()))
        	  continue;
          String value = null;
          String name = f.getName();
         // boolean json = false;
          if (!"json".equals(name))
          {
            type = f.getType();
            Method m = JsonUtils.getMethod(o.getClass(), new StringBuilder().append("get").append(name).toString());
            if (m == null)
              m = JsonUtils.getMethod(o.getClass(), new StringBuilder().append("is").append(name).toString());
            if (m != null) {
              if (String.class == type) {
                value = (String)m.invoke(o, new Object[0]);
                if(isEncode() && notNull(value)){
                	value = CoderUtils.encode(value);
                }
                if(notNull(value)){
                	value = "\"" + value +"\"";
                }
              } else if ((Integer.class == type) || (Integer.TYPE == type)) {
                value = String.valueOf((Integer)m.invoke(o, new Object[0]));
              } else if ((Boolean.class == type) || (Boolean.TYPE == type)) {
                value = String.valueOf((Boolean)m.invoke(o, new Object[0]));
              } else if ((Long.class == type) || (Long.TYPE == type)) {
                value = String.valueOf((Long)m.invoke(o, new Object[0]));
              } else if ((Float.class == type) || (Float.TYPE == type)) {
                value = String.valueOf((Float)m.invoke(o, new Object[0]));
              } else if (Timestamp.class == type) {
            	Object o1 = m.invoke(o, new Object[0]);
                value = o1!=null?String.valueOf(((Timestamp)o1).getTime()):null;
              } else if (JsonObject.class.isAssignableFrom(type)) {
                Object o1 = m.invoke(o, new Object[0]);
                JsonObject jsonObj = o1 != null ? (JsonObject)o1 : null;
                value = jsonObj!=null?jsonObj.toJson():null;
              } else if (Collection.class.isAssignableFrom(type)) {
            	  List<Object> jsonObj = (List<Object>) m.invoke(o, new Object[0]);
                  value = list2Json(jsonObj);
              }
            }
            if (notNull(value)) {
              //if (json)
              //  sb.append(new StringBuilder().append("\"").append(isEncode()?CoderUtils.encode(name):name).append("\"").append(":").append(value).toString());
              //else
              sb.append("\"").append(isEncode()?CoderUtils.encode(name):name).append("\"").
        		  append(":").append(value).append(",");
            }
          }
        }
      }
    } catch (Throwable t) { logger.error("", t); }

    if (sb.lastIndexOf(",") == sb.length() - 1)
      sb.deleteCharAt(sb.length() - 1);
    sb.append("}");
    return sb.toString();
  }

//  private static String encode(String s)
//  {
//    StringBuffer sb = new StringBuffer();
//    for (int i = 0; i < s.length(); i++) {
//      char c = s.charAt(i);
//      switch (c) {
//      case '"':
//        sb.append("\\\"");
//        break;
//      case '\\':
//        sb.append("\\\\");
//        break;
//      case '/':
//        sb.append("\\/");
//        break;
//      case '\b':
//        sb.append("\\b");
//        break;
//      case '\f':
//        sb.append("\\f");
//        break;
//      case '\n':
//        sb.append("\\n");
//        break;
//      case '\r':
//        sb.append("\\r");
//        break;
//      case '\t':
//        sb.append("\\t");
//        break;
//      default:
//        sb.append(c);
//      }
//    }
//    return sb.toString();
//  }

  private static Object newInstance(Class<?> class_, Object[] objs) {
    try {
      Class<?>[] par = objs.length>1?new Class<?>[]{ String.class, boolean.class }:new Class<?>[]{ String.class};
      Constructor<?> con = class_.getConstructor(par);
      return con.newInstance(objs);
    }
    catch (SecurityException e) {
    	logger.error("",e);
    }
    catch (IllegalArgumentException e) {
    	logger.error("",e);
    }
    catch (NoSuchMethodException e) {
    	logger.error("",e);
    }
    catch (InstantiationException e) {
    	logger.error("",e);
    }
    catch (IllegalAccessException e) {
    	logger.error("",e);
    }
    catch (InvocationTargetException e) {
      logger.error("",e);
    }
    return null;
  }

  public String getJson() {
    return this.json;
  }

  public void setJson(String json) {
    this.json = json;
  }

  private static boolean notNull(String str) {
    return (str != null) && (!"".equals(str));
  }
	
  public  boolean isEncode() {
	return encode_;
  }
  public boolean isDecode() {
	return decode_;
  }
  public void setDecode(boolean decode) {
	this.decode_ = decode;
  }
  public void setEncode(boolean encode) {
	this.encode_ = encode;
  }
}