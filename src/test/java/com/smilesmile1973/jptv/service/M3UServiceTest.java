package com.smilesmile1973.jptv.service;

import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.smilesmile1973.jptv.pojo.Channel;

public class M3UServiceTest {

	private static final Logger LOG = LoggerFactory.getLogger(M3UServiceTest.class);

	@Test
	public void testFetchWebSite() throws Exception {
		List<String> results = M3UService.getInstance().fetchWebSite("https://www.google.be/");
		assertTrue(!results.isEmpty());
	}

	@Test
	public void testbuildChannels() throws Exception {
		Map<String, List<Channel>> results = M3UService.getInstance().buildChannels("TTTTT");
		assertTrue(!results.isEmpty());
	}

}
