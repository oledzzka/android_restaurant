package com.example.oleg.instagramm;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by oleg on 25.10.17.
 */

public class HttpRequest {
    private static final String USER_KEY = "33117921ea79430c4433b2088ab8fe22";
    private final String mURL;
    private String mContent;
    private InputStream inputStream;
    private HttpsURLConnection connection;


    public HttpRequest(String url) {
        mURL = url;
    }

    public String makeRequest() throws IOException {
        URL url = new URL(mURL);
        try {

            connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("user-key", USER_KEY);
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            connection.setInstanceFollowRedirects(true);
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                inputStream = new BufferedInputStream(connection.getInputStream());
                mContent = StringUtils.readInputStream(inputStream);
            }
            return mContent;
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}
