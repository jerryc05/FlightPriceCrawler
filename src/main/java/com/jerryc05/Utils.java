package com.jerryc05;


import java.awt.Desktop;
import java.awt.TextField;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.zip.DeflaterInputStream;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.HttpsURLConnection;

class Utils {

    static final String VERSION = "0.1.7";
    static final String ACCEPT = "Accept";
    static final String ACCEPT_ALL = "*/*";
    static final String ACCEPT_ENCODING = "Accept-Encoding";
    static final String GZIP = "gzip";
    static final String USER_AGENT = "User-Agent";
    static final String MOZILLA = "";
    static final String CONTENT_TYPE = "Content-Type";
    static final String APP_JSON = "application/json";
    static final String CONTENT_LENGTH = "Content-Length";
    static long currentTime;

    private Utils() {
    }

    static void addCookiesToConnection(
            final HttpsURLConnection httpsURLConnection, Map<String, String> cookieMap) {
        if (cookieMap != null) {
            StringBuilder parseCookie = new StringBuilder();
            for (Map.Entry<String, String> cookieEntry : cookieMap.entrySet())
                parseCookie.append(cookieEntry.getKey())
                        .append("=")
                        .append(cookieMap.get(cookieEntry.getValue()))
                        .append(";");
            httpsURLConnection.setRequestProperty("Cookie", parseCookie.toString());
        }
    }

    static String airportCodeToCityCode(String airportCode) {
        switch (airportCode) {
            case "nay":
            case "pek":
                return "bjs";
            case "pvg":
                return "sha";
            default:
                return airportCode;
        }
    }

    static void closeConnection(
            final HttpsURLConnection httpsURLConnection, final Logger logger) {

        try {
            if (httpsURLConnection != null)
                httpsURLConnection.disconnect();
        } catch (Exception e) {
            Utils.handleException(e, logger);
        }
    }

    static void handleException(final Exception e, final Logger logger) {

        StringWriter stringWriter = new StringWriter();
        e.printStackTrace(new PrintWriter(stringWriter));
        logger.warning(stringWriter::toString);
    }

    static void logTime(final Logger logger) {
        logger.info(() -> Long.toString(System.currentTimeMillis() - Utils.currentTime));
        currentTime = System.currentTimeMillis();
    }

    static void saveCookies(
            final Map<String, List<String>> map, Map<String, String> cookieMap) {

        for (Map.Entry<String, List<String>> headerEntry : map.entrySet()) {
            if (!"Set-Cookie".equals(headerEntry.getKey())) break;
            if (cookieMap == null)
                cookieMap = new HashMap<>();
            for (String cookiesString : headerEntry.getValue()) {
                String[] cookiesInArray = cookiesString.split(";");
                for (String cookie : cookiesInArray) {
                    String keyOfACookie = cookie.split("=")[0];
                    String valueOfACookie = cookie.split("=")[1];
                    if (!cookieMap.keySet().contains(keyOfACookie))
                        cookieMap.put(keyOfACookie, valueOfACookie);
                }
            }
        }
    }

    static void openFile(final String filePath, final Logger logger) {
        try {
            Desktop.getDesktop().open(new File(filePath));
        } catch (Exception e) {
            Utils.handleException(e, logger);
        }
    }

    static String processJson(
            final HttpsURLConnection httpsURLConnection, final Logger logger) {

        try {
            if (httpsURLConnection.getResponseCode() != HttpURLConnection.HTTP_OK)
                throw new UnsupportedOperationException(
                        "HTTP " + httpsURLConnection.getResponseCode() + " Error");
            if (httpsURLConnection.getContentLength() == 0)
                throw new UnsupportedOperationException(
                        "Content Length = " + httpsURLConnection.getContentLength());
            InputStream inputStream;
            final String contentEncoding = httpsURLConnection.getContentEncoding();
            if (contentEncoding == null)
                inputStream = httpsURLConnection.getInputStream();
            else
                switch (contentEncoding) {
                    case "gzip":
                        inputStream = new GZIPInputStream(httpsURLConnection.getInputStream());
                        break;
                    case "deflate":
                        inputStream = new DeflaterInputStream(httpsURLConnection.getInputStream());
                        break;
                    default:
                        inputStream = httpsURLConnection.getInputStream();
                }

            String[] contentTypes = httpsURLConnection.getContentType().split(";");
            for (String encoding : contentTypes)
                if (encoding.contains("charset=")) {
                    encoding = encoding.trim();
                    BufferedReader bufferedReader = new BufferedReader(
                            new InputStreamReader(inputStream, encoding.substring(8)));
                    String jsonReturned;
                    String json = null;
                    while ((jsonReturned = bufferedReader.readLine()) != null) {
                        json = jsonReturned;
                    }
                    bufferedReader.close();
                    inputStream.close();
                    return json;
                }
        } catch (Exception e) {
            Utils.handleException(e, logger);
        }
        return null;
    }

    static void validateCode(TextField textField) {
        textField.setText(textField.getText().trim());
        String text = textField.getText();
        if (text.length() > 3) {
            text = text.substring(0, 3);
            textField.setText(text);
        }
    }
}