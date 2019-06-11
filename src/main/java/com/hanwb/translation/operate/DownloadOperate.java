package com.hanwb.translation.operate;

import java.io.IOException;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

public class DownloadOperate {

	public static String download(String url) throws IOException {
		
		Connection connection = Jsoup.connect(url);
		
		connection.timeout(50000).method(Connection.Method.GET).header("Accept", "*/*").followRedirects(true) // 是否跟随跳转,
																												// 处理3开头的状态码
				.ignoreHttpErrors(true) // 是否忽略网络错误, 处理5开头的状态码
				.ignoreContentType(true) // 是否忽略类型, 处理图片、音频、视频等下载
				.header("Accept-Language", "zh-CN").header("Content-Type", "application/x-www-form-urlencoded")
				.userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:37.0) Gecko/20100101 Firefox/37.0");
		Connection.Response response = connection.execute();
		return response.body();
	}

	public static void main(String[] args) throws Exception {
        String msg = DownloadOperate.download("https://translate.google.cn/translate_a/single?client=webapp&sl=zh-CN&tl=en&dt=t&tk=502424.75762&q=反正");
        System.out.println(msg);
	}
}
