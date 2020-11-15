/**
 * 
 */
package com.smilesmile1973.jptv.controller;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

import com.smilesmile1973.jptv.service.PreferencesService;

/**
 * @author smilesmile1973@gmail.com
 *
 */
public class PreferencesCtrlTest {

	@Test
	public void testReadProperty() throws IOException {
		PreferencesService ctrl = PreferencesService.getInstance();
		ctrl.writeProperty(PreferencesService.KEY_IPTV_M3U, "http://something");
		PreferencesService.refreshPreferences();
		String value = ctrl.readProperty(PreferencesService.KEY_IPTV_M3U);
		assertEquals("http://something", value);
		PreferencesService.removeConfigurationFile();
		PreferencesService.refreshPreferences();
		value = ctrl.readProperty(PreferencesService.KEY_IPTV_M3U);
		assertEquals("", value);
	}

	@Test
	public void testRemoveProperty() throws IOException {
		PreferencesService ctrl = PreferencesService.getInstance();
		ctrl.writeProperty(PreferencesService.KEY_IPTV_M3U, "http://something");
		ctrl.removeProperty(PreferencesService.KEY_IPTV_M3U);
		assertEquals("", ctrl.readProperty(PreferencesService.KEY_IPTV_M3U));
	}

	@Test
	public void testWriteProperty() throws IOException {
		PreferencesService.refreshPreferences();
		PreferencesService.removeConfigurationFile();
		PreferencesService ctrl = PreferencesService.getInstance();
		ctrl.writeProperty(PreferencesService.KEY_IPTV_M3U, "http://something");
		String value = ctrl.readProperty(PreferencesService.KEY_IPTV_M3U);
		assertEquals("http://something", value);

		PreferencesService.refreshPreferences();
		PreferencesService.removeConfigurationFile();
	}
}
