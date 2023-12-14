package com.smilesmile1973.jptv.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jna.platform.win32.Kernel32;

public class SystemService {

	private static final Logger LOG = LoggerFactory.getLogger(M3UService.class);

	private static SystemService instance = null;

	private long lastDontSleepCall = -1;

	private SystemService() {
		this.lastDontSleepCall = System.currentTimeMillis();
	};

	public static SystemService getInstance() {
		if (SystemService.instance == null) {
			SystemService.instance = new SystemService();
		}
		return instance;
	}

	public String getOperatingSystem() {
		final String os = System.getProperty("os.name");
		LOG.info("Using System Property: " + os);
		return os;
	}

	public void disableGoToSleep() {
		if (System.currentTimeMillis() - this.lastDontSleepCall > 40000) {
			LOG.info("Calling SetThreadExecutionState ES_SYSTEM_REQUIRED");
			Kernel32.INSTANCE.SetThreadExecutionState(Kernel32.ES_SYSTEM_REQUIRED | Kernel32.ES_CONTINUOUS);
			this.lastDontSleepCall = System.currentTimeMillis();
		}
	}

	public boolean isWindows() {
		return (this.getOperatingSystem().indexOf("win") >= 0);
	}

}
