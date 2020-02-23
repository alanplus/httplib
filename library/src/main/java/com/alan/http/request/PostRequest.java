package com.alan.http.request;

import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * @author Alan
 * 时 间：2019-12-13
 * 简 述：<功能简述>
 */
public class PostRequest extends BaseRequest {

    public PostRequest(String path) {
        super(path);
    }

    @Override
    protected Request create(String url, Request.Builder builder, String body) {
        return builder.url(url).post(RequestBody.create(mediaType, body)).build();
    }



}
