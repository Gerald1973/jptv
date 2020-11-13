package com.smilesmile1973.jptv.view;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.smilesmile1973.jptv.Utils;
import com.smilesmile1973.jptv.event.EventChannel;
import com.smilesmile1973.jptv.pojo.Channel;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

public class ChannelView extends GridPane {

	private static final double LOGO_PANE_WIDTH = 50;
	private static final double LOGO_PANE_HEIGHT = 50;
	private static final double LOGO_IMAGE_WIDTH = 30;
	private static final double LOGO_SIZE = 30;
	private static final double LABEL_MAX_WIDTH = 190;

	private static final Logger LOG = LoggerFactory.getLogger(ChannelView.class);

	private Channel channel;

	public ChannelView(Channel channel) {
		getStyleClass().add("channelView");
		LOG.debug(channel.getTvLogo());
		this.channel = channel;
		// Logo
		StackPane pane = new StackPane();
		pane.setPrefSize(LOGO_PANE_WIDTH, LOGO_PANE_HEIGHT);
		pane.setMaxSize(LOGO_PANE_WIDTH, LOGO_PANE_HEIGHT);
		ImageView imageView = new ImageView();
		this.loadImage(channel, imageView);
		pane.getChildren().add(imageView);
		// Channel name
		Label label = new Label(buildTextLabel());
		label.getStyleClass().add("labelChannelView");
		label.setPrefWidth(LABEL_MAX_WIDTH);
		label.setMaxWidth(LABEL_MAX_WIDTH);
		label.setMinWidth(LABEL_MAX_WIDTH);
		label.setWrapText(true);
		label.setOnMouseClicked(event -> {
			ChannelView channelView = ((ChannelView) ((Node) event.getSource()).getParent());
			LOG.debug("Change to channel {}", channelView.getChannel().getChannelURL());
			Utils.getEventBus().post(new EventChannel(channel));
		});

		ColumnConstraints col0 = new ColumnConstraints();
		ColumnConstraints col1 = new ColumnConstraints();
		col0.setPrefWidth(LOGO_IMAGE_WIDTH);
		col1.setPrefWidth(LABEL_MAX_WIDTH);
		col0.setMaxWidth(LOGO_IMAGE_WIDTH);
		col1.setMaxWidth(LABEL_MAX_WIDTH);
		this.getColumnConstraints().addAll(col0, col1);

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

	private String buildTextLabel() {
		String result = channel.getTvgName().isBlank() ? "UNK" : channel.getTvgName();
		if (!this.channel.getTvgName().isBlank()) {
			result = channel.getTvgName();
		} else if (!this.channel.getGroupTitle2().isBlank()) {
			result = this.channel.getGroupTitle2();
		} else {
			result = "No name";
		}
		return result;
	}

	private ImageView buildImageView(Channel channel) {
		ImageView result = new ImageView();
		if (channel.getTvLogo() != null && !channel.getTvLogo().isBlank()) {
			Image image = new Image(channel.getTvLogo(), true);
			result.setImage(image);
			if (image.getHeight() > image.getWidth()) {
				result.setFitHeight(LOGO_SIZE);
			} else {
				result.setFitWidth(LOGO_SIZE);
			}
			result.setPreserveRatio(true);
			result.setSmooth(true);
		}
		return result;
	}

	private void loadImage(Channel channel, ImageView imageView) {
		if (channel.getTvLogo() != null && !channel.getTvLogo().isBlank()) {
			Task<Void> task = new Task<Void>() {
				@Override
				protected Void call() throws Exception {
					Image image = new Image(channel.getTvLogo(), LOGO_SIZE, LOGO_SIZE, true, true);
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
