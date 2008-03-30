package org.cnoss.util;

import java.util.List;

import com.google.inject.Module;

public interface ClassScanListener {
	public void onStart();
	public void onScan(Class<?> clazz);
	public void onComplete(List<Module> modules);
}
