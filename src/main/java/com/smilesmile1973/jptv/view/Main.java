/**
 * 
 */
package com.smilesmile1973.jptv.view;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.eventbus.Subscribe;
import com.smilesmile1973.jptv.Constants;
import com.smilesmile1973.jptv.Utils;
import com.smilesmile1973.jptv.event.EventChannel;
import com.smilesmile1973.jptv.pojo.Channel;
import com.smilesmile1973.jptv.service.M3UService;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TitledPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
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
		try {
			Utils.getEventBus().register(this);
			M3UService.getInstance().buildChannels(getParameters().getRaw().get(0));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		LOG.info("JPTV starting.");
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		Scene scene = new Scene(buildRoot(stage), Constants.STAGE_WIDTH, Constants.STAGE_HEIGHT);
		scene.getStylesheets().add(getClass().getClassLoader().getResource("styles.css").toExternalForm());
		stage.setTitle("JPTV");
		stage.setScene(scene);
		stage.show();
		embeddedMediaPlayer.media().play(getParameters().getRaw().get(0));
		embeddedMediaPlayer.controls().setPosition(0.4f);
	}

	private Parent buildRoot(Window owner) {
		BorderPane root = new BorderPane();
		root.setId("background");
		root.setTop(buildTopPane(owner));
		Node left = buildLeftPane();
		Node right = buildCenterPane();
		SplitPane splitPane = new SplitPane(left,right);
		splitPane.setDividerPosition(0, Constants.CHANNEL_LIST_WIDTH/Constants.STAGE_WIDTH);
		root.setCenter(splitPane);
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
		Accordion accordion = new Accordion();
		Set<String> keys = M3UService.getInstance().getChannels().keySet();
		ScrollPane scrollPane = new ScrollPane();
		scrollPane.setId("scrollPaneAccordion");
		scrollPane.setContent(accordion);
		for (String key : keys) {
			TitledPane titledPane = new TitledPane();
			titledPane.setText(key);
			titledPane.setMaxWidth(Constants.CHANNEL_LIST_WIDTH);
			titledPane.setPrefWidth(Constants.CHANNEL_LIST_WIDTH);
			accordion.getPanes().add(titledPane);
			titledPane.setExpanded(false);
			
		}
		accordion.expandedPaneProperty().addListener(new ChangeListener<TitledPane>() {

			@Override
			public void changed(ObservableValue<? extends TitledPane> observable, TitledPane oldValue,
					TitledPane titledPane) {
				
				if (titledPane != null) {
					titledPane.setContent(null);
					TilePane pane = new TilePane();
					pane.getStyleClass().add("tilePaneChannelView");
					List<Channel> channels = M3UService.getInstance().sortGroup(titledPane.getText());
					for (int i = 0; i < channels.size(); i++) {
						ChannelView channelView = new ChannelView(channels.get(i));
						pane.getChildren().add(channelView);
					}
					titledPane.setContent(pane);
				}
			}
		});
		scrollPane.setMinWidth(0);
		scrollPane.setMaxWidth(Constants.CHANNEL_LIST_WIDTH);
		return scrollPane;

	}

	private Node buildTopPane(Window owner) {
		HBox hbox = new HBox();
		hbox.getChildren().add(buildMenu(owner));
		return hbox;
	}

	private Node buildCenterPane() {
		Pane node = new Pane();
		ImageView videoImageView = new ImageView();
		videoImageView.fitWidthProperty().bind(node.widthProperty());
		videoImageView.fitHeightProperty().bind(node.heightProperty());
		
		embeddedMediaPlayer.videoSurface().set(ImageViewVideoSurfaceFactory.videoSurfaceForImageView(videoImageView));
		
		node.widthProperty().addListener((observableValue, oldValue, newValue) -> {
		});
		

		node.heightProperty().addListener((observableValue, oldValue, newValue) -> {
		});
			
		node.getChildren().add(videoImageView);
		return node;
	}

	@Subscribe
	public void changeChannel(EventChannel eventChannel) {
		LOG.debug("Change channel to {}:", eventChannel.getChannel().getChannelURL());
		embeddedMediaPlayer.media().play(eventChannel.getChannel().getChannelURL());
	}

	@Override
	public void stop() throws Exception {
		super.stop();
		embeddedMediaPlayer.release();
		mediaPlayerFactory.release();
	}
}
