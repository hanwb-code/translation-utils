package com.hanwb;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.text.StringEscapeUtils;

import com.hanwb.srt.api.SRT;
import com.hanwb.srt.api.SRTInfo;
import com.hanwb.srt.api.SRTReader;
import com.hanwb.srt.api.SRTTimeFormat;
import com.hanwb.srt.api.SRTWriterException;
import com.hanwb.utils.GoogleTranslateUtils;

/**
 * 通过视频名查询当前目录 下英文字母 并翻译
 * @author work
 *
 */
public class VideoFindCn {
	
	public static void main(String[] args) throws IOException {
		
		File source = new File("D:/asrt-directory");
		
		if(source.isDirectory()){
			
			File[] videos = source.listFiles(new FilenameFilter() {
				@Override
				public boolean accept(File dir, String name) {
					
					if(name.endsWith(".mp4"))
						return true;
					
					return false;
				}
			});
			
			for (File sf : videos) {
				
				String videoName = sf.getName();
				
				videoName = videoName.substring(0, videoName.lastIndexOf("."));
				
				System.out.print("开始翻译视频字幕:[" + videoName + "]");
				
				toCnTT(videoName, source);
				
				System.out.print(" -> End...");
				System.out.println();
			}
			
		}else{
			System.err.println("非文件夹处理失败");
		}
		
		//delEnTT(source);
	}
	
	public static SRTInfo goEnTranslationCn(SRTInfo sourceInfo) throws IOException {

		SRTInfo resultInfo = new SRTInfo();

		for (SRT s : sourceInfo) {

			//System.out.println(s.number);
			//System.out.println(SRTTimeFormat.format(s.startTime) + " --> " + SRTTimeFormat.format(s.endTime));

			StringBuffer lines = new StringBuffer();
			List<String> newtext = new ArrayList<>();

			for (String line : s.text) {
				
				if(lines.length() == 0)
					lines.append(line);
				else
					lines.append(" " + line);
			}

			String enLine = StringEscapeUtils.unescapeHtml4(lines.toString());
			String cnLine = null;
			
			try {
				cnLine = GoogleTranslateUtils.enToCn(enLine);
				
			} catch (Exception e) {
				try {
					System.err.println("翻译异常：" + e.getMessage() + " 重试1次");
					cnLine = GoogleTranslateUtils.enToCn(enLine);
				} catch (Exception e2) {
					try {
						System.err.println("翻译异常：" + e.getMessage() + " 重试2次");
						cnLine = GoogleTranslateUtils.enToCn(enLine);
					} catch (Exception e3) {
						System.err.println("重试2次依然异常放弃:" + e.getMessage());
					}
				}
			}
			
			newtext.add(cnLine);
			newtext.add(enLine);

			//System.out.println(cnLine);
			//System.out.println(enLine);

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

	public static void go(File sourceFile, String videoName) throws IOException {
		
		SRTInfo info = SRTReader.read(sourceFile);

		SRTInfo resultInfo = goEnTranslationCn(info);

		String filename = videoName + " - lang_cn.srt";
		
		File resultFile = new File(sourceFile.getParent() + "/" + filename);

		write(resultFile, resultInfo);
	}
	
	public static void toCnTT(String videoName, File source) throws IOException {
		
		if(source.isDirectory()){
			File[] ens = source.listFiles(new FilenameFilter() {
				@Override
				public boolean accept(File dir, String name) {
					return (name.indexOf("lang_en") != -1 && name.endsWith(".srt") && name.indexOf(videoName) != -1);
				}
			});
			
			if(ens.length == 0)
				ens = source.listFiles(new FilenameFilter() {
					@Override
					public boolean accept(File dir, String name) {
						
						int indexOf = videoName.indexOf(" - ");
						int lastIndexOf = videoName.lastIndexOf(" - ");
						
						if(indexOf != -1 && lastIndexOf != -1){
							
							indexOf = indexOf + 3;
							
							if(indexOf > lastIndexOf)
								lastIndexOf = videoName.length();
							
							String seq = videoName.substring(indexOf, lastIndexOf);
							
							return (name.indexOf("lang_en") != -1 && name.endsWith(".srt") && name.indexOf(seq) != -1);
						}
						
						return false;
					}
				});
			
			for (File sf : ens) {
				go(sf, videoName);
			}
		}
	}
	
	public static void delEnTT(File source) throws IOException {
		if(source.isDirectory()){
			
			File[] tts = source.listFiles(new FilenameFilter() {
				@Override
				public boolean accept(File dir, String name) {
					
					if(name.endsWith(".srt"))
						return true;
					
					return false;
				}
			});
			
			for(File tt : tts){
				if(!tt.getName().endsWith("lang_cn.srt")){
					tt.delete();
				}
			}
		}
	}
}