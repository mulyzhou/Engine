package com.flying.view.webservice;

import java.util.List;
import java.util.Map;


public class XmlResponseType implements IResponseType {
	@Override
	public Object handler(String type, Object obj) {
		return null;
	}
	
	@Override
	public Object handler(Object obj) {
		return null;
	}

	@Override
	public Object handlerMap(Map map) {
		return null;
	}

	@Override
	public Object handlerList(List list) {
		return null;
	}
}
