package com.alan.http.build;

import android.os.Looper;
import android.text.TextUtils;

import com.alan.http.ApiResult;
import com.alan.http.XmHttpConfig;
import com.alan.http.request.BaseRequest;
import com.alan.http.request.FileRequest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * @author Alan
 * 时 间：2019-12-25
 * 简 述：<功能简述>
 */
public class XmFileHttpBuilder extends XmHttpBuilder {

    private String targetFile;
    private BaseRequest.OnFileHttpCallBack onFileHttpCallBack;
    private boolean isDel;

    public XmFileHttpBuilder(BaseRequest baseRequest) {
        super(baseRequest);
        isDel = true;
    }

    @Override
    public ApiResult build() {
//        File file = haveFile();
//        if (file != null) {
//            return file;
//        }
//
//        if (TextUtils.isEmpty(targetFile)) {
//            return null;
//        }
//
//        complete();
//        Response response = null;
//        try {
//            response = getFileRequest().response();
//        } catch (Exception ignore) {
//
//        }
//
//        if (null == response) {
//            return null;
//        }
//        return generateFile(response);
        return null;
    }

    private File generateFile(Response response) {
        try {
            if (response.isSuccessful()) {
                long l = response.body().contentLength();
                InputStream inputStream = response.body().byteStream();
                return generateFile(inputStream, l);
            }
        } catch (Exception ignore) {

        }
        return null;
    }

    @Override
    public void buildOnThread() {

//        File file = haveFile();
//        if (file != null) {
//            if (null != onFileHttpCallBack) {
//                onFileHttpCallBack.onSuccess("", null);
//            }
//            return;
//        }
//
//        if (TextUtils.isEmpty(targetFile)) {
//            if (null != onFileHttpCallBack) {
//                onFileHttpCallBack.onFailure(null, new Exception("目标文件为空"));
//            }
//            return;
//        }
//
//        complete();
//        try {
//            baseRequest.executeWithThread(callback);
//        } catch (Exception e) {
//            handlerFailure(null, e);
//        }
    }

    Callback callback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            handlerFailure(call, e);
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            try {
                if (response.isSuccessful()) {
                    File file = generateFile(response);
                    if (null != file) {
                        handlerSuccess(call, file);
                        return;
                    }
                }
            } catch (Exception e) {
                handlerFailure(call, e);
            }
            handlerFailure(call, new Exception("请求失败"));
        }
    };

    private void handlerSuccess(Call call, final File file) {
        if (null != onFileHttpCallBack) {
            XmHttpConfig.getInstance().getHandler().post(new Runnable() {
                @Override
                public void run() {
//                    onFileHttpCallBack.onSuccess("", file);
                }
            });
        }
    }

    @Override
    void handlerFailure(final Call call, final Exception e) {
        if (null != onHttpCallBack) {
            XmHttpConfig.getInstance().getHandler().post(new Runnable() {
                @Override
                public void run() {
                    onHttpCallBack.onFailure(call, e);
                }
            });
        }
    }

    private FileRequest getFileRequest() {
        return (FileRequest) baseRequest;
    }

    public XmFileHttpBuilder setOnFileHttpCallBack(BaseRequest.OnFileHttpCallBack onHttpCallBack) {
        this.onFileHttpCallBack = onHttpCallBack;
        return this;
    }

    public XmFileHttpBuilder setTargetFile(String targetFile) {
        this.targetFile = targetFile;
        return this;
    }

    public XmFileHttpBuilder setDel(boolean del) {
        isDel = del;
        return this;
    }

    private File haveFile() {
        File file = new File(targetFile);
        if (!isDel && file.exists()) {
            return file;
        }
        return null;
    }

    private synchronized File generateFile(InputStream inputStream, long length) throws Exception {
        getTargetDir();
        String tempName = targetFile + ".download";
        File tempFile = new File(tempName);
        if (tempFile.exists()) {
            tempFile.delete();
        }
        tempFile.createNewFile();

        FileOutputStream out = new FileOutputStream(tempFile);

        byte[] buf = new byte[2048];
        int len = 0;
        long current = 0;
        onProcess(current, length);
        while ((len = inputStream.read(buf)) != -1) {
            current += len;
            out.write(buf, 0, len);
            onProcess(current, length);
        }

        out.flush();
        out.close();
        inputStream.close();
        File target = new File(targetFile);
        if (target.exists()) {
            target.delete();
        }
        tempFile.renameTo(target);
        onProcess(length, length);
        return target;
    }

    private void onProcess(final long i, final long total) {
        if (null != onFileHttpCallBack) {
            if (Looper.getMainLooper().getThread() == Thread.currentThread()) {
                onFileHttpCallBack.onProgressCallback(i, total);
            } else {
                XmHttpConfig.getInstance().getHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        onFileHttpCallBack.onProgressCallback(i, total);
                    }
                });
            }
        }
    }

    private String getTargetDir() throws Exception {
        if (TextUtils.isEmpty(targetFile)) {
            throw new Exception("目标文件为空");
        }

        int i = targetFile.lastIndexOf("/");
        if (i == -1) {
            throw new Exception("没有找到目录");
        }
        String dirStr = targetFile.substring(0, i);
        File fileDir = new File(dirStr);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        return dirStr;
    }
}
