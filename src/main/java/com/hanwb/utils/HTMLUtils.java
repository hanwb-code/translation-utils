package com.hanwb.utils;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

public class HTMLUtils {
	
	public static void main(String[] args) {
		
	}

	/**
	 * 清除html代码
	 * <p>
	 * 所有包括在'<'与'>'之间的内容全部都会被清除掉,并返回
	 * </P>
	 * @param args
	 * @return String
	 */
	public static String clearHTMLToString(String args) {
		return clearHTMLToString(args, false);
	}

	/**
	 * 清除html代码
	 * <p>
	 * 所有包括在'<'与'>'之间的内容全部都会被清除掉,并返回
	 * </P>
	 * @param args
	 * @param replaceNull 是否替换空格等制表符
	 * @return String
	 */
	public static String clearHTMLToString(String args, boolean replaceNull) {
		if (StringUtils.isEmpty(args)) {
			return "";
		}
		args = args.replaceAll("(?is)<(.*?)>", "");
		if (replaceNull) {
			args = args.replaceAll("\\s*|\t|\r|\n", "");
		}
		return args;
	}

	/**
	 * 清除html代码
	 * <p>
	 * 所有包括在'<'与'>'之间的内容全部都会被清除掉，并指定返回的长度
	 * </P>
	 * @param args
	 * @return String
	 */
	public static String clearHTMLToString(String args, int maxSize) {
		return clearHTMLToString(args, maxSize, "");
	}

	/**
	 * 清除html代码
	 * <p>
	 * 所有包括在'<'与'>'之间的内容全部都会被清除掉，并指定返回的长度
	 * </P>
	 * @param args
	 * @return String
	 */
	public static String clearHTMLToString(String args, int maxSize, String replace) {
		args = clearHTMLToString(args);
		if (maxSize <= 0) {
			return args;
		}
		if (args.length() <= maxSize) {
			return args;
		}
		return args.substring(0, maxSize).concat(replace);
	}

	/**
	 * 将字符串截取指定长度
	 * @param args
	 * @param maxSize
	 * @param replace
	 * @return String
	 */
	public static String clearHTMLToSize(String args, int maxSize, String replace) {
		if (args.length() <= maxSize) {
			return args;
		}
		return args.substring(0, maxSize).concat(replace);
	}

	/**
	 * 从style样式中读取CSS的属性
	 * <pre>
	 * String style = "float:left;margin:0px;font-size:12px;";
	 * String fontSize = HTMLServices.getStyleNameValue(style, "font-size");
	 * </pre>
	 * @param style
	 * @param styleName
	 * @return String
	 */
	protected static String getStyleToString(String style, String styleName) {
		try {
			Map<String, String> css = cssToMap(style);
			return css.get(styleName);
		} catch (Exception e) {
		}
		return "";
	}

	public static String filterChare(String msg, String... chars) {
		for (String _char : chars) {
			msg = msg.replace(_char, "");
		}
		return msg;
	}

	public static String mapToCSS(Map<String, String> css) {
		StringBuffer style = new StringBuffer();
		for (Map.Entry<String, String> entry : css.entrySet()) {
			style.append(style.length() > 0 ? "," : "");
			style.append(entry.getKey()).append(":").append(entry.getValue());
		}
		return style.toString();
	}

	/**
	 * 将style封装成键值对
	 * <pre>
	 * String style = "float:left;margin:0px;font-size:12px;";
	 * Map<String, String> css = HTMLServices.getStyleToMap(style);
	 * System.out.println("font-size:" + css.get("font-size"));
	 * </pre>
	 * @param style
	 * @return Map
	 */
	public static Map<String, String> cssToMap(String style) {
		Map<String, String> map = new HashMap<String, String>();
		try {
			if (style == null || StringUtils.isEmpty(style)) {
				return map;
			}
			style = style.toLowerCase();
			style = style.split("\\}")[0];
			String[] csss = style.split("\\;");
			for (int i = 0, iSzie = csss.length; i < iSzie; i++) {
				String[] cssStyle = csss[i].split("\\:");
				for (int j = 0, jSize = cssStyle.length; (j + 1) < jSize; j += 2) {
					map.put(cssStyle[j].replace(" ", "").trim(), cssStyle[j + 1].toString().trim());
				}
			}
		} catch (Exception e) {
		}
		return map;
	}
}