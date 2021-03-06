package org.jrest4guice.persistence;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ:86895156)</a>
 *
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
