package com.hanwb;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import com.hanwb.srt.api.SRT;
import com.hanwb.srt.api.SRTInfo;
import com.hanwb.srt.api.SRTReader;
import com.hanwb.srt.api.SRTTimeFormat;
import com.hanwb.srt.api.SRTWriterException;
import com.hanwb.utils.GoogleTranslateUtils;

/**
 *  翻译文件夹的英文字幕文件翻译为中文字幕
 */
public class SrtEnToCN {

	public static void main(String[] args) throws IOException {
		
		File source = new File("D:/asrt-directory");
		
		if(source.isDirectory()){
			File[] enSRTList = source.listFiles(new FilenameFilter() {
				@Override
				public boolean accept(File dir, String name) {
					return (name.indexOf("lang_en_") != -1 && name.endsWith(".srt"));
				}
			});
			
			for (File sf : enSRTList) {
				go(sf);
			}
		}else{
			go(source);
		}
	}
	
	public static SRTInfo goEnTranslationCn(SRTInfo sourceInfo) throws IOException {

		SRTInfo resultInfo = new SRTInfo();

		for (SRT s : sourceInfo) {

			System.out.println(s.number);
			System.out.println(SRTTimeFormat.format(s.startTime) + " --> " + SRTTimeFormat.format(s.endTime));

			List<String> newtext = new ArrayList<>();

			for (String line : s.text) {
				
				String cnLine = null;
				
				try {
					cnLine = GoogleTranslateUtils.enToCn(line);
				} catch (Exception e) {
					cnLine = GoogleTranslateUtils.enToCn(line);
				}
				
				newtext.add(line);
				newtext.add(cnLine);
				
				System.out.println(line);
				System.out.println(cnLine);
			}
			System.out.println();

			resultInfo.add(new SRT(s.number, s.startTime, s.endTime, newtext));
		}

		return resultInfo;
	}
	
    public static void write(File srtFile, SRTInfo srtInfo) throws SRTWriterException {
    	PrintWriter pw = null;
        try {
        	pw = new PrintWriter(srtFile, "GB2312");
            for (SRT srt : srtInfo) {
                pw.println(srt.number);
                pw.println(
                    SRTTimeFormat.format(srt.startTime) +
                    SRTTimeFormat.TIME_DELIMITER +
                    SRTTimeFormat.format(srt.endTime));
                for (String text : srt.text) {
                    pw.println(text);
                }
                pw.println();
            }
        } catch (IOException e) {
            throw new SRTWriterException(e);
        }finally {
        	pw.close();
		}
    }

	public static void go(File sourceFile) throws IOException {

		SRTInfo info = SRTReader.read(sourceFile);

		SRTInfo resultInfo = goEnTranslationCn(info);

		String filename = null;
		
		if(sourceFile.getName().indexOf("lang_en") != -1){
			filename = sourceFile.getName().replace("lang_en", "lang_cn");
		}else{
			filename = sourceFile.getName().substring(0, sourceFile.getName().lastIndexOf(".")) + "_cn.srt";
		}
		
		File resultFile = new File(sourceFile.getParent() + "/" + filename);

		write(resultFile, resultInfo);
	}
}