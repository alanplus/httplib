package com.alan.http;


import com.alan.http.request.BitmapRequest;
import com.alan.http.request.GetRequest;
import com.alan.http.request.PostRequest;
import com.alan.http.request.XmRequest;


/**
 * @author Alan
 * 时 间：2019-12-13
 * 简 述：<功能简述>
 */
public class XmHttpExecutor {


    public static XmRequest get(String path) {
        return new GetRequest(path);
    }

    public static  XmRequest post(String path) {
        return new PostRequest(path);
    }

    public static XmRequest bitmap(String path) {
        return new BitmapRequest(path);
    }

//    public static XmFileHttpBuilder file(String path) {
//        return new XmFileHttpBuilder(new FileRequest(path));
//    }
}
