package com.smilesmile1973.jptv.service;

import java.awt.AWTException;

import org.slf4j.Logger;

import com.smilesmile1973.jptv.Constants;

import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.util.Duration;

public class AwakeRobotService extends ScheduledService<Void> {

	private static final Logger LOG = org.slf4j.LoggerFactory.getLogger(AwakeRobotService.class);

	private static AwakeRobotService instance = null;

	public static AwakeRobotService getInstance() {
		if (instance == null) {
			try {
				instance = new AwakeRobotService();
			} catch (final AWTException e) {
				e.printStackTrace();
			}
		}
		return instance;
	}

	private AwakeRobotService() throws AWTException {
		final Duration duration = new Duration(Constants.ROBOT_ACTION_INTERVAL_MS);
		this.setDelay(duration);
		this.setPeriod(duration);
		this.start();
	}

	@Override
	protected Task<Void> createTask() {
		return new Task<Void>() {

			@Override
			protected Void call() throws Exception {
				SystemService.getInstance().disableGoToSleep();
				return null;
			}
		};
	}

}
