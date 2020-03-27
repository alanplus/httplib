package com.alan.http.request;

import android.text.TextUtils;

import com.alan.http.ApiResult;
import com.alan.http.HttpConfig;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author Alan
 * 时 间：2019-12-13
 * 简 述：<功能简述>
 */
public abstract class XmRequest {

    protected OkHttpClient okHttpClient;
    protected static final String CHARSET_NAME = "UTF-8";

    protected boolean isEncoding;
    protected boolean isPrintLog;
    protected MediaType mediaType;

    protected String url;
    protected String tag;

    protected HashMap<String, String> mParams;
    protected HashMap<String, String> mHeaders;


    XmRequest(String path) {
        this.okHttpClient = HttpConfig.getOkHttpClient();
        this.url = path.startsWith("http://") || path.startsWith("https://") ? path : HttpConfig.getHost() + path;
        this.tag = url;
        this.isEncoding = HttpConfig.isEncoding();
        this.isPrintLog = HttpConfig.isPrintLog();
        this.mediaType = HttpConfig.getMediaType();
        this.mParams = HttpConfig.getCommonParams();
        this.mHeaders = HttpConfig.getHeadParams();
    }


    abstract Request create(String url, Request.Builder builder, String body);


    private String getContentFromMap(Map<String, String> params) throws UnsupportedEncodingException {
        if (null == params) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        Set<String> keys = params.keySet();
        for (String key : keys) {
            String value = params.get(key);
            if (value != null) {
                sb.append(key).append('=').append(isEncoding ? URLEncoder.encode(value, CHARSET_NAME) : value).append('&');
            }
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    private Request create() throws IOException {
        Request.Builder builder = new Request.Builder().tag(tag);
        addHeaders(builder);
        return create(url, builder, getContentFromMap(mParams));
    }


    private void addHeaders(Request.Builder builder) {
        if (!mHeaders.isEmpty()) {
            for (String key : mHeaders.keySet()) {
                String value = mHeaders.get(key);
                if (!TextUtils.isEmpty(value)) {
                    builder.addHeader(key, value);
                }
            }
        }
    }


    public XmRequest setEncoding(boolean encoding) {
        isEncoding = encoding;
        return this;
    }

    public XmRequest setMediaType(MediaType mediaType) {
        this.mediaType = mediaType;
        return this;
    }

    public XmRequest setUrl(String url) {
        this.url = url;
        return this;
    }

    public XmRequest setTag(String tag) {
        this.tag = tag;
        return this;
    }

    public XmRequest setParams(HashMap<String, String> mParams) {
        this.mParams = mParams;
        return this;
    }

    public XmRequest setHeaders(HashMap<String, String> mHeaders) {
        this.mHeaders = mHeaders;
        return this;
    }

    public String getUrl() {
        return url;
    }


    public XmRequest addParams(HashMap<String, String> mParams){
        this.mParams.putAll(mParams);
        return this;
    }

    public XmRequest addParam(String key,String value){
        this.mParams.put(key,value);
        return this;
    }

    public XmRequest addHeads(HashMap<String, String> mHeaders){
        this.mHeaders.putAll(mHeaders);
        return this;
    }

    public XmRequest addHead(String key,String value){
        this.mHeaders.put(key,value);
        return this;
    }

    /**
     * 执行请求
     */
    public String execute() throws Exception {
        Request request = create();
        Response response = okHttpClient.newCall(request).execute();
        if (response.isSuccessful()) {
            return response.body() == null ? "" : response.body().string();
        }
        return "";
    }

    public Response response() throws Exception {
        Request request = create();
        return okHttpClient.newCall(request).execute();
    }


    public void executeWithThread(Callback callback) throws Exception {
        Request request = create();
        okHttpClient.newCall(request).enqueue(callback);
    }

    public interface OnHttpCallBack {

        void onSuccess(String content, ApiResult t);

        void onFailure(Call call, Exception e);

    }

    public interface OnFileHttpCallBack extends OnHttpCallBack {

        void onProgressCallback(long progress, long len);

    }

}
