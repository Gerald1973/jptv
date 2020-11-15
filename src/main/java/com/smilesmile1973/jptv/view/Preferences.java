/**
 * 
 */
package com.smilesmile1973.jptv.view;

import java.io.IOException;

import com.smilesmile1973.jptv.Utils;
import com.smilesmile1973.jptv.event.ChannelListCreatedEvent;
import com.smilesmile1973.jptv.service.M3UService;
import com.smilesmile1973.jptv.service.PreferencesService;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 * @author smilesmile1973@gmail.com
 *
 */
public class Preferences extends Stage {

	private Label lblUrl;

	private TextField txtUrl;

	private Button buttonOk;

	private Button buttonCancel;

	private void init() {
		buttonCancel = new Button("Cancel");
		buttonOk = new Button("Ok");
		lblUrl = new Label("URL M3U");
		txtUrl = new TextField();
		buttonCancel.setOnAction(actionEvent -> this.close());
		try {
			txtUrl.setText(PreferencesService.getInstance().readProperty(PreferencesService.KEY_IPTV_M3U));
		} catch (IOException e) {
			e.printStackTrace();
		}
		buttonOk.setOnAction(actionEvent -> onOk());
	}

	private void onOk() {
		String key = PreferencesService.KEY_IPTV_M3U;
		String value = txtUrl.getText();
		try {
			PreferencesService.getInstance().writeProperty(key, value);
			this.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			M3UService.getInstance().buildChannels(PreferencesService.getInstance().readProperty(PreferencesService.KEY_IPTV_M3U));
			Utils.getEventBus().post(new ChannelListCreatedEvent(true));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Parent buildRoot() {
		GridPane node = new GridPane();
		node.add(lblUrl, 0, 0);
		node.add(txtUrl, 1, 0);
		FlowPane flowPane = new FlowPane();
		flowPane.getChildren().add(buttonOk);
		flowPane.getChildren().add(buttonCancel);
		node.add(flowPane, 0, 1, 2, 1);
		return node;
	}

	public Preferences(Window owner) {
		init();
		this.initOwner(owner);
		setTitle("Preferences");
		Scene scene = new Scene(buildRoot());
		this.setScene(scene);
		this.initModality(Modality.WINDOW_MODAL);
		this.showAndWait();
	}
}
