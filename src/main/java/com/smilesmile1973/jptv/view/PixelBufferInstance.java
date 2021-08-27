package com.smilesmile1973.jptv.view;

import static uk.co.caprica.vlcj.player.embedded.videosurface.VideoSurfaceAdapters.getVideoSurfaceAdapter;

import java.nio.ByteBuffer;

import com.smilesmile1973.jptv.Constants;

import javafx.scene.image.ImageView;
import javafx.scene.image.PixelBuffer;
import javafx.scene.image.WritableImage;
import uk.co.caprica.vlcj.player.embedded.videosurface.CallbackVideoSurface;
import uk.co.caprica.vlcj.player.embedded.videosurface.callback.BufferFormatCallback;
import uk.co.caprica.vlcj.player.embedded.videosurface.callback.RenderCallback;

public class PixelBufferInstance {

	private static PixelBufferInstance instance;

	private boolean displayed = false;

	public boolean isDisplayed() {
		return displayed;
	}

	public void setDisplayed(boolean displayed) {
		this.displayed = displayed;
	}

	public static PixelBufferInstance getInstance() {
		if (instance == null) {
			instance = new PixelBufferInstance();
		}
		return instance;
	}

	private int height = (int) Constants.STAGE_HEIGHT;

	private ImageView imageView;

	private PixelBuffer<ByteBuffer> pixelBuffer;

	private int width = (int) Constants.STAGE_WIDTH;

	private PixelBufferInstance() {
	}

	public int getHeight() {
		return height;
	}

	public ImageView getImageView() {
		return imageView;
	}

	public PixelBuffer<ByteBuffer> getPixelBuffer() {
		return pixelBuffer;
	}

	public int getWidth() {
		return width;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public void setImageView(ImageView imageView) {
		this.imageView = imageView;
	}

	public void setPixelBuffer(PixelBuffer<ByteBuffer> pixelBuffer) {
		this.pixelBuffer = pixelBuffer;
		this.imageView.setImage(new WritableImage(pixelBuffer));
	}

	public void setWidth(int width) {
		this.width = width;
	}
	// super(ImageViewVideoSurfaceFactory.this.bufferFormatCallback,
	// ImageViewVideoSurfaceFactory.this.renderCallback, true,
	// getVideoSurfaceAdapter());

	public CallbackVideoSurface buildCallBackVideoSurface(ImageView imageView) {
		this.imageView = imageView;
		BufferFormatCallback bufferFormatCallback = new BufferFormatCallbackImpl(imageView);
		RenderCallback renderCallBack = new RenderCallbackImpl();
		CallbackVideoSurface result = new CallbackVideoSurface(bufferFormatCallback, renderCallBack, false,
				getVideoSurfaceAdapter());
		return result;
	}

}
