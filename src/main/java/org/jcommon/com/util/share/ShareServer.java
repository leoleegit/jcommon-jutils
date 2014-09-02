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
package org.jcommon.com.util.share;

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

import org.apache.log4j.Logger;

public class ShareServer {
	private Logger logger = Logger.getLogger(this.getClass()); 
	
	private ShareObjectMBean shareObject;
	private ObjectName adapterName;
	
	private String name;
	
	public ShareServer(String name) {
		try {
			this.setName(name);
			adapterName = new ObjectName("ShareObject:name="+name);
		} catch (MalformedObjectNameException e) {
			// TODO Auto-generated catch block
			logger.error(adapterName.toString(), e);
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			logger.error(adapterName.toString(), e);
		}
		shareObject = new ShareObject();
	}
	
	public void setShareObject(ShareObjectMBean shareObject) {
		this.shareObject = shareObject;
	}

	public ShareObjectMBean getShareObject() {
		return shareObject;
	}

	public void setAdapterName(ObjectName adapterName) {
		this.adapterName = adapterName;
	}

	public ObjectName getAdapterName() {
		return adapterName;
	}
	
	public void addNotificationListener(String key, NotificationListener listener){
		if(shareObject!=null)
			shareObject.addNotificationListener(key, listener);
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
