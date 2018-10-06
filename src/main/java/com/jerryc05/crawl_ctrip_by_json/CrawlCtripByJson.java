package com.jerryc05.crawl_ctrip_by_json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.jerryc05.MyUtils;
import com.jerryc05.crawl_ctrip_by_json.ProductsJsonPost.AirportParamsItem;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.logging.Logger;

import javax.net.ssl.HttpsURLConnection;

public class CrawlCtripByJson {

    private static Map<String, String> cookieMap = null;
    private static String departureAirportCode;
    private static String arrivalAirportCode;
    private static String departDate;
    private static String returnDate;
    private static Logger logger;
    private static final XSSFWorkbook workbook = new XSSFWorkbook();

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
            logger.info(() -> "initCookie() successful!");
            if (!getProductsJson())
                return false;
            else {
                logger.info(() -> "getProductsJson() successful!");
                if (!getLowestPriceJson())
                    return false;
                else {
                    logger.info(() -> "getLowestPriceJson() successful!");
                    openExcelFile();
                }
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
            httpsURLConnection.setConnectTimeout(10 * 1000);
            httpsURLConnection.setReadTimeout(10 * 1000);
            httpsURLConnection.setDoOutput(true);
            httpsURLConnection.setDoInput(true);
            httpsURLConnection.setRequestProperty("Accept", "*/*");
            httpsURLConnection.setRequestProperty("Accept-Encoding", "gzip");
            httpsURLConnection.setRequestProperty("User-Agent", "Mozilla/5.0");
            httpsURLConnection = MyUtils.addCookiesToConnection(httpsURLConnection, cookieMap);
            httpsURLConnection.connect();

            if (httpsURLConnection.getResponseCode() != HttpURLConnection.HTTP_OK)
                throw new Exception("HTTP " + httpsURLConnection.getResponseCode() + " Error");
            cookieMap = MyUtils.saveCookies(httpsURLConnection.getHeaderFields(), cookieMap);
        } catch (Exception e) {
            MyUtils.handleException(e, logger);
            return false;
        } finally {
            MyUtils.closeConnection(httpsURLConnection, logger);
        }
        return true;
    }

    private static boolean getProductsJson() {

        HttpsURLConnection httpsURLConnection = null;
        URL url;

        try {
            url = new URL("https://flights.ctrip.com/itinerary/api/12808/products");
            httpsURLConnection = (HttpsURLConnection) url.openConnection();
            httpsURLConnection.setRequestMethod("POST");
            httpsURLConnection.setUseCaches(false);
            httpsURLConnection.setConnectTimeout(5 * 1000);
            httpsURLConnection.setReadTimeout(5 * 1000);
            httpsURLConnection.setDoOutput(true);
            httpsURLConnection.setDoInput(true);
            httpsURLConnection.setRequestProperty("Accept", "*/*");
            httpsURLConnection.setRequestProperty("Accept-Encoding", "gzip");
            httpsURLConnection.setRequestProperty("User-Agent", "Mozilla/5.0");
            httpsURLConnection.setRequestProperty("Content-Type", "application/json");
            httpsURLConnection = MyUtils.addCookiesToConnection(httpsURLConnection, cookieMap);

            ProductsJsonPost productsJsonPost = new ProductsJsonPost();
            productsJsonPost.airportParams = new AirportParamsItem[1];
            productsJsonPost.airportParams[0] = productsJsonPost.new AirportParamsItem();
            productsJsonPost.airportParams[0].dcity = departureAirportCode;
            productsJsonPost.airportParams[0].acity = arrivalAirportCode;
            productsJsonPost.airportParams[0].date = departDate;
            if (!returnDate.equals("")) {//todo d
                productsJsonPost.flightWay = "";
            }
            final String json = JSON.toJSONString(
                    productsJsonPost, SerializerFeature.NotWriteDefaultValue);
            logger.info(() -> json);

            httpsURLConnection.setRequestProperty("Content-Length", Integer.toString(json.length()));
            OutputStream outputStream = httpsURLConnection.getOutputStream();
            outputStream.write(json.getBytes());
            outputStream.close();
            httpsURLConnection.connect();

            final String result = MyUtils.processJson(httpsURLConnection, logger);
            logger.info(() -> result);
        } catch (Exception e) {
            MyUtils.handleException(e, logger);
            return false;
        } finally {
            MyUtils.closeConnection(httpsURLConnection, logger);
        }

        XSSFSheet sheet = workbook.createSheet(
                departureAirportCode + "->" + arrivalAirportCode + "@" + departDate);
        return true;
    }

    private static boolean getLowestPriceJson() {

        HttpsURLConnection httpsURLConnection = null;
        URL url;
        LowestPriceJsonReturned lowestPriceJsonReturned;

        try {
            url = new URL("https://flights.ctrip.com/itinerary/api/12808/lowestPrice");
            httpsURLConnection = (HttpsURLConnection) url.openConnection();
            httpsURLConnection.setRequestMethod("POST");
            httpsURLConnection.setUseCaches(false);
            httpsURLConnection.setConnectTimeout(5 * 1000);
            httpsURLConnection.setReadTimeout(5 * 1000);
            httpsURLConnection.setDoOutput(true);
            httpsURLConnection.setDoInput(true);
            httpsURLConnection.setRequestProperty("Accept", "*/*");
            httpsURLConnection.setRequestProperty("Accept-Encoding", "gzip");
            httpsURLConnection.setRequestProperty("User-Agent", "Mozilla/5.0");
            httpsURLConnection.setRequestProperty("Content-Type", "application/json");
            httpsURLConnection = MyUtils.addCookiesToConnection(httpsURLConnection, cookieMap);

            LowestPriceJsonPost lowestPriceJsonPost = new LowestPriceJsonPost();
            lowestPriceJsonPost.dcity = departureAirportCode;
            lowestPriceJsonPost.acity = arrivalAirportCode;
            if (returnDate.equals(""))
                lowestPriceJsonPost.flightWay = "Oneway";
            else
                lowestPriceJsonPost.flightWay = "Roundtrip";

            final String json = JSON.toJSONString(
                    lowestPriceJsonPost, SerializerFeature.NotWriteDefaultValue);
            logger.info(() -> json);

            httpsURLConnection.setRequestProperty("Content-Length", Integer.toString(json.length()));
            OutputStream outputStream = httpsURLConnection.getOutputStream();
            outputStream.write(json.getBytes());
            outputStream.close();
            httpsURLConnection.connect();

            final String result = MyUtils.processJson(httpsURLConnection, logger);
            logger.info(() -> result);
            lowestPriceJsonReturned = JSON.parseObject(result, LowestPriceJsonReturned.class);
        } catch (Exception e) {
            MyUtils.handleException(e, logger);
            return false;
        } finally {
            MyUtils.closeConnection(httpsURLConnection, logger);
        }

        XSSFSheet sheet = workbook.createSheet(
                departureAirportCode + "->" + arrivalAirportCode + "@LOWEST");
        if (lowestPriceJsonReturned == null)
            return false;
        if (returnDate.equals("")) {
            int rowNumber = 0;
            for (Map.Entry<String, Integer> entry :
                    lowestPriceJsonReturned.getData().getOneWayPrice()[0].entrySet()) {
                XSSFRow row = sheet.createRow(rowNumber);
                row.createCell(0).setCellValue(entry.getKey());
                row.createCell(1).setCellValue(entry.getValue());
                rowNumber++;
            }
        } else {
            int rowNumber = 0;
            int colNumber;
            for (Map.Entry<String, Map<String, Integer>> departureEntry :
                    lowestPriceJsonReturned.getData().getRoundTripPrice().entrySet()) {
                XSSFRow row = sheet.createRow(rowNumber);
                row.createCell(0).setCellValue(departureEntry.getKey());
                colNumber = 1;
                for (Map.Entry<String, Integer> arrivalEntry : departureEntry.getValue().entrySet()) {
                    row.createCell(colNumber).setCellValue(arrivalEntry.getKey() + "=" + arrivalEntry.getValue());
                    colNumber++;
                }
                rowNumber++;
            }
        }
        try (FileOutputStream out = new FileOutputStream("D:/CtripFlightPrices.xlsx")) {
            workbook.write(out);
        } catch (IOException e) {
            MyUtils.handleException(e, logger);
        }
        return true;
    }

    private static void openExcelFile() {
        try {
            Runtime.getRuntime().exec("");
        } catch (Exception e) {
            MyUtils.handleException(e, logger);
        }
    }
}