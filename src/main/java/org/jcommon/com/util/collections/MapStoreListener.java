package org.jcommon.com.util.collections;

public interface MapStoreListener {
	public boolean addOne(Object key, Object value);

	public boolean updateOne(Object key, Object value);
	
	public Object removeOne(Object key);
}
