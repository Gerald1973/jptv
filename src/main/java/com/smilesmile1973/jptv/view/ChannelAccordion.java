package com.smilesmile1973.jptv.view;

import java.util.Set;

import com.smilesmile1973.jptv.service.M3UService;

import javafx.scene.control.Accordion;

public class ChannelAccordion extends Accordion {

	public ChannelAccordion() {
		Set<String> keys = M3UService.getInstance().getChannels().keySet();
		for (String string : keys) {
			GroupTitlePane groupTitlePane = new GroupTitlePane(string);
			getPanes().add(groupTitlePane);
		}
	}
}
