package com.mbcu.nc.main;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import com.mbcu.nc.crawlers.ChicagoTribuneCrawler;
import com.mbcu.nc.crawlers.CnnCrawler;
import com.mbcu.nc.crawlers.HuffPoCrawler;
import com.mbcu.nc.crawlers.ReutersCrawler;
import com.mbcu.nc.crawlers.TimeCrawler;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

public class Controller {

	public static void main(String[] args) throws Exception {		
		int numberOfCrawlers = 7;

//		CrawlController crawlChiTrib = ChicagoTribuneCrawler.buildController();
//        crawlChiTrib.startNonBlocking(ChicagoTribuneCrawler.class, numberOfCrawlers);  
//		
//		CrawlController crawlCnn= CnnCrawler.buildController();
//		crawlCnn.startNonBlocking(CnnCrawler.class, numberOfCrawlers);

//		CrawlController crawlTime = TimeCrawler.buildController();
//		crawlTime.startNonBlocking(TimeCrawler.class, numberOfCrawlers);
		
//		CrawlController huffPoCrawler = HuffPoCrawler.buildController();
//		huffPoCrawler.startNonBlocking(HuffPoCrawler.class, numberOfCrawlers);
		
		CrawlController reutersController = ReutersCrawler.buildController();
		reutersController.startNonBlocking(ReutersCrawler.class, 7);

		


		
		
	}
}
