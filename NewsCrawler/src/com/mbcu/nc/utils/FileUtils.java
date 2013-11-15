package com.mbcu.nc.utils;

import com.mbcu.nc.main.Config;

public class FileUtils {

	private static String PERCENT = "%";
	private static String SINGLE_QUOTE = "'";

	public static final  String sanitize(String url){
		url = url.substring(Config.PROTOCOL_HTTP.length());
		url =  url.replaceAll("/", PERCENT);
		url = url.replaceAll("\\?", SINGLE_QUOTE);
		return url;
	}
}
