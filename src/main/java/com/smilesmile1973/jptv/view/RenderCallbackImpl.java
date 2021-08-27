package com.smilesmile1973.jptv.view;

import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.smilesmile1973.jptv.Utils;
import com.smilesmile1973.jptv.event.RendererCreatedEvent;

import javafx.application.Platform;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.embedded.videosurface.callback.BufferFormat;
import uk.co.caprica.vlcj.player.embedded.videosurface.callback.RenderCallback;

public class RenderCallbackImpl implements RenderCallback {

	private static final Logger LOG = LoggerFactory.getLogger(RenderCallbackImpl.class);


	@Override
	public void display(MediaPlayer mediaPlayer, ByteBuffer[] nativeBuffers, BufferFormat bufferFormat) {
		Platform.runLater(() -> PixelBufferInstance.getInstance().getPixelBuffer().updateBuffer(pb -> {
			if (!PixelBufferInstance.getInstance().isDisplayed()) {
				PixelBufferInstance.getInstance().setDisplayed(true);
				Utils.getEventBus().post(new RendererCreatedEvent(true));
			}
			return null;
		}));
	}
}
