package org.jrest.dao.test.entities;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

/**
 * 唯一标识 抽象类
 * 
 * @author Franky
 */
@MappedSuperclass
public abstract class AbstractEntity {

	// 非持久化属性
	@Transient
	protected int hashValue = 0;
	
	/**
	 * 获取当前实体的字符串形式的识别符
	 * @return
	 */
	@Transient
	protected abstract String identifier();

	@Override
	public int hashCode() {
		if (this.hashValue == 0) {
			final int PRIME = 31;
			int result = 1;
			this.hashValue = PRIME * result + ((identifier() == null) ? 0 : identifier().hashCode());
		}
		return this.hashValue;
	}

	@Override
	@SuppressWarnings("unchecked")
	public boolean equals(Object rhs) {
		if (rhs == null)
			return false;
		Class c = rhs.getClass();
		if (!c.getName().equals(this.getClass().getName()))
			return false;
		AbstractEntity that = (AbstractEntity) rhs;
		if (this.identifier() == null || that.identifier() == null)
			return false;
		return (this.identifier().equals(that.identifier()));
	}

}
