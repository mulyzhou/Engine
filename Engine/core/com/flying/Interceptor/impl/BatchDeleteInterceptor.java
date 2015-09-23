package com.flying.Interceptor.impl;

import com.flying.Interceptor.AbstractInterceptor;
import com.flying.service.Engine;
import com.flying.service.EngineParameter;

public class BatchDeleteInterceptor extends AbstractInterceptor {

	@Override
	public void before(EngineParameter ep) throws Exception {
		String sqlid = ep.getParam("SQLID") == null ?"":ep.getParam("SQLID").toString();
		String pk = ep.getParam("PK") == null ?"":ep.getParam("PK").toString();
		String pkValues = ep.getParam("PKVALUES") == null ?"":ep.getParam("PKVALUES").toString();
		
		String[] values = pkValues.split(",");
		
		EngineParameter selfEp = null;
		for(int i =0;i<values.length;i++){
			selfEp = new EngineParameter(sqlid);
			selfEp.putParam(pk, values[i]);
			
			Engine.execute(selfEp);
		}
	}

	@Override
	public void after(EngineParameter ep) throws Exception {
		
	}

}
