package com.mbcu.nc.tasks;

import java.io.File;
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

public class UsaToday extends Base{

	public static final String HOST = "usatoday.com";
	public static final String FOLDER = Config.PATH_BASE + "usatoday" + File.separator;

	static Set<String> ignores = new HashSet<String>(){{
		
		add("usatoday.com/big-page");
		add("usatoday.com/media");
		add("usatoday.com/videos");
		add("usatoday.com/puzzles");
		add("service.usatoday.com");
		add("usatoday.com/shop");
		add("usatoday.com/mobile-apps");
		add("usatoday.com/rss");
		add("usatoday.com/reporters");
		add("usatoday.com/contactus");
		add("usatoday.com/opinion/cartoons/");
		add("usatoday.com/about");
		add("usatoday30.usatoday.com");
		add("usatoday.com/mediakit");
		add("reg.e.usatoday.com");
		add("usatoday.com/shop");
		add("m.usatoday.com");
		add("usatoday.com/editorial-policy");
		add("usatoday.com/legal");
		add("developer.usatoday.com");
		add("usatoday.com/about-usaweekend");
		add("usatoday.com/educate");
		add("usatoday.com/picture-gallery");
		add("usatoday.com/weather");
	}
	};
	
	static Set<String> seeds = new HashSet<String>(){{		
		add(Config.PROTOCOL_HTTP + "www." + HOST);	
		add(Config.PROTOCOL_HTTP + "www." + HOST + "/");	
		add("http://www.usatoday.com/news/");
		add("http://www.usatoday.com/sports/");
		add("http://www.usatoday.com/life/");
		add("http://www.usatoday.com/money/");
		add("http://www.usatoday.com/tech/");
		add("http://www.usatoday.com/travel/");
		add("http://www.usatoday.com/opinion/");
		add("http://www.usatoday.com/news/nation/");
		add("http://www.usatoday.com/news/world/");
		add("http://www.usatoday.com/news/politics/");
		add("http://www.usatoday.com/sports/nfl");
		add("http://www.usatoday.com/sports/nba");
		add("http://www.usatoday.com/sports/nhl");
		add("http://www.usatoday.com/life/people/");
		add("http://www.usatoday.com/life/movies/");
		add("http://www.usatoday.com/life/music/");
		add("http://www.usatoday.com/money/markets/");
		add("http://www.usatoday.com/money/business/");
		add("http://www.usatoday.com/money/personal-finance/");
		add("http://www.usatoday.com/tech/personal-tech/");
		add("http://www.usatoday.com/tech/gaming/");
		add("http://www.usatoday.com/travel/destinations/");
		add("http://www.usatoday.com/travel/flights/");
		add("http://www.usatoday.com/travel/cruises/");
		add("http://www.usatoday.com/travel/deals/");
		add("http://traveltips.usatoday.com/");				
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
		
		if (!href.contains(HOST))
			return false;
		
		if (!href.startsWith(Config.PROTOCOL_HTTP + "www." + HOST)){
			return false;
		}

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
			/*
			 * Assuming the content is wrapped in p tags that have no attributes
			 */
			Elements cc = c.getElementsByAttribute("class");
			
			if (cc.isEmpty())
			{
				String ptemp = c.ownText();
				String[] split1 = ptemp.split("—"); // first regex
				if (split1.length > 1 && split1[0].length() < 25){ // estimate of the longest place string
					int l = split1[0].length();
					int nCaps = 0;
					for (int i = 0; i < l; i++){
						if (Character.isUpperCase(split1[0].charAt(i))){
							nCaps++;
							if (nCaps > l / 2){
								content.setPlace(split1[0].trim());
								break;
							}
						}				
					}
				}
				String[] split2 = ptemp.split("--"); // second regex
				if (split2.length > 1 && split2[0].length() < 25){ // estimate of the longest place string
					int l = split2[0].length();
					int nCaps = 0;
					for (int i = 0; i < l; i++){
						if (Character.isUpperCase(split2[0].charAt(i))){
							nCaps++;
							if (nCaps > l / 2){
								content.setPlace(split2[0].trim());
								break;
							}
						}				
					}
				}
				if (!ptemp.trim().isEmpty())
					texts.add(ptemp);				
			}
		}
		content.setTexts(texts);		

		Element title = doc.select("h1[itemprop=headline]").first();
		if (title != null){
			content.setTitle(title.text());
		}
		
		Element author = doc.select("span.asset-metabar-author.asset-metabar-item").first();
		if (author != null){
			content.setAuthor(author.text());
		}
		
		Element time = doc.select("span.asset-metabar-time.asset-metabar-item.nobyline").first();
		String ts;
		if (time != null) {
			ts = time.text();
			if (ts.contains("a.m.")) {
				ts = ts.replace("a.m.", "AM");
			} else if (ts.contains("p.m")) {
				ts = ts.replace("p.m.", "PM");
			}
			try {
				DateTimeFormatter formatter = DateTimeFormat.forPattern("hh:mm a z MMM d, yyyy");
				DateTime dt = DateTime.parse(ts, formatter);
				content.setTimestamp(dt.getMillis() / 1000);		
			}catch (IllegalArgumentException e){
				try{
//				first false format = 8 a.m. EST November 26, 2013
				DateTimeFormatter formatter = DateTimeFormat.forPattern("h a z MMM d, yyyy");
				DateTime dt = DateTime.parse(ts, formatter);
				content.setTimestamp(dt.getMillis() / 1000);	
				}catch (IllegalArgumentException ee){
					ee.printStackTrace();
				}
				
			}
			
		}
		
		return content;
	}

}
