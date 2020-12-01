/**
 * 
 */
package com.smilesmile1973.jptv.view;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.google.common.eventbus.Subscribe;
import com.smilesmile1973.jptv.Constants;
import com.smilesmile1973.jptv.Utils;
import com.smilesmile1973.jptv.event.ChannelListCreatedEvent;
import com.smilesmile1973.jptv.event.EventChannel;
import com.smilesmile1973.jptv.event.RendererCreatedEvent;
import com.smilesmile1973.jptv.pojo.Channel;
import com.smilesmile1973.jptv.service.M3UService;
import com.smilesmile1973.jptv.service.PreferencesService;
import com.smilesmile1973.jptv.view.fxservice.InfoStreamService;
import com.sun.jna.ptr.IntByReference;

import javafx.application.Application;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.Window;
import uk.co.caprica.vlcj.binding.LibVlc;
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

	public static void main(String[] args) {
		LOG.info("JPTV starting.");
		launch(args);
	}

	private final MediaPlayerFactory mediaPlayerFactory;

	private EmbeddedMediaPlayer embeddedMediaPlayer;

	private boolean keepRatio = true;

	private ImageView videoImageView = new ImageView();
	
	private InfoView infoView = new InfoView();

	public Main() {
		this.mediaPlayerFactory = new MediaPlayerFactory();
		this.embeddedMediaPlayer = mediaPlayerFactory.mediaPlayers().newEmbeddedMediaPlayer();
		this.embeddedMediaPlayer.events().addMediaPlayerEventListener(new MediaPlayerEventAdapter() {
			@Override
			public void paused(MediaPlayer mediaPlayer) {
				// LOG.debug("Media player paused");
			}

			@Override
			public void playing(MediaPlayer mediaPlayer) {
				LOG.debug("Media player playing");
			}

			@Override
			public void stopped(MediaPlayer mediaPlayer) {
				// LOG.debug("Media player stopped");
			}

			@Override
			public void timeChanged(MediaPlayer mediaPlayer, long newTime) {
				// LOG.debug("Media player time changed");
			}
		});
	}

	private Node buildRightSplit() {
		Pane videoPane = new Pane();
		videoPane.setId("videoPane");
		embeddedMediaPlayer.videoSurface().set(ImageViewVideoSurfaceFactory.videoSurfaceForImageView(videoImageView));

		videoPane.widthProperty().addListener((observableValue, oldValue, newValue) -> {
			if (keepRatio) {
				placeVideoImage(videoImageView, keepRatio);
			} else {
				videoImageView.fitHeightProperty().set(videoPane.getHeight());
				videoImageView.fitWidthProperty().set(newValue.doubleValue());
			}
		});

		videoPane.heightProperty().addListener((observableValue, oldValue, newValue) -> {
			if (!keepRatio) {
				videoImageView.fitHeightProperty().set(videoPane.getHeight());
				videoImageView.fitWidthProperty().set(newValue.doubleValue());
			} else {
				placeVideoImage(videoImageView, keepRatio);
			}
		});

		videoPane.getChildren().add(videoImageView);
		videoPane.getChildren().add(infoView);
		
		videoPane.setOnMouseMoved(eventMouse -> hideOrShowInfo(videoPane, eventMouse));
		return videoPane;
	}

	private Node buildLeftSplit() {
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
		accordion.expandedPaneProperty()
				.addListener((observable, oldValue, titledPane) -> expandTitledPane(titledPane));
		scrollPane.setMinWidth(0);
		scrollPane.setMaxWidth(Constants.CHANNEL_LIST_WIDTH);
		return scrollPane;

	}

	private void expandTitledPane(TitledPane titledPane) {
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

	private Node buildMenu(Window owner) {
		MenuBar menuBar = new MenuBar();
		MenuItem configuration = new MenuItem("Configuration");
		configuration.setOnAction(actionEvent -> new Preferences(owner));
		Menu preferences = new Menu("Preferences");
		preferences.getItems().add(configuration);
		menuBar.getMenus().add(preferences);
		return menuBar;
	}

	private BorderPane root;

	private Parent buildRoot(Window owner) {
		root = new BorderPane();
		root.setId("background");
		root.setTop(buildTopPane(owner));
		Node left = buildLeftSplit();
		Node right = buildRightSplit();
		SplitPane splitPane = new SplitPane(left, right);
		splitPane.setDividerPosition(0, Constants.CHANNEL_LIST_WIDTH / Constants.STAGE_WIDTH);
		splitPane.setOnMouseMoved(eventMouse -> hideOrShowChannelList(splitPane, eventMouse));
		root.setCenter(splitPane);
		return root;
	}

	private void hideOrShowChannelList(SplitPane splitPane, MouseEvent eventMouse) {
		double x = eventMouse.getSceneX();
		double sceneWidth = splitPane.getWidth();
		if (x < Constants.CHANNEL_LIST_WIDTH) {
			splitPane.getDividers().get(0).setPosition(Constants.CHANNEL_LIST_WIDTH / sceneWidth);
		} else {
			splitPane.getDividers().get(0).setPosition(0);
		}
	}

	private void hideOrShowInfo(Pane pane, MouseEvent eventMouse) {
		double x = eventMouse.getX();
		double y = eventMouse.getY();
		if (x > pane.getWidth() - Constants.INFO_ZONE_WIDTH && y < Constants.INFO_ZONE_HEIGHT) {
			this.infoView.setVisible(true);
		} else {
			this.infoView.setVisible(false);
		}
	}

	private Node buildTopPane(Window owner) {
		HBox hbox = new HBox();
		hbox.getChildren().add(buildMenu(owner));
		return hbox;
	}

	@Subscribe
	public void changeChannel(EventChannel eventChannel) {
		LOG.debug("Change channel to {}:", eventChannel.getChannel().getChannelURL());
		embeddedMediaPlayer.media().play(eventChannel.getChannel().getChannelURL());
	}

	@Subscribe
	public void rendererCreated(RendererCreatedEvent event) {
		placeVideoImage(videoImageView, true);
		InfoStreamService.getInstance().getInfo(embeddedMediaPlayer);
	}

	@Subscribe
	public void refreshCenter(ChannelListCreatedEvent event) {
		if (event.isCreated()) {
			Node left = buildLeftSplit();
			Node right = buildRightSplit();
			SplitPane splitPane = new SplitPane(left, right);
			splitPane.setDividerPosition(0, Constants.CHANNEL_LIST_WIDTH / Constants.STAGE_WIDTH);
			root.setCenter(splitPane);
		}
	}

	@Override
	public void init() {
		Utils.getEventBus().register(this);
	}

	private void initChannels(Window owner) {
		try {
			if (!getParameters().getRaw().isEmpty() && !Strings.isNullOrEmpty(getParameters().getRaw().get(0))) {
				M3UService.getInstance().buildChannels(getParameters().getRaw().get(0));
			} else {
				String mp3Ulist = PreferencesService.getInstance().readProperty(PreferencesService.KEY_IPTV_M3U);
				if (!mp3Ulist.isBlank()) {
					M3UService.getInstance().buildChannels(mp3Ulist);
				} else {
					new Preferences(owner);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void placeVideoImage(ImageView imageView, boolean keepRatio) {
		Region region = (Region) imageView.getParent();
		double imageViewWidth = region.getWidth();
		double imageViewHeight = region.getHeight();
		if (keepRatio) {
			IntByReference px = new IntByReference();
			IntByReference py = new IntByReference();
			LibVlc.libvlc_video_get_size(this.embeddedMediaPlayer.mediaPlayerInstance(), 0, px, py);
			double videoWidth = px.getValue();
			double videoHeight = py.getValue();
			double scaleWidth = imageViewWidth / videoWidth;
			double imageViewWidthScaled = scaleWidth * videoWidth;
			double imageViewHeightScaled = scaleWidth * videoHeight;
			double y = (imageViewHeight - imageViewHeightScaled) / 2.0;
			imageView.fitWidthProperty().set(imageViewWidthScaled);
			imageView.fitHeightProperty().set(imageViewHeightScaled);
			imageView.setX(0);
			imageView.setY(y);
		} else {
			videoImageView.fitHeightProperty().set(imageViewWidth);
			videoImageView.fitWidthProperty().set(imageViewWidth);
		}
	}

	@Override
	public void start(Stage stage) throws Exception {
		initChannels(stage);
		Scene scene = new Scene(buildRoot(stage), Constants.STAGE_WIDTH, Constants.STAGE_HEIGHT);
		scene.getStylesheets().add(getClass().getClassLoader().getResource("styles.css").toExternalForm());
		scene.setFill(Color.TRANSPARENT);
		stage.setTitle("JPTV");
		stage.setScene(scene);
		stage.show();
		boolean playing = false;
		if (!getParameters().getRaw().isEmpty()) {
			playing = embeddedMediaPlayer.media().play(getParameters().getRaw().get(0));
		} else {
			if (!PreferencesService.getInstance().readProperty(PreferencesService.KEY_IPTV_M3U).isBlank()) {
				playing = embeddedMediaPlayer.media()
						.play(PreferencesService.getInstance().readProperty(PreferencesService.KEY_IPTV_M3U));
			}
		}
		LOG.debug("Playing ok ? {}", playing);
	}

	@Override
	public void stop() throws Exception {
		super.stop();
		embeddedMediaPlayer.release();
		mediaPlayerFactory.release();
	}
}
