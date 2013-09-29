/*
 * Copyright 2013 ParanoidAndroid Project
 *
 * This file is part of Paranoid OTA.
 *
 * Paranoid OTA is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Paranoid OTA is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Paranoid OTA.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.paranoid.paranoidota.http;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.os.AsyncTask;

public class HttpStringReader extends AsyncTask<String, Void, Void> {

    public static interface HttpStringReaderListener {

        public void onReadEnd(String buffer);

        public void onReadError(Exception ex);
    };

    private HttpStringReaderListener mListener;

    public HttpStringReader(HttpStringReaderListener listener) {
        mListener = listener;
    }

    @Override
    protected Void doInBackground(String... urls) {
        try {
            HttpClient client = new DefaultHttpClient();
            HttpGet get = new HttpGet(urls[0]);
            HttpResponse r = client.execute(get);
            int status = r.getStatusLine().getStatusCode();
            HttpEntity e = r.getEntity();
            if (status == 200) {
                mListener.onReadEnd(EntityUtils.toString(e));
            } else {
                if (e != null)
                    e.consumeContent();
                String error = "Server responded with error " + status;
                mListener.onReadError(new Exception(error));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            mListener.onReadError(ex);
        }
        return null;
    }
}