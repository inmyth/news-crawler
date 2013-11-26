package com.mbcu.nc.main;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.mbcu.nc.tasks.Base;
import com.mbcu.nc.tasks.ChicagoTribune;
import com.mbcu.nc.tasks.Cnn;
import com.mbcu.nc.tasks.HuffPo;
import com.mbcu.nc.tasks.Reuters;
import com.mbcu.nc.tasks.Time;
import com.mbcu.nc.tasks.UsaToday;
import com.mbcu.nc.tasks.Voa;
import com.mbcu.nc.utils.FileUtils;

public class Extractor {

	/*
	 * Demonstration of how to extract / parse text from gzipped html
	 */
	public static void main(String[] args) {
		extractor(new HuffPo(), HuffPo.FOLDER);
		System.out.print("Done");
	}

	/**
	 * Extract text from gzip files of raw html and save it as .txt
	 * @param corpusTask 
	 * Worker that crawl and parses websites
	 * @param corpusFolder 
	 * Base folder of corpus data
	 */
	public static void extractor(Base corpusTask, String corpusFolder) {
		
		ArrayList<File> batchList = FileUtils.listFolders(corpusFolder);

		for (File batch : batchList) {
			for (File node : batch.listFiles()) {
				if (node.getName().endsWith(".html.z")) {
					System.out.println("Processing: " + node.getAbsolutePath());
					
					// Build the txt path
					String txtPath = node.getAbsolutePath().replace(".html.z", ".txt");
					
					// What if file already exist
					File txt = new File(txtPath);
					if (txt.exists()){
						txt.delete();
					}
					
					// Decompress zip, read content as String
					String raw = FileUtils.gunzipHtml(node.getAbsolutePath());
					
					// Parse the content, get all <p>s
					List<String> ps = corpusTask.extract(raw);
					String content = "";
					for (String p : ps){
						content += " " + p;
					}
					
					// Save it 
					if (!content.trim().isEmpty()){
						FileUtils.saveTxt(txtPath, content);
					}else{
						System.out.println("Skipped");
					}
				}

			}			
		}
	}

}
