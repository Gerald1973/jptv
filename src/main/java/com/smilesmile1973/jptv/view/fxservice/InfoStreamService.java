package com.smilesmile1973.jptv.view.fxservice;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.smilesmile1973.jptv.Constants;
import com.smilesmile1973.jptv.Utils;
import com.smilesmile1973.jptv.event.EventMediaStatistics;

import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.util.Duration;
import uk.co.caprica.vlcj.media.AudioTrackInfo;
import uk.co.caprica.vlcj.media.InfoApi;
import uk.co.caprica.vlcj.media.MediaStatistics;
import uk.co.caprica.vlcj.player.base.MediaPlayer;

public class InfoStreamService extends ScheduledService<Void> {

	private static final Logger LOG = LoggerFactory.getLogger(InfoStreamService.class);

	private static InfoStreamService instance;

	public static InfoStreamService getInstance(MediaPlayer mediaPlayer) {
		if (instance == null) {
			instance = new InfoStreamService();
			Duration duration = new Duration(Constants.REFRESH_INFO_INTERVAL_MS);
			instance.setDelay(duration);
			instance.mediaPlayer = mediaPlayer;
			instance.setPeriod(duration);
			instance.start();
		}
		return instance;
	}

	private MediaPlayer mediaPlayer;

	private EventMediaStatistics eventMediaStatistics;

	private InfoStreamService() {
	}

	@Override
	protected void cancelled() {
		LOG.debug("cancelled");
		this.reset();
	}

	@Override
	protected Task<Void> createTask() {
		return new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				InfoApi infoApi = mediaPlayer.media().info();
				MediaStatistics mediaStatics = infoApi.statistics();
				List<AudioTrackInfo> audioTracks = infoApi.audioTracks();
				for (AudioTrackInfo audioTrackInfo : audioTracks) {
					LOG.debug("Audio track info bit rate         : {}", audioTrackInfo.bitRate());
					LOG.debug("Audio track info codec            : {}", audioTrackInfo.codecDescription());
					LOG.debug("Audio track info codec            : {}", audioTrackInfo.codecName());
					LOG.debug("Audio track info codec description: {}", audioTrackInfo.description());
					LOG.debug("Audio track info language         : {}", audioTrackInfo.language());

				}
				eventMediaStatistics = new EventMediaStatistics(mediaStatics);
				Utils.getEventBus().post(eventMediaStatistics);
				return null;
			}
		};
	}

	@Override
	protected void failed() {
		super.failed();
	}

	@Override
	public void reset() {
		super.reset();
	}

	@Override
	protected void succeeded() {
		super.succeeded();
	}

}
