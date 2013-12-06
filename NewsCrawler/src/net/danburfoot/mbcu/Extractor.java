package net.danburfoot.mbcu;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Extractor {

	/*
	 * Demonstration of how to extract / parse text from gzipped html
	 */
	public static void main(String[] args) {
//		FileUtils.deleteRecursive(Voa.FOLDER, "txt");
		extractor(new Voa(), Voa.FOLDER, 0);
		System.out.print("Done");
	}

	/**
	 * Extract text from gzip files of raw html and save it as .txt
	 * @param corpusTask 
	 * Task that was used to crawl websites
	 * @param corpusFolder 
	 * Base folder of corpus data
	 * @param target
	 * Target file format (0 : json of {@link Content}, 1 : text only)
	 */
	public static void extractor(Base corpusTask, String corpusFolder, int format) {

		
		ArrayList<File> batchList = FileUtils.listFolders(corpusFolder);

		for (File batch : batchList) {
			for (File node : batch.listFiles()) {
				if (node.getName().endsWith(".html.z")) {
					System.out.println("Processing: " + node.getAbsolutePath());
					
					// Build the txt path
					String txtPath = node.getAbsolutePath().replace("html.z", "txt");
					
					// What if file already exist
					File txt = new File(txtPath);
					if (txt.exists()){
						txt.delete();
					}
					
					// Decompress zip, read content as String
					String raw = FileUtils.gunzipHtml(node.getAbsolutePath());
					
					// Parse the content, get all <p>s
					Content content = corpusTask.extract(raw);
					String out = "";
					switch (format){
					case 0 : 
						out = GsonUtils.toJson(content);
						break;
					case 1 :
						List<String> ps = content.getTexts();
						for (String s : ps){
							out += s + "\n";
						}				
					}
										
					// Save it 
					if (!out.trim().isEmpty()){
						FileUtils.saveTxt(txtPath, out);
					}else{
						System.out.println("Skipped");
					}
				}

			}			
		}
	}

}
