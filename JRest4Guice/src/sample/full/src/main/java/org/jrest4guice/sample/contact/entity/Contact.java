package org.jrest4guice.sample.contact.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.Email;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotEmpty;
import org.hibernate.validator.NotNull;
import org.jrest4guice.commons.i18n.annotations.ResourceBundle;
import org.jrest4guice.persistence.EntityAble;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ:86895156)</a>
 *
 */
@Entity()
@Table(name = "Contact_tb")
@NamedQueries( {
		@NamedQuery(name = "Contact.list[find]", query = "select e from Contact e order by e.name"),
		@NamedQuery(name = "Contact.list[count]", query = "select count(*) from Contact"),
		@NamedQuery(name = "Contact.byName[load]", query = "select e from Contact e where e.name=?") })
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "contactCache")
@ResourceBundle("fullSample")
public class Contact implements EntityAble<String>, Serializable {

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

	@Column(name = "headPic", nullable = true, length = 255)
	private String headPic;

	@Column(name = "homePhone", nullable = true, length = 20)
	private String homePhone;

	@Column(name = "mobilePhone", nullable = true, length = 20)
	private String mobilePhone;

	@Column(name = "eMail", nullable = true, length = 36)
	private String email;

	@Lob
	@Column(name = "address", nullable = true, length = 2000)
	private String address;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@NotNull(message="{contact.name}")
	@NotEmpty(message="{contact.name}")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHomePhone() {
		return homePhone;
	}

	public void setHomePhone(String homePhone) {
		this.homePhone = homePhone;
	}

	@NotNull(message="{contact.mobile}")
	@NotEmpty(message="{contact.mobile}")
	@Length(max=11,message="{contact.mobile}")
	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	@Email(message="{contact.email}")
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getHeadPic() {
		return headPic;
	}

	public void setHeadPic(String headPic) {
		this.headPic = headPic;
	}

	public String toString() {
		return "id=" + this.id + ";name=" + this.name + ";address="
				+ this.address;
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
		Contact other = (Contact) obj;
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
