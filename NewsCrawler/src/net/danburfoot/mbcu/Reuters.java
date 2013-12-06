package net.danburfoot.mbcu;

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
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import edu.uci.ics.crawler4j.url.WebURL;

public class Reuters extends Base {
	
	public static final String HOST = "reuters.com";
	public static final String FOLDER = Config.PATH_BASE + "reuters" + File.separator;
		
		
	static Set<String> ignores = new HashSet<String>(){{
		add("reuters.com/news/video");
		add("reuters.com/news/pictures");
		add("blogs.reuters.com/photo");
		add("reuters.com/tools");
		add("reuters.com/article/comments");
		add("reuters.com/article/interactive");
		add("reuters.com/article/slideshow");
		add("reuters.com/article/video");
		add("reuters.com/assets");
		add("reuters.com/finance/commodity?symbol");
		add("reuters.com%/finance/markets/index?symbol");
		add("reuters.com/finance/stocks");
		add("reuters.com/finance/bonds");		
		add("reuters.com/finance/currencies");
		add("reuters.com/finance/commodities");
		add("reuters.com/finance/futures");
		add("reuters.com/finance/funds");
		add("reuters.com/finance/markets/indices");
		add("reuters.com/sectors");
		add("reuters.com/video");
	}
	};
	
	static Set<String> seeds = new HashSet<String>(){{		
		add(Config.PROTOCOL_HTTP + "www." + HOST);	
		add(Config.PROTOCOL_HTTP + "www." + HOST + "/");	
		add(Config.PROTOCOL_HTTP + HOST);						
		add("http://www.reuters.com/finance");
		add("http://www.reuters.com/finance/economy");
		add("http://www.reuters.com/news/media");
		add("http://www.reuters.com/finance/smallBusiness");
		add("http://www.reuters.com/finance/deals");
		add("http://www.reuters.com/finance/EarningsUS");
		add("http://www.reuters.com/subjects/aerospace-and-defense");
		add("http://www.reuters.com/finance/summits");		
		add("http://www.reuters.com/finance/markets");
		add("http://www.reuters.com/finance/markets/us");
		add("http://www.reuters.com/finance/markets/europe");
		add("http://www.reuters.com/finance/markets/asia");
		add("http://www.reuters.com/finance/global-market-data");
		
		add("http://www.reuters.com/finance/deals/mergers");

		add("http://blogs.reuters.com/breakingviews/");
		add("http://www.reuters.com/finance/markets/dividends");
		add("http://www.reuters.com/news/world");
		add("http://www.reuters.com/news/us");
		add("http://www.reuters.com/places/brazil");
		add("http://www.reuters.com/places/china");
		add("http://www.reuters.com/subjects/euro-zone");
		add("http://www.reuters.com/places/japan");
		add("http://www.reuters.com/places/africa");
		add("http://www.reuters.com/places/mexico");
		add("http://www.reuters.com/places/russia");
		add("http://blogs.reuters.com/india");		
		add("http://www.reuters.com/politics");
		add("http://www.reuters.com/subjects/supreme-court");
		add("http://www.reuters.com/news/technology");
		add("http://www.reuters.com/news/science");
		add("http://www.reuters.com/news/lifestyle");
		add("http://www.reuters.com/bizfinance/technology/Top100Innovators");		
		add("http://blogs.reuters.com/us/");
		add("http://blogs.reuters.com/john-lloyd");
		add("http://blogs.reuters.com/felix-salmon");
		add("http://blogs.reuters.com/jackshafer");
		add("http://blogs.reuters.com/david-rohde");
		add("http://blogs.reuters.com/nader-mousavizadeh/");
		add("http://blogs.reuters.com/nicholas-wapshott/");
		add("http://blogs.reuters.com/bethany-mclean/");
		add("http://blogs.reuters.com/anatole-kaletsky");
		add("http://blogs.reuters.com/edgy-optimist/");
		add("http://blogs.reuters.com/edward-hadas/");
		add("http://blogs.reuters.com/hugo-dixon/");
		add("http://blogs.reuters.com/ian-bremmer/");
		add("http://blogs.reuters.com/lawrencesummers/");
		add("http://blogs.reuters.com/great-debate");
		add("http://blogs.reuters.com/reihan-salam/");
		add("http://blogs.reuters.com/mark-leonard/");
		add("http://blogs.reuters.com/stories-id-like-to-see/");
		add("http://blogs.reuters.com/alison-frankel/");
		add("http://www.reuters.com/finance/personal-finance");
		add("http://blogs.reuters.com/taxbreak/");
		add("http://blogs.reuters.com/globalinvesting");
		add("http://blogs.reuters.com/muniland/");
		add("http://blogs.reuters.com/unstructuredfinance/");
		add("http://blogs.reuters.com/linda-stern/");
		add("http://blogs.reuters.com/mark-miller/");
		add("http://blogs.reuters.com/john-wasik/");
		add("http://blogs.reuters.com/jim-saft/");
		add("http://blogs.reuters.com/james-saft/");
		add("http://blogs.reuters.com/aananthalakshmi/");
		add("http://www.reuters.com/news/health");
		add("http://www.reuters.com/news/sports");
		add("http://www.reuters.com/news/entertainment/arts");
		add("http://blogs.reuters.com/faithworld");
		add("http://www.reuters.com/news/entertainment");
		add("http://www.reuters.com/news/oddlyEnough");
		add("http://blogs.reuters.com/cancer-in-context/");
		add("http://www.reuters.com/assets/siteindex");		
		add("http://www.reuters.com/places/afghanistan");
		add("http://www.reuters.com/subjects/aerospace-and-defense");
		add("http://www.reuters.com/subjects/airlines");
		add("http://www.reuters.com/subjects/AlertNet");
		add("http://www.reuters.com/places/antarctica");
		add("http://www.reuters.com/places/arctic");
		add("http://www.reuters.com/places/argentina");
		add("http://www.reuters.com/news/entertainment/arts");
		add("http://www.reuters.com/places/australia");
		add("http://www.reuters.com/subjects/autos");
		add("http://www.reuters.com/people/barack-obama");
		add("http://www.reuters.com/places/brazil");
		add("http://www.reuters.com/subjects/g20");
		add("http://www.reuters.com/places/germany");
		add("http://www.reuters.com/places/greece");
		add("http://www.reuters.com/news/health");
		add("http://www.reuters.com/subjects/healthcare");
		add("http://www.reuters.com/subjects/housing-market");
		add("http://www.reuters.com/subjects/hurricanes");
		add("http://www.reuters.com/places/indonesia");
		add("http://www.reuters.com/subjects/ipad");
		add("http://www.reuters.com/places/iran");
		add("http://www.reuters.com/places/iraq");
		add("http://www.reuters.com/places/ireland");
		add("http://www.reuters.com/places/israel");
		add("http://www.reuters.com/subjects/nuclear-power");
		add("http://www.reuters.com/places/pakistan");
		add("http://www.reuters.com/news/entertainment/people");
		add("http://www.reuters.com/news/politics");
		add("http://www.reuters.com/politics");
		add("http://www.reuters.com/places/portugal");
		add("http://www.reuters.com/subjects/retail");
		add("http://www.reuters.com/people/rupert-murdoch");
		add("http://www.reuters.com/places/russia");
		add("http://www.reuters.com/people/sarah-palin");
		add("http://www.reuters.com/places/saudi-arabia");
		add("http://www.reuters.com/news/science");
		add("http://www.reuters.com/places/syria");
		add("http://www.reuters.com/places/china");
		add("http://www.reuters.com/places/congo");
		add("http://www.reuters.com/subjects/cop16");
		add("http://www.reuters.com/subjects/corporate-crime");
		add("http://www.reuters.com/places/cuba");
		add("http://www.reuters.com/politics/elections-2012");
		add("http://www.reuters.com/people/elizabeth-warren");
		add("http://www.reuters.com/subjects/entrepreneurs-edge");
		add("http://www.reuters.com/subjects/euro-zone");
		add("http://www.reuters.com/subjects/executive-compensation");
		add("http://www.reuters.com/subjects/fashion");
		add("http://www.reuters.com/news/entertainment/film");
		add("http://www.reuters.com/places/france");
		add("http://www.reuters.com/subjects/facebook");
		add("http://www.reuters.com/people/gabrielle-giffords");
		add("http://www.reuters.com/places/ivory-coast");
		add("http://www.reuters.com/places/italy");
		add("http://www.reuters.com/places/japan");
		add("http://www.reuters.com/places/kyrgyzstan");
		add("http://www.reuters.com/subjects/china-labor");
		add("http://www.reuters.com/subjects/livingwithcancer");
		add("http://www.reuters.com/places/libya");
		add("http://www.reuters.com/subjects/masters2010");
		add("http://www.reuters.com/news/media");
		add("http://www.reuters.com/places/mexico");
		add("http://www.reuters.com/subjects/mexico-drug-war");
		add("http://www.reuters.com/news/entertainment/music");
		add("http://www.reuters.com/finance/muni-bonds");
		add("http://www.reuters.com/places/myanmar");
		add("http://www.reuters.com/subjects/natural-disasters");
		add("http://www.reuters.com/places/north-korea");
		add("http://www.reuters.com/places/norway");
		add("http://www.reuters.com/places/south-korea");
		add("http://www.reuters.com/news/sports");
		add("http://www.reuters.com/subjects/state-budgets");
		add("http://www.reuters.com/subjects/swine-flu");
		add("http://www.reuters.com/news/entertainment/television");
		add("http://www.reuters.com/places/thailand");
		add("http://www.reuters.com/people/tiger-woods");
		add("http://www.reuters.com/places/tunisia");
		add("http://www.reuters.com/places/turkey");
		add("http://www.reuters.com/news/us");
		add("http://www.reuters.com/subjects/united-nations");
		add("http://www.reuters.com/places/venezuela");
		add("http://www.reuters.com/people/warren-buffett");
		add("http://www.reuters.com/subjects/wikileaks");
		add("http://www.reuters.com/subjects/wine");
		add("http://www.reuters.com/news/world");
		add("http://www.reuters.com/subjects/yuan");
		add("http://www.reuters.com/finance/commodities");
		add("http://www.reuters.com/finance/commodities/energy");
		add("http://www.reuters.com/finance/commodities/metals");
		add("http://www.reuters.com/finance/deals/regulatory");
		add("http://www.reuters.com/finance/economy");
		add("http://www.reuters.com/finance/earnings");
		add("http://www.reuters.com/finance/futures");
		add("http://www.reuters.com/finance/futures/cboe");
		add("http://www.reuters.com/finance/smallBusiness/resourceCenter");
		add("http://www.reuters.com/finance/stocks/lookup");
		add("http://www.reuters.com/finance/markets/hotStocks");
		add("http://www.reuters.com/finance/markets/advances");
		add("http://www.reuters.com/finance/commodities/grains");
		add("http://www.reuters.com/finance/commodities/softs");
		add("http://www.reuters.com/finance/commodities/oilseeds");
		add("http://www.reuters.com/finance/commodities/livestock");
		add("http://www.reuters.com/finance/deals/mergers");
		add("http://www.reuters.com/finance/deals/ipos");
		add("http://www.reuters.com/finance/deals/bankruptcy");
		add("http://www.reuters.com/finance/deals/privateCapital");
		add("http://www.reuters.com/finance/deals/hedgeFunds");
		add("http://www.reuters.com/finance/futures/cme");
		add("http://www.reuters.com/finance/futures/cbt");
		add("http://www.reuters.com/finance/futures/nbt");
		add("http://www.reuters.com/finance/markets");
		add("http://www.reuters.com/finance/global-market-data");
		add("http://www.reuters.com/finance/markets/asia");
		add("http://www.reuters.com/finance/markets/europe");
		add("http://www.reuters.com/finance/markets/uk");
		add("http://www.reuters.com/finance/markets/us");
		add("http://www.reuters.com/finance/personal-finance");
		add("http://www.reuters.com/finance/markets/mostActives");
		add("http://www.reuters.com/finance/markets/indices");
		add("http://www.reuters.com/finance/markets/earnings");
		add("http://www.reuters.com/finance/markets/conferenceCalls?country=USA&viewBy=type");
		add("http://www.reuters.com/finance/markets/dividends");
		add("http://www.reuters.com/finance/markets/conferenceCalls?country=USA&viewBy=type");
		add("http://www.reuters.com/finance/markets/dividends");
		add("http://www.reuters.com/finance/markets/upgrades");
		add("http://www.reuters.com/finance/markets/downgrades");
		add("http://www.reuters.com/finance/summits");
		add("http://www.reuters.com/finance/summits/past");
		add("http://www.reuters.com/people/roger-federer");
		add("http://www.reuters.com/people/andy-roddick");
		add("http://www.reuters.com/people/andy-murray");
		add("http://www.reuters.com/people/novak-djokovic");
		add("http://www.reuters.com/people/rafael-nadal");
		add("http://www.reuters.com/people/venus-williams");
		add("http://www.reuters.com/people/serena-williams");
		add("http://www.reuters.com/people/caroline-wozniacki");
		add("http://www.reuters.com/people/jelena-jankovic");
		add("http://www.reuters.com/people/kim-clijsters");
		add("http://www.reuters.com/sectors/industries/overview?industryCode=4");
		add("http://www.reuters.com/sectors/industries/overview?industryCode=6");
		add("http://www.reuters.com/sectors/industries/overview?industryCode=7");
		add("http://www.reuters.com/sectors/industries/overview?industryCode=8");
		add("http://www.reuters.com/sectors/industries/overview?industryCode=10");
		add("http://www.reuters.com/sectors/industries/overview?industryCode=11");
		add("http://www.reuters.com/sectors/industries/overview?industryCode=190");
		add("http://www.reuters.com/sectors/industries/overview?industryCode=192");
		add("http://www.reuters.com/sectors/industries/overview?industryCode=193");
		add("http://www.reuters.com/sectors/industries/overview?industryCode=15");
		add("http://www.reuters.com/sectors/industries/overview?industryCode=21");
		add("http://www.reuters.com/sectors/industries/overview?industryCode=22");
		add("http://www.reuters.com/sectors/industries/overview?industryCode=29");
		add("http://www.reuters.com/sectors/industries/overview?industryCode=30");
		add("http://www.reuters.com/sectors/industries/overview?industryCode=15");
		add("http://www.reuters.com/sectors/industries/overview?industryCode=16");
		add("http://www.reuters.com/sectors/industries/overview?industryCode=17");
		add("http://www.reuters.com/sectors/industries/overview?industryCode=18");
		add("http://www.reuters.com/sectors/industries/overview?industryCode=22");
		add("http://www.reuters.com/sectors/industries/overview?industryCode=23");
		add("http://www.reuters.com/sectors/industries/overview?industryCode=24");
		add("http://www.reuters.com/sectors/industries/overview?industryCode=26");
		add("http://www.reuters.com/sectors/industries/overview?industryCode=30");
		add("http://www.reuters.com/sectors/industries/overview?industryCode=32");
		add("http://www.reuters.com/sectors/industries/overview?industryCode=33");
		add("http://www.reuters.com/sectors/industries/overview?industryCode=37");
		add("http://www.reuters.com/sectors/industries/overview?industryCode=39");
		add("http://www.reuters.com/sectors/industries/overview?industryCode=40");
		add("http://www.reuters.com/sectors/industries/overview?industryCode=41");
		add("http://www.reuters.com/sectors/industries/overview?industryCode=42");
		add("http://www.reuters.com/sectors/industries/overview?industryCode=46");
		add("http://www.reuters.com/sectors/industries/overview?industryCode=48");
		add("http://www.reuters.com/sectors/industries/overview?industryCode=50");
		add("http://www.reuters.com/sectors/industries/overview?industryCode=51");
		add("http://www.reuters.com/sectors/industries/overview?industryCode=194");
		add("http://www.reuters.com/sectors/industries/overview?industryCode=195");
		add("http://www.reuters.com/sectors/industries/overview?industryCode=196");
		add("http://www.reuters.com/sectors/industries/overview?industryCode=55");
		add("http://www.reuters.com/sectors/industries/overview?industryCode=58");
		add("http://www.reuters.com/sectors/industries/overview?industryCode=60");
		add("http://www.reuters.com/sectors/industries/overview?industryCode=61");
		add("http://www.reuters.com/sectors/industries/overview?industryCode=63");
		add("http://www.reuters.com/sectors/industries/overview?industryCode=64");
		add("http://www.reuters.com/sectors/industries/overview?industryCode=66");
		add("http://www.reuters.com/sectors/industries/overview?industryCode=67");
		add("http://www.reuters.com/sectors/industries/overview?industryCode=68");
		add("http://www.reuters.com/sectors/industries/overview?industryCode=72");
		add("http://www.reuters.com/sectors/industries/overview?industryCode=73");
		add("http://www.reuters.com/sectors/industries/overview?industryCode=74");
		add("http://www.reuters.com/sectors/industries/overview?industryCode=77");
		add("http://www.reuters.com/sectors/industries/overview?industryCode=45");
		add("http://www.reuters.com/sectors/industries/overview?industryCode=78");
		add("http://www.reuters.com/sectors/industries/overview?industryCode=79");
		add("http://www.reuters.com/sectors/industries/overview?industryCode=199");
		add("http://www.reuters.com/sectors/industries/overview?industryCode=80");
		add("http://www.reuters.com/sectors/industries/overview?industryCode=200");
		add("http://www.reuters.com/sectors/industries/overview?industryCode=83");
		add("http://www.reuters.com/sectors/industries/overview?industryCode=84");
		add("http://www.reuters.com/sectors/industries/overview?industryCode=85");
		add("http://www.reuters.com/sectors/industries/overview?industryCode=88");
		add("http://www.reuters.com/sectors/industries/overview?industryCode=89");
		add("http://www.reuters.com/sectors/industries/overview?industryCode=90");
		add("http://www.reuters.com/sectors/industries/overview?industryCode=91");
		add("http://www.reuters.com/sectors/industries/overview?industryCode=93");
		add("http://www.reuters.com/sectors/industries/overview?industryCode=94");
		add("http://www.reuters.com/sectors/industries/overview?industryCode=95");
		add("http://www.reuters.com/sectors/industries/overview?industryCode=96");
		add("http://www.reuters.com/sectors/industries/overview?industryCode=97");
		add("http://www.reuters.com/sectors/industries/overview?industryCode=100");
		add("http://www.reuters.com/sectors/industries/overview?industryCode=101");
		add("http://www.reuters.com/sectors/industries/overview?industryCode=203");
		add("http://www.reuters.com/sectors/industries/overview?industryCode=204");
		add("http://www.reuters.com/sectors/industries/overview?industryCode=205");
		add("http://www.reuters.com/sectors/industries/overview?industryCode=103");
		add("http://www.reuters.com/sectors/industries/overview?industryCode=104");
		add("http://www.reuters.com/sectors/industries/overview?industryCode=206");
		add("http://www.reuters.com/sectors/industries/overview?industryCode=109");
		add("http://www.reuters.com/sectors/industries/overview?industryCode=110");
		add("http://www.reuters.com/sectors/industries/overview?industryCode=111");
		add("http://www.reuters.com/sectors/industries/overview?industryCode=113");
		add("http://www.reuters.com/sectors/industries/overview?industryCode=114");
		add("http://www.reuters.com/sectors/industries/overview?industryCode=115");
		add("http://www.reuters.com/sectors/industries/overview?industryCode=118");
		add("http://www.reuters.com/sectors/industries/overview?industryCode=119");
		add("http://www.reuters.com/sectors/industries/overview?industryCode=120");
		add("http://www.reuters.com/sectors/industries/overview?industryCode=123");
		add("http://www.reuters.com/sectors/industries/overview?industryCode=124");
		add("http://www.reuters.com/sectors/industries/overview?industryCode=128");
		add("http://www.reuters.com/sectors/industries/overview?industryCode=129");
		add("http://www.reuters.com/sectors/industries/overview?industryCode=131");
		add("http://www.reuters.com/sectors/industries/overview?industryCode=207");
		add("http://www.reuters.com/sectors/industries/overview?industryCode=209");
		add("http://www.reuters.com/sectors/industries/overview?industryCode=208");
		add("http://www.reuters.com/sectors/industries/overview?industryCode=210");
		add("http://www.reuters.com/sectors/industries/overview?industryCode=134");
		add("http://www.reuters.com/sectors/industries/overview?industryCode=211");
		add("http://www.reuters.com/sectors/industries/overview?industryCode=137");
		add("http://www.reuters.com/sectors/industries/overview?industryCode=138");
		add("http://www.reuters.com/sectors/industries/overview?industryCode=139");
		add("http://www.reuters.com/sectors/industries/overview?industryCode=140");
		add("http://www.reuters.com/sectors/industries/overview?industryCode=212");
		add("http://www.reuters.com/sectors/industries/overview?industryCode=213");
		add("http://www.reuters.com/sectors/industries/overview?industryCode=214");
		add("http://www.reuters.com/sectors/industries/overview?industryCode=214");
		add("http://www.reuters.com/sectors/industries/overview?industryCode=215");
		add("http://www.reuters.com/sectors/industries/overview?industryCode=216");
		add("http://www.reuters.com/sectors/industries/overview?industryCode=217");
		add("http://www.reuters.com/sectors/industries/overview?industryCode=151");
		add("http://www.reuters.com/sectors/industries/overview?industryCode=152");
		add("http://www.reuters.com/sectors/industries/overview?industryCode=154");
		add("http://www.reuters.com/sectors/industries/overview?industryCode=155");
		add("http://www.reuters.com/sectors/industries/overview?industryCode=158");
		add("http://www.reuters.com/sectors/industries/overview?industryCode=159");
		add("http://www.reuters.com/sectors/industries/overview?industryCode=160");
		add("http://www.reuters.com/sectors/industries/overview?industryCode=164");
		add("http://www.reuters.com/sectors/industries/overview?industryCode=165");
		add("http://www.reuters.com/sectors/industries/overview?industryCode=167");
		add("http://www.reuters.com/sectors/industries/overview?industryCode=169");
		add("http://www.reuters.com/sectors/industries/overview?industryCode=170");
		add("http://www.reuters.com/sectors/industries/overview?industryCode=173");
		add("http://www.reuters.com/sectors/industries/overview?industryCode=174");
		add("http://www.reuters.com/sectors/industries/overview?industryCode=178");
		add("http://www.reuters.com/sectors/industries/overview?industryCode=179");
		add("http://www.reuters.com/sectors/industries/overview?industryCode=183");
		add("http://www.reuters.com/sectors/industries/overview?industryCode=185");
		add("http://www.reuters.com/sectors/industries/overview?industryCode=189");
		add("http://blogs.reuters.com/faithworld");
		add("http://blogs.reuters.com/breakingviews");
		add("http://blogs.reuters.com/breakingviews");
		add("http://blogs.reuters.com/talesfromthetrail");						
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
			if(!href.startsWith(Config.PROTOCOL_HTTP + "blogs." + HOST)){
				return false;
			}
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
			String temp = c.select("p").text();
			if (temp.equals("Back to top") // these are end of story cues 
					|| temp.contains("Thomson Reuters is the world's largest international multimedia news agency, providing investing news, world news, business news,")
					|| temp.contains("list of exchanges and delays, please click here")
					|| temp.contains("Follow me on Twitter")					
					|| temp.contains("Our day's top images, in-depth photo essays and offbeat slices of life")
					|| temp.contains("Reuters Breakingviews is the world's leading source of agenda-setting financial insight")
					|| temp.contains("Breakingviews has published a selection of books for purchase and download")
					|| temp.equals("LATEST TITLES")
					|| temp.contains("(Additional reporting by")
					|| temp.contains("Our top photos from the past 24 hours")
					)
			{
				break;
			}else{
				texts.add(temp);
			}
			
		}
		content.setTexts(texts);
		
		content.setTitle(doc.select("META[property=og:title]").attr("content"));

		Element place = doc.select("span.articleLocation").first();
		if (place != null) {
			content.setPlace(place.text());
		}

		Element author = doc.select("p.byline").first();
		if (author != null) {
			String authorString = author.text();
			if (authorString != null && authorString.startsWith("By ")) {
				authorString = authorString.substring(3);
			}
			content.setAuthor(authorString);
		}
		Element date = doc.select("span.timestamp").first();
		if (date != null) {
			String dateString = date.text();
			if (dateString != null && !dateString.trim().isEmpty()) {
				try {
					DateTimeFormatter formatter = DateTimeFormat.forPattern("E MMM d, yyyy hh:mma z");
					DateTime dt = DateTime.parse(dateString, formatter);
					content.setTimestamp(dt.getMillis() / 1000);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
								
					try{
						System.out.println("Retrying");		
//						22 Nov 2013
						DateTimeFormatter formatter = DateTimeFormat.forPattern("d MMM yyyy");
						DateTime dt = DateTime.parse(dateString, formatter);
						content.setTimestamp(dt.getMillis() / 1000);
					}catch (IllegalArgumentException ee) {
						ee.printStackTrace();
						try {
							System.out.println("Retrying again");		
	//						7:13am EST
							DateTimeFormatter formatter = DateTimeFormat.forPattern("hh:mma z");
							DateTime dt = DateTime.parse(dateString, formatter);
							content.setTimestamp(dt.getMillis() / 1000);
						}catch(IllegalArgumentException eee){						
							eee.printStackTrace();
							try{
//								Jun 23, 2011	very mysterious error. parsing works if string retyped ??!
								System.out.println("Retrying x3");	
								DateTimeFormatter formatter = DateTimeFormat.forPattern("MMM d, yyyy");
								DateTime dt = DateTime.parse(dateString, formatter);
								content.setTimestamp(dt.getMillis() / 1000);
							}catch(IllegalArgumentException eeee){
								eeee.printStackTrace();
							}
						}
						
					}
				}
			}
		}
		return content;
	}



}
