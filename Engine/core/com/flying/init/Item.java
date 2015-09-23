package com.flying.init;

import java.util.HashMap;
import java.util.Map;
/**
 * 
 * <B>描述：</B>tablename的Item对应的实体对象<br/>
 * <B>版本：</B>v2.0<br/>
 * <B>创建时间：</B>2012-10-10<br/>
 * <B>版权：</B>flying团队<br/>
 * 
 * @author zdf
 *
 */
public class Item {
	//名称
	private String name;
	//别名
	private String alias;
	//是否加载
	private Boolean load;
	//操作列表
	private Map<String,Operation> mapOperation = new HashMap<String,Operation>();
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public Boolean getLoad() {
		return load;
	}
	public void setLoad(Boolean load) {
		this.load = load;
	}
	public Map<String, Operation> getMapOperation() {
		return mapOperation;
	}
	public void setMapOperation(Map<String, Operation> mapOperation) {
		this.mapOperation = mapOperation;
	}
	public void setOperation(Operation operation) {
		this.mapOperation.put(operation.getSqlid(), operation);
	}
}
