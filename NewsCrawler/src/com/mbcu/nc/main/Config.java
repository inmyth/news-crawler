package com.mbcu.nc.main;

import java.io.File;

public class Config {
	/*
	 * DRIVE is the drive where data folder will be created
	 */
	private static final String DRIVE = "M:";
	
	/*
	 * FOLDER_ROOT is the root folder, containing crawl, run config, etc.
	 */
	private static final String FOLDER_ROOT = "crawl";
	
	/*
	 * FOLDER_DATA is the folder containing all Corpus data
	 */
	private static final String FOLDER_DATA = "data";
	

	
	
	
	
	public static final String PATH_ROOT = DRIVE + File.separator + FOLDER_ROOT;
	public static final String PATH_BASE = PATH_ROOT + File.separator + FOLDER_DATA + File.separator;	
	public static final String crawlStorageFolder = PATH_ROOT + File.separator + "run" + File.separator;
	public static final String PROTOCOL_HTTP = "http://";
}
