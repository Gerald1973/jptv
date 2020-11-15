package com.smilesmile1973.jptv.event;

public class RendererCreatedEvent {
	
	private boolean created = false;
	
	public RendererCreatedEvent(boolean created) {
		this.created = created;
	}

	public boolean isCreated() {
		return created;
	}

}
