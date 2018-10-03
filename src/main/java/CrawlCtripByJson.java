import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        String result = null;
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

            httpsURLConnection = (HttpsURLConnection) url.openConnection();
            httpsURLConnection.setSSLSocketFactory(sslContext.getSocketFactory());
            httpsURLConnection.setHostnameVerifier((arg0, arg1) -> true);
            httpsURLConnection.setConnectTimeout(10 * 1000);
            httpsURLConnection.setReadTimeout(10 * 1000);
            if (cookieMap != null) {
                StringBuilder parseCookie = new StringBuilder();
                for (String keyOfACookie : cookieMap.keySet())
                    parseCookie.append(keyOfACookie)
                            .append("=")
                            .append(cookieMap.get(keyOfACookie))
                            .append(";");
                httpsURLConnection.setRequestProperty("Cookie", parseCookie.toString());
            }
            httpsURLConnection.setRequestProperty("connection", "Keep-Alive");
            httpsURLConnection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
//            httpsURLConnection.setRequestProperty(":authority","flights.ctrip.com");
//            httpsURLConnection.setRequestProperty(":method","GET");
//            httpsURLConnection.setRequestProperty(":path","/itinerary/oneway/foc-sha?date=2018-12-21");
//            httpsURLConnection.setRequestProperty(":scheme","https");
//            httpsURLConnection.setRequestProperty("dnt", "1");
//            httpsURLConnection.setRequestProperty("upgrade-insecure-requests", "1");
//            httpsURLConnection.setRequestProperty("user-agent", "");
//            httpsURLConnection.setRequestProperty("Charset", "utf-8");
//            httpsURLConnection.setRequestProperty("accept", "application/json");
//            httpsURLConnection.setRequestProperty("Content-Type", "application/json");
            httpsURLConnection.connect();

            if (httpsURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                if (httpsURLConnection.getContentLength() > 0) {
                    bufferedInputStream = new
                            BufferedInputStream(httpsURLConnection.getInputStream());
                    byteArrayOutputStream = new ByteArrayOutputStream();
                    byte[] b = new byte[httpsURLConnection.getContentLength()];
                    int length;
                    while ((length = bufferedInputStream.read(b)) > 0) {
                        byteArrayOutputStream.write(b, 0, length);
                    }
                    result = byteArrayOutputStream.toString(StandardCharsets.UTF_8);
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
        System.out.println(result);

        return true;
    }
}
