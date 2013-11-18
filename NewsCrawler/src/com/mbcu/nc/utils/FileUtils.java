package com.mbcu.nc.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;

import com.mbcu.nc.json.Content;
import com.mbcu.nc.main.Config;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

public class FileUtils {

	private static String PERCENT = "%";
	private static String SINGLE_QUOTE = "'";

	public static final  String sanitize(String url){
		url = url.substring(Config.PROTOCOL_HTTP.length());
		url =  url.replaceAll("/", PERCENT);
		url = url.replaceAll("\\?", SINGLE_QUOTE);
		return url;
	}
	
	
	public static final void save(Content content, String path, String url){
		try {		
			Writer out = new BufferedWriter(new OutputStreamWriter( new FileOutputStream(path + FileUtils.sanitize(url) + ".txt"), "UTF-8"));
			out.write(GsonUtils.toJson(content));
			out.close();			
			System.out.println("Done");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
