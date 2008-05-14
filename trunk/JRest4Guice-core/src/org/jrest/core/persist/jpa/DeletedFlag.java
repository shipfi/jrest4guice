package org.jrest.core.persist.jpa;

/**
 * 删除标记 接口
 * 
 * @author Franky
 */
public interface DeletedFlag {
	/**
	 * 是否有效（是否没有做上删除标记）
	 * 
	 * @return
	 */
	boolean isDeleted();

	/**
	 * 设置成无效（做上删除标记）
	 * 
	 * @param flag
	 * @return
	 */
	void setDeleted(boolean deleted);
}
