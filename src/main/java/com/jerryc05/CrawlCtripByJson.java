package com.jerryc05;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.jerryc05.pojo.ctrip.LowestPriceJsonPost;
import com.jerryc05.pojo.ctrip.LowestPriceJsonReturned;
import com.jerryc05.pojo.ctrip.ProductsJsonPost;
import com.jerryc05.pojo.ctrip.ProductsJsonReturned;
import com.jerryc05.pojo.ctrip.ProductsJsonReturned.Data.RecommendData.RedirectSingleProduct.FlightsItem;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.logging.Logger;

import javax.net.ssl.HttpsURLConnection;

class CrawlCtripByJson {

    private static Map<String, String> cookieMap = null;
    private static String departureAirportCode;
    private static String arrivalAirportCode;
    private static String departDate;
    private static String returnDate;
    private static Logger logger;
    private static final HSSFWorkbook workbook = new HSSFWorkbook();
    private static String excelFilePath;

    private CrawlCtripByJson() {
    }

    static boolean crawlCtripByJson(final String departureAirportCode,
                                    final String arrivalAirportCode,
                                    final String departDate,
                                    final String returnDate) {

        logger = Logger.getLogger(CrawlCtripByJson.class.getName());
        CrawlCtripByJson.departureAirportCode = departureAirportCode;
        CrawlCtripByJson.arrivalAirportCode = arrivalAirportCode;
        CrawlCtripByJson.departDate = departDate;
        CrawlCtripByJson.returnDate = returnDate;
        excelFilePath = "D:/Ctrip[" + departureAirportCode + " - " + arrivalAirportCode
                + " @ " + departDate + "].xls";
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
                    writeFile();
                    MyUtils.openFile(excelFilePath, logger);
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
            httpsURLConnection.setRequestProperty(MyUtils.ACCEPT, MyUtils.ACCEPT_ALL);
            httpsURLConnection.setRequestProperty(MyUtils.ACCEPT_ENCODING, MyUtils.GZIP);
            httpsURLConnection.setRequestProperty(MyUtils.USER_AGENT, MyUtils.MOZILLA);
            httpsURLConnection = MyUtils.addCookiesToConnection(httpsURLConnection, cookieMap);
            httpsURLConnection.connect();

            if (httpsURLConnection.getResponseCode() != HttpURLConnection.HTTP_OK)
                throw new UnsupportedOperationException(
                        "HTTP " + httpsURLConnection.getResponseCode() + " Error\n"
                                + httpsURLConnection.getResponseMessage());
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
        ProductsJsonReturned productsJsonReturned;

        try {
            url = new URL("https://flights.ctrip.com/itinerary/api/12808/products");
            httpsURLConnection = (HttpsURLConnection) url.openConnection();
            httpsURLConnection.setRequestMethod("POST");
            httpsURLConnection.setUseCaches(false);
            httpsURLConnection.setConnectTimeout(5 * 1000);
            httpsURLConnection.setReadTimeout(5 * 1000);
            httpsURLConnection.setDoOutput(true);
            httpsURLConnection.setDoInput(true);
            httpsURLConnection.setRequestProperty(MyUtils.ACCEPT, MyUtils.ACCEPT_ALL);
            httpsURLConnection.setRequestProperty(MyUtils.ACCEPT_ENCODING, MyUtils.GZIP);
            httpsURLConnection.setRequestProperty(MyUtils.USER_AGENT, MyUtils.MOZILLA);
            httpsURLConnection.setRequestProperty(MyUtils.CONTENT_TYPE, MyUtils.APP_JSON);
            httpsURLConnection = MyUtils.addCookiesToConnection(httpsURLConnection, cookieMap);

            ProductsJsonPost productsJsonPost = new ProductsJsonPost();
            productsJsonPost.setAirportParams(new ProductsJsonPost.AirportParamsItem[1]);
            productsJsonPost.getAirportParams()[0] = productsJsonPost.new AirportParamsItem();
            productsJsonPost.getAirportParams()[0].setDcity(departureAirportCode);
            productsJsonPost.getAirportParams()[0].setAcity(arrivalAirportCode);
            productsJsonPost.getAirportParams()[0].setDate(departDate);
            productsJsonPost.setSearchIndex(1);

            if (!returnDate.equals("")) {
                productsJsonPost.setFlightWay("");//todo
            }
            final String json = JSON.toJSONString(
                    productsJsonPost, SerializerFeature.NotWriteDefaultValue);
            logger.info(() -> json);

            httpsURLConnection.setRequestProperty(
                    MyUtils.CONTENT_LENGTH, Integer.toString(json.length()));
            OutputStream outputStream = httpsURLConnection.getOutputStream();
            outputStream.write(json.getBytes());
            outputStream.close();
            httpsURLConnection.connect();

            final String result = MyUtils.processJson(httpsURLConnection, logger);
            logger.info(() -> result);
            if (result == null) return false;
            productsJsonReturned = JSON.parseObject(result, ProductsJsonReturned.class);
        } catch (Exception e) {
            MyUtils.handleException(e, logger);
            return false;
        } finally {
            MyUtils.closeConnection(httpsURLConnection, logger);
        }

        HSSFSheet sheet = workbook.createSheet(
                departureAirportCode + "->" + arrivalAirportCode + "@" + departDate);
        HSSFRow row0 = sheet.createRow(0);
        FlightsItem recFlight = productsJsonReturned.getData().getRecommendData()
                .getRedirectSingleProduct().getFlights().get(0);
        row0.createCell(1).setCellValue(recFlight.getTransportNo());
        row0.createCell(2).setCellValue(recFlight.getDepartureCityName());
        row0.createCell(3).setCellValue(recFlight.getArrivalCityName());
        row0.createCell(4).setCellValue(recFlight.getDepartureTime());
        row0.createCell(5).setCellValue(recFlight.getArrivalTime());
        row0.createCell(6).setCellValue(recFlight.getPrice());
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
            httpsURLConnection.setRequestProperty(MyUtils.ACCEPT, MyUtils.ACCEPT_ALL);
            httpsURLConnection.setRequestProperty(MyUtils.ACCEPT_ENCODING, MyUtils.GZIP);
            httpsURLConnection.setRequestProperty(MyUtils.USER_AGENT, MyUtils.MOZILLA);
            httpsURLConnection.setRequestProperty(MyUtils.CONTENT_TYPE, MyUtils.APP_JSON);
            httpsURLConnection = MyUtils.addCookiesToConnection(httpsURLConnection, cookieMap);

            LowestPriceJsonPost lowestPriceJsonPost = new LowestPriceJsonPost();
            lowestPriceJsonPost.setDcity(departureAirportCode);
            lowestPriceJsonPost.setAcity(arrivalAirportCode);
            if (returnDate.equals(""))
                lowestPriceJsonPost.setFlightWay("Oneway");
            else
                lowestPriceJsonPost.setFlightWay("Roundtrip");

            final String json = JSON.toJSONString(
                    lowestPriceJsonPost, SerializerFeature.NotWriteDefaultValue);
            logger.info(() -> json);

            httpsURLConnection.setRequestProperty(
                    MyUtils.CONTENT_LENGTH, Integer.toString(json.length()));
            OutputStream outputStream = httpsURLConnection.getOutputStream();
            outputStream.write(json.getBytes());
            outputStream.close();
            httpsURLConnection.connect();

            final String result = MyUtils.processJson(httpsURLConnection, logger);
            logger.info(() -> result);
            if (result == null) return false;
            lowestPriceJsonReturned = JSON.parseObject(result, LowestPriceJsonReturned.class);
        } catch (Exception e) {
            MyUtils.handleException(e, logger);
            return false;
        } finally {
            MyUtils.closeConnection(httpsURLConnection, logger);
        }

