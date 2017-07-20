package com.conning.compents.servlet;

import org.springframework.stereotype.Component;

@Component
public class AdminInvokeTest{
	
	private String testString = "hello world";
	
	public String sayHello(){
		StringBuilder sb = new StringBuilder();
		//do your business
		//service.doSomething();
		
		//return to show
		return sb.append(testString).toString();
	}
}
