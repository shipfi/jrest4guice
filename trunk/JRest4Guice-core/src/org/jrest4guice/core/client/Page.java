package org.jrest4guice.core.client;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss</a>
 *
 */
public class Page<T> implements java.io.Serializable {

	private static final long serialVersionUID = 6141397074402785607L;

	/**
	 * 获取任一页第一条数据的位置,从0开始
	 */
	public static int getStartOfPage(final int pageNo, final int pageSize) {
		return (pageNo - 1) * pageSize;
	}

	/** 当前页第一条数据的位置,从0开始 */
	private int start;
	/** 每页记录数 */
	private int pageSize;
	/** 当前页中的数据集 */
	private List<T> result;

	/** 总记录数 */
	private long totalCount;

	/**
	 * 构造方法，只构造空页
	 */
	public Page() {
		this.result = new ArrayList<T>();
	}

	/**
	 * 默认构造方法
	 * 
	 * @param start
	 *            本页第一项数据的起始位置
	 * @param totalSize
	 *            总记录数
	 * @param pageSize
	 *            本页容量
	 * @param result
	 *            本页包含的数据集
	 */
	public Page(final int start, final long totalSize, final int pageSize,
			final List<T> result) {
		this.pageSize = pageSize;
		this.start = start;
		this.totalCount = totalSize;
		this.result = result;
	}

	@SuppressWarnings("unchecked")
	public T[] arrayResult() {
		return (T[]) this.result.toArray();
	}

	/**
	 * 取当前页码,页码从1开始
	 */
	public int getCurrentPageNo() {
		return this.start / this.pageSize + 1;
	}

	/**
	 * 获取 每页记录数
	 */
	public int getPageSize() {
		return this.pageSize;
	}

	/**
	 * 获取 当前页中的数据集
	 */
	public List<T> getResult() {
		return this.result;
	}

	/**
	 * 获取 当前页中的数据集合大小
	 */
	public int getResultSize() {
		return this.result.size();
	}

	/**
	 * 获取 记录开始位置<br>
	 * 相对于数据表的位置，从0开始
	 */
	public int getStart() {
		return this.start;
	}

	/**
	 * 获取 总记录数
	 */
	public long getTotalCount() {
		return this.totalCount;
	}

	/**
	 * 获取 总页数
	 */
	public long getTotalPageCount() {
		if (this.totalCount % this.pageSize == 0) {
			return this.totalCount / this.pageSize;
		} else {
			return this.totalCount / this.pageSize + 1;
		}
	}

	/**
	 * 是否有下一页
	 */
	public boolean hasNextPage() {
		return this.getCurrentPageNo() < this.getTotalPageCount() - 1;
	}

	/**
	 * 是否有上一页
	 */
	public boolean hasPreviousPage() {
		return this.getCurrentPageNo() > 1;
	}

	/**
	 * 获取 当前页中的数据集
	 */
	public Iterator<T> iteratorResult() {
		return this.result.iterator();
	}

	// Setter methods ...

	/**
	 * @param pageSize
	 *            the pageSize to set
	 */
	public void setPageSize(final int pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * @param result
	 *            the result to set
	 */
	public void setResult(final List<T> result) {
		this.result = result;
	}

	/**
	 * @param start
	 *            the start to set
	 */
	public void setStart(final int start) {
		this.start = start;
	}

	/**
	 * @param totalCount
	 *            the totalCount to set
	 */
	public void setTotalCount(final int totalCount) {
		this.totalCount = totalCount;
	}
}