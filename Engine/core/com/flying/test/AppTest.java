package com.flying.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.flying.init.EngineInit;
import com.flying.init.Item;
import com.flying.util.GenerateUtil;

public class AppTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		EngineInit.appStart();
		Map map = new HashMap();
		map.put("BMC", "T_SYS_ROLE");
		map.put("BZS", "角色信息");
		List<Map> listMap = new ArrayList<Map>();
		listMap.add(map);
		
		Item item = new Item();
		item.setAlias("角色信息");
		item.setName("T_SYS_ROLE");
		try {
			GenerateUtil.generateExtPage(item, "D:\\test\\");
			//GenerateUtil.generateTablename(listMap, "D:\\test\\");
			//GenerateUtil.generateModuleTablename("D:\\test\\");
			//GenerateUtil.generateTablename(listMap, "D:\\test\\");
			//GenerateUtil.generateModuleJunitCode(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

