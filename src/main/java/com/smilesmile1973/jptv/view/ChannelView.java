package com.smilesmile1973.jptv.view;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.smilesmile1973.jptv.pojo.Channel;

import javafx.application.Platform;
import javafx.concurrent.Task;
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

	public ChannelView(Channel channel) {
		LOG.debug(channel.getTvLogo());
		// Logo
		StackPane pane = new StackPane();
		pane.getStyleClass().add("imageViewPane");
		pane.setPrefSize(LOGO_PANE_WIDTH, LOGO_PANE_HEIGHT);
		pane.getChildren().add(imageView);
		loadImage(channel);
		// Channel name
		String txtLabel = channel.getTvgName().isBlank() ? "UNK" : channel.getTvgName();
		Label label = new Label(txtLabel);

		this.add(pane, 0, 0);
		this.add(label, 1, 0);
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
							LOG.debug("Image height : {}", image.getHeight());
							LOG.debug("Image width  : {}", image.getWidth());
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
