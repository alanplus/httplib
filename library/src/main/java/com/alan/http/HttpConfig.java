package com.alan.http;

/**
 * @author Alan
 * 时 间：2020-03-27
 * 简 述：<功能简述>
 */
public class HttpConfig {

    public static IHttpConfig iHttpConfig;

    public static void regist(IHttpConfig iHttpConfig) {
        HttpConfig.iHttpConfig = iHttpConfig;
    }

}
