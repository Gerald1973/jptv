# JPTV

## Description

Wit this software you can access some IPTV channel around the world, depending your M3u provider.  
I did this software to try JavaFX 15 and test the PixelBuffer available from JDK 13.

### Prerequisite

OpenJDK (Open Java Development Toolkit) must be installed.  
You can download the last version (_16 at the time_) at the following URL:  
<https://openjdk.java.net/>

VIDEOLAN aka VLC must be installed on your computer.  
You can download the last version at the following URL:  
<https://www.videolan.org/vlc/index.fr.html>

Some M3U list for the TV channels:  
<https://github.com/iptv-org/iptv>

### Documentation

#### Execution

##### Command line

```cmd
java -jar JPTV.jar "https://iptv-org.github.io/iptv/index.language.m3u"
```

or

```cmd
java -jar JPTV.jar
```

In this case, a popup will be displaying inviting you to enter an MRL (aka URL) ...m3u

#### Desktop

If you have installed Java with an setup for windows, for example the Oracle JDK, then, you can use it by double clicking on it.

### User Interface

| Action                                                | Result                         |
| ----------------------------------------------------- | ------------------------------ |
| Move the mouse pointer on the left of the window      | The channels list will appear  |
| Move the mouse pointer on the top right of the window | The technical info will appear |
| Double inside the window                              | Toggle full screen             |
