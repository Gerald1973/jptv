package com.smilesmile1973.jptv.view.fxservice;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import uk.co.caprica.vlcj.media.AudioTrackInfo;
import uk.co.caprica.vlcj.media.InfoApi;
import uk.co.caprica.vlcj.media.MediaStatistics;
import uk.co.caprica.vlcj.player.base.MediaPlayer;

public class InfoStreamService extends Service<MediaPlayer>{
	
	private static final Logger LOG = LoggerFactory.getLogger(InfoStreamService.class);
	
	private static InfoStreamService instance;
	
	private InfoStreamService() {}
	
	private MediaPlayer mediaPlayer;
	
	public static InfoStreamService getInstance() {
		if (instance == null) {
			instance = new InfoStreamService();
		}
		return instance;
	}
	
	public void getInfo(MediaPlayer mediaPlayer) {
		this.mediaPlayer = mediaPlayer;
		LOG.debug("State: {}", this.getState());
		if (this.getState() == State.READY) {
			this.start();
		}
	}

	@Override
	protected Task<MediaPlayer> createTask() {
		return new Task<MediaPlayer>() {
			@Override
			protected MediaPlayer call() throws Exception {
				mediaPlayer.media().parsing().parse();
				return mediaPlayer;
			}
		};
	}

	@Override
	protected void succeeded() {
		LOG.debug("succeeded");
		InfoApi infoApi = mediaPlayer.media().info();
		MediaStatistics mediaStatics = infoApi.statistics();
		List<AudioTrackInfo> audioTracks = infoApi.audioTracks();
		for (AudioTrackInfo audioTrackInfo : audioTracks) {
			LOG.debug("Audio track info bit rate: {}", audioTrackInfo.bitRate());
		}
		LOG.debug("Duration           : {}", infoApi.duration());
		LOG.debug("MRL                : {}", infoApi.mrl());
		
		
		
		LOG.debug("audioBuffersLost   : {}", mediaStatics.audioBuffersLost());
		LOG.debug("audioBuffersPlayed : {}", mediaStatics.audioBuffersPlayed());
		LOG.debug("decodedAudio       : {}", mediaStatics.decodedAudio());
		LOG.debug("decodedVideo       : {}", mediaStatics.decodedVideo());
		LOG.debug("demuxBytesRead     : {}", mediaStatics.demuxBytesRead());
		LOG.debug("demuxCorrupted     : {}", mediaStatics.demuxCorrupted());
		LOG.debug("demuxDiscontinuity : {}", mediaStatics.demuxDiscontinuity());
		LOG.debug("inputBitrate       : {}", mediaStatics.inputBitrate());
		LOG.debug("inputBytesRead     : {}", mediaStatics.inputBytesRead());
		LOG.debug("picturesDisplayed  : {}", mediaStatics.picturesDisplayed());
		LOG.debug("picturesLost       : {}", mediaStatics.picturesLost());
		LOG.debug("sendBitrate        : {}", mediaStatics.sendBitrate());
		LOG.debug("sentBytes          : {}", mediaStatics.sentBytes());
		LOG.debug("sentPackets        : {}", mediaStatics.sentPackets());
		
		
		
		this.reset();
	}

	@Override
	protected void cancelled() {
		LOG.debug("cancelled");
		this.reset();
	}

	@Override
	protected void failed() {
		LOG.debug("failed");
		this.reset();
	}
}
