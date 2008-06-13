package org.cnoss.cms.entity.audit;

import java.util.Date;

/**
 * 需要审计记录的Entity POJO实现本接口.
 * 
 * 如果用户要定义其他的审计信息,重新实现本类并在AuditableEntityListener中替换.
 * 

 */
public interface Auditable {
	public String getLastModifiedBy();

	public void setLastModifiedBy(String lastModifiedBy);

	public Date getLastModifiedTime();

	public void setLastModifiedTime(Date lastModifiedTime);

	public String getCreatedBy();

	public void setCreatedBy(String createdBy);

	public Date getCreatedTime();

	public void setCreatedTime(Date createdTime);
}
