package com.hanwb.utils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import org.apache.commons.io.FileUtils;

public class SRTUtils {

	public void writeStringToFile(File file, String data){
		try {
			FileUtils.writeStringToFile(file, data, Charset.forName("UTF-8"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
