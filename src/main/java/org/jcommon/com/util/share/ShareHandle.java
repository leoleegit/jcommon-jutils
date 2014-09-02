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

import java.util.Map;

public interface ShareHandle {
	public void  addObject(String request, ShareListener listener, Object key, Object value);
	
	public void  hasObject(String request, ShareListener listener, Object key);
	
	public void  updateObject(String request, ShareListener listener, Object key, Object value);
	
	public void  getObject(String request, ShareListener listener, Object key);
	
	public void  removeObject(String request, ShareListener listener, Object key);
	
	public void  getAllObject(String request, ShareListener listener);
	
	
	public boolean  addObject(Object key, Object value);
	
	public boolean  hasObject(Object key);
	
	public boolean  updateObject(Object key, Object value);
	
	public Object  getObject(Object key);
	
	public Object  removeObject(Object key);
	
	public Map<Object, Object>  getAllObject();
}
