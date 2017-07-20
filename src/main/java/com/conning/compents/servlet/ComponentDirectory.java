package com.conning.compents.servlet;

import java.util.ArrayList;
import java.util.List;

import com.conning.compents.annotation.Compareable;

@Compareable(value = "children", adapter = Object.class)
public class ComponentDirectory {
	private String id;
	private ComponentDirectory parent;
	private List<ComponentDirectory> children = new ArrayList();
	private int level;
	private String packageName;
	private boolean leaf;

	public void addChildren(ComponentDirectory cd) {
		this.children.add(cd);
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getLevel() {
		return this.level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getPackageName() {
		return this.packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public boolean isLeaf() {
		return this.leaf;
	}

	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}

	public ComponentDirectory getParent() {
		return this.parent;
	}

	public void setParent(ComponentDirectory parent) {
		this.parent = parent;
	}

	public List<ComponentDirectory> getChildren() {
		return this.children;
	}

	public void setChildren(List<ComponentDirectory> children) {
		this.children = children;
	}

	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (this == obj) {
			return true;
		}

		if ((obj instanceof ComponentDirectory)) {
			ComponentDirectory cd = (ComponentDirectory) obj;
			if (((cd.getParent() == getParent()) || (cd.getParent().hashCode() == getParent().hashCode()))
					&& (cd.getLevel() == getLevel()) && (cd.getPackageName().equals(getPackageName()))) {
				return true;
			}
		}

		return false;
	}

	public int hashCode() {
		StringBuffer sb = new StringBuffer();
		if (getParent() != null)
			sb.append(getParent().hashCode());
		sb.append(this.level).append(this.packageName.hashCode());

		Double value = Double.valueOf(Double.parseDouble(sb.toString().replaceAll("-", "")));
		return value.hashCode();
	}

	public static void main(String[] args) {
	}
}