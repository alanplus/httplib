package com.alan.http.request;


import android.text.TextUtils;

import okhttp3.Request;

/**
 * @author Alan
 * 时 间：2019-12-13
 * 简 述：<功能简述>
 */
public class GetRequest extends XmRequest {


    public GetRequest(String path) {
        super(path);
    }

    @Override
    protected Request create(String url, Request.Builder builder, String body) {
        if(!TextUtils.isEmpty(body)){
            url += "?" + body;
        }
        return builder.url(url).build();
    }


}
