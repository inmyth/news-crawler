package com.mbcu.nc.tasks;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.mbcu.nc.json.Content;
import com.mbcu.nc.main.Config;
import com.mbcu.nc.utils.FileUtils;
import com.mbcu.nc.utils.GsonUtils;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import edu.uci.ics.crawler4j.url.WebURL;

public class Time extends Base{
	
	public static final String HOST = "time.com";
	public static final String FOLDER = Config.PATH_BASE + "time" + File.separator;
		
	static Set<String> ignores = new HashSet<String>(){{
		add("content.time.com/time/rss");
		add("content.time.com/time/photogallery");
		add("time.com/time/mobile");
		add("content.time.com/time/mobile-apps");
		add("content.time.com/time/reprints");			
		add("content.time.com/time/magazine");
		add("life.time.com");
		add("content.time.com/time/video");
		add("content.time.com/time/photoessays");
		add("content.time.com/time/faq");
		add("content.time.com/time/contactus");
		add("time.com/time/archive/feedback");
		add("content.time.com/time/archive");
		add("content.time.com/time/covers");
		add("content.time.com/time/static");
		
	}
	};
	
	static Set<String> seeds = new HashSet<String>(){{		
		add(Config.PROTOCOL_HTTP + HOST + "/time");		
		add(Config.PROTOCOL_HTTP + "www." + HOST + "/time/");		
		add("http://newsfeed.time.com");
		add("http://nation.time.com");
		add("http://swampland.time.com");
		add("http://world.time.com");
		add("http://business.time.com");
		add("http://techland.time.com");
		add("http://healthland.time.com");
		add("http://science.time.com");
		add("http://science.time.com");
		add("http://content.time.com/time/specials");
		add("http://time100.time.com");
		add("http://poy.time.com");
		add("http://content.time.com/time/quotes");		
		add("http://content.time.com/time/top10/");
		add("http://content.time.com/time/static/sitemap/");
		
	}
	};
	
	
	public static final CrawlController buildController() throws Exception{		
        
        CrawlConfig config = new CrawlConfig();
        config.setCrawlStorageFolder(Config.crawlStorageFolder);     
        config.setResumableCrawling(false);
        config.setMaxDepthOfCrawling(3);
        config.setPolitenessDelay(3600);
        config.setUserAgentString("Mozilla/5.0 (Windows NT 6.1; rv:25.0) Gecko/20100101 Firefox/25.0");
        /*
         * Instantiate the controller for this crawl.
         */
        PageFetcher pageFetcher = new PageFetcher(config);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
        CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);
        for (String seed : seeds){
        	controller.addSeed(seed);
        }            
        return controller;
		
	}
	
	@Override
	public boolean shouldVisit(WebURL url) {
		String href = url.getURL();
		
		if (!href.contains(HOST))
			return false;
		
		for (String s : ignores){
			if (href.contains(s)){
				return false;
			}			
		}
			
		File f = new File(FileUtils.getHtmlFilePath(FOLDER, href));
		if (f.exists())
			return false;
		
		href = href.toLowerCase();
		return !FILTERS.matcher(href).matches();
	}
	
	@Override
	public void visit(Page page) {
		String url = page.getWebURL().getURL();
		for (String s : seeds){
			if (url.equals(s)){
				return;
			}
		}
		
		System.out.println("URL: " + url);
		if (page.getParseData() instanceof HtmlParseData) {
			HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
			String text = htmlParseData.getText();
			String html = htmlParseData.getHtml();
			List<WebURL> links = htmlParseData.getOutgoingUrls();

			System.out.println("Text length: " + text.length());
			System.out.println("Html length: " + html.length());
			System.out.println("Number of outgoing links: " + links.size());

			String path = FileUtils.getHtmlFilePath(FOLDER, url);
			FileUtils.gzipHtml(path, html);
		}
	}
	    	
	@Override
	public Content extract(String html) {
		Content content = new Content();
		Document doc = Jsoup.parse(html);

		Elements contents = doc.select("p");
		Iterator<Element> it = contents.iterator();
		ArrayList<String> texts = new ArrayList<String>();
		while (it.hasNext()) {
			Element c = it.next();
			String temp = c.text();
			Elements cc = c.getElementsByAttribute("class");
			Element parent = c.parent();
			if (!temp.trim().isEmpty() &&				
				!parent.hasClass("fyre-comment") &&
				cc.isEmpty() && 
				!temp.contains("Follow @TIME")
				)
			{
				texts.add(temp);
			}
		}
		content.setTexts(texts);

		Element title = doc.select("h1.entry-title").first();
		content.setTitle(title != null ? title.text() : null);

		Element author = doc.select("span.entry-byline").first();
		if (author != null) {
			String authorString = author.text();
			if (authorString != null && authorString.startsWith("By ")) {
				authorString = authorString.substring(3);
			}
			content.setAuthor(authorString);
		}

		Element date = doc.select("span.entry-date").first();
		if (date != null) {
			String dateString = date.text();
			// "Monday, Jan 15, 2007";
			if (dateString != null && !dateString.trim().isEmpty()) {
				try {
					dateString = dateString.replace(".", "");
					DateTimeFormatter formatter = DateTimeFormat.forPattern("E, MMM d, yyyy");
					DateTime dt = DateTime.parse(dateString, formatter);
					content.setTimestamp(dt.getMillis() / 1000);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
					try {
//						"Nov 25, 2013"
						DateTimeFormatter formatter = DateTimeFormat.forPattern("MMM dd, yyyy");
						DateTime dt = DateTime.parse(dateString, formatter);
						content.setTimestamp(dt.getMillis() / 1000);
					}catch(IllegalArgumentException ee){
//						3 hours ago ??
						ee.printStackTrace();						
					}
				}
			}
		}
		return content;
	}

}
