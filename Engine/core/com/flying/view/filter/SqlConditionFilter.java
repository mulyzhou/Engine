package com.flying.view.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.flying.service.EngineParameter;

/**
 * 
 * <B>描述：</B>条件过滤器<br/>
 * <B>版本：</B>v2.0<br/>
 * <B>创建时间：</B>2012-10-10<br/>
 * <B>版权：</B>flying团队<br/>
 * 
 * @author zdf
 */
public class SqlConditionFilter implements Filter {

    /**
     * Default constructor. 
     */
    public SqlConditionFilter() {
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest)request;
		HttpServletResponse res = (HttpServletResponse)response;
		
		 //从Request获取参数
		 EngineParameter ep = (EngineParameter) req.getAttribute("ep");
		 //修改分页参数
		 int start = ep.getParam("start")==null?0:Integer.parseInt(ep.getParam("start").toString());
		 int limit = ep.getParam("limit")==null?10:Integer.parseInt(ep.getParam("limit").toString());
		 //limit = start+limit;
		 
		 if(ep.getFileDownloadName()== null){
			 /*造成后果，ep.paramMap中，永远有start，limit值;当我们后台使用到了start，limit，却又不需要前台默认值时，需要手动删除start，limit两个属性*/
			 ep.putParam("start", start);
			 ep.putParam("limit", limit);
		 }else{
			 ep.removeParam("start");
			 ep.removeParam("limit");
		 }
		 //排序
		 if(ep.getParam("dir") != null && ep.getParam("sort") != null ){
			 String order = "ORDER BY "+ep.getParam("sort").toString()+" "+ep.getParam("dir").toString();
			 ep.removeParam("dir");
			 ep.removeParam("sort");
			 ep.putParam("order", order);
		 }
		 //修改条件参数
		 String searchField = ep.getParam("searchField")==null?"":ep.getParam("searchField").toString();
		 String searchValue = ep.getParam("searchValue")==null?"":ep.getParam("searchValue").toString();
		 String filter = "";
		 
		 StringBuilder filterBuilder = new StringBuilder();
		 
		 if(searchField.indexOf("-") > 0){
			 String sf[] = searchField.split("-");
			 filterBuilder.append("(");
			 for(int m = 0; m < sf.length; m++){
				 filterBuilder.append(sf[m]+ " LIKE '%"+searchValue+"%' ");
				 if(m != sf.length-1){
					 filterBuilder.append(" OR ");
				 }
			 }
			 filterBuilder.append(")");
		 }else if(!"".equals(searchField) && !"".equals(searchValue)){
			 filterBuilder.append("(" + searchField+" LIKE '%"+searchValue+"%')");
			 //filterBuilder.append(" OR " + "GETPINYIN(" + searchField +",1) LIKE '%"+searchValue+"%')");
		 }
		 
		 String f = ep.getParam("filter")==null?"":ep.getParam("filter").toString();//原本带过滤条件
		 
		 if(filterBuilder.length() >0){
			 filter = filterBuilder.toString();
		 }
		 
		 if(filter.length() >0 && !"".equals(f)){
			 filter += " AND " + f;
		 }else if(filter.length() == 0 && !"".equals(f)){
			 filter = f;
		 }
		 
		 ep.removeParam("searchField");//删除条件
		 ep.removeParam("searchValue");
		 ep.putParam("filter", filter);//添加条件
		 
		 //过滤器向下传递
		chain.doFilter(req, res);
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
	}
}
