package com.jerryc05.crawl_ctrip_by_json;

import com.alibaba.fastjson.JSON;

import org.apache.commons.compress.compressors.brotli.BrotliCompressorInputStream;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.zip.DeflaterInputStream;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.HttpsURLConnection;

public class CrawlCtripByJson {

    private static Map<String, String> cookieMap = null;
    private static String departureAirportCode;
    private static String arrivalAirportCode;
    private static String departDate;
    private static String returnDate;
    private static Logger logger;

    private CrawlCtripByJson() {
    }

    public static boolean crawlCtripByJson(final String departureAirportCode,
                                           final String arrivalAirportCode,
                                           final String departDate,
                                           final String returnDate) {

        logger = Logger.getLogger(CrawlCtripByJson.class.getName());
        CrawlCtripByJson.departureAirportCode = departureAirportCode;
        CrawlCtripByJson.arrivalAirportCode = arrivalAirportCode;
        CrawlCtripByJson.departDate = departDate;
        CrawlCtripByJson.returnDate = returnDate;
        if (!initCookie())
            return false;
        else {
            logger.info("initCookie() successful!");
            if (!getProductJson())
                return false;
            else {
                logger.info("getProductJson() successful!");
                //
            }
        }
        return true;
    }

    private static boolean initCookie() {

        HttpsURLConnection httpsURLConnection = null;
        URL url;

        try {
            if (returnDate.equals(""))
                url = new URL("https://flights.ctrip.com/itinerary/oneway/"
                        + departureAirportCode + "-" + arrivalAirportCode + "?date=" + departDate);
            else
                url = new URL("https://flights.ctrip.com/itinerary/roundtrip/"
                        + departureAirportCode + "-" + arrivalAirportCode + "?date=" + departDate + "%2" + returnDate);
            httpsURLConnection = (HttpsURLConnection) url.openConnection();
            httpsURLConnection.setRequestMethod("GET");
            httpsURLConnection.setInstanceFollowRedirects(true);
            httpsURLConnection.setConnectTimeout(10 * 1000);
            httpsURLConnection.setReadTimeout(10 * 1000);
            httpsURLConnection.setDoOutput(true);
            httpsURLConnection.setDoInput(true);
            httpsURLConnection.setRequestProperty("Accept", "*/*");//text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8
            httpsURLConnection.setRequestProperty("Accept-Encoding", "gzip");//, deflate, br
            httpsURLConnection.setRequestProperty("User-Agent", "Mozilla/5.0");// (compatible); MSIE 6.0; Windows NT 5.1;SV1
            addCookiesToConnection(httpsURLConnection);
            httpsURLConnection.connect();

            if (httpsURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK)
                saveCookies(httpsURLConnection.getHeaderFields());
        } catch (Exception e) {
            logger.warning(e.getMessage());
            return false;
        } finally {
            try {
                if (httpsURLConnection != null)
                    httpsURLConnection.disconnect();
            } catch (Exception e) {
                logger.warning(e.getMessage());
            }
        }
        return true;
    }

    private static boolean getProductJson() {

        ByteArrayOutputStream byteArrayOutputStream = null;
        BufferedInputStream bufferedInputStream = null;
        String result = null;
        HttpsURLConnection httpsURLConnection = null;
        URL url;

        try {

            url = new URL("https://flights.ctrip.com/itinerary/api/12808/products");
            httpsURLConnection = (HttpsURLConnection) url.openConnection();
            httpsURLConnection.setRequestMethod("POST");
            httpsURLConnection.setInstanceFollowRedirects(true);
            httpsURLConnection.setUseCaches(false);
            httpsURLConnection.setConnectTimeout(10 * 1000);
            httpsURLConnection.setReadTimeout(10 * 1000);
            httpsURLConnection.setDoOutput(true);
            httpsURLConnection.setDoInput(true);
            httpsURLConnection.setRequestProperty("Accept", "*/*");
            httpsURLConnection.setRequestProperty("Accept-Encoding", "gzip");//, deflate, br
            httpsURLConnection.setRequestProperty("User-Agent", "Mozilla/5.0");// (compatible); MSIE 6.0; Windows NT 5.1;SV1
            addCookiesToConnection(httpsURLConnection);

//            JsonPOST jsonPOST=new JsonPOST()
//            jsonPOST.airportParamsItem.dcity = departureAirportCode;
//            jsonPOST.AirportParamsItem.acity = arrivalAirportCode;
//            jsonPOST.AirportParamsItem.date = departDate;
//            if (!returnDate.equals("")) {//tod d
//                JsonPOST.flightWay = "";
//            }
            OutputStream outputStream = httpsURLConnection.getOutputStream();
            JSON.writeJSONString(outputStream, "");
            outputStream.close();
            httpsURLConnection.connect();

            if (httpsURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK
                    && httpsURLConnection.getContentLength() > 0) {
                InputStream inputStream;
                switch (httpsURLConnection.getContentEncoding()) {
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
                        result = byteArrayOutputStream.toString(encoding.substring(8));
                    }
                logger.info(result);
            }
        } catch (
                Exception e) {
            logger.warning(e.getMessage());
            return false;
        } finally {
            try {
                if (byteArrayOutputStream != null)
                    byteArrayOutputStream.close();
                if (bufferedInputStream != null)
                    bufferedInputStream.close();
                if (httpsURLConnection != null)
                    httpsURLConnection.disconnect();
            } catch (Exception e) {
                logger.warning(e.getMessage());
                ;

            }
        }
        return true;
    }

    private static void addCookiesToConnection(final HttpsURLConnection httpsURLConnection) {
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

    private static void saveCookies(final Map<String, List<String>> map) {

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
}