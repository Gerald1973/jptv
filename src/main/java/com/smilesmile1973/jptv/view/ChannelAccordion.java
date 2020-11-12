package com.smilesmile1973.jptv.view;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.smilesmile1973.jptv.service.M3UService;

import javafx.scene.control.Accordion;

public class ChannelAccordion extends Accordion {

	public ChannelAccordion() {
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
