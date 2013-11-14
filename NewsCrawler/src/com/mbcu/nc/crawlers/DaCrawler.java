package com.mbcu.nc.crawlers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
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

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

public class DaCrawler extends WebCrawler{
    private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|bmp|gif|jpe?g" 
            + "|png|tiff?|mid|mp2|mp3|mp4"
            + "|wav|avi|mov|mpeg|ram|m4v|pdf" 
            + "|rm|smil|wmv|swf|wma|zip|rar|gz))$");

    
	@Override
	public boolean shouldVisit(WebURL url) {
		String href = url.getURL().toLowerCase();
		File f = new File(Config.PATH_RESULT + FileUtils.sanitize(href) + ".txt");
		if (f.exists())
			return false;
		else {
			return !FILTERS.matcher(href).matches();
		}
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
                    	File file = new File(Config.PATH_RESULT + FileUtils.sanitize(url) + ".txt");                       
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
    	if (domain.equals(Config.HOST_CHICAGO_TRIBUNE)){
    		Document doc = Jsoup.parse(html);
    		Element author = doc.select("span[class=byline bordered]").first();  		
    		content.setAuthor(author != null ? author.ownText() : null);	
    		Element story = doc.select("div#story-body-text").first();
    		content.setHtml(story != null ? story.text() : null);
    		Element title = doc.select("h1").first();
    		content.setTitle(title != null ? title.ownText() : null);
    		
    		String datetime;
    		Element date = doc.select("span.dateString").first();
    		datetime = date != null ? date.ownText() : null;
    		
    		if (datetime == null){
    			return content;
    		}
    		
    		String ts = null;
    		Element time = doc.select("span.timeString").first();
    		if (time != null){
    			ts = time.ownText();
    			if (ts.contains("a.m.")){
    				ts.replace("a.m.", "AM");
    			}else if (ts.contains("p.m")){
    				ts.replace("p.m.", "PM");
    			}
    		}    		
    		DateTimeFormatter f ;
    		if (ts != null){
    			f = DateTimeFormat.forPattern("MMM d, yyyy hh:mm a z");
    			datetime += " " + ts;   			  			
    		}else{
    			f = DateTimeFormat.forPattern("MMM d, yyyy");
    		}    		
    		if (f != null){
    			DateTime dt = DateTime.parse(datetime, f);
    			content.setTimestamp(dt.getMillis() / 1000);    			
    		}
    	}
    	return content;   	
    }

}
