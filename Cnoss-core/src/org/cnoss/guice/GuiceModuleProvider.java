package org.cnoss.guice;

import java.util.List;

import com.google.inject.Module;

public interface GuiceModuleProvider {
	public List<Module> getModules();
}
