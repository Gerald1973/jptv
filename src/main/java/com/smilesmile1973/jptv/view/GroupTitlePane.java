package com.smilesmile1973.jptv.view;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.smilesmile1973.jptv.pojo.Channel;
import com.smilesmile1973.jptv.service.M3UService;

import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class GroupTitlePane extends TitledPane {

	private static final Logger LOG = LoggerFactory.getLogger(GroupTitlePane.class);

	private VBox vbox = new VBox();

	public GroupTitlePane(String title) {
		this.setText(title);
		Pane stackPane = new StackPane();
		this.setContent(stackPane);

		stackPane.getChildren().add(getVbox(title));
		LOG.debug("Expanded : {}", this.isExpanded());
		this.setOnMouseClicked(event -> {
			this.setContent(null);
			this.setContent(buildGridPane(title));
			LOG.debug("Expanded : {}", this.isExpanded());
		});
	}

	private Node buildFlowPane(String group) {
		FlowPane flowPane = new FlowPane();
		flowPane.setOrientation(Orientation.VERTICAL);
		List<Channel> channels = M3UService.getInstance().sortGroup(group);
		for (Channel channel : channels) {
			ChannelView channelView = new ChannelView(channel);
			flowPane.getChildren().add(channelView);
		}
		return flowPane;
	}

	private Node buildGridPane(String group) {
		GridPane flowPane = new GridPane();
		List<Channel> channels = M3UService.getInstance().sortGroup(group);
		for (int i = 0; i < channels.size(); i++) {
			Node node = new ChannelView(channels.get(i));
			flowPane.add(node, 0, i);
		}
		return flowPane;
	}

	private Node getVbox(String group) {
		ScrollPane scrollPane = new ScrollPane();
		scrollPane.setContent(vbox);
		return scrollPane;
	}

	private Node fillVbox(String group) {
		List<Channel> channels = M3UService.getInstance().sortGroup(group);
		for (Channel channel : channels) {
			ChannelView channelView = new ChannelView(channel);
			this.vbox.getChildren().add(channelView);
		}
		return this.vbox;
	}

}
