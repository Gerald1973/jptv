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
	public void lock(MediaPlayer mediaPlayer) {
		//LOG.debug("Lock: buffer vidéo locking");
	}

	@Override
	public void display(MediaPlayer mediaPlayer, ByteBuffer[] nativeBuffers, BufferFormat bufferFormat,
			int displayWidth, int displayHeight) {
		//LOG.debug("display: Rendu de la frame, display={}x{}", displayWidth, displayHeight);
        if (PixelBufferInstance.getInstance().getPixelBuffer() != null) {
            Platform.runLater(() -> {
                PixelBufferInstance.getInstance().getPixelBuffer().updateBuffer(pb -> null);
                if (!PixelBufferInstance.getInstance().isDisplayed()) {
                    PixelBufferInstance.getInstance().setDisplayed(true);
                    Utils.getEventBus().post(new RendererCreatedEvent(true));
                }
            });
        } else {
            LOG.warn("PixelBuffer non initialisé, ignorer le rendu");
        }
		
	}

	@Override
	public void unlock(MediaPlayer mediaPlayer) {
		//LOG.debug("unlock: Unlock video buffer");
	}
}
