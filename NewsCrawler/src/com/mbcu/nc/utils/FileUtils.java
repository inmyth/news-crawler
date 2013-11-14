package com.mbcu.nc.utils;

import com.mbcu.nc.main.Config;

public class FileUtils {

	private static String PERCENT = "%";

	public static final  String sanitize(String url){
		url = url.substring(Config.PROTOCOL_HTTP.length());
		return url.replaceAll("/", PERCENT);	
	}
}
