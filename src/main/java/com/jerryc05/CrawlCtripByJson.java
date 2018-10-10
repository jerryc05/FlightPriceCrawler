package com.jerryc05;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.jerryc05.pojo.ctrip.FlightsItem;
import com.jerryc05.pojo.ctrip.LegsItem;
import com.jerryc05.pojo.ctrip.LowestPriceJsonPost;
import com.jerryc05.pojo.ctrip.LowestPriceJsonReturned;
import com.jerryc05.pojo.ctrip.ProductsJsonPost;
import com.jerryc05.pojo.ctrip.ProductsJsonReturned;
import com.jerryc05.pojo.ctrip.RouteListItem;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.net.ssl.HttpsURLConnection;
import javax.swing.filechooser.FileSystemView;

class CrawlCtripByJson {

    private static Map<String, String> cookieMap = null;
    private static String departureAirportCode;
    private static String arrivalAirportCode;
    private static String departDate;
    private static String returnDate;
    private static Logger logger;
    private static final HSSFWorkbook workbook = new HSSFWorkbook();
    private static String excelFilePath;
    private static final HSSFDataFormat dataFormat = workbook.createDataFormat();
    private static final HSSFCellStyle dateStyle = workbook.createCellStyle();
    private static final HSSFCellStyle currencyStyle = workbook.createCellStyle();
    private static final HSSFCellStyle centeredStyle = workbook.createCellStyle();
    private static final HSSFCellStyle wrapRow = workbook.createCellStyle();


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
        excelFilePath = FileSystemView.getFileSystemView().getHomeDirectory()
                + "\\Ctrip[" + departureAirportCode + " - " + arrivalAirportCode
                + " @ " + departDate + "].xls";
        dateStyle.setDataFormat(dataFormat.getFormat("m\"月\"d\"日\";@"));
        currencyStyle.setDataFormat(dataFormat.getFormat("[$¥-zh-CN]#,##0"));
        centeredStyle.setAlignment(HorizontalAlignment.CENTER);
        wrapRow.setWrapText(true);

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
            httpsURLConnection.setConnectTimeout(5 * 1000);
            httpsURLConnection.setReadTimeout(5 * 1000);
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

        HSSFSheet sheet = workbook.createSheet(departureAirportCode.toUpperCase()
                + "->" + arrivalAirportCode.toUpperCase() + "@" + departDate);

        HSSFRow row0 = sheet.createRow(0);
        HSSFCell r0 = row0.createCell(0);

