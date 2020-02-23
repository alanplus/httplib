package com.alan.http;

import android.graphics.Bitmap;

import com.alan.http.build.XmFileHttpBuilder;
import com.alan.http.build.XmHttpBuilder;
import com.alan.http.build.XmHttpStreamBuilder;
import com.alan.http.request.BitmapRequest;
import com.alan.http.request.FileRequest;
import com.alan.http.request.GetRequest;
import com.alan.http.request.PostRequest;


/**
 * @author Alan
 * 时 间：2019-12-13
 * 简 述：<功能简述>
 */
public class XmHttpExecutor {


    public static <T> XmHttpBuilder<T> get(String path) {
        return new XmHttpBuilder<>(new GetRequest(path));
    }


    public static <T> XmHttpBuilder<T> post(String path) {
        return new XmHttpBuilder<>(new PostRequest(path));
    }

    public static XmHttpBuilder<Bitmap> bitmap(String path) {
        return new XmHttpStreamBuilder(new BitmapRequest(path));
    }

    public static XmFileHttpBuilder file(String path) {
        return new XmFileHttpBuilder(new FileRequest(path));
    }
}
