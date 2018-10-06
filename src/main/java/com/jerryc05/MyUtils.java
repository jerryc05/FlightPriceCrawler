package com.jerryc05;

import org.apache.commons.compress.compressors.brotli.BrotliCompressorInputStream;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
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

public class MyUtils {

    private MyUtils() {
    }

    public static HttpsURLConnection addCookiesToConnection(
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
        return httpsURLConnection;
    }

    public static void closeConnection(
            final HttpsURLConnection httpsURLConnection, final Logger logger) {

        try {
            if (httpsURLConnection != null)
                httpsURLConnection.disconnect();
        } catch (Exception e) {
            MyUtils.handleException(e, logger);
        }
    }

    public static void handleException(final Exception e, final Logger logger) {

        StringWriter stringWriter = new StringWriter();
        e.printStackTrace(new PrintWriter(stringWriter));
        logger.warning(stringWriter::toString);
    }

    public static String processJson(
            final HttpsURLConnection httpsURLConnection, final Logger logger) {

        BufferedInputStream bufferedInputStream = null;
        ByteArrayOutputStream byteArrayOutputStream = null;
        try {
            if (httpsURLConnection.getResponseCode() != HttpURLConnection.HTTP_OK)
                throw new Exception("HTTP " + httpsURLConnection.getResponseCode() + " Error");
            if (httpsURLConnection.getContentLength() <= 0)
                throw new Exception("Content Length = " + httpsURLConnection.getContentLength());
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
                    case "br":
                        inputStream = new BrotliCompressorInputStream(httpsURLConnection.getInputStream());
                        break;
                    default:
                        inputStream = httpsURLConnection.getInputStream();
                }
            bufferedInputStream = new BufferedInputStream(inputStream);
            byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] bytes = new byte[httpsURLConnection.getContentLength()];
            int length;
            while ((length = bufferedInputStream.read(bytes)) > 0) {
                byteArrayOutputStream.write(bytes, 0, length);
            }
            String[] contentTypes = httpsURLConnection.getContentType().split(";");
            for (String encoding : contentTypes)
                if (encoding.contains("charset=")) {
                    encoding = encoding.trim();
                    return byteArrayOutputStream.toString(encoding.substring(8));
                }
        } catch (Exception e) {
            MyUtils.handleException(e, logger);
        } finally {
            try {
                if (byteArrayOutputStream != null)
                    byteArrayOutputStream.close();
                if (bufferedInputStream != null)
                    bufferedInputStream.close();
            } catch (Exception e) {
                MyUtils.handleException(e, logger);
            }
        }
        return null;
    }

    public static Map<String, String> saveCookies(
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
        return cookieMap;
    }
}