package com.mbcu.nc.crawlers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

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
            			String fileName = String.valueOf(System.currentTimeMillis());
            			File file = new File("M:\\data\\res\\" + fileName + ".txt");            
            			if (!file.exists()) {
            				file.createNewFile();
            			}else{
            				return;
            			}
             
            			FileWriter fw = new FileWriter(file.getAbsoluteFile());
            			BufferedWriter bw = new BufferedWriter(fw);
            			bw.write(new String(page.getContentData()));
            			bw.close();           
            			System.out.println("Done");
             
            		} catch (IOException e) {
            			e.printStackTrace();
            		}
            }
    }

}
