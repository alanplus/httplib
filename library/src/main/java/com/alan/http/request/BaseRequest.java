package com.alan.http.request;

import android.text.TextUtils;

import com.alan.http.XmHttpConfig;

import java.io.File;
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
public abstract class BaseRequest {

    OkHttpClient okHttpClient = XmHttpConfig.getInstance().getHttpClient();
    private static final String CHARSET_NAME = "UTF-8";

    private boolean isEncoding;
    MediaType mediaType;

    private String url;
    private String tag;

    HashMap<String, String> mParams;
    private HashMap<String, String> mHeaders;


    BaseRequest(String path) {
        url = path.startsWith("http://") || path.startsWith("https://") ? path : XmHttpConfig.getInstance().getHost() + path;
        tag = url;
    }

    protected abstract Request create(String url, Request.Builder builder, String body);


    public String getContentFromMap(Map<String, String> params) throws UnsupportedEncodingException {
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

    Request create() throws IOException {
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


    public void setEncoding(boolean encoding) {
        isEncoding = encoding;
    }

    public void setMediaType(MediaType mediaType) {
        this.mediaType = mediaType;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setParams(HashMap<String, String> mParams) {
        this.mParams = mParams;
    }

    public void setHeaders(HashMap<String, String> mHeaders) {
        this.mHeaders = mHeaders;
    }

    public String getUrl() {
        return url;
    }


    /**
     * 执行请求
     */
    public String execute() throws Exception {
        Request request = create();
        Response response = okHttpClient.newCall(request).execute();
        if (response.isSuccessful()) {
            return response.body().string();
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

    public interface OnHttpCallBack<T> {

        void onSuccess(String content, T t);

        void onFailure(Call call, Exception e);

    }

    public interface OnFileHttpCallBack extends OnHttpCallBack<File> {

        void onProgressCallback(long progress, long len);

    }

}
