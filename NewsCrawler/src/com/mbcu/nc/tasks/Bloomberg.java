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

public class Bloomberg extends Base{
	
	public static final String HOST = "bloomberg.com";
	public static final String FOLDER  =  Config.PATH_BASE + "bloomberg" + File.separator;
	

	static Set<String> ignores = new HashSet<String>(){{
		add("bloomberg.com/markets");
		add("bloomberg.com/personal-finance/calculators");
		add("bloomberg.com/technology/slideshows");
		add("bloomberg.com/video");
		add("bloomberg.com/sustainability/slideshows");
		add("bloomberg.com/video/sustainability");
		add("bloomberg.com/blogs/sustainability/the-grid");
		add("bloomberg.com/tv");
		add("bloomberg.com/radio");
		add("bloomberg.com/privacy");
		add("bloomberg.com/tos");
		add("bloomberg.com/careers");
		add("bloomberg.com/now");
		add("bloomberg.com/contentlicensing");
		add("bloomberg.com/pressroom");
		add("bloomberg.com/trademarks");
		add("bloomberg.com/feedback");
		add("bloomberg.com/help.html");
		add("jobsearch.bloomberg.com");
		add("bloomberg.com/infographics");
		add("bloomberg.com/podcasts");
		add("bloomberg.com/professional");
		add("bloomberg.com/trading-solutions");
		add("bloomberg.com/quicktake");
		add("bloomberg.com/mobile");
		add("bloomberg.com/visual-data");
	}
	};
	
