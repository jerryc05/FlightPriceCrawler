import org.apache.commons.compress.compressors.brotli.BrotliCompressorInputStream;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.DeflaterInputStream;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

class CrawlCtripByJson {

    private static Map<String, String> cookieMap = null;

    static boolean crawlCtripByJson(String departureAirportCode,
                                    String arrivalAirportCode,
                                    String departDate,
                                    String returnDate) {

        ByteArrayOutputStream byteArrayOutputStream = null;
        BufferedInputStream bufferedInputStream = null;
        String result;
        HttpsURLConnection httpsURLConnection = null;
        URL url;

        try {
            if (returnDate.equals(""))
                url = new URL("https://flights.ctrip.com/itinerary/oneway/"
                        + departureAirportCode + "-" + arrivalAirportCode + "?date=" + departDate);
            else
                url = new URL("https://flights.ctrip.com/itinerary/roundtrip/"
                        + departureAirportCode + "-" + arrivalAirportCode + "?date=" + departDate + "%2" + returnDate);
            SSLContext sslContext = SSLContext.getInstance("SSL");
            TrustManager[] trustManagers = {new MyX509TrustManager()};
            sslContext.init(null, trustManagers, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());

            httpsURLConnection = (HttpsURLConnection) url.openConnection();
            httpsURLConnection.setRequestMethod("GET");
            httpsURLConnection.setInstanceFollowRedirects(true);
            httpsURLConnection.setConnectTimeout(10 * 1000);
            httpsURLConnection.setReadTimeout(10 * 1000);
            httpsURLConnection.setDoOutput(true);
            httpsURLConnection.setDoInput(true);
            httpsURLConnection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
            httpsURLConnection.setRequestProperty("Accept-Encoding", "gzip, deflate, br");
            httpsURLConnection.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.9,en-US;q=0.8,en;q=0.7");
            httpsURLConnection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            httpsURLConnection.setRequestProperty("DNT", "1");
            httpsURLConnection.setRequestProperty("Upgrade-Insecure-Requests", "1");
            httpsURLConnection.setRequestProperty("Connection", "keep-alive");
            httpsURLConnection.setRequestProperty("Host", "flights.ctrip.com");
            httpsURLConnection.setHostnameVerifier((hostname, session) -> true);
            if (cookieMap != null) {
                StringBuilder parseCookie = new StringBuilder();
                for (String keyOfACookie : cookieMap.keySet())
                    parseCookie.append(keyOfACookie)
                            .append("=")
                            .append(cookieMap.get(keyOfACookie))
                            .append(";");
                httpsURLConnection.setRequestProperty("Cookie", parseCookie.toString());
            }
            httpsURLConnection.connect();

            if (httpsURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                if (httpsURLConnection.getContentLength() > 0) {
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
                    byte[] b = new byte[httpsURLConnection.getContentLength()];
                    int length;
                    while ((length = bufferedInputStream.read(b)) > 0) {
                        byteArrayOutputStream.write(b, 0, length);
                    }
                    result = byteArrayOutputStream.toString(StandardCharsets.UTF_8);
                    System.out.println(result);


                    InputStream input = httpsURLConnection.getInputStream();
                    InputStreamReader reader = new InputStreamReader(input, "gb2312");
                    StringBuilder sb1 = new StringBuilder();
                    int ss;
                    while ((ss = reader.read()) != -1) {
                        sb1.append((char) ss);
                        result = sb1.toString();
                    }
                    System.out.println(result);
                }

                //get cookie
                final Map<String, List<String>> map = httpsURLConnection.getHeaderFields();
                for (String header : map.keySet()) {
                    if ("Set-Cookie".equals(header) || "set-cookie".equals(header)) {
                        for (String singleCookie : map.get(header)) {
                            String[] cookieStringArray = singleCookie.split(";");
                            for (String partOfACookie : cookieStringArray) {
                                String keyOfACookie = partOfACookie.split("=")[0],
                                        valueOfACookie = partOfACookie.split("=")[1];
                                if (cookieMap == null)
                                    cookieMap = new HashMap<>();
                                if (!cookieMap.keySet().contains(keyOfACookie))
                                    cookieMap.put(keyOfACookie, valueOfACookie);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (byteArrayOutputStream != null) {
                    byteArrayOutputStream.close();
                }
                if (bufferedInputStream != null) {
                    bufferedInputStream.close();
                }
                if (httpsURLConnection != null) {
                    httpsURLConnection.disconnect();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return true;
    }
}