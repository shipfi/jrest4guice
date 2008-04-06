package org.jrest.core.util;

import com.google.inject.Module;

/**
 * 类扫描监听器 接口
 */
public interface ClassScanListener {

	/**
	 * 完成扫描,将结果放入 Module 列表内
	 * @param modules
	 */
	public Module onComplete();

	/**
	 * 检查类是否正确
	 * @param clazz 被检查类
	 */
	public void onScan(Class<?> clazz);

}
