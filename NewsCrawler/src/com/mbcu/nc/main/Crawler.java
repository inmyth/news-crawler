package com.mbcu.nc.main;

import org.apache.commons.codec.binary.Base64;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import com.mbcu.nc.crawlers.BloombergCrawler;
import com.mbcu.nc.crawlers.ChicagoTribuneCrawler;
import com.mbcu.nc.crawlers.CnnCrawler;
import com.mbcu.nc.crawlers.CrawlerParent;
import com.mbcu.nc.crawlers.HuffPoCrawler;
import com.mbcu.nc.crawlers.ReutersCrawler;
import com.mbcu.nc.crawlers.TimeCrawler;
import com.mbcu.nc.crawlers.UsaTodayCrawler;
import com.mbcu.nc.crawlers.VoaCrawler;
import com.mbcu.nc.utils.FileUtils;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

public class Crawler {

	public static void main(String[] args) throws Exception {	
		
		int numberOfCrawlers = 7;

//		CrawlController crawlChiTrib = ChicagoTribuneCrawler.buildController();
//        crawlChiTrib.startNonBlocking(ChicagoTribuneCrawler.class, numberOfCrawlers);  
		
//		CrawlController crawlCnn= CnnCrawler.buildController();
//		crawlCnn.startNonBlocking(CnnCrawler.class, numberOfCrawlers);

//		CrawlController crawlTime = TimeCrawler.buildController();
//		crawlTime.startNonBlocking(TimeCrawler.class, numberOfCrawlers);
		
//		CrawlController huffPoCrawler = HuffPoCrawler.buildController();
//		huffPoCrawler.startNonBlocking(HuffPoCrawler.class, numberOfCrawlers);
		
		CrawlController reutersController = ReutersCrawler.buildController();
		reutersController.startNonBlocking(ReutersCrawler.class, 7);

		
//		CrawlController usatController = UsaTodayCrawler.buildController();
//		usatController.startNonBlocking(UsaTodayCrawler.class, numberOfCrawlers);


//		CrawlController voaController = VoaCrawler.buildController();
//		voaController.startNonBlocking(VoaCrawler.class, numberOfCrawlers);


		
	}
}
