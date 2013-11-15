package com.mbcu.nc.crawlers;

import java.util.regex.Pattern;

import edu.uci.ics.crawler4j.crawler.WebCrawler;

public class CrawlerParent extends WebCrawler {

	final static Pattern FILTERS = Pattern
			.compile(".*(\\.(css|js|bmp|gif|jpe?g"
					+ "|png|tiff?|mid|mp2|mp3|mp4"
					+ "|wav|avi|mov|mpeg|ram|m4v|pdf"
					+ "|xml"
					+ "|rm|smil|wmv|swf|wma|zip|rar|gz))$");

}
