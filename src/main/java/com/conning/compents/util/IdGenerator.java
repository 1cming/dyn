package com.conning.compents.util;

import java.util.concurrent.atomic.AtomicLong;

public class IdGenerator {
	private static IdGenerator instance = null;
	private static AtomicLong atomicLong = null;
	private static Object lock = new Object();

	private IdGenerator() {
		atomicLong = new AtomicLong();
		atomicLong.set(10000L);
	}

	public static IdGenerator getInstance() {
		if (instance == null) {
			synchronized (lock) {
				if (instance == null) {
					instance = new IdGenerator();
				}
			}
		}
		return instance;
	}

	public String generatorId() {
		return atomicLong.getAndIncrement() + "";
	}
}
