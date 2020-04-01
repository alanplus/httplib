package com.alan.http.request;

import android.graphics.BitmapFactory;
import android.text.TextUtils;

import com.alan.http.ApiResult;

import java.io.IOException;
import java.io.InputStream;

import okhttp3.Request;
import okhttp3.Response;

import static java.lang.System.in;

/**
 * @author Alan
 * 时 间：2019-12-17
 * 简 述：<功能简述>
 */
public class BitmapRequest extends XmRequest {


    public BitmapRequest(String path) {
        super(path);
    }

    @Override
    public Request create(String url, Request.Builder builder, String body) {
        if (!TextUtils.isEmpty(body)) {
            url += "?" + body;
        }
        return builder.url(url).build();
    }


    @Override
    protected ApiResult handlerResponse(Response response){
        ApiResult apiResult = new ApiResult(-122);
        apiResult.httpCode = response.code();
        if (response.isSuccessful()) {
            if (response.body() != null) {
                InputStream in = response.body().byteStream();
                apiResult.object = BitmapFactory.decodeStream(in);
                return apiResult;
            }
        }
        return apiResult;
    }
}
