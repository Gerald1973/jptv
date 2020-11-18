/**
 * 
 */
package com.smilesmile1973.jptv.view;

import java.io.IOException;

import com.smilesmile1973.jptv.Utils;
import com.smilesmile1973.jptv.event.ChannelListCreatedEvent;
import com.smilesmile1973.jptv.service.M3UService;
import com.smilesmile1973.jptv.service.PreferencesService;

import javafx.geometry.HPos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
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
		buttonCancel.getStyleClass().add("buttonPreferences");
		buttonOk = new Button("Ok");
		buttonOk.getStyleClass().add("buttonPreferences");
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
		StackPane pane = new StackPane();
		pane.setId("panePreferences");
		
		ColumnConstraints cc0 = new ColumnConstraints();
		ColumnConstraints cc1 = new ColumnConstraints();
		ColumnConstraints cc2 = new ColumnConstraints();
		cc2.setHalignment(HPos.RIGHT);
		cc2.setHgrow(Priority.ALWAYS);
		GridPane gridPane = new GridPane();
		gridPane.getStyleClass().add("gridPanePreferences");
	    gridPane.setPrefWidth(600);
		gridPane.add(lblUrl, 0, 0,1,1);
		gridPane.add(txtUrl, 1, 0,2,1);
		gridPane.add(buttonOk, 0, 1);
		gridPane.add(buttonCancel, 2, 1);
		gridPane.getColumnConstraints().setAll(cc0,cc1,cc2);
		pane.getChildren().add(gridPane);
		return pane;
	}

	public Preferences(Window owner) {
		init();
		this.initOwner(owner);
		this.initStyle(StageStyle.TRANSPARENT);
		Scene scene = new Scene(buildRoot());
		scene.getStylesheets().add(getClass().getClassLoader().getResource("styles.css").toExternalForm());
		scene.setFill(Color.TRANSPARENT);
		this.setScene(scene);
		this.initModality(Modality.WINDOW_MODAL);
		this.showAndWait();
	}
}
