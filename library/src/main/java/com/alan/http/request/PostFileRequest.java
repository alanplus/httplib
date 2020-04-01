package com.alan.http.request;

import android.text.TextUtils;

import com.alan.http.LogUtil;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;

/**
 * @author Alan
 * 时 间：2019-12-13
 * 简 述：<功能简述>
 */
public class PostFileRequest extends XmRequest {


    public PostFileRequest(String path) {
        super(path);
    }

    @Override
    public Request create(String url, Request.Builder builder, String str) {
        builder.url(url);
        MultipartBody.Builder body = new MultipartBody.Builder();
        body.setType(MultipartBody.FORM);


        // 添加参数
        if (!mParams.isEmpty()) {
            String type = mParams.get("media_type");
            MediaType mediaType = TextUtils.isEmpty("type") ? MediaType.parse("application/octet-stream") : MediaType.parse(type);

            for (String key : mParams.keySet()) {
                if ("media_type".equals(key)) {
                    continue;
                }
                String value = mParams.get(key);
                if ("file".equals(key)) {
                    File file = new File(value);
                    body.addFormDataPart(key, file.getName(), RequestBody.create(mediaType, file));
                    continue;
                }
                body.addFormDataPart(key, TextUtils.isEmpty(value) ? "" : value);
            }
        }
        return builder.post(body.build()).build();
    }

    private class UpdateRequestBody extends RequestBody {

        private File mFile;
        private MediaType mediaType;

        private UpdateRequestBody(File file, MediaType mediaType) {
            mFile = file;
            this.mediaType = mediaType;
        }

        @Override
        public MediaType contentType() {
            return mediaType;
        }

        @Override
        public long contentLength() {
            return mFile.length();
        }

        @Override
        public void writeTo(BufferedSink sink) throws IOException {
            Source source = null;
            try {
                source = Okio.source(mFile);
                Buffer buffer = new Buffer();
                // 文件大小
                long totalSize = contentLength();
                // 当前进度
                long currentSize = 0;
                long readCount;
                while ((readCount = source.read(buffer, 2048)) != -1) {
                    sink.write(buffer, readCount);
                    currentSize += readCount;
                    LogUtil.d(mFile.getName() + "文件总字节：" + totalSize + "，已上传字节：" + currentSize);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (source != null) {
                    source.close();
                }
            }
        }
    }
}
