package com.alan.http;


import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Api请求结果类
 * Title: ApiResult
 * Date: 2017/11/6 11:14
 * Company: WeiCi
 */
public class ApiResult implements Serializable {

    // -121 代码错误 -122 http 返回错误
    public int code;
    public String msg;
    public String url;
    public Object object;

    public HashMap<String, Object> map;

    public int type;

    public JSONObject jsonObject;

    public int httpCode;
    public String originText;

    public ApiResult() {
    }

    public ApiResult(int code) {
        this(code, null);
    }

    public ApiResult(int code, String msg) {
        this(code, msg, null);
    }

    public ApiResult(int code, String msg, String url) {
        this(code, msg, url, null);
    }

    public ApiResult(int code, String msg, String url, Object object) {
        this(code, msg, url, object, null);
    }


    public ApiResult(int code, String msg, String url, Object object, HashMap<String, Object> map) {
        this.code = code;
        this.msg = msg;
        this.object = object;
        this.url = url;
        this.map = map;
    }


    public String getStringFromMap(String key) {
        if (null == map) {
            return null;
        }

        if (map.containsKey(key)) {
            Object o = map.get(key);
            if (o instanceof String) {
                return (String) o;
            }
        }
        return null;
    }

    public int getIntFromMap(String key) {
        if (null == map) {
            return -1;
        }

        if (map.containsKey(key)) {
            Object o = map.get(key);
            if (o instanceof Integer) {
                return (Integer) o;
            }
        }
        return -1;
    }
}
