package org.jrest.dao.test.entities;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.hibernate.annotations.Parent;

/**
 * 作者、编著者、编译者
 * @author Franky
 */
@Embeddable
public class Author {

	private Book owner;
	private String name;
	
	public Author() {}
	
	public Author(String name) {
		this.name = name;
	}

	@Parent
	public Book getOwner() {
		return owner;
	}

	public void setOwner(Book owner) {
		this.owner = owner;
	}

	@Column(name = "auth_name", nullable = false, length = 20)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		final Author that = (Author) obj;
		if (!name.equals(that.name))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		int result;
		result = name.hashCode();
		return result;
	}

}
