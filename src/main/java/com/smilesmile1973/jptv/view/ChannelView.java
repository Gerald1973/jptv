package com.smilesmile1973.jptv.view;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.smilesmile1973.jptv.pojo.Channel;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

public class ChannelView extends HBox {

	private static final Logger LOG = LoggerFactory.getLogger(ChannelView.class);

	private ImageView imageView;

	private Label label;

	public ChannelView(Channel channel) {
		this.imageView = new ImageView();
		LOG.debug(channel.getTvLogo());
		label = new Label(channel.getTvgName());
		Pane pane = new Pane();
		pane.getStyleClass().add("channelViewPane");
		this.getChildren().add(pane);
		pane.getChildren().add(this.imageView);
		this.getChildren().add(label);
		// loadImage(channel);
		if (channel.getTvLogo() != null && !channel.getTvLogo().isBlank()) {
			Image image = new Image(channel.getTvLogo(), 75, 75, true, true, true);
			imageView.setImage(image);
			imageView.minWidth(75);
			imageView.minHeight(75);
			imageView.maxWidth(75);
			imageView.maxHeight(75);
		}
	}

	public void loadImage(Channel channel) {
		if (channel.getTvLogo() != null && !channel.getTvLogo().isBlank()) {
			Task<Void> task = new Task<Void>() {
				@Override
				protected Void call() throws Exception {
					Image image = new Image(channel.getTvLogo());
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							imageView.setImage(image);
						}
					});
					return null;
				}
			};
			Thread th = new Thread(task);
			th.start();
		}
	}

}
