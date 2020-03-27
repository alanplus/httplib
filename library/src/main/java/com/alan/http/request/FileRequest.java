package com.alan.http.request;

import okhttp3.Request;

/**
 * @author Alan
 * 时 间：2019-12-25
 * 简 述：<功能简述>
 */
public class FileRequest extends XmRequest{

    public FileRequest(String path) {
        super(path);
    }

    @Override
    protected Request create(String url, Request.Builder builder, String body) {
        return null;
    }
}
