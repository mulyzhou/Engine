package com.flying.test;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.flying.test.FlyingJunitSuite;

public class SuiteTest {
	public static Test suite(){
		TestSuite sysTest = (TestSuite) FlyingJunitSuite.suite();
		sysTest.setName("关口电能项目自动化测试集");
		return sysTest;
	}
}
