package org.jrest.core.guice;

import java.util.List;
import java.util.Set;

import com.google.inject.Module;

public interface GuiceModuleProvider {
	public void setScanPackageList(Set<String> packageList);
	public List<Module> getModules();
}
