package com.creator.utils;

import java.io.*;

public class FileUtils {

	
	private static final String ENCODING_UTF8 = "utf-8";

	public static String readFile(File file) {
		return readFile(file,ENCODING_UTF8);
	}
		public static String readFile(File file,String encode) {

		String content=null;
		try {
			
			byte[] fileBytes = readBytes(file);
			content = new String(fileBytes,encode);
			 
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return content;
	}
	
	public static byte[] readBytes(File file) {
		RandomAccessFile randomAccess=null;
		try {
			
			 randomAccess	= new RandomAccessFile(file, "r");
			long len = randomAccess.length();
			byte[] allBytes = new byte[(int)len];
			randomAccess.readFully(allBytes);
			
			return allBytes;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
				try {
					if(null != randomAccess)
							randomAccess.close();
					randomAccess=null;
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		
		
		return null;
		
	}
	
}
