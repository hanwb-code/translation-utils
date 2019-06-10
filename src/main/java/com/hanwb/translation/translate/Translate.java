package com.hanwb.translation.translate;

import com.hanwb.model.Message;

public interface Translate {

	Message translate(String source, String sourceCode, String targetCode);
}
