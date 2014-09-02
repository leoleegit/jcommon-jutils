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
package org.jcommon.com.util.system;

import java.util.List;
import java.util.Map;

import org.jcommon.com.util.config.BaseConfigMBean;

public interface SystemConfigMBean extends BaseConfigMBean {
	public void setSystem_object(Map<String,String> system_object);

	public Map<String,String> getSystem_object();

	public void setShareObject(List<String> ShareObject);

	public List<String> getShareObject();
	
	public void setInit_threads(int init_threads);

	public int getInit_threads();
	
	public void setHtmladapter(String htmladapter);

	public String getHtmladapter();
	
	public void setMbean_server(String mbean_server);

	public String getMbean_server();

	public void setRmiadapter(String rmiadapter);

	public String getRmiadapter();
	
	public void setVersion(long version);

	public long getVersion();
	
	public void setLib_url(String lib_url);

	public String getLib_url();
	
	public void setAuth(boolean auth);

	public boolean isAuth();
	
	public boolean isStandby();

	public void setStandby(boolean standby);
	
	public void setGroup_id(String group_id);

	public String getGroup_id();
}
