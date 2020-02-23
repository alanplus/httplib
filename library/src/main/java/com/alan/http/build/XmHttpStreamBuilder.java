package com.alan.http.build;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.alan.http.XmHttpConfig;
import com.alan.http.request.BitmapRequest;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * @author Alan
 * 时 间：2019-12-17
 * 简 述：<功能简述>
 */
public class XmHttpStreamBuilder extends XmHttpBuilder<Bitmap> {
    public XmHttpStreamBuilder(BitmapRequest baseRequest) {
        super(baseRequest);
    }

    @Override
    public Bitmap build() {
        complete();
        BitmapRequest bitmapRequest = getBitmapRequest();
        try {
            Response response = bitmapRequest.response();
            if (response.isSuccessful()) {
                return BitmapFactory.decodeStream(response.body().byteStream());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void buildOnThread() {
        complete();
        try {
            baseRequest.executeWithThread(callback);
        } catch (Exception e) {
            handlerFailure(null, e);
        }
    }

    private BitmapRequest getBitmapRequest() {
        return (BitmapRequest) baseRequest;
    }

    Callback callback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            handlerFailure(call, e);
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            if (response.isSuccessful()) {
                Bitmap bitmap = BitmapFactory.decodeStream(response.body().byteStream());
                handlerSuccess(call, bitmap);
            } else {
                handlerFailure(call, new Exception("请求失败"));
            }
        }
    };

    void handlerSuccess(Call call, final Bitmap bitmap) {
        if (null != onHttpCallBack) {
            XmHttpConfig.getInstance().getHandler().post(new Runnable() {
                @Override
                public void run() {
                    onHttpCallBack.onSuccess("", bitmap);
                }
            });
        }
    }
}
