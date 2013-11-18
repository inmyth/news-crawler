package com.mbcu.nc.crawlers;

import java.io.File;
import java.util.regex.Pattern;

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
	


}
