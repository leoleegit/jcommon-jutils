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

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.jcommon.com.util.collections.MapStore;

public class ShareObject extends MapStore implements ShareObjectMBean, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 15919065160L;

	public static final String SERVER = "server";
	
	public static final String CLIENT = "client";
	
	private static Map<String, NotificationListener> NotificationListeners = new HashMap<String, NotificationListener>();
	
	@Override
	public boolean _addOne(Object key, Object value) {
		// TODO Auto-generated method stub
		if(NotificationListeners.containsKey(key))
			NotificationListeners.get(key).onAddObject(key, value);
		return super.addOne(key, value);
	}

	@Override
	public boolean _hasKey(Object key) {
		// TODO Auto-generated method stub
		return super.hasKey(key);
	}

	@Override
	public boolean _updateOne(Object key, Object value) {
		// TODO Auto-generated method stub
		if(NotificationListeners.containsKey(key))
			NotificationListeners.get(key).onUpdateObject(key, value);
		return super.updateOne(key, value);
	}

	@Override
	public Object _getOne(Object key) {
		// TODO Auto-generated method stub
		return super.getOne(key);
	}

	@Override
	public Object _removeOne(Object key) {
		// TODO Auto-generated method stub
		if(NotificationListeners.containsKey(key))
			NotificationListeners.get(key).onRemoveObject(key, null);
		return super.removeOne(key);
	}

	@Override
	public Map<Object, Object> _getAll() {
		// TODO Auto-generated method stub
		return super.getAll();
	}

	@Override
	public void addNotificationListener(String key,
			NotificationListener listener) {
		// TODO Auto-generated method stub
		NotificationListeners.put(key, listener);
	}
}
