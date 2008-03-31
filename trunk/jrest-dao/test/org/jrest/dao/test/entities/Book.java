package org.jrest.dao.test.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.CollectionOfElements;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.IndexColumn;

/**
 * 书本
 * @author Franky
 */
@Entity
@Table(name = "book")
@NamedQueries({
	@NamedQuery(name = "Book.lengthMoreThan", 
			query = "from Book b where b.packingInfo.length > :length")
})
public class Book extends AbstractEntity {

	private String id; // 唯一标识
	private String title; // 标题
	private List<Author> authors; // 作者列表
	private float price; // 价格
	private String summary; // 摘要
	private PackingInfo packingInfo; // 包装信息
	
	private Category category; // 所属分类

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "id", nullable = false, insertable = true, updatable = false, length = 32, columnDefinition = "CHAR(32)")
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "title", nullable = false)
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	@CollectionOfElements(fetch = FetchType.EAGER)
	@JoinTable(name = "book_authors", joinColumns = @JoinColumn(name="book_id"))
    @IndexColumn(name="auth_index")
	public List<Author> getAuthors() {
		if (this.authors == null)
			this.authors = new ArrayList<Author>();
		return this.authors;
	}

	public void setAuthors(List<Author> authors) {
		this.authors = authors;
	}

	@Column(name = "summary", nullable = false)
	@Lob
	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	@Column(name = "price", nullable = false)
	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	@Embedded
	public PackingInfo getPackingInfo() {
		return packingInfo;
	}

	public void setPackingInfo(PackingInfo packingInfo) {
		this.packingInfo = packingInfo;
	}

	@ManyToOne(targetEntity = Category.class)
	@JoinColumn(name = "cate_id")
	@ForeignKey(name = "FK_Category")
	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	@Override
	protected String identifier() {
		return id;
	}

}
