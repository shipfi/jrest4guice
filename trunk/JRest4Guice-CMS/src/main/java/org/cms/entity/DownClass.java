package org.cms.entity;

import org.jrest4guice.jpa.audit.AuditableEntity;



/**
 * 下载分类
 *  
*/
public class DownClass extends AuditableEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2817818360831263272L;
	private String name;
	private Long parentId;
	private String parentEnum;
	private int childNum;
	private String description;
	private int depth;
	private int branchId;
	private int ordering;
	private String folders;
	private int columnId;
	
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
	public int getColumnId() {
		return columnId;
	}
	public void setColumnId(int columnId) {
		this.columnId = columnId;
	}
	
}
