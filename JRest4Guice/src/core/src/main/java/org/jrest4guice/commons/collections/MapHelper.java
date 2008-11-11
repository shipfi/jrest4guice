package org.jrest4guice.commons.collections;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ:86895156)</a>
 *
 */
public class MapHelper<K,V> {
	private Map<K,V> map = new HashMap<K,V>();


	public MapHelper<K,V> put(K key,V value){
		this.map.put(key, value);
		return this;
	}
	
	public Map<K,V> toMap(){
		return this.map;
	}
}
