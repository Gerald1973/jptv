/**
 *
 */
package com.smilesmile1973.jptv.view;

import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
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
import com.smilesmile1973.jptv.service.AwakeRobotService;
import com.smilesmile1973.jptv.service.M3UService;
import com.smilesmile1973.jptv.service.PreferencesService;
import com.smilesmile1973.jptv.service.SystemService;
import com.smilesmile1973.jptv.view.fxservice.InfoStreamService;

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
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.Window;
import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.embedded.videosurface.CallbackVideoSurface;

/**
 * @author smilesmile1973@gmail.com
 *
 */
public class Main extends Application {

	private static final Logger LOG = LoggerFactory.getLogger(Main.class);

	public static void main(final String[] args) {
		LOG.info("JPTV starting.");
		launch(args);
	}

	private final MediaPlayerFactory mediaPlayerFactory = new MediaPlayerFactory();

	private EmbeddedMediaPlayer embeddedMediaPlayer;

	private final InfoView infoView = new InfoView();

	private BorderPane root;

	private Stage stage;

	public Main() {
	}

	@Subscribe
	public void buildCenter(final ChannelListCreatedEvent event) {
		if (event.isCreated()) {
			final Node left = this.buildLeftSplit();
			final Node right = this.buildRightSplit();
			final SplitPane splitPane = new SplitPane(left, right);
			splitPane.setDividerPosition(0, 0);
			this.root.setCenter(splitPane);
			splitPane.setOnMouseMoved(eventMouse -> this.hideOrShowChannelList(splitPane, eventMouse));
			final Channel channel = M3UService.getInstance().getFirst();
			if (StringUtils.isBlank(channel.getOption())) {
				this.embeddedMediaPlayer.media().play(channel.getChannelURL());
			} else {
				this.embeddedMediaPlayer.media().play(channel.getChannelURL(), channel.getOption());
			}
		}
	}

	private Node buildLeftSplit() {
		final Accordion accordion = new Accordion();
		final Set<String> keys = M3UService.getInstance().getChannels().keySet();
		final ScrollPane scrollPane = new ScrollPane();
		scrollPane.setId("scrollPaneAccordion");
		scrollPane.setContent(accordion);
		for (final String key : keys) {
			final TitledPane titledPane = new TitledPane();
			titledPane.setText(key);
			titledPane.setMaxWidth(Constants.CHANNEL_LIST_WIDTH);
			titledPane.setPrefWidth(Constants.CHANNEL_LIST_WIDTH);
			accordion.getPanes().add(titledPane);
			titledPane.setExpanded(false);
		}
		accordion.expandedPaneProperty()
				.addListener((observable, oldValue, titledPane) -> this.expandTitledPane(titledPane));
		scrollPane.setMinWidth(0);
		scrollPane.setMaxWidth(Constants.CHANNEL_LIST_WIDTH);
		return scrollPane;

	}

	private Node buildMenu(final Window owner) {
		final MenuBar menuBar = new MenuBar();
		final MenuItem configuration = new MenuItem("Configuration");
		configuration.setOnAction(actionEvent -> new Preferences(owner));
		final Menu preferences = new Menu("Preferences");
		preferences.getItems().add(configuration);
		menuBar.getMenus().add(preferences);
		return menuBar;
	}

	private Node buildRightSplit() {
		final Pane videoPane = new Pane();
		videoPane.setId("videoPane");
		final CallbackVideoSurface callBackVideoSurface = PixelBufferInstance.getInstance().buildCallBackVideoSurface();
		this.embeddedMediaPlayer.videoSurface().set(callBackVideoSurface);
		videoPane.widthProperty().addListener((observableValue, oldValue, newValue) -> {
			this.placeVideoImage();
		});

		videoPane.heightProperty().addListener((observableValue, oldValue, newValue) -> {
			this.placeVideoImage();
		});

		videoPane.getChildren().add(PixelBufferInstance.getInstance().getImageView());
		videoPane.getChildren().add(this.infoView);
		this.infoView.setLayoutX(videoPane.getWidth() - Constants.INFO_VIEW_WIDTH);
		this.infoView.setVisible(false);
		videoPane.setOnMouseClicked(eventMouse -> {
			if (eventMouse.getButton().equals(MouseButton.PRIMARY) && eventMouse.getClickCount() == 2) {
				this.getStage().setFullScreen(!this.getStage().isFullScreen());
			}
		});
		videoPane.setOnMouseMoved(eventMouse -> this.hideOrShowInfo(videoPane, eventMouse));
		return videoPane;
	}

	private Parent buildRoot(final Window owner) {
		this.root = new BorderPane();
		this.root.setId("background");
		this.root.setTop(this.buildTopPane(owner));
		return this.root;
	}

