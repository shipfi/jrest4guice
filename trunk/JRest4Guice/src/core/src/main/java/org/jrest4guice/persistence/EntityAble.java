package org.jrest4guice.persistence;

import java.io.Serializable;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ:86895156)</a>
 *
 */
public interface EntityAble<PK extends Serializable> extends Serializable {
	/**
	 * 范围为所有
	 */
	public static final int SCOPE_ALL = -1;

	/**
	 * 范围为没有做上删除标记的
	 */
	public static final int SCOPE_VALID = 1;

	/**
	 * 范围为已经做上删除标记的
	 */
	public static final int SCOPE_INVALID = 0;

	/**
	 * 获取实体主键
	 * 
	 * @return
	 */
	public PK getId();

	/**
	 * 设置实体主键
	 * 
	 * @param id
	 */
	public void setId(PK id);

}
