/**
 * 
 */
package com.smilesmile1973.jptv.view;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.Window;
import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.base.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

/**
 * @author smilesmile1973@gmail.com
 *
 */
public class Main extends Application {

	private static final Logger LOG = LoggerFactory.getLogger(Main.class);

	private final MediaPlayerFactory mediaPlayerFactory;

	private EmbeddedMediaPlayer embeddedMediaPlayer;

	private ImageView videoImageView;

	public Main() {
		this.mediaPlayerFactory = new MediaPlayerFactory();
		this.embeddedMediaPlayer = mediaPlayerFactory.mediaPlayers().newEmbeddedMediaPlayer();
		this.embeddedMediaPlayer.events().addMediaPlayerEventListener(new MediaPlayerEventAdapter() {
			@Override
			public void playing(MediaPlayer mediaPlayer) {
			}

			@Override
			public void paused(MediaPlayer mediaPlayer) {
			}

			@Override
			public void stopped(MediaPlayer mediaPlayer) {
			}

			@Override
			public void timeChanged(MediaPlayer mediaPlayer, long newTime) {
			}
		});
	}

	@Override
	public void init() {
		videoImageView = new ImageView();
		this.videoImageView.setPreserveRatio(true);
		embeddedMediaPlayer.videoSurface()
				.set(ImageViewVideoSurfaceFactory.videoSurfaceForImageView(this.videoImageView));
	}

	public static void main(String[] args) {
		LOG.info("JPTV starting.");
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		ImageView videoImageView;
		videoImageView = new ImageView();
		videoImageView.setPreserveRatio(true);
		Scene scene = new Scene(buildRoot(stage), 960, 540);
		stage.setTitle("JPTV");
		stage.setScene(scene);
		stage.show();
		embeddedMediaPlayer.media().play(getParameters().getRaw().get(0));
        embeddedMediaPlayer.controls().setPosition(0.4f);
	}

	private Parent buildRoot(Window owner) {
		BorderPane root = new BorderPane();
		root.setTop(buildTopPane(owner));
		root.setLeft(buildLeftPane());
		videoImageView.fitWidthProperty().bind(root.widthProperty());
		videoImageView.fitHeightProperty().bind(root.heightProperty());
		root.widthProperty().addListener((observableValue, oldValue, newValue) -> {
            // If you need to know about resizes
        });

        root.heightProperty().addListener((observableValue, oldValue, newValue) -> {
            // If you need to know about resizes
        });
        root.setCenter(videoImageView);
		return root;
	}

	private Node buildMenu(Window owner) {
		MenuBar menuBar = new MenuBar();
		MenuItem configuration = new MenuItem("Configuration");
		configuration.setOnAction(actionEvent -> new Preferences(owner));
		Menu preferences = new Menu("Preferences");
		preferences.getItems().add(configuration);
		menuBar.getMenus().add(preferences);
		return menuBar;
	}

	private Node buildLeftPane() {
		VBox vBox = new VBox();
		vBox.getChildren().add(new Label("Label"));
		return vBox;
	}

	private Node buildTopPane(Window owner) {
		HBox hbox = new HBox();
		hbox.getChildren().add(buildMenu(owner));
		return hbox;
	}

}