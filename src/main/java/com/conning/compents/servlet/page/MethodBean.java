package com.conning.compents.servlet.page;

public class MethodBean {
	private String name;
	private String returnType;
	private String declaringClass;

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getReturnType() {
		return this.returnType;
	}

	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}

	public String getDeclaringClass() {
		return this.declaringClass;
	}

	public void setDeclaringClass(String declaringClass) {
		this.declaringClass = declaringClass;
	}
}