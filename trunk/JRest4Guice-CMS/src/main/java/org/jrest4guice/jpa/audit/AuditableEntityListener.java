package org.jrest4guice.jpa.audit;

import java.util.Date;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

/**
 * 添加审计记录的JPA Listener.
 * 如果用户修改了Auditable接口或UserContext实现,需要在本类中进行替换.
 * 
 */
public class AuditableEntityListener {

	@PrePersist
	public void setCreatedInfo(Object o) {
		if (o instanceof Auditable) {
			String currentUser =UserContext.getCurrentUser();
			Date now = new Date();
			Auditable entity = (Auditable) o;

			if (currentUser != null) {
				entity.setCreatedBy(currentUser);
				entity.setLastModifiedBy(currentUser);
			}

			entity.setCreatedTime(now);
			entity.setLastModifiedTime(now);
		}
	}

	@PreUpdate
	public void setUpdatedInfo(Object o) {
		if (o instanceof Auditable) {
			String currentUser = UserContext.getCurrentUser();
			Date now = new Date();
			Auditable entity = (Auditable) o;

			if (currentUser != null) {
				entity.setLastModifiedBy(currentUser);
			}

			entity.setLastModifiedTime(now);
		}
	}
}