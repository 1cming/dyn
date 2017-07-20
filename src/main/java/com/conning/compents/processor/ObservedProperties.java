package com.conning.compents.processor;

import java.lang.reflect.Method;
import java.util.Observable;
import java.util.Observer;
import java.util.Properties;

public class ObservedProperties extends Properties {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3196148649846046768L;
	private Observable observale = new Observable();

	public Observable getObservale() {
		return this.observale;
	}

	public void addObserver(Observer o) {
		this.observale.addObserver(o);
	}

	public void deleteObserver(Observer o) {
		this.observale.deleteObserver(o);
	}

	public void notifyObservers(Object arg) throws Exception {
		Method method = Observable.class.getDeclaredMethod("setChanged", null);
		Boolean accessiable = Boolean.valueOf(method.isAccessible());
		method.setAccessible(true);
		method.invoke(this.observale, null);
		this.observale.notifyObservers(arg);
		method.setAccessible(accessiable.booleanValue());
	}
}