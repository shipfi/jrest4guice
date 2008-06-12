package org.cms.entity;

import org.jrest4guice.jpa.audit.AuditableEntity;



/**
 * 文章分类
 *  
*/
public class Category extends AuditableEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1253758001385907671L;
	private String name;
	private Long parentId;
	private String parentEnum;
	private int childNum;
	private String description;
	private int depth;
	private int branchId;
	private int ordering;
	private String folders;
	private Long columnId;
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getOrdering() {
		return ordering;
	}
	public void setOrdering(int ordering) {
		this.ordering = ordering;
	}
	public int getChildNum() {
		return childNum;
	}
	public void setChildNum(int childNum) {
		this.childNum = childNum;
	}
	public String getParentEnum() {
		return parentEnum;
	}
	public void setParentEnum(String parentEnum) {
		this.parentEnum = parentEnum;
	}
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	public int getBranchId() {
		return branchId;
	}
	public void setBranchId(int branchId) {
		this.branchId = branchId;
	}
	public int getDepth() {
		return depth;
	}
	public void setDepth(int depth) {
		this.depth = depth;
	}
	public String getFolders() {
		return folders;
	}
	public void setFolders(String folders) {
		this.folders = folders;
	}
	public Long getColumnId() {
		return columnId;
	}
	public void setColumnId(Long columnId) {
		this.columnId = columnId;
	}
		
}
