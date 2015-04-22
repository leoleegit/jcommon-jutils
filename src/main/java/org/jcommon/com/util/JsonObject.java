package org.jcommon.com.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
import org.jcommon.com.util.CoderUtils;
import org.jcommon.com.util.JsonUtils;
import org.jcommon.com.util.config.ConfigLoader;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonObject{
  protected static Logger logger = Logger.getLogger(JsonObject.class);
  private String json;
  
  private boolean encode = true;
  private boolean decode = false;

  public JsonObject(String json){
    this.json = json;
    json2Object();
  }
  
  public JsonObject(String json,boolean decode){
	    this.json = json;
	    this.decode = decode;
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
                args = Integer.valueOf(value);
              else if ((Boolean.class == type) || (Boolean.TYPE == type))
                args = Boolean.valueOf(Boolean.parseBoolean(value));
              else if ((Long.class == type) || (Long.TYPE == type))
                args = Long.valueOf(value);
              else if ((Float.class == type) || (Float.TYPE == type))
                args = Float.valueOf(value);
              else if (JsonObject.class.isAssignableFrom(type)) {
                try {
                  args = newInstance(type, new Object[]{ value, isDecode() });
                }
                catch (SecurityException e) {
                  logger.warn(e);
                  continue;
                }
                catch (IllegalArgumentException e) {
                  logger.warn(e);
                  continue;
                }
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
          boolean json = false;
          if (!"json".equals(name))
          {
            type = f.getType();
            Method m = JsonUtils.getMethod(o.getClass(), new StringBuilder().append("get").append(name).toString());
            if (m == null)
              m = JsonUtils.getMethod(o.getClass(), new StringBuilder().append("is").append(name).toString());
            if (m != null) {
              if (String.class == type) {
                value = (String)m.invoke(o, new Object[0]);
              } else if ((Integer.class == type) || (Integer.TYPE == type)) {
                value = String.valueOf((Integer)m.invoke(o, new Object[0]));
              } else if ((Boolean.class == type) || (Boolean.TYPE == type)) {
                value = String.valueOf((Boolean)m.invoke(o, new Object[0]));
              } else if ((Long.class == type) || (Long.TYPE == type)) {
                value = String.valueOf((Long)m.invoke(o, new Object[0]));
              } else if ((Float.class == type) || (Float.TYPE == type)) {
                value = String.valueOf((Float)m.invoke(o, new Object[0]));
              } else if (JsonObject.class.isAssignableFrom(type)) {
                Object o1 = m.invoke(o, new Object[0]);
                JsonObject jsonObj = o1 != null ? (JsonObject)o1 : null;
                value = jsonObj!=null?jsonObj.toJson():null;
                json = true;
              } else if (Collection.class.isAssignableFrom(type)) {
            	  List<Object> jsonObj = (List<Object>) m.invoke(o, new Object[0]);
                  value = list2Json(jsonObj);
                  json = true;
              }
            }
            if (notNull(value)) {
              if (json)
                sb.append(new StringBuilder().append("\"").append(isEncode()?CoderUtils.encode(name):name).append("\"").append(":").append(value).toString());
              else
                sb.append(new StringBuilder().append("\"").append(isEncode()?CoderUtils.encode(name):name).append("\"").append(":\"").append(isEncode()?CoderUtils.encode(value):value).append("\"").toString());
              sb.append(",");
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
      Class<?>[] par = { String.class };
      Constructor<?> con = class_.getConstructor(par);
      return con.newInstance(objs);
    }
    catch (SecurityException e) {
      logger.warn(e);
    }
    catch (IllegalArgumentException e) {
      logger.warn(e);
    }
    catch (NoSuchMethodException e) {
      logger.warn(e);
    }
    catch (InstantiationException e) {
      logger.warn(e);
    }
    catch (IllegalAccessException e) {
      logger.warn(e);
    }
    catch (InvocationTargetException e) {
      logger.warn(e);
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
	return encode;
  }
  public boolean isDecode() {
	return decode;
  }
  public void setDecode(boolean decode) {
	this.decode = decode;
  }
  public void setEncode(boolean encode) {
	this.encode = encode;
  }
}