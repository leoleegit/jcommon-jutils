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
package org.jcommon.com.util.collections;

import java.util.*;

import org.jcommon.com.util.system.SystemListener;
/**
 * 
 * @author LeoLee<leo.li@protel.com.hk>
 *
 */
public class MapStore implements SystemListener{
	private Map<Object, Object> store;
	private List<MapStoreListener> listeners = new ArrayList<MapStoreListener>();
	
	public MapStore(){
		store = Collections.synchronizedMap(new HashMap<Object,Object>());
	}
	
	public MapStore(Map<Object, Object> store){
		this.store = store;
	}
	
	protected boolean addOne(Object key, Object value){
		synchronized(store){
			if(!store.containsKey(key)){
				store.put(key, value);
				synchronized(listeners){
					for(MapStoreListener listener : listeners)
						listener.addOne(key, value);
				}
				return true;
			}
		}
		return false;
	}
	
	protected boolean hasKey(Object key){
		return store.containsKey(key);
	}
	
	protected boolean updateOne(Object key, Object value){
		synchronized(store){
			if(store.containsKey(key)){
				store.put(key, value);
				synchronized(listeners){
					for(MapStoreListener listener : listeners)
						listener.updateOne(key, value);
				}
				return true;
			}
		}
		return false;
	}
	
	protected Object getOne(Object key){
		if(key==null)return null;
		synchronized(store){
			if(store.containsKey(key))
				return store.get(key);
		}
		return null;
	}
	
	protected Object removeOne(Object key){
		synchronized(store){
			if(store.containsKey(key)){
				synchronized(listeners){
					for(MapStoreListener listener : listeners)
						listener.removeOne(key);
				}
				return store.remove(key);
			}
		}
		return null;
	}
	
	protected Map<Object, Object> getAll(){
		return store;
	}
	
	public void clear() {
		// TODO Auto-generated method stub
		store.clear();
		listeners.clear();
	}

	@Override
	public void shutdown() {
		// TODO Auto-generated method stub
		clear();
	}

	@Override
	public void startup() {
		// TODO Auto-generated method stub
		
	}

	public List<MapStoreListener> getMapStoreListeners() {
		return listeners;
	}
	
	public void removeMapStoreListener(MapStoreListener listener) {
		synchronized(listeners){
			if(listeners.contains(listener))
				listeners.remove(listener);
		}
	}

	public void addMapStoreListener(MapStoreListener listener) {
		synchronized(listeners){
			if(!listeners.contains(listener))
				listeners.add(listener);
		}
	}

	@Override
	public boolean isSynchronized() {
		// TODO Auto-generated method stub
		return false;
	}
}
