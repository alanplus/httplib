package com.alan.http.build;

import android.text.TextUtils;

import com.alan.http.HttpConfig;
import com.alan.http.IHttpConfig;
import com.alan.http.IParseStrategy;
import com.alan.http.XmHttpConfig;
import com.alan.http.request.BaseRequest;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.Response;

/**
 * @author Alan
 * 时 间：2019-12-13
 * 简 述：<功能简述>
 */
public class XmHttpBuilder<T> {

    BaseRequest baseRequest;

    HashMap<String, String> mParams;
    HashMap<String, String> mHeaders;

    boolean isEncoding;
    boolean isLog;

    IParseStrategy<T> iParseStrategy;

    String tag;
    MediaType mediaType;


    BaseRequest.OnHttpCallBack<T> onHttpCallBack;


    public XmHttpBuilder(BaseRequest baseRequest) {
        this.baseRequest = baseRequest;
        setHttpConfig(HttpConfig.iHttpConfig);
        this.tag = baseRequest.getUrl();
    }

    public XmHttpBuilder<T> setHttpConfig(IHttpConfig iHttpConfig) {
        mParams = new HashMap<>();
        mHeaders = new HashMap<>();
        if (null != iHttpConfig) {
            mParams.putAll(iHttpConfig.getCommonParams());
            mHeaders.putAll(iHttpConfig.getHeadParams());
        }
        isEncoding = null == iHttpConfig || iHttpConfig.isEncoding();
        isLog = null == iHttpConfig || iHttpConfig.isPrintLog();
        mediaType = null == iHttpConfig ? XmHttpConfig.MEDIA_TYPE_JSON : iHttpConfig.getMediaType();
        return this;
    }


    public XmHttpBuilder<T> addParam(String key, String value) {
        if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(value)) {
            mParams.put(key, value);
        }
        return this;
    }

    public XmHttpBuilder<T> addParams(HashMap<String, String> hashMap) {
        if (null != hashMap) {
            mParams.putAll(hashMap);
        }
        return this;
    }

    public XmHttpBuilder<T> addHeader(String key, String value) {
        if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(value)) {
            mHeaders.put(key, value);
        }
        return this;
    }

    public XmHttpBuilder<T> addAllHeaders(HashMap<String, String> hashMap) {
        if (null != hashMap) {
            mHeaders.putAll(hashMap);
        }
        return this;
    }

    public XmHttpBuilder<T> setMediaType(MediaType mediaType) {
        this.mediaType = mediaType;
        return this;
    }

    public XmHttpBuilder<T> setParseStrategy(IParseStrategy<T> iParseStrategy) {
        this.iParseStrategy = iParseStrategy;
        return this;
    }

    public XmHttpBuilder<T> setEncoding(boolean encoding) {
        isEncoding = encoding;
        return this;
    }

    public XmHttpBuilder<T> setParams(HashMap<String, String> mParams) {
        this.mParams = mParams;
        return this;
    }

    public XmHttpBuilder<T> setHeaders(HashMap<String, String> mHeaders) {
        this.mHeaders = mHeaders;
        return this;
    }

    public XmHttpBuilder<T> setTag(String tag) {
        this.tag = tag;
        return this;
    }

    public XmHttpBuilder<T> setOnHttpCallBack(BaseRequest.OnHttpCallBack<T> onHttpCallBack) {
        this.onHttpCallBack = onHttpCallBack;
        return this;
    }

    void complete() {
        baseRequest.setEncoding(isEncoding);
        baseRequest.setHeaders(mHeaders);
        baseRequest.setMediaType(mediaType);
        baseRequest.setParams(mParams);
        baseRequest.setTag(tag);
    }

    public T build() {


        try {
            complete();
            String content = baseRequest.execute();
            return iParseStrategy.parse(content);
        } catch (Exception e) {

        }
        return null;
    }

    public void buildOnThread() {
        complete();
        try {
            baseRequest.executeWithThread(callback);
        } catch (Exception e) {
            handlerFailure(null, e);
        }
    }

    private Callback callback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            handlerFailure(call, e);
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            if (response.isSuccessful() && null != response.body()) {
                try {
                    handlerSuccess(call, response.body().string());
                } catch (IOException e) {
                    handlerFailure(call, e);
                }
            }
        }
    };

    private void handlerSuccess(final Call call, final String string) {
        T t = null;
        if (null != iParseStrategy) {
            t = iParseStrategy.parse(string);
        }
        final T finalT = t;
        XmHttpConfig.getInstance().getHandler().post(new Runnable() {
            @Override
            public void run() {
                try {
                    if (null != onHttpCallBack) {
                        onHttpCallBack.onSuccess(string, finalT);
                    }
                } catch (Exception e) {
                    handlerFailure(call, e);
                }
            }
        });
    }

    void handlerFailure(final Call call, final Exception e) {
        if (null != onHttpCallBack) {
            XmHttpConfig.getInstance().getHandler().post(new Runnable() {
                @Override
                public void run() {
                    onHttpCallBack.onFailure(call, e);
                }
            });
        }
    }


}
