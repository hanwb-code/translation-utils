## TranslationUtil

实现翻译工具项目。

调用主流翻译网站接口，完成翻译工作。

### 使用

使用 TranslateFactory 工厂类获取翻译工具类的实例，调用 translate 方法，进行翻译。

```
String source = "Using the Hello World guide, you’ll create a repository, start a branch, write comments, and open a pull request.";
Message message = TranslateFactory.googleTranslate()
        .translate(source, "auto", "zh-CN");
System.out.println(message);

---------------- out ----------------
Message(sourceCode=auto, targetCode=zh-CN, sourceMsg=Using the Hello World guide, you’ll create a repository, start a branch, write comments, and open a pull request., targetMsg=使用Hello World指南，您将创建存储库，启动分支，编写注释并打开拉取请求。)
```


### 已经实现

+ Google 翻译

### TODO

+ 百度翻译
+ 有道翻译

