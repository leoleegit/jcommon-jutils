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

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

import org.apache.log4j.Logger;
import org.jcommon.com.util.JsonUtils;
import org.json.JSONException;

import com.sun.jdmk.comm.AuthInfo;
import com.sun.jdmk.comm.HtmlAdaptorServer;
/**
 * 
 * @author LeoLee<leo.li@protel.com.hk>
 *
 */
public class HtmlAdaptor extends HtmlAdaptorServer {
	private static Logger logger = Logger.getLogger(HtmlAdaptor.class);
	
	private String user;
	private String password;
	private int port;
	
	private ObjectName adapterName;
	
	public HtmlAdaptor(String data){
		super();
		init(data);
	}
	
	private void init(String data){
		if(data==null){
			logger.info("html adaptor have not Descriptions data");
			return;
		}
		try {
			user = JsonUtils.getValueByKey(data, "user");
			password = JsonUtils.getValueByKey(data, "password");
			String p = JsonUtils.getValueByKey(data, "port");
			if(p!=null)port = Integer.valueOf(p);
			logger.info("user:"+user+"	password:"+password+"	port:"+port);
			
			if(user!=null && password!=null){
		        AuthInfo authInfo = new AuthInfo();
		        authInfo.setLogin(user);
		        authInfo.setPassword(password);
		        addUserAuthenticationInfo(authInfo);
			}
			this.setPort(port);
			adapterName = new ObjectName("HtmlAdaptor:name=htmladapter");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			logger.error("", e);
		} catch (MalformedObjectNameException e) {
			// TODO Auto-generated catch block
			logger.error("", e);
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			logger.error("", e);
		}
	}
	
	public ObjectName getObjectName_(){
		return adapterName;
	}
}