	static Set<String> seeds = new HashSet<String>(){{		
		add(Config.PROTOCOL_HTTP + "www." + HOST);	
		add(Config.PROTOCOL_HTTP + "www." + HOST + "/");	
		add("http://www.bloomberg.com/quickview/");
		add("http://www.bloomberg.com/news/");
		add("http://www.bloomberg.com/popular/");
		add("http://www.bloomberg.com/news/exclusive/");
		add("http://www.bloomberg.com/news/law/");
		add("http://www.bloomberg.com/entrepreneurs/");
		add("http://www.bloomberg.com/leaders/");
		add("http://www.bloomberg.com/luxury/muse/");
		add("http://www.bloomberg.com/news/economy/");
		add("http://www.bloomberg.com/news/environment/");
		add("http://www.bloomberg.com/news/science/");
		add("http://www.bloomberg.com/sports/");
		add("http://www.bloomberg.com/markets-magazine/");
		add("http://topics.bloomberg.com/bloomberg-pursuits/");
		add("http://www.bloomberg.com/news/us/");
		add("http://www.bloomberg.com/news/china/");
		add("http://www.bloomberg.com/news/europe/");
		add("http://www.bloomberg.com/news/asia/");
		add("http://www.bloomberg.com/news/uk-ireland/");
		add("http://www.bloomberg.com/news/australia-newzealand/");
		add("http://www.bloomberg.com/news/canada/");
		add("http://www.bloomberg.com/news/india-pakistan/");
		add("http://www.bloomberg.com/news/japan/");
		add("http://www.bloomberg.com/news/africa/");
		add("http://www.bloomberg.com/news/eastern-europe/");
		add("http://www.bloomberg.com/news/regions/");
		add("http://www.bloomberg.com/news/commodities/");
		add("http://www.bloomberg.com/news/currencies/");
		add("http://www.bloomberg.com/news/bonds/");
		add("http://www.bloomberg.com/news/energy-markets/");
		add("http://www.bloomberg.com/news/municipal-bonds/");
		add("http://www.bloomberg.com/news/emerging-markets/");
		add("http://www.bloomberg.com/news/funds/");
		add("http://topics.bloomberg.com/islamic-finance/");
		add("http://www.bloomberg.com/news/energy/");
		add("http://www.bloomberg.com/news/technology/");
		add("http://www.bloomberg.com/news/real-estate/");
		add("http://www.bloomberg.com/news/finance/");
		add("http://www.bloomberg.com/news/health-care/");
		add("http://www.bloomberg.com/news/transportation/");
		add("http://www.bloomberg.com/news/insurance/");
		add("http://www.bloomberg.com/news/retail/");
		add("http://www.bloomberg.com/news/media/");
		add("http://www.bloomberg.com/news/manufacturing/");
		add("http://www.bloomberg.com/view/");
		add("http://www.bloomberg.com/view/editorials/");
		add("http://www.bloomberg.com/view/commentary/");
		add("http://www.bloomberg.com/view/world-view/");
		add("http://www.bloomberg.com/blogs/view/jeffrey-goldberg/");
		add("http://www.bloomberg.com/view/the-ticker/");
		add("http://www.bloomberg.com/blogs/view/megan-mcardle/");
		add("http://www.bloomberg.com/blogs/view/matt-levine/");
		add("http://www.bloomberg.com/blogs/view/barry-ritholtz/");
		add("http://www.bloomberg.com/personal-finance/");
		add("http://www.bloomberg.com/personal-finance/saving-and-investing/");
		add("http://www.bloomberg.com/personal-finance/real-estate/");
		add("http://www.bloomberg.com/personal-finance/retirement-planning/");
		add("http://www.bloomberg.com/personal-finance/financial-advisers/");
		add("http://www.bloomberg.com/personal-finance/taxes/");
		add("http://www.bloomberg.com/blogs/personal_finance/ventured-gained/");
		add("http://www.bloomberg.com/personal-finance/consumer-spending/");
		add("http://www.bloomberg.com/personal-finance/money-gallery/");
		add("http://www.bloomberg.com/personal-finance/insurance-and-health/");
		add("http://www.bloomberg.com/personal-finance/portfolio-impact/");
		add("http://www.bloomberg.com/technology/");
		add("http://www.bloomberg.com/technology/social-media/");
		add("http://www.bloomberg.com/technology/mobile-wireless/");
		add("http://www.bloomberg.com/technology/web/");
		add("http://www.bloomberg.com/technology/enterprise-tech/");
		add("http://www.bloomberg.com/technology/tv-games-movies/");
		add("http://www.bloomberg.com/technology/apple/");
		add("http://go.bloomberg.com/tech-deals/");
		add("http://www.bloomberg.com/blogs/technology/global-tech/");
		add("http://www.bloomberg.com/politics/");
		add("http://www.bloomberg.com/politics/elections/");
		add("http://www.bloomberg.com/politics/white-house/");
		add("http://www.bloomberg.com/politics/congress/");
		add("http://www.bloomberg.com/politics/state-local/");
		add("http://go.bloomberg.com/political-economy/");
		add("http://www.bloomberg.com/politics/live-blogs/");
		add("http://www.bloomberg.com/sustainability/");
		add("http://www.bloomberg.com/sustainability/energy/");
		add("http://www.bloomberg.com/sustainability/corporate/");
		add("http://www.bloomberg.com/sustainability/policy/");
		add("http://www.bloomberg.com/sustainability/natural-resources/");
		add("http://www.bloomberg.com/sustainability/health-population/");
		add("http://www.bloomberg.com/luxury/");
		add("http://www.bloomberg.com/luxury/muse/");
		add("http://www.bloomberg.com/billionaires/");
		add("http://www.bloomberg.com/luxury/living/");
		add("http://www.bloomberg.com/luxury/property/");
		add("http://www.bloomberg.com/luxury/travel/");
		add("http://www.bloomberg.com/luxury/well-spent/");
		add("http://www.bloomberg.com/blogs/luxury/loot/");
		add("http://topics.bloomberg.com/bloomberg-pursuits/");
		add("http://www.bloomberg.com/archive/news/");
		add("http://www.bloomberg.com/sitemap/");

		
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
	public List<String> extract(String html) {
		ArrayList<String> res = new ArrayList<String>();
		Document doc = Jsoup.parse(html);
		
		Elements contents = doc.select("p");
		Iterator<Element> it = contents.iterator();
		while (it.hasNext()) {
			Element c = it.next();
			String temp =  c.text();
			if (temp.trim().isEmpty()){
				res.add(temp);				
			}
		}
		return res;
	}
	
	@Override
	public Content extract2Json(String html) {
		Content content = new Content();
		Document doc = Jsoup.parse(html);
		
		Elements contents = doc.select("p");
		Iterator<Element> it = contents.iterator();
		String cString = "";
		while (it.hasNext()) {
			Element c = it.next();
			cString += c.select("p").text();
		}
		content.setText(cString);
		

		content.setTitle(doc.select("meta[property=og:title]").attr("content"));

		Elements byLines = doc.select("cite.byline span.last");
		if (byLines != null && byLines.size() == 1){
			content.setAuthor(byLines.first().text());
		}

//		<span class='datestamp' bglocalize='true' bgdatestamp='mmm d, yyyy h:MM TT Z' epoch='1381896100000'>2013-10-16T04:01:40Z</span>
		String tsString = doc.select("span[class=datestamp]").attr("epoch");
		try{
			Long ts = Long.parseLong(tsString);
			content.setTimestamp(ts / 1000);
			
		}catch(NumberFormatException e){
			
		}
		return content;
	}


}
