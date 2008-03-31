package org.jrest.dao.test.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * 包装信息
 * @author Franky
 */
@Embeddable
@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
public class PackingInfo implements Serializable {

	private static final long serialVersionUID = 3611493885619896535L;

	@XmlElement
	private String packaging; // 包装
	@XmlElement
	private String paper; // 纸张
	@XmlElement
	private Integer length; // 页数
	
	public PackingInfo() {}
	
	public PackingInfo(String packaging, String paper, Integer length) {
		this.packaging = packaging;
		this.paper = paper;
		this.length = length;
	}

	@Column(name = "pack_packaging")
	public String getPackaging() {
		return packaging;
	}
	
	public void setPackaging(String packaging) {
		this.packaging = packaging;
	}

	@Column(name = "pack_paper")
	public String getPaper() {
		return paper;
	}

	public void setPaper(String paper) {
		this.paper = paper;
	}

	@Column(name = "pack_length")
	public Integer getLength() {
		return length;
	}

	public void setLength(Integer length) {
		this.length = length;
	}

}
