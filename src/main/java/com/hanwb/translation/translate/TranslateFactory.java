package com.hanwb.translation.translate;

public class TranslateFactory {

	private static Translate GOOGLE_TRANSLATE = new GoogleTranslate();

	public static Translate googleTranslate() {
		return GOOGLE_TRANSLATE;
	}

}
