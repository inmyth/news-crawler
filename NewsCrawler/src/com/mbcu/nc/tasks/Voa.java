package com.mbcu.nc.tasks;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
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

public class Voa extends Base {
	
	public static final String HOST = "www.voanews.com";
	public static final String FOLDER = Config.PATH_BASE + "voa" + File.separator;
		
	static Set<String> ignores = new HashSet<String>(){{
		add("learningenglish.voanews.com");
		add("voanews.com/login.html");
		add("voanews.com/signup.html");
		add("voanews.com/articleprintview");
		add("voanews.com/emailtofriend");
		add("voanews.com/media");
		add("voanews.com/info/podcast");
		add("voanews.com/rsspage");
		add("voanews.com/subscribe");
		add("m.voanews.com");
		add("blogs.voanews.com");
		add("voanews.com/info/contact_us");
		add("pronounce.voanews.com");
		add("voanews.com/programindex");		
		add("voanews.com/api");
	}
	};
	
	static Set<String> seeds = new HashSet<String>(){{		
		add(Config.PROTOCOL_HTTP + HOST);		
		add(Config.PROTOCOL_HTTP + HOST + "/");	
		add("http://www.voanews.com/section/usa/2203.html");
		add("http://www.voanews.com/section/africa/2204.html");
		add("http://www.voanews.com/section/asia/2205.html");
		add("http://www.voanews.com/section/middle_east/2206.html");
		add("http://www.voanews.com/section/europe/2210.html");
		add("http://www.voanews.com/section/americas/2211.html");
		add("http://www.voanews.com/section/science-and-technology/2214.html");
		add("http://www.voanews.com/section/health/2215.html");
		add("http://www.voanews.com/section/arts_and_entertainment/2216.html");
		add("http://www.voanews.com/section/economy_and_business/2217.html");
	}
	};
	
	
	public static final CrawlController buildController() throws Exception{		
        
        CrawlConfig config = new CrawlConfig();
        config.setCrawlStorageFolder(Config.crawlStorageFolder);     
        config.setResumableCrawling(false);
        config.setMaxDepthOfCrawling(3);
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
		
		if (!href.startsWith(Config.PROTOCOL_HTTP + HOST))
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
	public List<String> extract(String html) {
		ArrayList<String> res = new ArrayList<String>();
		Document doc = Jsoup.parse(html);
		Element e = doc.select("div.zoomMe").first();
		if (e != null)
			res.add(e.text());		
		return res;
	}
	    
	@Override
	public Content extract2Json(String html) {
		Content content = new Content();
		content.setHtml(html);
		Document doc = Jsoup.parse(html);

		String date = doc.select("p.article_date").first().text();
		String text = doc.select("div.zoomMe").first().text();
		content.setText(text);

		Element title = doc.select("title").first();
		content.setTitle(title != null ? title.text() : null);

		content.setAuthor(doc.select("meta[name=Author]").attr("content"));

		if (text != null && text.contains(" — ")) {
			String[] parts = text.split(" — ");
			if (parts.length == 2) {
				content.setPlace(parts[0]);
			}
		}

		if (date != null) {
			DateTimeFormatter formatter = DateTimeFormat.forPattern("MMM d, yyyy");
			DateTime dt = DateTime.parse(date, formatter);
			content.setTimestamp(dt.getMillis() / 1000);
		}

		return content;
	}

}
