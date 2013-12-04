package com.mbcu.nc.main;

import org.apache.commons.codec.binary.Base64;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import com.mbcu.nc.tasks.Bloomberg;
import com.mbcu.nc.tasks.ChicagoTribune;
import com.mbcu.nc.tasks.Cnn;
import com.mbcu.nc.tasks.Base;
import com.mbcu.nc.tasks.HuffPo;
import com.mbcu.nc.tasks.Reuters;
import com.mbcu.nc.tasks.Time;
import com.mbcu.nc.tasks.UsaToday;
import com.mbcu.nc.tasks.Voa;
import com.mbcu.nc.utils.FileUtils;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

public class Crawler {

	/**
	 * Demonstration of how to crawl a website 
	 * Start by instantiating {@link CrawlController} that controls delay time, depth, etc. , configurable in each task
	 * Then start the process by calling either start or startNonBlocking (the latter is non blocking so it won't wait)
	 */
	public static void main(String[] args) throws Exception {	
		
		int numberOfCrawlers = 7;

//		CrawlController crawlChiTrib = ChicagoTribune.buildController();
//        crawlChiTrib.startNonBlocking(ChicagoTribune.class, numberOfCrawlers);  
		
//		CrawlController crawlCnn= Cnn.buildController();
//		crawlCnn.startNonBlocking(Cnn.class, numberOfCrawlers);

//		CrawlController crawlTime = Time.buildController();
//		crawlTime.startNonBlocking(Time.class, numberOfCrawlers);
		
		
//		CrawlController reutersController = Reuters.buildController();
//		reutersController.startNonBlocking(Reuters.class, 7);

		
//		CrawlController usatController = UsaToday.buildController();
//		usatController.startNonBlocking(UsaToday.class, numberOfCrawlers);


		CrawlController voaController = Voa.buildController();
		voaController.startNonBlocking(Voa.class, numberOfCrawlers);


		
		
	}
}
