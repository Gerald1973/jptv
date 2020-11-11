package com.smilesmile1973.jptv.view;

import com.smilesmile1973.jptv.pojo.Channel;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class ChannelView extends HBox {

	private final Channel channel;

	private final Label label;

	public ChannelView(Channel channel) {
		this.channel = channel;
		label = new Label(channel.getTvgName());
		// this.getChildren().add(new ImageView(channel.getTvLogo()));
		this.getChildren().add(label);
	}

}
