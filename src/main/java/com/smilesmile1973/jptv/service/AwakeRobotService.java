package com.smilesmile1973.jptv.service;

import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;

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
			} catch (AWTException e) {
				LOG.error("Robot not created.", e);
			}
		}
		return instance;
	}

	int mousePosX;

	int mousePosY;

	boolean autoInc;

	private final Robot robot;

	private AwakeRobotService() throws AWTException {
		robot = new Robot();
		Point point = MouseInfo.getPointerInfo().getLocation();
		mousePosX = point.x;
		mousePosY = point.y;
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
				if (autoInc) {
					mousePosX++;
					mousePosY++;
					autoInc = false;
				} else {
					mousePosX--;
					mousePosY--;
					autoInc = true;
				}
				robot.mouseMove(mousePosX, mousePosY);
				LOG.info("Robot awaker trigered. X: {} Y:{}", mousePosX, mousePosY);
				return null;
			}

		};
	}

}
