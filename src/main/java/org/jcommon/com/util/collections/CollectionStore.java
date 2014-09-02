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

import java.util.Collection;
import java.util.Vector;
/**
 * 
 * @author LeoLee<leo.li@protel.com.hk>
 *
 */
public class CollectionStore {
	private Collection<Object> store;
	
	public CollectionStore(){
		store = new Vector<Object>();
	}
	
	public CollectionStore(Collection<Object> store){
		this.store = store;
	}
	
	protected boolean addOne(Object key){
		synchronized(store){
			if(!store.contains(key)){
				store.add(key);
				return true;
			}
		}
		return false;
	}
	
	protected boolean hasKey(Object key){
		return store.contains(key);
	}
	
	protected boolean removeOne(Object key){
		synchronized(store){
			if(store.contains(key)){
				store.remove(key);
				return true;
			}
		}
		return false;
	}
	
	protected Collection<Object> getAll(){
		return store;
	}
	
	protected void resetAll(Collection<Object> store){
		this.store = store;
	}
	
	protected void clear() {
		// TODO Auto-generated method stub
		store.clear();
	}
}
