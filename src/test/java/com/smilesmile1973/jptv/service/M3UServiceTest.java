package com.smilesmile1973.jptv.service;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class M3UServiceTest {

	private static final Logger LOG = LoggerFactory.getLogger(M3UServiceTest.class);

	@Test
	public void test() throws Exception {
		List<String> results = M3UService.getInstance().fetchWebSite("https://www.google.be/");
		assertTrue(!results.isEmpty());
		int c = 0;
		for (String string : results) {
			LOG.info("Line {} : {}", ++c, string);
		}
	}

}
