package com.smilesmile1973.jptv.view;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.smilesmile1973.jptv.pojo.Channel;
import com.smilesmile1973.jptv.service.M3UService;

import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;

public class GroupTitlePane extends TitledPane {

	private final String title;

	private static final Logger LOG = LoggerFactory.getLogger(GroupTitlePane.class);

	private VBox vbox = new VBox();

	public GroupTitlePane(String title) {
		this.title = title;
		this.setText(title);
		this.setContent(getVbox(title));
		this.setOnMouseClicked(event -> {
			LOG.debug("Expanded : {}", this.isExpanded());
			this.fillVbox(title);
			this.setContent(getVbox(title));
			if (!this.isExpanded()) {
				if (this.getContent() == null) {
					this.setContent(getVbox(title));
				}
				this.setExpanded(false);
			} else {
				this.setExpanded(true);
			}
		});
	}

	public Node getVbox(String group) {
		ScrollPane scrollPane = new ScrollPane();
		scrollPane.setContent(vbox);
		return scrollPane;
	}

	public Node fillVbox(String group) {
		List<Channel> channels = M3UService.getInstance().sortGroup(group);
		for (Channel channel : channels) {
			ChannelView channelView = new ChannelView(channel);
			this.vbox.getChildren().add(channelView);
		}
		return this.vbox;
	}

}
