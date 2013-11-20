package com.mbcu.nc.crawlers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
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
import com.mbcu.nc.utils.GsonUtils;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import edu.uci.ics.crawler4j.url.WebURL;

public class CnnCrawler extends CrawlerParent {

	public static final String HOST = "edition.cnn.com";
	public static final String PATH_RESULT = "M:\\data\\res\\cnn\\";

	@Override
	public void onStart() {
		makeDir(PATH_RESULT);
	}

	static Set<String> ignores = new HashSet<String>() {
		{
			add("edition.cnn.com/video");
			add("edition.cnn.com/services/podcasting");
			add("edition.cnn.com/exchange/blogs");
			add("edition.cnn.com/mobile");
			add("edition.cnn.com/services/rss");
			add("edition.cnn.com/EMAIL");
			add("edition.cnn.com/email");
			add("edition.cnn.com/interactive_legal");
			add("edition.cnn.com/privacy");
			add("edition.cnn.com/about");
			add("edition.cnn.com/feedback");
			add("edition.cnn.com/help");
			add("weather.edition.cnn.com/weather");
			add("edition.cnn.com/profile");
			add("edition.cnn.com/espanol");
			add("edition.cnn.com/CNN/Programs");
			add("edition.cnn.com/HLN");

		}
	};

	static Set<String> seeds = new HashSet<String>() {
		{
			add(Config.PROTOCOL_HTTP + HOST);
			add("http://edition.cnn.com/WORLD/europe/archive/");
			add("http://edition.cnn.com/WORLD/asiapcf/archive/");
			add("http://edition.cnn.com/WORLD/africa/archive/");
			add("http://edition.cnn.com/WORLD/meast/archive/");
			add("http://edition.cnn.com/WORLD/americas/archive/");
			add("http://edition.cnn.com/BUSINESS/archive/");
			add("http://edition.cnn.com/TECH/archive/");
			add("http://edition.cnn.com/SHOWBIZ/Movies/archive/");
			add("http://edition.cnn.com/SHOWBIZ/TV/archive/");
			add("http://edition.cnn.com/SHOWBIZ/Music/archive/");
			add("http://edition.cnn.com/SPORT/archive/");
			add("http://edition.cnn.com/SPORT/football/");
			add("http://edition.cnn.com/SPORT/golf/");
			add("http://edition.cnn.com/SPORT/tennis/");
			add("http://edition.cnn.com/SPORT/motorsport/");
			add("http://edition.cnn.com/CNNI/Programs/main.sail/");
			add("http://worldsport.blogs.cnn.com/");
			add("http://edition.cnn.com/cnnsi/");
			add("http://edition.cnn.com/TRAVEL/archive/");
			add("http://edition.cnn.com/TRAVEL/business.travel/archive/");
			add("http://edition.cnn.com/topics/");
		}
	};

	public static final CrawlController buildController() throws Exception {

		CrawlConfig config = new CrawlConfig();
		config.setCrawlStorageFolder(Config.crawlStorageFolder);
		config.setResumableCrawling(false);
		config.setMaxDepthOfCrawling(3);
		/*
		 * Instantiate the controller for this crawl.
		 */
		PageFetcher pageFetcher = new PageFetcher(config);
		RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
		RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig,
				pageFetcher);
		CrawlController controller = new CrawlController(config, pageFetcher,
				robotstxtServer);
		for (String seed : seeds) {
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
			
		File f = new File(PATH_RESULT + sanitize(href) + ".txt");
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

			Content content = parse(html);
			content.setUrl(url);
			save(content, PATH_RESULT, url);
		}
	}

	private Content parse(String html) {
		Content content = new Content();
		content.setHtml(html);
		Document doc = Jsoup.parse(html);

		Elements contents = doc.select("p");
		Iterator<Element> it = contents.iterator();
		String cString = "";
		while (it.hasNext()) {
			
			Element c = it.next();
			String temp = c.text();
			if (!temp.contains("Loading weather data ...")){
				cString += " " + temp;
			}			
		}
		content.setText(cString);

		content.setTitle(doc.select("meta[itemprop=headline][property=og:title]").attr("content"));
		content.setAuthor(doc.select("meta[itemprop=author][name=author]").attr("content"));
		String date = doc.select("meta[itemprop=dateCreated]").attr("content");
		if (date != null && !date.trim().isEmpty()) {
			try {
			DateTimeFormatter formatter = ISODateTimeFormat.dateTimeNoMillis().withZoneUTC();
			DateTime dt = DateTime.parse(date, formatter);
			content.setTimestamp(dt.getMillis() / 1000);
			}catch (IllegalArgumentException e){
				
			}
		}
		return content;
	}

}
