package org.jrest4guice.sample.entity;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.jrest4guice.core.jpa.EntityAble;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss</a>
 *
 */
@Entity()
@Table(name = "Role_tb")
@NamedQueries( {
		@NamedQuery(name = "Role.list[find]", query = "select e from Role e order by e.name"),
		@NamedQuery(name = "Role.list[count]", query = "select count(*) from Role"),
		@NamedQuery(name = "Role.listByUserName[find]", query = "select roles from User where name=? order by name"),
		@NamedQuery(name = "Role.byName[load]", query = "select e from Role e where e.name=?") })
public class Role implements EntityAble<String>, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2422008878326999364L;

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "id", nullable = false, updatable = false)
	private String id;

	@Column(name = "name", nullable = false, length = 36, unique = true)
	private String name;
	
	@Column(name = "description", nullable = true, length = 60)
	private String description;

	@ManyToMany(fetch=FetchType.LAZY)
	@JoinTable(name = "UserRole_rl_tb", joinColumns = @JoinColumn(name = "role_id"),inverseJoinColumns = @JoinColumn(name = "user_id"))
	private Set<User> user;

	public Set<User>  getUser() {
		return user;
	}

	public void setUser(Set<User> user) {
		this.user = user;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

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
		Role other = (Role) obj;
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
