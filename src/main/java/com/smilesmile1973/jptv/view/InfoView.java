package com.smilesmile1973.jptv.view;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.eventbus.Subscribe;
import com.smilesmile1973.jptv.Utils;
import com.smilesmile1973.jptv.event.EventMediaStatistics;

import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import uk.co.caprica.vlcj.media.MediaStatistics;

public class InfoView extends Pane {

	private static final Logger LOG = LoggerFactory.getLogger(InfoView.class);

	private MediaStatistics mediaStatics;

	Text txtAudioBuffersLost = new Text();
	Text txtAudioBuffersPlayed = new Text();
	Text txtDecodedAudio = new Text();
	Text txtDecodedVideo = new Text();
	Text txtDemuxBytesRead = new Text();
	Text txtDemuxCorrupted = new Text();
	Text txtDemuxDiscontinuity = new Text();
	Text txtInputBitrate = new Text();
	Text txtInputBytesRead = new Text();
	Text txtPicturesLost = new Text();
	Text txtSendBitrate = new Text();
	Text txtSentBytes = new Text();
	Text txtSentPackets = new Text();
	Text txtPicturesDisplayed = new Text();

	public InfoView() {
		this.init();
	}

	@Subscribe
	public void eventMediaStaticsFetched(EventMediaStatistics event) {
		this.mediaStatics = event.getMediaStatistics();
		this.txtPicturesDisplayed.setText(String.valueOf(this.mediaStatics.picturesDisplayed()));
		this.txtInputBitrate.setText(String.valueOf(this.mediaStatics.inputBitrate()));
	}

	private void init() {
		Utils.getEventBus().register(this);
		GridPane gridPane = new GridPane();
		Label lblBitRate = new Label("Bit rate");
		gridPane.add(lblBitRate, 0, 0);
		gridPane.add(txtInputBitrate, 1, 0);

		Label lblPicturesDisplayed = new Label("Pictures displayed");
		gridPane.add(lblPicturesDisplayed, 0, 1);
		gridPane.add(txtPicturesDisplayed, 1, 1);

		BackgroundFill bf = new BackgroundFill(Color.RED, null, null);
		Background bg = new Background(bf);
		this.setBackground(bg);
		this.setMinWidth(300);
		this.setPrefWidth(300);
		this.setMinHeight(400);
		this.setPrefHeight(400);
		this.getChildren().add(gridPane);
	}

}
