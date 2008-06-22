package org.jrest4guice.sample.contact.entity;

import java.io.Serializable;
import java.util.List;

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
import org.jrest4guice.jpa.EntityAble;
import org.jrest4guice.rest.json.annotations.JsonExclude;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ:86895156)</a>
 * 
 */
@Entity()
@Table(name = "User_tb")
@NamedQueries( {
		@NamedQuery(name = "User.list[find]", query = "select e from User e order by e.name"),
		@NamedQuery(name = "User.list[count]", query = "select count(*) from User"),
		@NamedQuery(name = "User.byName[load]", query = "select e from User e where e.name=?"),
		@NamedQuery(name = "User.byNameAndPassword[load]", query = "select e from User e where e.name=? and e.password=?") })
public class User implements EntityAble<String>, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2422008878326999364L;

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "id", nullable = false, updatable = false,length=32)
	private String id;

	@Column(name = "name", nullable = false, length = 36, unique = true)
	private String name;

	@Column(name = "password", nullable = false, length = 32)
	@JsonExclude
	private String password;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "UserRole_rl_tb", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	private List<Role> roles;

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

	public String getPassword() {
		return password;
	}

	public void setPassword(String headPic) {
		this.password = headPic;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
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
		User other = (User) obj;
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

	public String toString(){
		return "name="+this.name;
	}
}
