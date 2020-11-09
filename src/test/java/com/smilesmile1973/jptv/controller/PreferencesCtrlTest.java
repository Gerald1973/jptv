/**
 * 
 */
package com.smilesmile1973.jptv.controller;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

/**
 * @author smilesmile1973@gmail.com
 *
 */
public class PreferencesCtrlTest {

	@Test
	public void testReadProperty() throws IOException {
		PreferencesCtrl ctrl = PreferencesCtrl.getInstance();
		ctrl.writeProperty(PreferencesCtrl.KEY_IPTV_M3U, "http://something");
		PreferencesCtrl.refreshPreferences();
		String value = ctrl.readProperty(PreferencesCtrl.KEY_IPTV_M3U);
		assertEquals("http://something", value);
		PreferencesCtrl.removeConfigurationFile();
		PreferencesCtrl.refreshPreferences();
		value = ctrl.readProperty(PreferencesCtrl.KEY_IPTV_M3U);
		assertEquals("", value);
	}

	@Test
	public void testRemoveProperty() throws IOException {
		PreferencesCtrl ctrl = PreferencesCtrl.getInstance();
		ctrl.writeProperty(PreferencesCtrl.KEY_IPTV_M3U, "http://something");
		ctrl.removeProperty(PreferencesCtrl.KEY_IPTV_M3U);
		assertEquals("", ctrl.readProperty(PreferencesCtrl.KEY_IPTV_M3U));
	}

	@Test
	public void testWriteProperty() throws IOException {
		PreferencesCtrl.refreshPreferences();
		PreferencesCtrl.removeConfigurationFile();
		PreferencesCtrl ctrl = PreferencesCtrl.getInstance();
		ctrl.writeProperty(PreferencesCtrl.KEY_IPTV_M3U, "http://something");
		String value = ctrl.readProperty(PreferencesCtrl.KEY_IPTV_M3U);
		assertEquals("http://something", value);

		PreferencesCtrl.refreshPreferences();
		PreferencesCtrl.removeConfigurationFile();
	}
}
