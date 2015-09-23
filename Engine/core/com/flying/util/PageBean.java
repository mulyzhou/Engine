package com.flying.util;
/**
 * 
 * <B>描述：</B>分页工具类<br/>
 * <B>版本：</B>v2.0<br/>
 * <B>创建时间：</B>2012-10-10<br/>
 * <B>版权：</B>flying团队<br/>
 * 
 * @author zdf
 *
 */
public class PageBean {
	
	private int currentpage =1; //当前页   
	private int resultcount; //总记录数   
	private int pagesize; //每页显示数   
	private int pagecount ; //总页数 
	
	public int getCurrentpage() {
		return currentpage;
	}
	public void setCurrentpage(int currentpage) {
		this.currentpage = currentpage;
	}
	public int getResultcount() {
		return resultcount;
	}
	public void setResultcount(int resultcount) {
		this.resultcount = resultcount;
	}
	public int getPagesize() {
		return pagesize;
	}
	public void setPagesize(int pagesize) {
		this.pagesize = pagesize;
	}
	public int getPagecount() {
		return pagecount;
	}
	public void setPagecount() {
		this.pagecount=(resultcount%pagesize==0 ? resultcount/pagesize : resultcount/pagesize + 1);   
	}
}