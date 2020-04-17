package com.alan.http.request;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.alan.http.ApiResult;
import com.alan.http.HttpConfig;
import com.alan.http.LogUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Request;
import okhttp3.Response;

import static android.content.ContentValues.TAG;

/**
 * @author Alan
 * 时 间：2019-12-25
 * 简 述：<功能简述>
 */
public class FileRequest extends XmRequest {

    public FileRequest(String path) {
        super(path);
    }

    @Override
    public Request create(String url, Request.Builder builder, String body) {
        return builder.url(url).build();
    }

    @Override
    protected ApiResult handlerResponse(Response response, OnHttpCallBack onHttpCallBack) throws IOException {
        OnDownloadHttpCallBack onDownloadHttpCallBack = null;

        if (onHttpCallBack instanceof OnDownloadHttpCallBack) {
            onDownloadHttpCallBack = (OnDownloadHttpCallBack) onHttpCallBack;
        }

        ApiResult result = new ApiResult(-122);
        InputStream is = null;
        byte[] buf = new byte[2048];
        int len = 0;
        FileOutputStream fos = null;
        try {
            File file = getFile();

            long total = response.body().contentLength();
            long current = 0;
            is = response.body().byteStream();
            fos = new FileOutputStream(file);
            callback(onDownloadHttpCallBack, current, total);
            while ((len = is.read(buf)) != -1) {
                current += len;
                fos.write(buf, 0, len);
                callback(onDownloadHttpCallBack, current, total);
            }
            callback(onDownloadHttpCallBack, total, total);
            fos.flush();
            result.code = 200;
            result.object = file.getAbsoluteFile();
        } catch (IOException e) {
            LogUtil.error(e);
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                LogUtil.error(e);
            }
        }
        return result;
    }

    private void callback(OnDownloadHttpCallBack onDownloadHttpCallBack, long p, long total) {
        Handler handler = HttpConfig.handler();
        if (handler != null && onDownloadHttpCallBack != null) {
            onDownloadHttpCallBack.onProgressCallback(p, total);
        }
    }

    private String getDownloadDir() {
        if (TextUtils.isEmpty(downloadDir)) {
            downloadDir = HttpConfig.getContext().getFilesDir().getAbsolutePath();
        }
        LogUtil.d(downloadDir);
        File file = new File(downloadDir);
        if (!file.exists()) {
            file.mkdirs();
        }
        return downloadDir;
    }

    private String getFileName() {
        if (TextUtils.isEmpty(downloadName)) {
            int i = url.lastIndexOf("/");
            downloadName = url.substring(i + 1);
        }
        LogUtil.d(downloadName);
        return downloadName;
    }

    private File getFile() throws IOException {
        String downloadDir = getDownloadDir();
        String fileName = getFileName();
        File file = new File(downloadDir, fileName);
        if (file.exists()) {
            file.delete();
        }
        file.createNewFile();
        return file;
    }
}
