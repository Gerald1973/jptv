package com.smilesmile1973.jptv.service;

import java.awt.AWTException;
import java.awt.Robot;

import org.slf4j.Logger;

import com.smilesmile1973.jptv.Constants;

import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.scene.input.KeyCode;
import javafx.util.Duration;

public class AwakeRobotService extends ScheduledService<Void> {

	private static final Logger LOG = org.slf4j.LoggerFactory.getLogger(AwakeRobotService.class);

	private static AwakeRobotService instance = null;

	public static AwakeRobotService getInstance() {
		if (instance == null) {
			try {
				instance = new AwakeRobotService();
			} catch (AWTException e) {
				LOG.error("Robot not created.", e);
			}
		}
		return instance;
	}

	private final Robot robot;

	private AwakeRobotService() throws AWTException {
		robot = new Robot();
		Duration duration = new Duration(Constants.ROBOT_ACTION_INTERVAL_MS);
		setDelay(duration);
		setPeriod(duration);
		start();
		LOG.info("AwakeRobotService instanciated.");
	}

	@Override
	protected Task<Void> createTask() {
		return new Task<Void>() {

			@Override
			protected Void call() throws Exception {
				robot.keyPress(KeyCode.F24.getCode());
				LOG.info("Windows wake up with F24");
				return null;
			}
		};
	}

}
