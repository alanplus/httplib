package com.alan.http;

import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;

/**
 * @author Alan
 * 时 间：2020-03-27
 * 简 述：<功能简述>
 */
public interface IHttpConfig {

    boolean isPrintLog();

    boolean isEncoding();

    HashMap<String, String> getCommonParams();

    HashMap<String, String> getHeadParams();

    IParseStrategy getParseStrategy();

    OkHttpClient getOkHttpClinet();

    MediaType getMediaType();

}
