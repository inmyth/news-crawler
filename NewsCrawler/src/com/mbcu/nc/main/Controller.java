package com.mbcu.nc.main;

import com.mbcu.nc.crawlers.ChicagoTribuneCrawler;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

public class Controller {

	public static void main(String[] args) throws Exception {		
		int numberOfCrawlers = 7;

		CrawlController crawlChiTrib = ChicagoTribuneCrawler.buildController();
        crawlChiTrib.startNonBlocking(ChicagoTribuneCrawler.class, numberOfCrawlers);   
	}
}
