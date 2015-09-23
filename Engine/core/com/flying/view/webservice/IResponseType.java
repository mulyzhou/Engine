package com.flying.view.webservice;

import java.util.List;
import java.util.Map;

public interface IResponseType {
	
	public Object handler(String type,Object obj);
	public Object handler(Object obj);
	public Object handlerMap(Map map);
	public Object handlerList(List list);
}
