/*
 * This file is part of VLCJ.
 *
 * VLCJ is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * VLCJ is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with VLCJ.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2009-2020 Caprica Software Limited.
 */

package com.smilesmile1973.jptv.view;

import static uk.co.caprica.vlcj.player.embedded.videosurface.VideoSurfaceAdapters.getVideoSurfaceAdapter;

import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.smilesmile1973.jptv.Utils;
import com.smilesmile1973.jptv.event.RendererCreatedEvent;

import javafx.application.Platform;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelBuffer;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.WritableImage;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.embedded.videosurface.CallbackVideoSurface;
import uk.co.caprica.vlcj.player.embedded.videosurface.VideoSurface;
import uk.co.caprica.vlcj.player.embedded.videosurface.callback.BufferFormat;
import uk.co.caprica.vlcj.player.embedded.videosurface.callback.BufferFormatCallback;
import uk.co.caprica.vlcj.player.embedded.videosurface.callback.RenderCallback;
import uk.co.caprica.vlcj.player.embedded.videosurface.callback.format.RV32BufferFormat;

/**
 * Factory used to create a vlcj {@link VideoSurface} component for a JavaFX
 * {@link ImageView}.
 * <p>
 * Developer note: the imageView reference will keep this factory object alive.
 */
public final class ImageViewVideoSurfaceFactory {

	private class PixelBufferBufferFormatCallback implements BufferFormatCallback {

		private int sourceWidth;
		private int sourceHeight;

		@Override
		public void allocatedBuffers(ByteBuffer[] buffers) {
			LOG.debug("allocatedBuffers");
			PixelFormat<ByteBuffer> pixelFormat = PixelFormat.getByteBgraPreInstance();
			pixelBuffer = new PixelBuffer<>(sourceWidth, sourceHeight, buffers[0], pixelFormat);
			imageView.setImage(new WritableImage(pixelBuffer));
		}

		@Override
		public BufferFormat getBufferFormat(int sourceWidth, int sourceHeight) {
			LOG.debug("getBufferFormat");
			this.sourceWidth = sourceWidth;
			this.sourceHeight = sourceHeight;
			return new RV32BufferFormat(sourceWidth, sourceHeight);
		}
	}

	private class PixelBufferRenderCallback implements RenderCallback {
		@Override
		public void display(MediaPlayer mediaPlayer, ByteBuffer[] nativeBuffers, BufferFormat bufferFormat) {
			Platform.runLater(() -> pixelBuffer.updateBuffer(pb -> {
				if (!displayed) {
					displayed = true;
					Utils.getEventBus().post(new RendererCreatedEvent(true));
				}
				return null;
			}));
		}
	}

	private class PixelBufferVideoSurface extends CallbackVideoSurface {
		private PixelBufferVideoSurface() {
			super(ImageViewVideoSurfaceFactory.this.bufferFormatCallback,
					ImageViewVideoSurfaceFactory.this.renderCallback, true, getVideoSurfaceAdapter());
		}
	}

	private static final Logger LOG = LoggerFactory.getLogger(ImageViewVideoSurfaceFactory.class);

	/**
	 * Get a new {@link VideoSurface} for an {@link ImageView}.
	 *
	 * @param imageView image view used to render the video
	 * @return video surface
	 */
	public static VideoSurface videoSurfaceForImageView(ImageView imageView) {
		return new ImageViewVideoSurfaceFactory(imageView).videoSurface;
	}

	private final ImageView imageView;

	private final PixelBufferBufferFormatCallback bufferFormatCallback;

	private final PixelBufferRenderCallback renderCallback;

	private final PixelBufferVideoSurface videoSurface;

	private PixelBuffer<ByteBuffer> pixelBuffer;

	private boolean displayed = false;

	private ImageViewVideoSurfaceFactory(ImageView imageView) {
		this.imageView = imageView;
		this.bufferFormatCallback = new PixelBufferBufferFormatCallback();
		this.renderCallback = new PixelBufferRenderCallback();
		this.videoSurface = new PixelBufferVideoSurface();
	}
}