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

	public GroupTitlePane(String title) {
		this.title = title;
		this.setText(title);
		this.setContent(getVbox(title));
		this.setOnMouseClicked(event -> {
			LOG.debug("Expanded : {}", this.isExpanded());
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
		VBox vbox = new VBox();
		List<Channel> channels = M3UService.getInstance().sortGroup(group);

		for (Channel channel : channels) {
			vbox.getChildren().add(new ChannelView(channel));
		}
		scrollPane.setContent(vbox);
		return scrollPane;
	}

}
