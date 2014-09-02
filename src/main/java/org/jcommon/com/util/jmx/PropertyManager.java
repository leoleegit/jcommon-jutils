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
package org.jcommon.com.util.jmx;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.SortedSet;
import java.util.TreeSet;
import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.AttributeNotFoundException;
import javax.management.DynamicMBean;
import javax.management.InvalidAttributeValueException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanOperationInfo;
import javax.management.ReflectionException;

import org.jcommon.com.util.config.ConfigLoader;

public class PropertyManager implements DynamicMBean {
    private final String propertyFileName;
    protected final Properties properties;
    
    private Integer count;
    
    private boolean isReadable, isWritable;
    
    private List<MBeanOperationInfo> oper = new ArrayList<MBeanOperationInfo>();
    
    public PropertyManager(String propertyFileName) throws IOException {
        this.propertyFileName = propertyFileName;
        properties = new Properties();
        load();
        defaultOperation();
    }

    public PropertyManager(String propertyFileName,boolean isReadable, boolean isWritable) throws IOException {
    	this.isReadable = isReadable;
    	this.isWritable = isWritable;
    	
        this.propertyFileName = propertyFileName;
        properties = new Properties();
        load();
        defaultOperation();
    }
    
    public PropertyManager(){
    	this.propertyFileName = null;
        properties = new Properties();
        defaultOperation();
    }
    
    public PropertyManager(boolean isReadable, boolean isWritable){
    	this.isReadable = isReadable;
    	this.isWritable = isWritable;
    	
    	this.propertyFileName = null;
        properties = new Properties();
        defaultOperation();
    }
    
    
    private void defaultOperation(){
        addOperation(new MBeanOperationInfo(
                        "reload",
                        "Reload properties from file",
                        null,   // no parameters
                        "void",
                        MBeanOperationInfo.ACTION));
    }
    
    public void addProperties(String name, String value){
    	 if (properties.containsKey(name)){
    		 removeProperties(name);
    	 }
    	 properties.setProperty(name, value);
    }
    
    public void removeProperties(String name){
    	 if (properties.containsKey(name)){
    		 properties.remove(name);
    	 }
    }
    
    public String getProperties(String name){
		 if (properties.containsKey(name)){
			 return  properties.getProperty(name);
		 }
		 return null;
    }
    
    public boolean hasProperties(String name){
		 if (properties.containsKey(name)){
			 return true;
		 }
		 return false;
    }
    
    public void addOperation(MBeanOperationInfo info){
    	if(oper.contains(info))return ;
    	oper.add(info);
    }
    
    public synchronized String getAttribute(String name)
    throws AttributeNotFoundException {
        String value = properties.getProperty(name);
        if (value != null)
            return value;
        else
            throw new AttributeNotFoundException("No such property: " + name);
    }

    public synchronized void setAttribute(Attribute attribute)
    throws InvalidAttributeValueException, MBeanException, AttributeNotFoundException {
        String name = attribute.getName();
        if (properties.getProperty(name) == null)
            throw new AttributeNotFoundException(name);
        Object value = attribute.getValue();
        if (!(value instanceof String)) {
            throw new InvalidAttributeValueException(
                    "Attribute value not a string: " + value);
        }
        properties.setProperty(name, (String) value);
        try {
            save();
        } catch (IOException e) {
            throw new MBeanException(e);
        }
    }

    public synchronized AttributeList getAttributes(String[] names) {
        AttributeList list = new AttributeList();
        for (String name : names) {
            String value = properties.getProperty(name);
            if (value != null)
                list.add(new Attribute(name, value));
        }
        return list;
    }

    public synchronized AttributeList setAttributes(AttributeList list) {
        Attribute[] attrs = (Attribute[]) list.toArray(new Attribute[0]);
        AttributeList retlist = new AttributeList();
        for (Attribute attr : attrs) {
            String name = attr.getName();
            Object value = attr.getValue();
            if (properties.getProperty(name) != null && value instanceof String) {
                properties.setProperty(name, (String) value);
                retlist.add(new Attribute(name, value));
            }
        }
        try {
            save();
        } catch (IOException e) {
            return new AttributeList();
        }
        return retlist;
    }

    public Object invoke(String name, Object[] args, String[] sig)
    throws MBeanException, ReflectionException {
        if (name.equals("reload") &&
                (args == null || args.length == 0) &&
                (sig == null || sig.length == 0)) {
            try {
                load();
                return null;
            } catch (IOException e) {
                throw new MBeanException(e);
            }
        }else{
        	java.lang.reflect.Method m = ConfigLoader.getMethod(this.getClass(), name);
        	if(m==null)throw new ReflectionException(new NoSuchMethodException(name));
        	try{
				m.invoke(this, args);
				return null;
			}catch(Exception e){
				 throw new ReflectionException(e);
			}	
        }
    }
    
    public synchronized MBeanInfo getMBeanInfo() {
    	properties.setProperty("00000000000000  Count", String.valueOf(getCount()-1));
    	
        SortedSet<String> names = new TreeSet<String>();
        for (Object name : properties.keySet())
            names.add((String) name);
        MBeanAttributeInfo[] attrs = new MBeanAttributeInfo[names.size()];
  
        Iterator<String> it = names.iterator();
        for (int i = 0; i < attrs.length; i++) {
            String name = it.next();
            attrs[i] = new MBeanAttributeInfo(
                    name,
                    "java.lang.String",
                    "Property " + name,
                    isReadable,   // isReadable
                    isWritable,   // isWritable
                    false); // isIs
        }
        MBeanOperationInfo[] opers = new MBeanOperationInfo[oper.size()];
        for(int i=0; i<oper.size(); i++){
        	opers[i]= oper.get(i);
        }
        return new MBeanInfo(
                this.getClass().getName(),
                "Property Manager MBean",
                attrs,
                null,  // constructors
                opers,
                null); // notifications
    }

    private void load() throws IOException {
    	if(propertyFileName==null)return;
        InputStream input = new FileInputStream(propertyFileName);
        properties.load(input);
        input.close();
    }

    private void save() throws IOException {
    	if(propertyFileName==null)return;
        String newPropertyFileName = propertyFileName + "$$new";
        File file = new File(newPropertyFileName);
        OutputStream output = new FileOutputStream(file);
        String comment = "Written by " + this.getClass().getName();
        properties.store(output, comment);
        output.close();
        if (!file.renameTo(new File(propertyFileName))) {
            throw new IOException("Rename " + newPropertyFileName + " to " +
                    propertyFileName + " failed");
        }
    }

	public void setCount(int count) {
		this.count = count;
	}

	public int getCount() {
		count = properties.size();
		return count;
	}
}