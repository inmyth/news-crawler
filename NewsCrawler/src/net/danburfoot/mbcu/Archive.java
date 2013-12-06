package net.danburfoot.mbcu;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Archive {
	
	
	/*
	 * Demonstration of how to archive a corpus folder
	 */
	public static void main(String[] args) throws Exception {	
		FileUtils.batchArchiver(Voa.FOLDER);			
		System.out.println("done");
	}
	

}
