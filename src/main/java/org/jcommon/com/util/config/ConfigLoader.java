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
package org.jcommon.com.util.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import org.apache.log4j.Logger;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.jcommon.com.util.JsonUtils;
/**
 * 
 * @author LeoLee<leo.li@protel.com.hk>
 *
 */
public class ConfigLoader {
	private static Logger logger = Logger.getLogger(ConfigLoader.class);  
	
	/**
	 * 
	 * @param conn
	 * @param config
	 * @param table{key,value,map,list}
	 */
	public static void loadConf4db(Connection conn,PreparedStatement ps,BaseConfigMBean config,String[] table){
	    ResultSet rs = null;    
	    String key_   = table[0];
	    String value_ = table[1];
        try {
            rs = ps.executeQuery();
            
		    Class<?> type = null;
			String name;
        	String value;
            while (rs.next()) {
            	name  = rs.getString(key_);
            	value = rs.getString(value_);
            	java.lang.reflect.Field f = null;
            	try{ 
            		f = config.getClass().getDeclaredField(name);
            	}catch(java.lang.NoSuchFieldException e){
            		continue;
            	}
            	
            	if(f!=null)
            		type = f.getType();
            	Object args = null;
            	java.lang.reflect.Method m = getMethod(config.getClass(),"set"+name);
            	if(m!=null && notNull(value) && type!=null){
            		if(String.class == type){
						args = value;
					}else if(java.lang.Integer.class == type || Integer.TYPE == type){
						args = Integer.valueOf(value);
					}else if(java.lang.Boolean.class == type || Boolean.TYPE == type){
						args = Boolean.parseBoolean(value);
					}else if(java.lang.Long.class == type || Long.TYPE==type){
						args = Long.valueOf(value);
					}else if(java.lang.Float.class == type || Float.TYPE==type){
						args = Float.valueOf(value);
					}else{
						logger.info("not map Class:"+type);
					}
					m.invoke(config, args);
					logger.info(name+":"+value);
				}else if(notNull(value))
					logger.warn("can't find element:"+value);
            }
        } catch (Exception e) {
            logger.error("load config error:", e);
        } finally {
            try {
				conn.close();
				ps.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				 logger.error("load config error:", e);
			}
            conn = null;
        }
	}
	
	public static void loadConf4xml(String file_url, BaseConfigMBean config) throws FileNotFoundException{
		loadConf4xml(new File(file_url),config);
	}
	
	public static void loadConf4xml(File file, BaseConfigMBean config) throws FileNotFoundException{
		loadConf4xml(new FileInputStream(file),config);
	}
	
	public static void loadConf4xml(InputStream is, BaseConfigMBean config) throws FileNotFoundException{
		if(is==null)
			throw new FileNotFoundException();
		try {
			Document doc = new  SAXReader().read(is);		 
			Element root = doc.getRootElement();
			
			String element = null;
		    Class<?> type = null;
			java.lang.reflect.Field[] fs = config.getClass().getDeclaredFields();
			for(java.lang.reflect.Field f : fs){
				element = f.getName();
				String value = getTextFromElement(root, element);
				if(value==null)continue;
				type = f.getType();
				Object args = null;
				java.lang.reflect.Method m = getMethod(config.getClass(),"set"+element);
				if(m==null)
					m = getMethod(config.getClass(),"is"+element);
				if(m!=null){
					if(notNull(value)){
						if(String.class == type){
							args = value;
						}else if(java.lang.Integer.class == type || Integer.TYPE == type){
							args = Integer.valueOf(value);
						}else if(java.lang.Boolean.class == type || Boolean.TYPE == type){
							args = Boolean.parseBoolean(value);
						}else if(java.lang.Long.class == type || Long.TYPE==type){
							args = Long.valueOf(value);
						}else if(java.lang.Float.class == type || Float.TYPE==type){
							args = Float.valueOf(value);
						}else if(java.util.Collection.class.isAssignableFrom(type)){
							args = loadCollection(root,element,f);
						}else if(java.util.Map.class.isAssignableFrom(type)){
							args = loadMap(root,element,f);
						}else{
							logger.info("not map Class:"+type);
							continue;
						}
					}
					
					try{
						m.invoke(config, args);
						logger.info(element+":"+value);
					}catch(Exception e){
						logger.warn(e);
						continue;
					}				
				}
				else if(notNull(value))
					logger.warn("can't find element:"+value);
			}
			try{is.close();}catch(Exception e){}
			
			logger.info(String.format("Read config file:  %s", is));

		} catch (Throwable t) {
			logger.error("read config file error:"+is +"\n"+t.getMessage());
		}
	}
	
	public static void saveConf2xml(String file_url, BaseConfigMBean config) throws FileNotFoundException{
		saveConf2xml(new File(file_url),config);
	}
	
	public static void saveConf2xml(java.net.URL file_url, BaseConfigMBean config) throws FileNotFoundException{
		saveConf2xml(file_url.getFile(),config);
	}
	
	public static void saveConf2xml(File file, BaseConfigMBean config) throws FileNotFoundException{
	    InputStream is = new FileInputStream(file);
	    StringBuilder sb = updateString(is, config);
        OutputStream output = new FileOutputStream(file);
        try {
			output.write(sb.toString().getBytes());
			output.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("save config file error:"+is +"\n"+e.getMessage());
		}
	}
	
