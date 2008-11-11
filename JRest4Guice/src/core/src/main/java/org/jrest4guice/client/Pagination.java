package org.jrest4guice.client;

import java.io.Serializable;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ:86895156)</a>
 * 
 */
public class Pagination implements Serializable {

	private static final long serialVersionUID = 6505288541338596959L;

	private static final int MIX_PAGE_NO = 1;
	private static final int MIX_PAGE_SIZE = 1;
	private static final int DEFAULT_PAGE_SIZE = 12;

	private int pageIndex = Pagination.MIX_PAGE_NO;
	private int pageSize = DEFAULT_PAGE_SIZE;

	/**
	 * 默认 页码与每页记录数均为1
	 */
	public Pagination() {
	}

	/**
	 * 构造方法
	 * 
	 * @param pageIndex
	 *            页码
	 * @param pageSize
	 *            每页记录数
	 */
	public Pagination(final int pageIndex, final int pageSize) {
		this.setPageIndex(pageIndex);
		this.setPageSize(pageSize);
	}
	
	public static Pagination createPaginationWithStartAndLimit(int start,int limit){
		if(limit == 0)
			limit = DEFAULT_PAGE_SIZE;
		int pageIndex = start / limit;
		if(start%limit != 0)
			pageIndex ++;
		return new Pagination(pageIndex++,limit);
	}

	/**
	 * 获取 起始记录位置
	 */
	public int getFirstResult() {
		return (this.pageIndex - 1) * this.pageSize;
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

	/**
	 * 获取 页码
	 */
	public int getPageIndex() {
		return this.pageIndex;
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
	 * @param pageIndex
	 *            页码
	 */
	public void setPageIndex(final int pageNo) {
		if (pageNo < Pagination.MIX_PAGE_NO)
			this.pageIndex = Pagination.MIX_PAGE_NO;
		else
			this.pageIndex = pageNo;
	}

	/**
	 * 设置 每页记录数
	 * 
	 * @param pageSize
	 *            每页记录数
	 */
	public void setPageSize(final int pageSize) {
		if (pageSize < Pagination.MIX_PAGE_SIZE)
			this.pageSize = Pagination.DEFAULT_PAGE_SIZE;
		else
			this.pageSize = pageSize;
	}
}
