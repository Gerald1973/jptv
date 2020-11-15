package com.smilesmile1973.jptv.event;

public class ChannelListCreatedEvent {
	
	private boolean created = false;
	
	public boolean isCreated() {
		return created;
	}

	public ChannelListCreatedEvent(boolean created){
		created = true;
	}
}
