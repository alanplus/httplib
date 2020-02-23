package com.alan.http;

import android.os.AsyncTask;

/**
 * @author Alan
 * 时 间：2019-12-17
 * 简 述：<功能简述>
 */
public class SimpleAsycTask<T> extends AsyncTask<Void,Integer,T> {
    @Override
    protected T doInBackground(Void... voids) {
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(T t) {
        super.onPostExecute(t);

    }
}
