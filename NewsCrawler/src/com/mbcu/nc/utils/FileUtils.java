package com.mbcu.nc.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.zip.GZIPOutputStream;

import com.mbcu.nc.json.Content;

public class FileUtils {
	private final static String PREFIX_BATCH = "batch";
	private final static String PREFIX_NODE  = "node";
	
	public static final String getNode(String url){
		int hc = url.hashCode();
		hc = (hc < 0 ? -hc : hc); // Make sure it's positive
		hc = (hc % 100000000); //Eight zeroes, maximum value is 99,999,999
		 
		// Now to get the STRING form, pad with leading zeroes:
		String hashstr = String.valueOf(hc);
		 
		while(hashstr.length() < 8) { 
			hashstr = "0" + hashstr; 
		}
		
		return hashstr;
	}
		
	public static final String getBatch(String url){		
		int hc = url.hashCode();
		hc = (hc < 0 ? -hc : hc); 
		hc = (hc % 1000); 
		 
		// Now to get the STRING form, pad with leading zeroes:
		String hashstr = String.valueOf(hc);
		 		
		while(hashstr.length() < 3) { 
			hashstr = "0" + hashstr; 
		}
		return hashstr;
	}
	
	public static final String getHtmlFilePath(String corpFolder, String url){
		String batchFolder = PREFIX_BATCH + getBatch(url);
		String file = PREFIX_NODE + getNode(url);		
		return (corpFolder + "//" + batchFolder + "//" +  file + ".html.z");
	}
	
	
	public static boolean makeFile(String path){
		boolean res = false;
		File file = new File(path);
		if (!file.exists()) {
			file.getParentFile().mkdirs();
			try {
				file.createNewFile();
				System.out.println("File is created!");
				res = true;
			} catch (IOException e) {
				e.printStackTrace();
			}			
		}			
		return res;
	}
	
	public static final void saveHtml(String path, String html){
		
		if (makeFile(path)){
			Writer out = null;
			try {		
		        GZIPOutputStream zip = new GZIPOutputStream(new FileOutputStream(new File(path)));
				out = new BufferedWriter(new OutputStreamWriter(zip, "UTF-8"));
				out.write(html);
				out.close();			
				System.out.println("Done");
			} catch (IOException e) {
				e.printStackTrace();
			}finally{           
		        if(out != null)
					try {
						out.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
		    }
		}	
	}		
	
}
