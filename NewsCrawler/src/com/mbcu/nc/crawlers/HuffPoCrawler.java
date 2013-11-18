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

public class HuffPoCrawler extends CrawlerParent {

	public static final String HOST = "huffingtonpost.com";
	public static final String PATH_RESULT = "M:\\data\\res\\huffpo\\";

	@Override
	public void onStart() {
		makeDir(PATH_RESULT);
	}

	static Set<String> ignores = new HashSet<String>() {
		{
			add("huffingtonpost.com/users/login");
			add("huffingtonpost.com/makehome");
			add("huffingtonpost.com/syndication");
			add("huffingtonpost.com/jobs");
			add("huffingtonpost.com/faq");
			add("huffingtonpost.com/terms");
			add("huffingtonpost.com/privacy");
			add("huffingtonpost.com/comment");
			add("huffingtonpost.com/p/huffington-post");
			add("huffingtonpost.com/contact/");
			add("huffingtonpost.com/news/huffpolitics-blog/");
			add("elections.huffingtonpost");
			add("voces.huffingtonpost");
			add("videos.huffingtonpost");
			add("live.huffingtonpost");

		}
	};

	static Set<String> seeds = new HashSet<String>() {
		{
			add("http://www.huffingtonpost.com/?country=US");
			add("http://www.huffingtonpost.com/");
			add("http://www.huffingtonpost.com/the-news/");
			add("http://www.huffingtonpost.com/world/");
			add("http://www.huffingtonpost.com/business/");
			add("http://www.huffingtonpost.com/small-business/");
			add("http://www.huffingtonpost.com/news/");
			add("http://www.huffingtonpost.com/news/small-business-success-stories/");
			add("http://www.huffingtonpost.com/news/small-business-news-and-trends/");
			add("http://www.huffingtonpost.com/news/how-i-did-it/");
			add("http://www.huffingtonpost.com/news/what-is-working-small-businesses/");
			add("http://www.huffingtonpost.com/news/@buildabiz/");
			add("http://www.huffingtonpost.com/news/c-suite/");
			add("http://www.huffingtonpost.com/news/financial-education/");
			add("http://www.huffingtonpost.com/money/");
			add("http://www.huffingtonpost.com/news/credit-cards/");
			add("http://www.huffingtonpost.com/news/savings/");
			add("http://www.huffingtonpost.com/news/debt/");
			add("http://www.huffingtonpost.com/media/");
			add("http://www.huffingtonpost.com/sports/");
			add("http://www.huffingtonpost.com/education/");
			add("http://www.huffingtonpost.com/crime/");
			add("http://www.huffingtonpost.com/weird-news/");
			add("http://www.huffingtonpost.com/good-news/");
			add("http://www.huffingtonpost.com/entertainment/");
			add("http://www.huffingtonpost.com/celebrity/");
			add("http://www.huffingtonpost.com/comedy/");
			add("http://www.huffingtonpost.com/arts/");
			add("http://www.huffingtonpost.com/books/");
			add("http://www.huffingtonpost.com/tv/");
			add("http://www.huffingtonpost.com/healthy-living/");
			add("http://www.huffingtonpost.com/gps-for-the-soul/");
			add("http://www.huffingtonpost.com/style/");
			add("http://www.huffingtonpost.com/home/");
			add("http://www.huffingtonpost.com/food/");
			add("http://www.huffingtonpost.com/taste/");
			add("http://www.huffingtonpost.com/weddings/");
			add("http://www.huffingtonpost.com/travel/");
			add("http://www.huffingtonpost.com/parents/");
			add("http://www.huffingtonpost.com/divorce/");
			add("http://www.huffingtonpost.com/50/");
			add("http://www.huffingtonpost.com/marlothomas/");
			add("http://www.huffingtonpost.com/own/");
			add("http://www.huffingtonpost.com/tech/");
			add("http://www.huffingtonpost.com/science/");
			add("http://www.huffingtonpost.com/green/");
			add("http://www.huffingtonpost.com/tedweekends/");
			add("http://www.huffingtonpost.com/code/");
			add("http://www.huffingtonpost.com/1-hour-recipes/");
			add("http://www.huffingtonpost.com/women/");
			add("http://www.huffingtonpost.com/black-voices/");
			add("http://www.huffingtonpost.com/latino-voices/");
			add("http://www.huffingtonpost.com/gay-voices/");
			add("http://www.huffingtonpost.com/religion/");
			add("http://www.huffingtonpost.com/college/");
			add("http://www.huffingtonpost.com/teen/");
			add("http://www.huffingtonpost.com/impact/");
			add("http://www.huffingtonpost.com/chicago/");
			add("http://www.huffingtonpost.com/dc/");
			add("http://www.huffingtonpost.com/denver/");
			add("http://www.huffingtonpost.com/detroit/");
			add("http://www.huffingtonpost.com/hawaii/");
			add("http://www.huffingtonpost.com/los-angeles/");
			add("http://www.huffingtonpost.com/miami/");
			add("http://www.huffingtonpost.com/new-york/");
			add("http://www.huffingtonpost.com/san-francisco/");

			add("http://www.huffingtonpost.com/business/the-news/");
			add("http://www.huffingtonpost.com/small-business/the-news/");
			add("http://www.huffingtonpost.com/money/the-news/");
			add("http://www.huffingtonpost.com/news/credit-cards/");
			add("http://www.huffingtonpost.com/news/savings/");
			add("http://www.huffingtonpost.com/news/debt/");
			add("http://www.huffingtonpost.com/media/the-news/");
			add("http://www.huffingtonpost.com/sports/the-news/");
			add("http://www.huffingtonpost.com/education/the-news/");
			add("http://www.huffingtonpost.com/crime/the-news/");
			add("http://www.huffingtonpost.com/weird-news/the-news/");
			add("http://www.huffingtonpost.com/good-news/the-news/");
			add("http://www.huffingtonpost.com/entertainment/the-news/");
			add("http://www.huffingtonpost.com/celebrity/the-news/");
			add("http://www.huffingtonpost.com/comedy/the-news/");
			add("http://www.huffingtonpost.com/arts/the-news/");
			add("http://www.huffingtonpost.com/books/the-news/");
			add("http://www.huffingtonpost.com/tv/the-news/");
			add("http://www.huffingtonpost.com/healthy-living/the-news/");
			add("http://www.huffingtonpost.com/gps-for-the-soul/the-news/");
			add("http://www.huffingtonpost.com/style/the-news/");
			add("http://www.huffingtonpost.com/home/the-news/");
			add("http://www.huffingtonpost.com/food/the-news/");
			add("http://www.huffingtonpost.com/taste/the-news/");
			add("http://www.huffingtonpost.com/weddings/the-news/");
			add("http://www.huffingtonpost.com/travel/the-news/");
			add("http://www.huffingtonpost.com/parents/the-news/");
			add("http://www.huffingtonpost.com/divorce/the-news/");
			add("http://www.huffingtonpost.com/50/the-news/");
			add("http://www.huffingtonpost.com/marlothomas/the-news/");
			add("http://www.huffingtonpost.com/own/the-news/");
			add("http://www.huffingtonpost.com/tech/the-news/");
			add("http://www.huffingtonpost.com/science/the-news/");
			add("http://www.huffingtonpost.com/green/the-news/");
			add("http://www.huffingtonpost.com/tedweekends/the-news/");
			add("http://www.huffingtonpost.com/code/the-news/");

			add("http://www.huffingtonpost.com/women/the-news/");
			add("http://www.huffingtonpost.com/black-voices/the-news/");
			add("http://www.huffingtonpost.com/latino-voices/the-news/");
			add("http://www.huffingtonpost.com/gay-voices/the-news/");
			add("http://www.huffingtonpost.com/religion/the-news/");
			add("http://www.huffingtonpost.com/college/the-news/");
			add("http://www.huffingtonpost.com/teen/the-news/");
			add("http://www.huffingtonpost.com/impact/the-news/");
			add("http://www.huffingtonpost.com/chicago/the-news/");
			add("http://www.huffingtonpost.com/dc/the-news/");
			add("http://www.huffingtonpost.com/denver/the-news/");
			add("http://www.huffingtonpost.com/detroit/the-news/");
			add("http://www.huffingtonpost.com/hawaii/the-news/");
			add("http://www.huffingtonpost.com/los-angeles/the-news/");
			add("http://www.huffingtonpost.com/miami/the-news/");
			add("http://www.huffingtonpost.com/new-york/the-news/");
			add("http://www.huffingtonpost.com/san-francisco/the-news/");

			add("http://www.huffingtonpost.com/politics/");
			add("http://www.huffingtonpost.com/news/pollster/");
			add("http://www.huffingtonpost.com/tedtalks/");

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

		for (String s : ignores) {
			if (href.contains(s)) {
				return false;
			}
		}

		File f = new File(PATH_RESULT + FileUtils.sanitize(href) + ".txt");
		if (f.exists())
			return false;

		href = href.toLowerCase();
		return !FILTERS.matcher(href).matches();
	}

	@Override
	public void visit(Page page) {

		String url = page.getWebURL().getURL();
		for (String s : seeds) {
			if (url.equals(s)) {
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
			FileUtils.save(content, PATH_RESULT, url);
		}
	}

	private Content parse(String html) {
		Content content = new Content();
		Document doc = Jsoup.parse(html);

		Elements contents = doc.select("p");
		Iterator<Element> it = contents.iterator();
		String cString = "";
		while (it.hasNext()) {
			Element c = it.next();
			cString += c.select("p").text();
		}
		content.setHtml(cString);

		Element title = doc.select("title").first();
		content.setTitle(title != null ? title.text() : null);
		
//		<meta name="author" content="Elise Foley" />
		String author = doc.select("meta[author]").attr("content");
		content.setAuthor(author);

		String dateTime = doc.select("meta[sailthru.date]").attr("content");
		if (dateTime != null && !dateTime.trim().isEmpty()) {
			try{
				DateTimeFormatter formatter = DateTimeFormat.forPattern("E, d MMM yyyy hh:mm:ss Z");
				DateTime dt = DateTime.parse(dateTime, formatter);
				content.setTimestamp(dt.getMillis() / 1000);			
			}catch(IllegalArgumentException e){			
			}
		}

		return content;
	}

}
