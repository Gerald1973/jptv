package com.smilesmile1973.jptv.view;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.smilesmile1973.jptv.Utils;
import com.smilesmile1973.jptv.event.EventChannel;
import com.smilesmile1973.jptv.pojo.Channel;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

public class ChannelView extends GridPane {

	private static final double LOGO_PANE_WIDTH = 40;
	private static final double LOGO_PANE_HEIGHT = 40;
	private static final double LOGO_IMAGE_WIDTH = 30;
	private static final double LOGO_IMAGE_HEIGHT = 30;

	private static final Logger LOG = LoggerFactory.getLogger(ChannelView.class);

	private ImageView imageView = new ImageView();

	private Channel channel;

	public ChannelView(Channel channel) {
		getStyleClass().add("channelView");
		LOG.debug(channel.getTvLogo());
		this.channel = channel;
		// Logo
		StackPane pane = new StackPane();
		pane.setPrefSize(LOGO_PANE_WIDTH, LOGO_PANE_HEIGHT);
		pane.getChildren().add(imageView);
		loadImage(channel);
		// Channel name
		String txtLabel = channel.getTvgName().isBlank() ? "UNK" : channel.getTvgName();
		Label label = new Label(txtLabel);
		label.setOnMouseClicked(event -> {
			ChannelView channelView = ((ChannelView) ((Node) event.getSource()).getParent());
			LOG.debug("Change to channel {}", channelView.getChannel().getChannelURL());
			Utils.getEventBus().post(new EventChannel(channel));
		});

		this.add(pane, 0, 0);
		this.add(label, 1, 0);

		this.setOnMouseClicked(event -> {
			ChannelView channelView = (ChannelView) event.getSource();
			LOG.debug("Change to channel {}", channelView.getChannel().getChannelURL());
			Utils.getEventBus().post(new EventChannel(channel));
		});
	}

	private Channel getChannel() {
		return this.channel;
	}

	public void loadImage(Channel channel) {
		if (channel.getTvLogo() != null && !channel.getTvLogo().isBlank()) {
			Task<Void> task = new Task<Void>() {
				@Override
				protected Void call() throws Exception {
					Image image = new Image(channel.getTvLogo(), LOGO_IMAGE_WIDTH, LOGO_IMAGE_HEIGHT, true, true);
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							if (image.getHeight() > 0 && image.getWidth() > 0) {
								imageView.setX((LOGO_PANE_WIDTH - image.getWidth()) / 2);
								imageView.setY((LOGO_PANE_HEIGHT - image.getHeight()) / 2);
								imageView.setImage(image);
							}
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