	private Node buildTopPane(final Window owner) {
		final HBox hbox = new HBox();
		hbox.getChildren().add(this.buildMenu(owner));
		return hbox;
	}

	@Subscribe
	public void changeChannel(final EventChannel eventChannel) {
		final Channel channel = eventChannel.getChannel();
		LOG.debug("Change channel to {}:", channel.getChannelURL());
		PixelBufferInstance.getInstance().setDisplayed(false);
		if (StringUtils.isBlank(channel.getOption())) {
			this.embeddedMediaPlayer.media().play(channel.getChannelURL());
		} else {
			this.embeddedMediaPlayer.media().play(channel.getChannelURL(), channel.getOption());
		}
	}

	private void expandTitledPane(final TitledPane titledPane) {
		if (titledPane != null) {
			titledPane.setContent(null);
			final TilePane pane = new TilePane();
			pane.getStyleClass().add("tilePaneChannelView");
			final List<Channel> channels = M3UService.getInstance().sortGroup(titledPane.getText());
			for (int i = 0; i < channels.size(); i++) {
				final ChannelView channelView = new ChannelView(channels.get(i));
				pane.getChildren().add(channelView);
			}
			titledPane.setContent(pane);
		}
	}

	public Stage getStage() {
		return this.stage;
	}

	private void hideOrShowChannelList(final SplitPane splitPane, final MouseEvent eventMouse) {
		final double x = eventMouse.getSceneX();
		final double sceneWidth = splitPane.getWidth();
		if (x < Constants.CHANNEL_LIST_WIDTH) {
			splitPane.getDividers().get(0).setPosition(Constants.CHANNEL_LIST_WIDTH / sceneWidth);
		} else {
			splitPane.getDividers().get(0).setPosition(0);
		}
	}

	private void hideOrShowInfo(final Pane pane, final MouseEvent eventMouse) {
		final double x = eventMouse.getX();
		final double y = eventMouse.getY();
		if (x > pane.getWidth() - Constants.INFO_ZONE_WIDTH && y < Constants.INFO_ZONE_HEIGHT) {
			this.infoView.setLayoutX(pane.getWidth() - Constants.INFO_VIEW_WIDTH);
			this.infoView.setVisible(true);
		} else {
			this.infoView.setVisible(false);
		}
	}

	@Override
	public void init() {
		Utils.getEventBus().register(this);
		this.embeddedMediaPlayer = this.mediaPlayerFactory.mediaPlayers().newEmbeddedMediaPlayer();
	}

	private void initChannels(final Window owner) {
		try {
			if (!this.getParameters().getRaw().isEmpty() && !Strings.isNullOrEmpty(this.getParameters().getRaw().get(0))) {
				M3UService.getInstance().buildChannels(this.getParameters().getRaw().get(0));
				Utils.getEventBus().post(new ChannelListCreatedEvent(true));
			} else {
				final String mp3Ulist = PreferencesService.getInstance().readProperty(PreferencesService.KEY_IPTV_M3U);
				if (!mp3Ulist.isBlank()) {
					M3UService.getInstance().buildChannels(mp3Ulist);
					Utils.getEventBus().post(new ChannelListCreatedEvent(true));
				} else {
					new Preferences(owner);
				}
			}
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	private void placeVideoImage() {
		final ImageView imageView = PixelBufferInstance.getInstance().getImageView();
		final Region region = (Region) imageView.getParent();
		imageView.fitWidthProperty().set((int) region.getWidth());
		imageView.fitHeightProperty().set((int) region.getHeight());
		imageView.setX(0);
		imageView.setY(0);
	}

	@Subscribe
	public void rendererCreated(final RendererCreatedEvent event) {
		InfoStreamService.getInstance(this.embeddedMediaPlayer);
	}

	public void setStage(final Stage stage) {
		this.stage = stage;
	}

	@Override
	public void start(final Stage stage) throws Exception {
		final Scene scene = new Scene(this.buildRoot(stage), Constants.STAGE_WIDTH, Constants.STAGE_HEIGHT);
		scene.getStylesheets().add(this.getClass().getClassLoader().getResource("styles.css").toExternalForm());
		scene.setFill(Color.TRANSPARENT);
		stage.setTitle("JPTV");
		stage.setScene(scene);
		stage.show();
		this.setStage(stage);
		this.initChannels(stage);
		if (SystemService.getInstance().isWindows()) {
			AwakeRobotService.getInstance();
		}
	}

	@Override
	public void stop() throws Exception {
		super.stop();
		this.embeddedMediaPlayer.release();
		this.mediaPlayerFactory.release();
	}
}
