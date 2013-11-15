package com.mbcu.nc.crawlers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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

public class CnnCrawler extends CrawlerParent{
	
	public static final String HOST = "edition.cnn.com";
	public static final String PATH_RESULT = "M:\\data\\res\\cnn\\";
	
	@Override
	public void onStart() {
		File file = new File(PATH_RESULT);
		if (!file.exists()) {
			if (file.mkdir()) {
				System.out.println("Directory is created!");
			} else {
				System.out.println("Failed to create directory!");
			}
		}
	}
	
	static Set<String> ignores = new HashSet<String>(){{
		add("http://edition.cnn.com/video/");
		add("http://edition.cnn.com/services/podcasting/");
		add("http://edition.cnn.com/exchange/blogs/");
		add("http://edition.cnn.com/mobile/");
		add("http://edition.cnn.com/services/rss/");
		add("http://edition.cnn.com/EMAIL/");
		add("http://edition.cnn.com/interactive_legal.html");
		add("http://edition.cnn.com/privacy.html");
		add("http://edition.cnn.com/about/");
		add("http://edition.cnn.com/feedback/");
		add("http://edition.cnn.com/help/");
		add("http://weather.edition.cnn.com/weather");
		add("http://edition.cnn.com/profile/");
		add("http://edition.cnn.com/espanol/");
		add("http://edition.cnn.com/CNN/Programs/");
		add("http://edition.cnn.com/HLN/");
		
	}
	};
	
	static Set<String> overlaps = new HashSet<String>(){{
		add(Config.PROTOCOL_HTTP + HOST);
	}	
	};
	
	static Set<String> seeds = new HashSet<String>(){{
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
		String href = url.getURL().toLowerCase();
		
		if (!href.contains(HOST))
			return false;
		
		for (String s : ignores){
			if (href.contains(s))
				return false;		
		}
		
		for (String s : overlaps){
			if (href.equals(s))
				return false;			
		}
		
		for (String s : seeds){
			if (href.equals(s))
				return false;
		}
		
		File f = new File(PATH_RESULT + FileUtils.sanitize(href) + ".txt");
		if (f.exists())
			return false;
		
		return !FILTERS.matcher(href).matches();

	}
	
	   @Override
	    public void visit(Page page) {          
	            String url = page.getWebURL().getURL();      
	            System.out.println("URL: " + url);            
	            if (page.getParseData() instanceof HtmlParseData) {
	                    HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
	                    String text = htmlParseData.getText();
	                    String html = htmlParseData.getHtml();
	                    List<WebURL> links = htmlParseData.getOutgoingUrls();

	                    System.out.println("Text length: " + text.length());
	                    System.out.println("Html length: " + html.length());
	                    System.out.println("Number of outgoing links: " + links.size());
	                    
	                    try {                   	 
	                    	Content content = parse(page.getWebURL().getDomain(), new String(page.getContentData(), "UTF-8"));   
	                    	if (content == null)
	                    		return;
	                    	
	                    	File file = new File(PATH_RESULT + FileUtils.sanitize(url) + ".txt");                       
	            			FileWriter fw = new FileWriter(file.getAbsoluteFile());
	            			BufferedWriter bw = new BufferedWriter(fw);
	            			bw.write(GsonUtils.toJson(content));
	            			bw.close();           
	            			System.out.println("Done");	             
	            		} catch (IOException e) {
	            			e.printStackTrace();
	            		}
	            }
	    }
	    
	    private Content parse(String domain, String html){
			Content content = new Content();
			Document doc = Jsoup.parse(html);
//			<meta content="en-US" itemprop="inLanguage"/>
//			Element language = doc.select("meta[itemprop=inLanguage]").first();	
//			String locale = language.select("content").toString();
//			if (locale == null || !locale.contains("en")){
//				return null;
//			}			
			Elements contents = doc.select("p");
			Iterator<Element> it = contents.iterator();
			String cString = "";
			while (it.hasNext()){
				Element c = it.next();
				cString += c.select("p").text();
			}
			content.setHtml(cString);
			
			content.setTitle(doc.select("meta[itemprop=headline][property=og:title]").attr("content"));
			content.setAuthor(doc.select("meta[itemprop=author][name=author]").attr("content")); 
			String date = doc.select("meta[itemprop=dateCreated]").attr("content");
			if (date != null && !date.trim().isEmpty()){
				DateTimeFormatter formatter = ISODateTimeFormat.dateTimeNoMillis().withZoneUTC();
				DateTime dt = DateTime.parse(date, formatter);
				content.setTimestamp(dt.getMillis()/1000);				
			}
			return content;
		}

}
