package com.smilesmile1973.jptv;

import com.smilesmile1973.jptv.view.Main;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.factory.discovery.NativeDiscovery;

public class EntryPoint {
	
	public static void main(String[] args) {
		boolean found = new NativeDiscovery().discover();
        System.out.println(found);
        System.out.println(LibVlc.libvlc_get_version());
		Main.main(args);
	}
}
