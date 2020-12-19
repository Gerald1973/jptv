package com.smilesmile1973.jptv.event;

import uk.co.caprica.vlcj.media.MediaStatistics;

public class EventMediaStatistics {

	private MediaStatistics mediaStatistics;

	public MediaStatistics getMediaStatistics() {
		return mediaStatistics;
	}

	public EventMediaStatistics(MediaStatistics mediaStatistics) {
		super();
		this.mediaStatistics = mediaStatistics;
	}
}
