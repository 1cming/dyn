package com.conning.compents.servlet;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.conning.compents.util.IdGenerator;

public class ComponentMgr {
	private static List<ComponentDirectory> list = new ArrayList();

	public static ComponentDirectory addComponentDirectory(ComponentDirectory cd, ComponentDirectory pcd) {
		List<ComponentDirectory> existList = queryComponentDirectory(Integer.valueOf(cd.getLevel()), cd.getPackageName());
		if (existList == null) {
			cd.setId(IdGenerator.getInstance().generatorId());
			list.add(cd);
			if (pcd != null)
				pcd.addChildren(cd);
		} else {
			boolean exist = false;
			for (ComponentDirectory ecd : existList) {
				if (ecd.equals(cd)) {
					cd = ecd;
					exist = true;
					break;
				}
			}
			if (!exist) {
				cd.setId(IdGenerator.getInstance().generatorId());
				list.add(cd);
				if (pcd != null)
					pcd.addChildren(cd);
			}
		}
		return cd;
	}

	public static ComponentDirectory queryComponentById(String id) {
		ComponentDirectory rcd = null;
		for (ComponentDirectory cd : list) {
			if (cd.getId().equals(id)) {
				rcd = cd;
			}
		}
		return rcd;
	}

	public static List<ComponentDirectory> queryComponentDirectory(Integer level, String packageName) {
		List queryList = null;
		for (ComponentDirectory cd : list) {
			if (level != null) {
				if (!StringUtils.isEmpty(packageName)) {
					if ((cd.getLevel() == level.intValue()) && (cd.getPackageName().contains(packageName))) {
						if (queryList == null)
							queryList = new ArrayList();
						queryList.add(cd);
					}

				} else if (cd.getLevel() == level.intValue()) {
					if (queryList == null)
						queryList = new ArrayList();
					queryList.add(cd);
				}

			} else if (cd.getPackageName().contains(packageName)) {
				if (queryList == null)
					queryList = new ArrayList();
				queryList.add(cd);
			}
		}

		return queryList;
	}

	public static List<ComponentDirectory> queryComponentDirectory(String packageName) {
		if (StringUtils.isEmpty(packageName)) {
			return list;
		}
		return queryComponentDirectory(null, packageName);
	}
}