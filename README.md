[TOC]

### 使用

```
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

```
dependencies {
    implementation 'com.github.alanplus:httplib:1.2.13'
    implementation 'com.squareup.okhttp3:okhttp:3.10.0'
    implementation 'com.squareup.okio:okio:1.14.0'
}
```

- 注册 网络请求配置

```java

 //HttpConfig.regist(MyHttpConfig.getInstance());

```

```java

public class MyHttpConfig implements IHttpConfig {

    private OkHttpClient client;

    private static MyHttpConfig myHttpConfig;

    private MyHttpConfig() {

    }

    public static MyHttpConfig getInstance() {
        if (null == myHttpConfig) {
            myHttpConfig = new MyHttpConfig();
        }
        return myHttpConfig;
    }

    @Override
    public boolean isPrintLog() {
        return true;
    }

    @Override
    public boolean isEncoding() {
        return true;
    }

    @Override
    public HashMap<String, String> getCommonParams() {
        return null;
    }

    @Override
    public HashMap<String, String> getHeadParams() {
        HashMap<String, String> hashMap = new HashMap<>();
        return hashMap;
    }

    @Override
    public IParseStrategy getParseStrategy() {
        return new HttpParseStrategy();
    }

    @Override
    public OkHttpClient getOkHttpClient() {
        if (null == client) {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.connectTimeout(600, TimeUnit.SECONDS);
            builder.readTimeout(600, TimeUnit.SECONDS);
            builder.writeTimeout(600, TimeUnit.SECONDS);
            client = builder.build();
        }
        return client;
    }


    @Override
    public MediaType getMediaType() {
        return HttpConfig.MEDIA_TYPE_APPLICATION;
    }

    @Override
    public String host() {
        return null;
    }

    @Override
    public Handler handler() {
        return null;
    }
}

```