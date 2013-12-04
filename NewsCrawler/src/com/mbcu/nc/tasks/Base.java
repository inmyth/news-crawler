package com.mbcu.nc.tasks;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;
import java.util.regex.Pattern;

import com.mbcu.nc.json.Content;
import com.mbcu.nc.main.Config;
import com.mbcu.nc.utils.GsonUtils;

import edu.uci.ics.crawler4j.crawler.WebCrawler;

public abstract class Base extends WebCrawler {

	final static Pattern FILTERS = Pattern
			.compile(".*(\\.(css|js|bmp|gif|jpe?g"
					+ "|png|tiff?|mid|mp2|mp3|mp4"
					+ "|wav|avi|mov|mpeg|ram|m4v|pdf"
					+ "|xml|ico|cssx"
					+ "|rm|smil|wmv|swf|wma|zip|rar|gz))$");

	public abstract Content extract(String html);
	

}
