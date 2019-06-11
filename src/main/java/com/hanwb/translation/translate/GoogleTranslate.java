package com.hanwb.translation.translate;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.hanwb.model.Message;
import com.hanwb.translation.operate.DownloadOperate;

public class GoogleTranslate implements Translate {

    private String tkk = "429420.603232582";    // https://translate.google.cn 页面源码中可以获取（tkk），可以长期使用所以没做更新

    public Message translate(String source, String sourceCode, String targetCode) {
    	
        Message message = new Message();
        message.setSourceCode(sourceCode);
        message.setTargetCode(targetCode);
        message.setSourceMsg(source);
        try {
            String translateUrl = getUrl(source, sourceCode, targetCode);
            
            String returnMsg = DownloadOperate.download(translateUrl);
            
            JSONArray l1 = JSON.parseArray(returnMsg);
            if (l1 == null || l1.isEmpty()) return message;
            JSONArray l2 = l1.getJSONArray(0);
            if (l2 == null || l2.isEmpty()) return message;
            String target = "";
            for (int i = 0; i < l2.size(); i++) {
                JSONArray l3 = l2.getJSONArray(i);
                if (l3 == null || l3.isEmpty()) return message;
                target += l3.getString(0);

            }
            if (StringUtils.isNotBlank(target)) {
                message.setTargetMsg(target);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return message;
    }

    private String getUrl(String source, String sourceCode, String targetCode) throws UnsupportedEncodingException {
    	
        String tk = getTk(source);
        StringBuilder sb = new StringBuilder();
        source = URLEncoder.encode(source, "UTF-8");
        
        sb.append("https://translate.google.cn/translate_a/single?client=webapp&dt=t&sl=").append(sourceCode)
                .append("&tl=").append(targetCode)
                .append("&tk=").append(tk)
                .append("&q=").append(source);
        
        return sb.toString();
    }

    private String getTk(String source) {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("js");
        engine.put("b", tkk);
        String tk = null;
        try {
            engine.eval(JS_FUNCTION);
            Invocable invokeEngine = (Invocable)engine;
            Object tkObj = invokeEngine.invokeFunction("Ho", source);
            if (tkObj != null) {
                tk = (String) tkObj;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return tk;
    }

    private static final String JS_FUNCTION =
            "Fo = function(a, b) {\n" +
            "    for (var c = 0; c < b.length - 2; c += 3) {\n" +
            "        var d = b.charAt(c + 2);\n" +
            "        d = \"a\" <= d ? d.charCodeAt(0) - 87 : Number(d);\n" +
            "        d = \"+\" == b.charAt(c + 1) ? a >>> d : a << d;\n" +
            "        a = \"+\" == b.charAt(c) ? a + d & 4294967295 : a ^ d\n" +
            "    }\n" +
            "    return a\n" +
            "}\n" +
            "\n" +
            "Ho = function(a) {\n" +
            "    c = \"&tk=\"\n" +
            "    d = b.split(\".\");\n" +
            "    b = Number(d[0]) || 0;\n" +
            "    for (var e = [], f = 0, g = 0; g < a.length; g++) {\n" +
            "        var k = a.charCodeAt(g);\n" +
            "        128 > k ? e[f++] = k : (2048 > k ? e[f++] = k >> 6 | 192 : (55296 == (k & 64512) && g + 1 < a.length && 56320 == (a.charCodeAt(g + 1) & 64512) ? (k = 65536 + ((k & 1023) << 10) + (a.charCodeAt(++g) & 1023),\n" +
            "                    e[f++] = k >> 18 | 240,\n" +
            "                    e[f++] = k >> 12 & 63 | 128) : e[f++] = k >> 12 | 224,\n" +
            "                e[f++] = k >> 6 & 63 | 128),\n" +
            "            e[f++] = k & 63 | 128)\n" +
            "    }\n" +
            "    a = b;\n" +
            "    for (f = 0; f < e.length; f++)\n" +
            "        a += e[f],\n" +
            "        a = Fo(a, \"+-a^+6\");\n" +
            "    a = Fo(a, \"+-3^+b+-f\");\n" +
            "    a ^= Number(d[1]) || 0;\n" +
            "    0 > a && (a = (a & 2147483647) + 2147483648);\n" +
            "    a %= 1E6;\n" +
            "    return c + (a.toString() + \".\" + (a ^ b))\n" +
            "};\n";
    
}
