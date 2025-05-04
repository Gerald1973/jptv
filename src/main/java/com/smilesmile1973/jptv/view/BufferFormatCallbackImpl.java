package com.smilesmile1973.jptv.view;

import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.scene.image.PixelBuffer;
import javafx.scene.image.PixelFormat;
import uk.co.caprica.vlcj.player.embedded.videosurface.callback.BufferFormat;
import uk.co.caprica.vlcj.player.embedded.videosurface.callback.BufferFormatCallback;
import uk.co.caprica.vlcj.player.embedded.videosurface.callback.format.RV32BufferFormat;

public class BufferFormatCallbackImpl implements BufferFormatCallback {
	private static final Logger LOG = LoggerFactory.getLogger(BufferFormatCallbackImpl.class);

	@Override
	public void allocatedBuffers(ByteBuffer[] buffers) {
		PixelFormat<ByteBuffer> pixelFormat = PixelFormat.getByteBgraPreInstance();
		int height = PixelBufferInstance.getInstance().getHeight();
		int width = PixelBufferInstance.getInstance().getWidth();
		PixelBuffer<ByteBuffer> result = new PixelBuffer<>(width, height, buffers[0], pixelFormat);
		PixelBufferInstance.getInstance().setPixelBuffer(result);
	}

	@Override
	public BufferFormat getBufferFormat(int sourceWidth, int sourceHeight) {
		PixelBufferInstance.getInstance().setHeight(sourceHeight);
		PixelBufferInstance.getInstance().setWidth(sourceWidth);
		return new RV32BufferFormat(sourceWidth, sourceHeight);
	}

	@Override
	public void newFormatSize(int bufferWidth, int bufferHeight, int displayWidth, int displayHeight) {
		LOG.debug("newFormatSize: buffer={}x{}, display={}x{}", bufferWidth, bufferHeight, displayWidth, displayHeight);
        int currentWidth = PixelBufferInstance.getInstance().getWidth();
        int currentHeight = PixelBufferInstance.getInstance().getHeight();
        if (bufferWidth != currentWidth || bufferHeight != currentHeight) {
            PixelBufferInstance.getInstance().setWidth(bufferWidth);
            PixelBufferInstance.getInstance().setHeight(bufferHeight);
            if (PixelBufferInstance.getInstance().getPixelBuffer() != null) {
                PixelFormat<ByteBuffer> pixelFormat = PixelFormat.getByteBgraPreInstance();
                PixelBuffer<ByteBuffer> newPixelBuffer = new PixelBuffer<>(
                    bufferWidth, bufferHeight,
                    PixelBufferInstance.getInstance().getPixelBuffer().getBuffer(),
                    pixelFormat
                );
                PixelBufferInstance.getInstance().setPixelBuffer(newPixelBuffer);
                PixelBufferInstance.getInstance().getImageView().setImage(
                    new javafx.scene.image.WritableImage(newPixelBuffer)
                );
            } else {
                LOG.warn("PixelBuffer non initialisé, ignorer la mise à jour");
            }
        }
    }
}
