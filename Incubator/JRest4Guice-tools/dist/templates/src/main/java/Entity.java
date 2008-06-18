package ${context.packageName}.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.jrest4guice.jpa.EntityAble;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ:86895156)</a>
 *
 */
@Entity()
@Table(name = "${context.entityName}_tb")
@NamedQueries( {
		@NamedQuery(name = "${context.entityName}.list[find]", query = "select e from ${context.entityName} e order by e.name desc"),
		@NamedQuery(name = "${context.entityName}.list[count]", query = "select count(*) from ${context.entityName}"),
		@NamedQuery(name = "${context.entityName}.byName[load]", query = "select e from ${context.entityName} e where e.name=?") })
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "${context.packageName}Cache")
public class ${context.entityName} implements EntityAble<String>, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2422008878326999364L;

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "id", nullable = false, updatable = false,length=32)
	private String id;

	@Column(name = "name", nullable = false, length = 24, unique = true)
	private String name;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public String toString() {
		return "id=" + this.id + ";name=" + this.name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		${context.entityName} other = (${context.entityName}) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
}
