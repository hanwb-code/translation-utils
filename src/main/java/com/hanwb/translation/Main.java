package com.hanwb.translation;

import com.alibaba.fastjson.JSON;
import com.hanwb.model.Message;
import com.hanwb.translation.translate.TranslateFactory;

public class Main {
	
	public static void main(String[] args) {
		
		String source = "Using the Hello World guide, you’ll create a repository, start a branch, write comments, and open a pull request.";
		
		Message message = TranslateFactory.googleTranslate().translate(source, "auto", "zh-CN");
		
		System.out.println(JSON.toJSON(message));
	}
}
