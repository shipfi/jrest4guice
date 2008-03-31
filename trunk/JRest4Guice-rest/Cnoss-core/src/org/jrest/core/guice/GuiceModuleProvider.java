package org.jrest.core.guice;

import java.util.List;

import com.google.inject.Module;

public interface GuiceModuleProvider {
	public List<Module> getModules();
}
