package com.smilesmile1973.jptv.view.fxservice;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.smilesmile1973.jptv.Utils;
import com.smilesmile1973.jptv.event.EventMediaStatistics;

import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.util.Duration;
import uk.co.caprica.vlcj.media.AudioTrackInfo;
import uk.co.caprica.vlcj.media.InfoApi;
import uk.co.caprica.vlcj.media.MediaStatistics;
import uk.co.caprica.vlcj.player.base.MediaPlayer;

public class InfoStreamService extends ScheduledService<MediaPlayer>{
	
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
			this.setPeriod(Duration.millis(500));
		}
	}

	@Override
	protected Task<MediaPlayer> createTask() {
		return new Task<MediaPlayer>() {
			@Override
			protected MediaPlayer call() throws Exception {
				//mediaPlayer.media().parsing().parse();
				return mediaPlayer;
			}
		};
	}

	@Override
	protected void succeeded() {
		//LOG.debug("succeeded");
		InfoApi infoApi = mediaPlayer.media().info();
		MediaStatistics mediaStatics = infoApi.statistics();
		List<AudioTrackInfo> audioTracks = infoApi.audioTracks();
		for (AudioTrackInfo audioTrackInfo : audioTracks) {
			LOG.debug("Audio track info bit rate: {}", audioTrackInfo.bitRate());
		}
		//LOG.debug("Duration           : {}", infoApi.duration());
		//LOG.debug("MRL                : {}", infoApi.mrl());
		EventMediaStatistics eventMediaStatistics = new EventMediaStatistics(mediaStatics);
		Utils.getEventBus().post(eventMediaStatistics);
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
