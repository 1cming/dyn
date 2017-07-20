package com.conning.compents.monitor;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Queue;
import java.util.Set;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.reflect.Reflection;

import com.conning.compents.annotation.MonitorBegin;
import com.conning.compents.annotation.MonitorEnd;
import com.conning.compents.annotation.MonitorMethod;


@SuppressWarnings("restriction")
public class MonitorableCodeBean {
	private static ThreadLocal<Map<String, Properties>> monitorExcuteData = new ThreadLocal();

	private static Logger logger = LoggerFactory.getLogger(MonitorableCodeBean.class);

	private static Map<String, Queue<Properties>> monitorData = new HashMap();

	private static Map<String, Set<String>> monitorDataProp = new HashMap();

	private boolean monitorFlag = true;

	private int monitorDataSize = 100;

	public void monitorMethod(ProceedingJoinPoint jp, MonitorMethod monitor) {
		try {
			beginMonitor(monitor.value());
			jp.proceed();
			endMonitor(monitor.value());
		} catch (Throwable throwable) {
			logger.error("monitorMethod error", throwable);
			monitorExcuteData.remove();
		}
	}

	public void monitorFlowBegin(JoinPoint jp, MonitorBegin monitorBegin) {
		beginMonitor(monitorBegin.value());
	}

	public void monitorFlowBegin(JoinPoint jp, MonitorEnd monitorEnd) {
		endMonitor(monitorEnd.value());
	}

	public void beginMonitor(String monitorkey) {
		if (this.monitorFlag) {
			Map map = (Map) monitorExcuteData.get();
			if (map == null) {
				map = new HashMap();
				monitorExcuteData.set(map);
			}
			Properties prop = (Properties) map.get(monitorkey);
			if (prop != null) {
				String callerClass = prop.getProperty("beginMonitorClass");
				logger.warn("duplicate invoke beginMonitor(),the first caller class : " + callerClass);
				return;
			}
			prop = new Properties();
			Class callerClass = Reflection.getCallerClass();
			prop.setProperty("beginMonitorClass", callerClass.getName());
			prop.put("beginTime", new Date());
			if (monitorDataProp.get(monitorkey) == null) {
				Set set = new HashSet();
				set.add("beginMonitorClass");
				set.add("beginTime");
				set.add("executeTime");
				set.add("monitorName");
				set.add("endTime");
				set.add("endMonitorClass");
				monitorDataProp.put(monitorkey, set);
			}

			map.put(monitorkey, prop);
		}
	}

	public void endMonitor(String monitorKey) {
		endMonitor(monitorKey, null);
	}

	public void putMonitorData(String monitorKey, Properties extraProp) {
		if (this.monitorFlag) {
			Map map = (Map) monitorExcuteData.get();
			if ((map != null) && (map.get(monitorKey) != null)) {
				((Properties) map.get(monitorKey)).putAll(extraProp);
				Set set = (Set) monitorDataProp.get(monitorKey);
				if (set != null)
					set.addAll(extraProp.keySet());
			}
		}
	}

	public void putMonitorData(String monitorKey, String key, Object value) {
		if (this.monitorFlag) {
			Map map = (Map) monitorExcuteData.get();
			if ((map != null) && (map.get(monitorKey) != null)) {
				((Properties) map.get(monitorKey)).put(key, value);
				Set set = (Set) monitorDataProp.get(monitorKey);
				if ((set != null) && (!set.contains(key)))
					set.add(key);
			}
		}
	}

	public void endMonitor(String monitorKey, Properties extraProp) {
		if (this.monitorFlag) {
			Map map = (Map) monitorExcuteData.get();
			try {
				if ((map == null) || (map.get(monitorKey) == null)) {
					logger.warn("not invoke beginMonitor,or duplicate invoke endMonitor,please check");
				} else {
					Properties prop = (Properties) map.get(monitorKey);
					Date beginTime = (Date) prop.get("beginTime");
					Date endDate = new Date();
					Long executeTime = Long.valueOf(endDate.getTime() - beginTime.getTime());
					Queue allMonitorDatas = (Queue) monitorData.get(monitorKey);
					if (allMonitorDatas == null) {
						allMonitorDatas = new LinkedList();
						monitorData.put(monitorKey, allMonitorDatas);
					}
					Class callerClass = Reflection.getCallerClass();
					prop.put("executeTime", executeTime);
					prop.put("monitorName", monitorKey);
					prop.put("endTime", endDate);
					prop.put("endMonitorClass", callerClass.getName());
					if (extraProp != null)
						prop.putAll(extraProp);
					if (allMonitorDatas.size() < this.monitorDataSize) {
						allMonitorDatas.add(prop);
					} else {
						allMonitorDatas.poll();
						allMonitorDatas.add(prop);
					}

					if (map.size() > 1)
						map.remove(monitorKey);
					else if (map.size() == 1)
						monitorExcuteData.remove();
				}
			} finally {
				if (map.size() > 1)
					map.remove(monitorKey);
				else if (map.size() == 1)
					monitorExcuteData.remove();
			}
		}
	}

	public synchronized void clearData() {
		monitorData.clear();
	}

	public Map<String, Set<String>> getMonitorDataProp() {
		return monitorDataProp;
	}

	public Map<String, List<Properties>> getMonitorData() {
		Map datas = new HashMap();
		for (Map.Entry entry : monitorData.entrySet()) {
			List<Properties> list = (List) entry.getValue();
			List newList = new ArrayList();
			for (Properties prop : list) {
				Properties newProp = new Properties();
				for (Map.Entry entry2 : prop.entrySet()) {
					newProp.put(entry2.getKey() == null ? "" : entry2.getKey().toString(),
							entry2.getValue() == null ? "" : entry2.getValue().toString());
				}
				newList.add(newProp);
			}
			datas.put(entry.getKey(), newList);
		}
		return datas;
	}

	public boolean isMonitorFlag() {
		return this.monitorFlag;
	}

	public int getMonitorDataSize() {
		return this.monitorDataSize;
	}

	public void setMonitorFlag(boolean monitorFlag) {
		this.monitorFlag = monitorFlag;
	}

	public void setMonitorDataSize(int monitorDataSize) {
		this.monitorDataSize = monitorDataSize;
	}
}