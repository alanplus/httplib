package com.alan.http;

import android.os.Handler;
import android.os.Looper;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;


/**
 * @author Alan
 * 时 间：2019-12-13
 * 简 述：<功能简述>
 */
public class XmHttpConfig {


    private static volatile XmHttpConfig xmHttpConfig;

    private Handler handler = new Handler(Looper.getMainLooper());

    private OkHttpClient mClient;

    private HashMap<String, String> mParamMap;
    private HashMap<String, String> mHeadMap;

    private boolean mLogEnable;
    private boolean isEncoding;
    private String mHost;


    public static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");//mdiatype 这个需要和服务端保持一致
    public static final MediaType MEDIA_TYPE_APPLICATION = MediaType.parse("application/x-www-form-urlencoded");
    private MediaType mediaType;

    private XmHttpConfig(Builder builder) {
        mClient = builder.mClientBuilder.build();
        mParamMap = builder.mParamMap;
        mHeadMap = builder.mHeadMap;
        mLogEnable = builder.mLogEnable;
        isEncoding = builder.isEncoding;
        mHost = builder.mHost;
    }

    public Handler getHandler() {
        return handler;
    }

    public static XmHttpConfig getInstance() {
        return xmHttpConfig;
    }

    public OkHttpClient getHttpClient() {
        return mClient;
    }

    public HashMap<String, String> getHttpParamMap() {
        return mParamMap;
    }

    public HashMap<String, String> getHttpHeadMap() {
        return mHeadMap;
    }

    public boolean isLogEnable() {
        return mLogEnable;
    }

    public String getHost() {
        return mHost;
    }

    public boolean isEncoding() {
        return isEncoding;
    }

    public MediaType getMediaType() {
        return mediaType;
    }

    public static XmHttpConfig.Builder with() {
        return new XmHttpConfig.Builder();
    }


    private static void setXmHttpConfig(XmHttpConfig xmHttpConfig) {
        XmHttpConfig.xmHttpConfig = xmHttpConfig;
    }

    public static class Builder {

        private OkHttpClient.Builder mClientBuilder;

        private HashMap<String, String> mParamMap;
        private HashMap<String, String> mHeadMap;

        private boolean mLogEnable;
        private String mHost;

        private boolean isEncoding;
        private MediaType mediaType;

        private int mConnectionTimeOut;
        private int mWriteTimeOut;
        private int mReadTimeOut;


        public Builder() {
            mClientBuilder = new OkHttpClient.Builder();
            mParamMap = new HashMap<>();
            mHeadMap = new HashMap<>();
            mLogEnable = false;
            isEncoding = true;
            mediaType = MEDIA_TYPE_APPLICATION;

            mConnectionTimeOut = 20 * 1000;//默认20秒连接超时
            mWriteTimeOut = 1000;//默认1秒超时
            mReadTimeOut = 10 * 1000;// 默认10秒超时
        }

        public Builder setMediaType(MediaType mediaType) {
            this.mediaType = mediaType;
            return this;
        }

        public Builder setEncoding(boolean encoding) {
            isEncoding = encoding;
            return this;
        }

        public Builder setLogEnable(boolean mLogEnable) {
            this.mLogEnable = mLogEnable;
            return this;
        }

        public Builder setHost(String mHost) {
            this.mHost = mHost;
            return this;
        }

        public Builder setConnectionTimeOut(int mConnectionTimeOut) {
            this.mConnectionTimeOut = mConnectionTimeOut;
            return this;
        }

        public Builder setWriteTimeOut(int mWriteTimeOut) {
            this.mWriteTimeOut = mWriteTimeOut;
            return this;
        }

        public Builder setReadTimeOut(int mReadTimeOut) {
            this.mReadTimeOut = mReadTimeOut;
            return this;
        }

        public Builder addHeader(String key, String value) {
            mHeadMap.put(key, value);
            return this;
        }

        public Builder addParam(String key, String value) {
            mParamMap.put(key, value);
            return this;
        }

        public void build() {
            mClientBuilder.connectTimeout(mConnectionTimeOut, TimeUnit.MILLISECONDS);
            mClientBuilder.writeTimeout(mWriteTimeOut, TimeUnit.MILLISECONDS);
            mClientBuilder.readTimeout(mReadTimeOut, TimeUnit.MILLISECONDS);
            XmHttpConfig.setXmHttpConfig(new XmHttpConfig(this));
        }
    }


}