	@SuppressWarnings("unchecked")
	private static StringBuilder updateString(InputStream is, BaseConfigMBean config) throws FileNotFoundException{
		StringBuilder sb = new StringBuilder();
		if(is==null)
			throw new FileNotFoundException();
		try {
			boolean empty = false;
			Document doc = null;
			try{
				doc = new SAXReader().read(is);	
			}catch(Exception e){
				empty = true;
				doc   = DocumentHelper.createDocument();
			}
				
			Element root = doc.getRootElement();
			
			String element = null;
		    Class<?> type = null;
			java.lang.reflect.Field[] fs = config.getClass().getDeclaredFields();
			sb
				.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n")
				.append("<config>\n");
			for(java.lang.reflect.Field f : fs){
				element = f.getName();
				if(getTextFromElement(root, element)==null && !empty)continue;
				Object value = null;
				type = f.getType();
				java.lang.reflect.Method m = getMethod(config.getClass(),"get"+element);
				if(m==null)
					m = getMethod(config.getClass(),"is"+element);
				if(m!=null){
					try{
						value = m.invoke(config);
						logger.info(element+":"+value);
					}catch(Exception e){
						logger.warn(e);
						continue;
					}	
					if(String.class == type){
						String data = String.valueOf(value);
						if(data.startsWith("{")){
							org.json.JSONObject jsonO = JsonUtils.getJSONObject(data);
							if(jsonO!=null){
								Iterator<String> it = jsonO.keys();
								sb.append("	<"+element+" ");
								for(;it!=null && it.hasNext();){
									String k = it.next();
									String v = jsonO.getString(k);
									sb.append(k)
									  .append("=")
									  .append("\""+v+"\" ");
								}
								sb.append("/>\n");
								continue;
							}
						}
						sb
							.append("	<"+element+">")
							.append(data)
							.append("</"+element+">\n");
					}else if(
							(java.lang.Integer.class == type || Integer.TYPE == type)||
							(java.lang.Boolean.class == type || Boolean.TYPE == type)||
							(java.lang.Long.class == type || Long.TYPE==type)||
							(java.lang.Float.class == type || Float.TYPE==type)){
						sb
							.append("	<"+element+">")
							.append(String.valueOf(value))
							.append("</"+element+">\n");
					}else if(java.util.Collection.class.isAssignableFrom(type)){
						Collection<String> list = (Collection<String>) value;
						for(String s : list){
							sb
							.append("	<"+element+">")
							.append(s)
							.append("</"+element+">\n");
						}
					}else if(java.util.Map.class.isAssignableFrom(type)){
						Map<String,String> map = (Map<String, String>) value;
						for(String s : map.keySet()){
							String v = map.get(s);
							sb
							.append("	<"+element)
							.append(" key=\""+s+"\"")
							.append(" value=\""+v+"\"")
							.append(" />\n");
						}
					}else{
						logger.info("not map Class:"+type);
						continue;
					}							
				}
				else 
					logger.warn("can't find element:"+element);
			}
			sb.append("</config>");
			logger.info(String.format("Read config file:  %s", is));

		} catch (Throwable t) {
			//t.printStackTrace();
			logger.error("read config file error:"+is +"\n"+t.getMessage());
		}
		return sb;
	}
	
	private static Collection<String> loadCollection(Element root, String element, java.lang.reflect.Field f) {
		// TODO Auto-generated method stub
		Collection<String> list = null;
		list = new ArrayList<String>();
		Element nextFoo;
        for (Iterator<?> j = root.elementIterator(element); j.hasNext();) {
        	nextFoo = (Element) j.next();
        	if(nextFoo!=null)
        		list.add(getTextFromElement(nextFoo));
        }
		return list;
	}

	private static Map<String,String> loadMap(Element root, String element, java.lang.reflect.Field f) {
		// TODO Auto-generated method stub
		Map<String,String> map = null;
		map = new HashMap<String,String>();
		Element nextFoo;
        for (Iterator<?> j = root.elementIterator(element); j.hasNext();) {
        	nextFoo = (Element) j.next();
        	String key  = (String) nextFoo.attributeValue("key");
            String value = (String) nextFoo.attributeValue("value");
            if(notNull(key) && notNull(value)){
           		map.put(key, value);
            }
        }
		return map;
	}
	
	private static String getTextFromElement(Element e, String name){
		Element element = e!=null?e.element(name):null;
		return getTextFromElement(element);
	}
	
	@SuppressWarnings("unchecked")
	private static String getTextFromElement(Element e){
		Element element = e;
		if(element!=null){
			List<Attribute> l  = element.attributes();
			if(l!=null && l.size()!=0){
				String[] keys = new String[l.size()];
				String[] values = new String[l.size()];
				for(int i=0; i<l.size(); i++){
					Attribute a = l.get(i);
					keys[i]     = a.getName();
					values[i]   = a.getValue();
				}
				return JsonUtils.toJson(keys, values, false);
			}
			return element.getTextTrim();
		}
		return null;
	}
	
	private static boolean notNull(String str){
		return str!=null && !"".equals(str);
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
}
