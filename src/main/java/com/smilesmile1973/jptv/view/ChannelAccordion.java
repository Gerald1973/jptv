package com.smilesmile1973.jptv.view;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.smilesmile1973.jptv.service.M3UService;

import javafx.scene.control.Accordion;
import javafx.scene.control.TitledPane;

public class ChannelAccordion extends Accordion {

	public ChannelAccordion() {
		TitledPane titlePane = new TitledPane();
		getPanes().addAll(buildGroupTitlePanes());
	}

	public List<GroupTitlePane> buildGroupTitlePanes() {
		List<GroupTitlePane> results = new LinkedList<>();
		Set<String> keys = M3UService.getInstance().getChannels().keySet();
		for (String string : keys) {
			results.add(new GroupTitlePane(string));
		}
		return results;
	}

}
