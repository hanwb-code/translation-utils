## translation-utils

字幕翻译工具

调用Google翻译网站接口,完成翻译工作

### 使用

使用 TranslateFactory 工厂类获取翻译工具类的实例，调用 translate方法，进行翻译。

```
String source = "Hello World.";
Message message = TranslateFactory.googleTranslate().translate(source, "auto", "zh-CN");
System.out.println(message);

字母翻译主函数
com.hanwb.SrtEnToCN 翻译文件夹的英文字幕文件翻译为中文字幕