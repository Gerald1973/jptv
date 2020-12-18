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

	private void addLabelAndTxt(GridPane gridPane, int row, String labelText, Text text) {
		Label lbl = new Label(labelText);
		gridPane.add(lbl, 0, row);
		gridPane.add(text, 1, row);
	}

	@Subscribe
	public void eventMediaStaticsFetched(EventMediaStatistics event) {
		MediaStatistics mediaStatistics = event.getMediaStatistics();
		txtAudioBuffersLost.setText(String.valueOf(mediaStatistics.audioBuffersLost()));
		txtAudioBuffersPlayed.setText(String.valueOf(mediaStatistics.audioBuffersPlayed()));
		txtDecodedAudio.setText(String.valueOf(mediaStatistics.decodedAudio()));
		txtDecodedVideo.setText(String.valueOf(mediaStatistics.decodedVideo()));
		txtDemuxBytesRead.setText(String.valueOf(mediaStatistics.demuxBitrate()));
		txtDemuxCorrupted.setText(String.valueOf(mediaStatistics.demuxCorrupted()));
		txtDemuxDiscontinuity.setText(String.valueOf(mediaStatistics.demuxDiscontinuity()));
		txtInputBitrate.setText(String.valueOf(mediaStatistics.inputBitrate()));
		txtInputBytesRead.setText(String.valueOf(mediaStatistics.inputBytesRead()));
		txtPicturesLost.setText(String.valueOf(mediaStatistics.picturesLost()));
		txtSendBitrate.setText(String.valueOf(mediaStatistics.sendBitrate()));
		txtSentBytes.setText(String.valueOf(mediaStatistics.sentBytes()));
		txtSentPackets.setText(String.valueOf(mediaStatistics.sentPackets()));
		txtPicturesDisplayed.setText(String.valueOf(mediaStatistics.picturesDisplayed()));
	}

	private void init() {
		Utils.getEventBus().register(this);
		GridPane gridPane = new GridPane();
		int c = 0;
		addLabelAndTxt(gridPane, c++, "Audio buffers lost", this.txtAudioBuffersLost);
		addLabelAndTxt(gridPane, c++, "Audio buffers played", this.txtAudioBuffersPlayed);
		addLabelAndTxt(gridPane, c++, "Decoded audio", this.txtDecodedAudio);
		addLabelAndTxt(gridPane, c++, "Decoded video", this.txtDecodedVideo);
		addLabelAndTxt(gridPane, c++, "Demux bytes read", this.txtDemuxBytesRead);
		addLabelAndTxt(gridPane, c++, "Demux bytes corrupted", this.txtDemuxCorrupted);
		addLabelAndTxt(gridPane, c++, "Demux discontinuity", this.txtDemuxDiscontinuity);
		addLabelAndTxt(gridPane, c++, "Input bit rate", this.txtInputBitrate);
		addLabelAndTxt(gridPane, c++, "Input bytes read", this.txtInputBytesRead);
		addLabelAndTxt(gridPane, c++, "Pictures lost", txtPicturesLost);
		addLabelAndTxt(gridPane, c++, "Send bit rate", txtSendBitrate);
		addLabelAndTxt(gridPane, c++, "Send bytes", txtSentBytes);
		addLabelAndTxt(gridPane, c++, "Send packets", txtSentPackets);
		addLabelAndTxt(gridPane, c++, "Pictures displayed", txtPicturesDisplayed);
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
