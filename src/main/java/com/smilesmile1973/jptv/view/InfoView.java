package com.smilesmile1973.jptv.view;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.eventbus.Subscribe;
import com.smilesmile1973.jptv.Constants;
import com.smilesmile1973.jptv.Utils;
import com.smilesmile1973.jptv.event.EventMediaStatistics;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import uk.co.caprica.vlcj.media.MediaStatistics;

public class InfoView extends Pane {

	private static final String CLASS_INFO_VIEW_TEXT = "infoViewText";

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

	private void addInfo(GridPane gridPane, int row, String labelString, Text text) {
		Text textCol0 = new Text(labelString);
		textCol0.getStyleClass().add(CLASS_INFO_VIEW_TEXT);
		gridPane.add(textCol0, 0, row);
		gridPane.add(text, 1, row);
	}

	@Subscribe
	public void eventMediaStaticsFetched(EventMediaStatistics event) {
		MediaStatistics mediaStatistics = event.getMediaStatistics();
		txtAudioBuffersLost.setText(String.valueOf(mediaStatistics.audioBuffersLost()));
		txtAudioBuffersLost.getStyleClass().add(CLASS_INFO_VIEW_TEXT);
		txtAudioBuffersPlayed.setText(String.valueOf(mediaStatistics.audioBuffersPlayed()));
		txtAudioBuffersPlayed.getStyleClass().add(CLASS_INFO_VIEW_TEXT);
		txtDecodedAudio.setText(String.valueOf(mediaStatistics.decodedAudio()));
		txtDecodedAudio.getStyleClass().add(CLASS_INFO_VIEW_TEXT);
		txtDecodedVideo.setText(String.valueOf(mediaStatistics.decodedVideo()));
		txtDecodedVideo.getStyleClass().add(CLASS_INFO_VIEW_TEXT);
		txtDemuxBytesRead.setText(String.valueOf(mediaStatistics.demuxBitrate()));
		txtDemuxBytesRead.getStyleClass().add(CLASS_INFO_VIEW_TEXT);
		txtDemuxCorrupted.setText(String.valueOf(mediaStatistics.demuxCorrupted()));
		txtDemuxCorrupted.getStyleClass().add(CLASS_INFO_VIEW_TEXT);
		txtDemuxDiscontinuity.setText(String.valueOf(mediaStatistics.demuxDiscontinuity()));
		txtDemuxDiscontinuity.getStyleClass().add(CLASS_INFO_VIEW_TEXT);
		txtInputBitrate.setText(String.valueOf(mediaStatistics.inputBitrate()));
		txtInputBitrate.getStyleClass().add(CLASS_INFO_VIEW_TEXT);
		txtInputBytesRead.setText(String.valueOf(mediaStatistics.inputBytesRead()));
		txtInputBytesRead.getStyleClass().add(CLASS_INFO_VIEW_TEXT);
		txtPicturesLost.setText(String.valueOf(mediaStatistics.picturesLost()));
		txtPicturesLost.getStyleClass().add(CLASS_INFO_VIEW_TEXT);
		txtSendBitrate.setText(String.valueOf(mediaStatistics.sendBitrate()));
		txtSendBitrate.getStyleClass().add(CLASS_INFO_VIEW_TEXT);
		txtSentBytes.setText(String.valueOf(mediaStatistics.sentBytes()));
		txtSentBytes.getStyleClass().add(CLASS_INFO_VIEW_TEXT);
		txtSentPackets.setText(String.valueOf(mediaStatistics.sentPackets()));
		txtSentPackets.getStyleClass().add(CLASS_INFO_VIEW_TEXT);
		txtPicturesDisplayed.setText(String.valueOf(mediaStatistics.picturesDisplayed()));
		txtPicturesDisplayed.getStyleClass().add(CLASS_INFO_VIEW_TEXT);
	}

	private void init() {
		Utils.getEventBus().register(this);
		GridPane gridPane = new GridPane();
		int c = 0;
		addInfo(gridPane, c++, "Audio buffers lost", this.txtAudioBuffersLost);
		addInfo(gridPane, c++, "Audio buffers played", this.txtAudioBuffersPlayed);
		addInfo(gridPane, c++, "Decoded audio", this.txtDecodedAudio);
		addInfo(gridPane, c++, "Decoded video", this.txtDecodedVideo);
		addInfo(gridPane, c++, "Demux bytes read", this.txtDemuxBytesRead);
		addInfo(gridPane, c++, "Demux bytes corrupted", this.txtDemuxCorrupted);
		addInfo(gridPane, c++, "Demux discontinuity", this.txtDemuxDiscontinuity);
		addInfo(gridPane, c++, "Input bit rate", this.txtInputBitrate);
		addInfo(gridPane, c++, "Input bytes read", this.txtInputBytesRead);
		addInfo(gridPane, c++, "Pictures lost", txtPicturesLost);
		addInfo(gridPane, c++, "Send bit rate", txtSendBitrate);
		addInfo(gridPane, c++, "Send bytes", txtSentBytes);
		addInfo(gridPane, c++, "Send packets", txtSentPackets);
		addInfo(gridPane, c++, "Pictures displayed", txtPicturesDisplayed);
		this.setMinWidth(Constants.INFO_VIEW_WIDTH);
		this.setPrefWidth(Constants.INFO_VIEW_WIDTH);
		this.setMinHeight(Constants.INFO_VIEW_HEIGHT);
		this.setPrefHeight(Constants.INFO_VIEW_HEIGHT);
		this.getChildren().add(gridPane);
	}

}
