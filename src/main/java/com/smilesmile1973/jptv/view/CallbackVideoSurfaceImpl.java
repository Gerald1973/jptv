package com.smilesmile1973.jptv.view;

import uk.co.caprica.vlcj.player.embedded.videosurface.CallbackVideoSurface;
import uk.co.caprica.vlcj.player.embedded.videosurface.VideoSurfaceAdapter;
import uk.co.caprica.vlcj.player.embedded.videosurface.callback.BufferFormatCallback;
import uk.co.caprica.vlcj.player.embedded.videosurface.callback.RenderCallback;

public class CallbackVideoSurfaceImpl extends CallbackVideoSurface {

	public CallbackVideoSurfaceImpl(BufferFormatCallback bufferFormatCallback, RenderCallback renderCallback,
			boolean lockBuffers, VideoSurfaceAdapter videoSurfaceAdapter) {
		super(bufferFormatCallback, renderCallback, lockBuffers, videoSurfaceAdapter);
	}

}
