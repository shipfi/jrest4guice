package org.jrest4guice.sna;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ:86895156)</a>
 * 
 */
@SuppressWarnings("unchecked")
public class SNASession extends HashMap{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6602219922071210040L;
	
	private boolean duty;

	public boolean isDuty() {
		return duty;
	}

	public void setDuty(boolean duty) {
		this.duty = duty;
	}
	
	@Override
	public Object put(Object key, Object value) {
		this.setDuty(true);
		return super.put(key, value);
	}

	@Override
	public void putAll(Map values) {
		super.putAll(values);
		this.setDuty(true);
	}

	@Override
	public Object remove(Object key) {
		this.setDuty(true);
		return super.remove(key);
	}
}