        r0.setCellValue("Recommended Nearby Flights:");
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 7));
        r0.setCellStyle(centeredStyle);
        row0.setRowStyle(wrapRow);

        HSSFRow row1 = sheet.createRow(1);
        HSSFCell r1c0 = row1.createCell(0);
        r1c0.setCellValue("N/A");
        if (productsJsonReturned.getData().getRecommendData() != null) {
            FlightsItem recFlight = productsJsonReturned.getData()
                    .getRecommendData().getRedirectSingleProduct().getFlights().get(0);

            row1.createCell(1).setCellValue(recFlight.getTransportNo());
            row1.createCell(2).setCellValue(recFlight.getDepartureCityName());
            row1.createCell(3).setCellValue(recFlight.getArrivalCityName());
            row1.setRowStyle(wrapRow);

            HSSFCell r1c4 = row1.createCell(4);
            r1c4.setCellValue(recFlight.getDepartureTime().substring(11, 16));
            r1c4.setCellStyle(centeredStyle);

            HSSFCell r1c5 = row1.createCell(5);
            r1c5.setCellValue(recFlight.getArrivalTime().substring(11, 16));
            r1c5.setCellStyle(centeredStyle);

            HSSFCell r1c6 = row1.createCell(6);
            r1c6.setCellValue(recFlight.getPrice());
            r1c6.setCellStyle(currencyStyle);

            row1.createCell(7).setCellValue("N/A");
        } else {
            sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 7));
            r1c0.setCellStyle(centeredStyle);
            row1.setRowStyle(wrapRow);
        }

        HSSFRow row2 = sheet.createRow(3);
        HSSFCell r2 = row2.createCell(0);
        r2.setCellValue("Flights from " + departureAirportCode.toUpperCase()
                + " to " + arrivalAirportCode.toUpperCase() + " @ " + departDate);
        sheet.addMergedRegion(new CellRangeAddress(3, 3, 0, 7));
        r2.setCellStyle(centeredStyle);

        List<RouteListItem> routeLists = productsJsonReturned.getData().getRouteList();
        Collections.sort(routeLists);
        LegsItem legs;
        int row = 4;

        for (RouteListItem routeList : routeLists) {
            if (!routeList.getRouteType().equals("Flight")) //todo
                continue;
            legs = routeList.getLegs().get(0);
            HSSFRow hssfRow;
            hssfRow = sheet.createRow(row);

            hssfRow.createCell(0).setCellValue(legs.getFlight().getAirlineName());
            hssfRow.createCell(1).setCellValue(legs.getFlight().getFlightNumber());
            hssfRow.createCell(2).setCellValue(legs.getFlight().getDepartureAirportInfo().getAirportName()
                    + legs.getFlight().getDepartureAirportInfo().getTerminal().getName());
            hssfRow.createCell(3).setCellValue(legs.getFlight().getArrivalAirportInfo().getAirportName()
                    + legs.getFlight().getArrivalAirportInfo().getTerminal().getName());

            HSSFCell c4 = hssfRow.createCell(4);
            c4.setCellValue(legs.getFlight().getDepartureDate().substring(11, 16));
            c4.setCellStyle(centeredStyle);

            HSSFCell c5 = hssfRow.createCell(5);
            c5.setCellValue(legs.getFlight().getArrivalDate().substring(11, 16));
            c5.setCellStyle(centeredStyle);

            HSSFCell c6 = hssfRow.createCell(6);
            c6.setCellValue(legs.getCharacteristic().getLowestPrice());
            c6.setCellStyle(currencyStyle);

            hssfRow.createCell(7).setCellValue(legs.getFlight().getCraftTypeName()
                    + "(" + legs.getFlight().getCraftTypeCode() + ")"
                    + "（" + legs.getFlight().getCraftTypeKindDisplayName() + "）");
            hssfRow.setRowStyle(wrapRow);
            row++;
        }
        sheet.setColumnWidth(0, 11 * 256);
        sheet.setColumnWidth(1, 10 * 256);
        sheet.setColumnWidth(2, 8 * 2 * 256);
        sheet.setColumnWidth(3, 8 * 2 * 256);
        sheet.setColumnWidth(4, 7 * 256);
        sheet.setColumnWidth(5, 7 * 256);
        sheet.setColumnWidth(6, 8 * 256);
        sheet.autoSizeColumn(7);
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
            lowestPriceJsonPost.setDcity(MyUtils.airportCodeToCityCode(departureAirportCode));
            lowestPriceJsonPost.setAcity(MyUtils.airportCodeToCityCode(arrivalAirportCode));
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

        HSSFSheet sheet = workbook.createSheet(departureAirportCode.toUpperCase()
                + "->" + arrivalAirportCode.toUpperCase() + "@LOWEST");
        if (lowestPriceJsonReturned == null)
            return false;
        if (returnDate.equals("")) {
            HSSFRow row0 = sheet.createRow(0);
            row0.createCell(0).setCellValue("Arrival Date");
            row0.createCell(1).setCellValue("Lowest Price");
            int rowNumber = 1;

            Map<String, Integer> oneWayPrice = lowestPriceJsonReturned.getData().getOneWayPrice()[0];
            List<Map.Entry<String, Integer>> list = new LinkedList<>(oneWayPrice.entrySet());
            list.sort((entry1, entry2) -> {
                int priceCompareResult = entry1.getValue().compareTo(entry2.getValue());
                if (priceCompareResult != 0)
                    return priceCompareResult;
                else {
                    int year1 = Integer.parseInt(entry1.getKey().substring(0, 4));
                    int year2 = Integer.parseInt(entry2.getKey().substring(0, 4));
                    if (year1 != year2)
                        return year1 - year2;
                    else {
                        int month1 = Integer.parseInt(entry1.getKey().substring(4, 6));
                        int month2 = Integer.parseInt(entry2.getKey().substring(4, 6));
                        if (month1 != month2)
                            return month1 - month2;
                        else
                            return Integer.parseInt(entry1.getKey().substring(6))
                                    - Integer.parseInt(entry2.getKey().substring(6));
                    }
                }
            });


            oneWayPrice = new LinkedHashMap<>();
            for (Map.Entry<String, Integer> entry : list) {
                oneWayPrice.put(entry.getKey(), entry.getValue());
            }

            for (Map.Entry<String, Integer> entry :
                    oneWayPrice.entrySet()) {
                HSSFRow row = sheet.createRow(rowNumber);
                HSSFCell cell0 = row.createCell(0);
                String date = entry.getKey();
                date = date.substring(0, 4) + "-" + date.substring(4, 6) + "-" + date.substring(6);
                cell0.setCellValue(date);
                HSSFCell cell1 = row.createCell(1);
                cell1.setCellValue(entry.getValue());

                cell0.setCellStyle(dateStyle);
                cell1.setCellStyle(currencyStyle);
                rowNumber++;
            }
            sheet.autoSizeColumn(0);
            sheet.setColumnWidth(1, 13 * 256);
        } else {//todo
            int rowNumber = 0;
            int colNumber;
            for (Map.Entry<String, Map<String, Integer>> departureEntry :
                    lowestPriceJsonReturned.getData().getRoundTripPrice().entrySet()) {
                HSSFRow row = sheet.createRow(rowNumber);
                row.createCell(0).setCellValue(departureEntry.getKey());
                colNumber = 1;
                for (Map.Entry<String, Integer> arrivalEntry :
                        departureEntry.getValue().entrySet()) {
                    row.createCell(colNumber).setCellValue(
                            arrivalEntry.getKey() + "=" + arrivalEntry.getValue());
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