package com.mbcu.nc.crawlers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

















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
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import edu.uci.ics.crawler4j.url.WebURL;

public class ChicagoTribuneCrawler extends CrawlerParent{
	public static final String HOST = "chicagotribune.com";
	public static final String PATH_RESULT = "M:\\data\\res\\chitrib\\";
	
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
		add("https://members.chicagotribune.com/learn-more/");
		add("http://www.chicagotribune.com/news/weather/");
		add("https://myaccount2.chicagotribune.com/");
		add("http://touch.chicagotribune.com/"); // mobile version
		add("https://members.chicagotribune.com/eedition/");
		add("http://www.chicagotribune.com/news/plus");
		add("http://community.chicagotribune.com/"); // citizen journalism 
		add("http://www.chicagotribune.com/newsletters");
		add("http://www.chicagotribune.com/news/photo/");
		add("http://www.chicagotribune.com/videogallery/");
		add("http://www.chicagotribune.com/classified"); 
		add("http://www.chicagotribune.com/advertiser/");
		add("http://www.chicagotribune.com/videogallery/");
		add("http://galleries.apps.chicagotribune.com/chi-smack-photos");
		add("http://www.chicagotribune.com/sports/video");
		add("http://local.chicagotribune.com");
		add("http://www.chicagotribune.com/about/");
		add("http://privacy.tribune.com/");			
	}
	};
	
	static Set<String> overlaps = new HashSet<String>(){{
		add(Config.PROTOCOL_HTTP + HOST);
		add(Config.PROTOCOL_HTTP + "www." + HOST);
		add(Config.PROTOCOL_HTTP + HOST + "/");
		add(Config.PROTOCOL_HTTP + "www." + HOST + "/");
		add("http://www.chicagotribune.com/business/"); // overlap with breaking... ?
		add("http://www.chicagotribune.com/news/local/");
		add("http://www.chicagotribune.com/sports/");
		add("http://www.chicagotribune.com/entertainment/");
		add("http://www.chicagotribune.com/features/");
		add("http://www.chicagotribune.com/news/opinion/");
	}	
	};
	
	static Set<String> seeds = new HashSet<String>(){{
		add("http://www.chicagotribune.com/news/local/breaking");
		
		add("http://www.chicagotribune.com/news/local/suburbs");
		add("http://www.chicagotribune.com/news/nationworld/");
		add("http://www.chicagotribune.com/news/watchdog/");
		add("http://www.chicagotribune.com/clout");
		add("http://www.chicagotribune.com/news/politics");
		add("http://www.chicagotribune.com/news/obituaries/");
		add("http://www.chicagotribune.com/news/education");
		add("http://crime.chicagotribune.com/");
		add("http://www.chicagotribune.com/news/data/");
		add("http://www.chicagotribune.com/news/tribnation");
		add("http://www.chicagotribune.com/news/columnists");
		add("http://www.chicagotribune.com/business/breaking/");
		add("http://bluesky.chicagotribune.com/");		
		add("http://www.chicagotribune.com/business/technology");
		add("http://www.chicagotribune.com/business/yourmoney/");
		add("http://www.chicagotribune.com/business/careers");
		add("http://markets.chicagotribune.com/custom/tribune-interactive/localmktscreener/html-localmktscreener-full.asp?siteid=chicagotribune");
		add("http://www.chicagotribune.com/business/columnists/");
		add("http://www.chicagotribune.com/sports/breaking/");
		add("http://www.chicagotribune.com/sports/football/bears/");
		add("http://www.chicagotribune.com/sports/football/bears/beardownload/");
		add("http://www.chicagotribune.com/sports/hockey/blackhawks/");
		add("http://www.chicagotribune.com/sports/basketball/bulls/");
		add("http://www.chicagotribune.com/sports/baseball/cubs/");
		add("http://www.chicagotribune.com/sports/baseball/whitesox/");
		add("http://www.chicagotribune.com/sports/college/");
		add("http://www.chicagotribune.com/sports/highschool/");
		add("http://www.chicagotribune.com/sports/international/");
		add("http://www.chicagotribune.com/sports/soccer");
		add("http://www.chicagotribune.com/sports/golf/");
		add("http://www.chicagotribune.com/sports/columnists/");
		add("http://www.chicagotribune.com/entertainment/breaking");
		add("http://www.chicagotribune.com/entertainment/movies");
		add("http://www.chicagotribune.com/entertainment/music");
		add("http://www.chicagotribune.com/entertainment/theater/");
		add("http://www.chicagotribune.com/entertainment/tv");
		add("http://www.chicagotribune.com/entertainment/celebrity/aboutlastnight/");
		add("http://www.chicagotribune.com/entertainment/events/");
		add("http://games.chicagotribune.com/");
		add("http://www.chicagotribune.com/entertainment/funstuff/comics/");
		add("http://www.chicagotribune.com/entertainment/columnists/");
		add("http://www.chicagotribune.com/travel/");
		add("http://www.chicagotribune.com/health/");
		add("http://www.chicagotribune.com/features/food");
		add("http://www.chicagotribune.com/features/books");
		add("http://www.chicagotribune.com/classified/realestate/home/");
		add("http://www.chicagotribune.com/news/religion");
		add("http://www.chicagotribune.com/features/life/");
		add("http://www.chicagotribune.com/features/tribu/askamy/");
		add("http://www.chicagotribune.com/news/tribnation/events/");
		add("http://www.chicagotribune.com/features/holiday");
		add("http://www.chicagotribune.com/features/horoscopes/");
		add("http://www.chicagotribune.com/features/chi-lottery,0,3718200.htmlstory");
		add("http://www.chicagotribune.com/features/columnists/");
		add("http://www.chicagotribune.com/news/opinion/editorials/");
		add("http://www.chicagotribune.com/news/opinion/commentary/");
		add("http://www.chicagotribune.com/news/opinion/letters/");
		add("http://www.chicagotribune.com/news/opinion/chi-stantis-cartoongallery,0,2807119.cartoongallery");
		add("http://www.chicagotribune.com/news/columnists/today/");
		add("http://www.chicagotribune.com/news/columnists/all/");		
		

	}
	};

	
	public static final CrawlController buildController() throws Exception{		
        
        CrawlConfig config = new CrawlConfig();
        config.setCrawlStorageFolder(Config.crawlStorageFolder);     
        config.setResumableCrawling(false);
        config.setMaxDepthOfCrawling(2);
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
                    	Content content = parse(page.getWebURL().getDomain(), new String(page.getContentData()));                   	
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
		Elements byLines = doc.select("div.byline");
		if (byLines != null && byLines.size() > 1){
			Element authorEl = byLines.get(1);
			String author = authorEl.ownText();
			if (author.startsWith("By ")){
				author = author.substring(3);
			}
			content.setAuthor(author);
		}
		
		Element story = doc.select("div#story-body-text").first();
		content.setHtml(story != null ? story.text() : null);
		Element title = doc.select("h1").first();
		content.setTitle(title != null ? title.ownText() : null);

		String datetime;
		Element date = doc.select("span.dateString").first();
		datetime = date != null ? date.ownText() : null;

		if (datetime == null) {
			return content;
		}

		String ts = null;
		Element time = doc.select("span.timeString").first();
		if (time != null) {
			ts = time.ownText();
			if (ts.contains("a.m.")) {
				ts = ts.replace("a.m.", "AM");
			} else if (ts.contains("p.m")) {
				ts = ts.replace("p.m.", "PM");
			}
		}
		DateTimeFormatter f;
		if (ts != null) {
			f = DateTimeFormat.forPattern("MMM d, yyyy hh:mm a z");
			datetime += " " + ts;
		} else {
			f = DateTimeFormat.forPattern("MMM d, yyyy");
		}
		if (f != null) {
			DateTime dt = DateTime.parse(datetime, f);
			content.setTimestamp(dt.getMillis() / 1000);
		}

		return content;
	}

}
