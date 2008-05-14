package org.jrest.core.persist.jpa;

import java.io.Serializable;

/**
 * 分页参数类<br>
 * 用于传递分页参数给 DAO 实现类，限制查询的记录返回数量
 */
public class Pagination implements Serializable {

	private static final long serialVersionUID = 6505288541338596959L;

	private static final int MIX_PAGE_NO = 1;
	private static final int MIX_PAGE_SIZE = 1;

	private int pageNo = Pagination.MIX_PAGE_NO;
	private int pageSize = Pagination.MIX_PAGE_SIZE;

	/**
	 * 默认 页码与每页记录数均为1
	 */
	public Pagination() {
	}

	/**
	 * 构造方法
	 * 
	 * @param pageNo
	 *            页码
	 * @param pageSize
	 *            每页记录数
	 * @throws IllegalArgumentException
	 *             页码小于1或每页记录数小于1时抛出
	 */
	public Pagination(final int pageNo, final int pageSize)
			throws IllegalArgumentException {
		if (pageNo < Pagination.MIX_PAGE_NO
				|| pageSize < Pagination.MIX_PAGE_SIZE) {
			throw new IllegalArgumentException("页码与每页记录数最小分别不能小于"
					+ Pagination.MIX_PAGE_NO + "与" + Pagination.MIX_PAGE_SIZE);
		}
		this.pageNo = pageNo;
		this.pageSize = pageSize;
	}

	/**
	 * 获取 起始记录位置
	 */
	public int getFirstResult() {
		return (this.pageNo - 1) * this.pageSize;
	}

	/**
	 * 获取 最大返回记录数
	 */
	public int getMaxResults() {
		return this.getPageSize();
	}

	/**
	 * 通过记录数量，计算页面数量
	 * 
	 * @param count
	 *            记录数量
	 * @return 页面数量
	 */
	public int getPageCount(final int count) {
		return (count - 1) / this.pageSize + 1;
	}

	// 注入与获取方法

	/**
	 * 获取 页码
	 */
	public int getPageNo() {
		return this.pageNo;
	}

	/**
	 * 获取 每页记录数
	 */
	public int getPageSize() {
		return this.pageSize;
	}

	/**
	 * 设置 页码
	 * 
	 * @param pageNo
	 *            页码
	 * @throws IllegalArgumentException
	 *             页码小于1时抛出
	 */
	public void setPageNo(final int pageNo) throws IllegalArgumentException {
		if (pageNo < Pagination.MIX_PAGE_NO) {
			throw new IllegalArgumentException("页码最小不能小于"
					+ Pagination.MIX_PAGE_NO);
		}
		this.pageNo = pageNo;
	}

	/**
	 * 设置 每页记录数
	 * 
	 * @param pageSize
	 *            每页记录数
	 * @throws IllegalArgumentException
	 *             每页记录数小于1时抛出
	 */
	public void setPageSize(final int pageSize) throws IllegalArgumentException {
		if (pageSize < Pagination.MIX_PAGE_SIZE) {
			throw new IllegalArgumentException("每页记录数最小不能小于"
					+ Pagination.MIX_PAGE_SIZE);
		}
		this.pageSize = pageSize;
	}
}
