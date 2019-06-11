package com.hanwb.srt.gui;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;

import com.hanwb.srt.api.SRT;
import com.hanwb.srt.api.SRTInfo;
import com.hanwb.srt.api.SRTReader;
import com.hanwb.srt.api.SRTTimeFormat;
import com.hanwb.srt.api.SRTWriter;
import com.hanwb.srt.editor.SRTEditor;
import com.hanwb.utils.GoogleTranslateUtils;

public class Demo {

	public static void print(SRTInfo info) throws IOException {
		for (SRT s : info) {
			System.out.println("Number: " + s.number);
			System.out.println("Start time: " + SRTTimeFormat.format(s.startTime));
			System.out.println("End time: " + SRTTimeFormat.format(s.endTime));
			System.out.println("Texts:");

			for (String line : s.text) {
				
				System.out.println("    " + line);				
				
			}

			System.out.println();
		}
	}

	public static void testRead() throws IOException {
		SRTInfo info = SRTReader.read(new File("D://temp/q.srt"));
		print(info);
	}

	public static void testWrite() {
		SRTInfo info = new SRTInfo();
		Date d = new Date();
		info.add(new SRT(1, d, d, "Hello", "World"));
		info.add(new SRT(2, d, d, "Bye", "World"));

		File f = new File("out1.srt");
		f.deleteOnExit();
		SRTWriter.write(f, info);
	}

	public static void testEdit() throws IOException {
		SRTInfo info = SRTReader.read(new File("in.srt"));
		SRTEditor.updateText(info, 1, 10);
		SRTEditor.updateTime(info, 1, SRTTimeFormat.Type.MILLISECOND, 100);
		SRTEditor.prependSubtitle(info, "00:00:05,000", "00:00:07,000", Arrays.asList("Test"));
		SRTEditor.appendSubtitle(info, "00:01:05,000", "00:01:07,000", Arrays.asList("Test"));

		print(info);

		// Write it back
		File f = new File("out2.srt");
		f.deleteOnExit();
		SRTWriter.write(f, info);
	}

	public static void main(String[] args) throws IOException {
		//testRead();
		// testWrite();
		// testEdit();
		
		System.out.println(GoogleTranslateUtils.enToCn("Welcome to term three."));
	}
}