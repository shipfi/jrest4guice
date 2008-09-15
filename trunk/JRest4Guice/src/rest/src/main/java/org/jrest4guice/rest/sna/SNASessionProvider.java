package org.jrest4guice.rest.sna;

import com.google.inject.Provider;

public class SNASessionProvider implements Provider<SNASession> {
	private static ThreadLocal<SNASession> session = new ThreadLocal<SNASession>();
	
	public static void setCurrentSNASession(SNASession s){
		session.set(s);
	}
	
	public static SNASession getCurrentSNASession(){
		return session.get();
	}
	
	public static void clearCurrentSNASession(){
		session.remove();
	}
	
	@Override
	public SNASession get() {
		return SNASessionProvider.getCurrentSNASession();
	}
}
