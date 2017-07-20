package com.conning.compents.servlet.page;

public class FieldBean {
	private String name;
	private String value;
	private String type;
	private boolean change;

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isChange() {
		return this.change;
	}

	public void setChange(boolean change) {
		this.change = change;
	}
}