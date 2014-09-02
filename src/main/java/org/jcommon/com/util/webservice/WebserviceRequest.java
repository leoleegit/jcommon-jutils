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
package org.jcommon.com.util.webservice;

import java.rmi.RemoteException;
import java.util.Properties;

import org.apache.axis.AxisFault;
import org.apache.axis.client.Call;

public class WebserviceRequest implements Runnable{

	private Call call;
	private WebserviceListener listener;
	private Object[] params;
	
	private Properties properties = new Properties();
	
	public Properties getProperties() {
		return properties;
	}
	  
	public void setProperty(String key, String value){
		this.properties.put(key, value);
	}
	  
	public String getProperty(String key){
		if(properties.containsKey(key))
			return properties.getProperty(key);
		return null;
	}
	
	public WebserviceRequest(WebserviceListener listener, Call call, Object[] params){
		this.listener = listener;
		this.call = call;
		this.params = params;
	}
	
	public void run() {
		// TODO Auto-generated method stub
		try {
			if(call!=null)
				listener.onSuccessful(this, this.call.invoke(params));
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			listener.onException(this, e);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			listener.onTimeout(this);
		}
	}

}
