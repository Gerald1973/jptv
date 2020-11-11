package com.smilesmile1973.jptv.converter;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.smilesmile1973.jptv.pojo.Channel;

public class ChannelConverterTest {

	private String[] source = {
			"#EXTINF:-1 tvg-id=\"Eurosport2.fr\" tvg-name=\"|FR| EUROSPORT 2 FHD\" tvg-logo=\"http://one-iptv.com/logoX/SfrSport/eurosport2.png\" group-title=\"FRANCE SPORT\",|FR| EUROSPORT 2 FHD",
			"http://test/test2" };

	private Channel target = new Channel();

	@Before
	public void setUp() throws Exception {
		this.target.setGroupTitle("FRANCE SPORT");
		this.target.setGroupTitle2("|FR| EUROSPORT 2 FHD");
		this.target.setLength(-1);
		this.target.setTvgId("Eurosport2.fr");
		this.target.setTvgName("|FR| EUROSPORT 2 FHD");
		this.target.setTvLogo("http://one-iptv.com/logoX/SfrSport/eurosport2.png");
		this.target.setChannelURL("http://test/test2");
	}

	@Test
	public void testToTarget() {
		Channel result = ChannelConverter.getInstance().toTarget(source);
		assertEquals(-1, result.getLength());
		assertEquals("Eurosport2.fr", result.getTvgId());
		assertEquals("|FR| EUROSPORT 2 FHD", result.getTvgName());
		assertEquals("http://one-iptv.com/logoX/SfrSport/eurosport2.png", result.getTvLogo());
		assertEquals("FRANCE SPORT", result.getGroupTitle());
		assertEquals("|FR| EUROSPORT 2 FHD", result.getGroupTitle2());
		assertEquals("http://test/test2", result.getChannelURL());

	}

	@Test
	public void testToSource() {
		assertEquals(source[0], ChannelConverter.getInstance().toSource(target)[0]);
		assertEquals(source[1], ChannelConverter.getInstance().toSource(target)[1]);
	}

}
