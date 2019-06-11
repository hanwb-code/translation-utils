package com.hanwb.utils;

import java.io.IOException;

import com.hanwb.model.Message;
import com.hanwb.translation.translate.TranslateFactory;

public class GoogleTranslateUtils {

	public static String enToCn(String text) throws IOException {
		
		Message message = TranslateFactory.googleTranslate().translate(text, "en", "zh-CN");
		
		return message.getTargetMsg();
	}
	
    public static void main(String[] args) {

		String source = "Hello World.";
		
		Message message = TranslateFactory.googleTranslate().translate(source, "en", "zh-CN");
		
		System.out.println(message.getTargetMsg());
    }
}