        HSSFSheet sheet = workbook.createSheet(
                departureAirportCode + "->" + arrivalAirportCode + "@LOWEST");
        if (lowestPriceJsonReturned == null)
            return false;
        if (returnDate.equals("")) {
            HSSFRow row0 = sheet.createRow(0);
            row0.createCell(0).setCellValue("Arrival Date");
            row0.createCell(1).setCellValue("Flight Price");
            int rowNumber = 1;
            for (Map.Entry<String, Integer> entry :
                    lowestPriceJsonReturned.getData().getOneWayPrice()[0].entrySet()) {
                HSSFRow row = sheet.createRow(rowNumber);
                HSSFCell cell0 = row.createCell(0);
                String date = entry.getKey();
                date = date.substring(0, 4) + "-" + date.substring(4, 6) + "-" + date.substring(6);
                cell0.setCellValue(date);
                HSSFCell cell1 = row.createCell(1);
                cell1.setCellValue(entry.getValue());

                HSSFCellStyle cellStyle = workbook.createCellStyle();
                HSSFDataFormat dataFormat = workbook.createDataFormat();
                cellStyle.setDataFormat(dataFormat.getFormat("yyyy\"年\"m\"月\"d\"日\";@"));
                cell0.setCellStyle(cellStyle);
                cellStyle.setDataFormat(dataFormat.getFormat("[$¥-zh-CN]#,##0.00"));
                cell1.setCellStyle(cellStyle);
                rowNumber++;
            }
            sheet.autoSizeColumn(0);
            sheet.setColumnWidth(1, 10 * 256);
        } else {
            int rowNumber = 0;
            int colNumber;
            for (Map.Entry<String, Map<String, Integer>> departureEntry : lowestPriceJsonReturned.getData().getRoundTripPrice().entrySet()) {
                HSSFRow row = sheet.createRow(rowNumber);
                row.createCell(0).setCellValue(departureEntry.getKey());
                colNumber = 1;
                for (Map.Entry<String, Integer> arrivalEntry : departureEntry.getValue().entrySet()) {
                    row.createCell(colNumber).setCellValue(arrivalEntry.getKey() + "=" + arrivalEntry.getValue());
                    colNumber++;
                }
                rowNumber++;
            }
        }
        return true;
    }

    private static void writeFile() {

        try (FileOutputStream out = new FileOutputStream(excelFilePath)) {
            workbook.write(out);
        } catch (FileNotFoundException fNF) {
            MyUtils.handleException(fNF, logger);
            excelFilePath += ".xls";
            writeFile();
        } catch (IOException iO) {
            MyUtils.handleException(iO, logger);
        }
    }
}