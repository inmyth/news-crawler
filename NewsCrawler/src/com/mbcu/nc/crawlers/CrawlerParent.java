package com.mbcu.nc.crawlers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.regex.Pattern;

import com.mbcu.nc.json.Content;
import com.mbcu.nc.main.Config;
import com.mbcu.nc.utils.GsonUtils;

import edu.uci.ics.crawler4j.crawler.WebCrawler;

public class CrawlerParent extends WebCrawler {

	final static Pattern FILTERS = Pattern
			.compile(".*(\\.(css|js|bmp|gif|jpe?g"
					+ "|png|tiff?|mid|mp2|mp3|mp4"
					+ "|wav|avi|mov|mpeg|ram|m4v|pdf"
					+ "|xml|ico"
					+ "|rm|smil|wmv|swf|wma|zip|rar|gz))$");
	
	public void makeDir(String path){
		File file = new File(path);
		if (!file.exists()) {
			if (file.mkdir()) {
				System.out.println("Directory is created!");
			} else {
				System.out.println("Failed to create directory!");
			}
		}		
	}
	
	public static final void save(Content content, String path, String url){
		try {		
			Writer out = new BufferedWriter(new OutputStreamWriter( new FileOutputStream(path + sanitize(url) + ".txt"), "UTF-8"));
			out.write(GsonUtils.toJson(content));
			out.close();			
			System.out.println("Done");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	private static String PERCENT = "%";
	private static String SINGLE_QUOTE = "'";

	public static final  String sanitize(String url){
		url = url.substring(Config.PROTOCOL_HTTP.length());
		url =  url.replaceAll("/", PERCENT);
		url = url.replaceAll("\\?", SINGLE_QUOTE);
		return url;
	}

}
