package com.jerryc05;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.jerryc05.pojo.ctrip.AirportParamsItem;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
                + "\\携程网爬虫[" + departureAirportCode.toUpperCase() + " - "
                + arrivalAirportCode.toUpperCase() + " @ " + departDate + "].xls";
        dateStyle.setDataFormat(dataFormat.getFormat("m\"月\"d\"日\";@"));
        currencyStyle.setDataFormat(dataFormat.getFormat("[$¥-zh-CN]#,##0"));
        currencyStyle.setAlignment(HorizontalAlignment.CENTER);
        centeredStyle.setAlignment(HorizontalAlignment.CENTER);

        if (!initCookie())
            return false;
        else {
            boolean state = true;
            logger.info(() -> "initCookie() successful!");
            if (!getProductsJson())
                state = false;
            else
                logger.info(() -> "getProductsJson() successful!");
            if (!getLowestPriceJson())
                state = false;
            else
                logger.info(() -> "getLowestPriceJson() successful!");
            writeFile();
            Utils.openFile(excelFilePath, logger);
            return state;
        }
    }

    private static boolean initCookie() {

        HttpsURLConnection httpsURLConnection = null;

        try {
            URL url;
            if (returnDate.isEmpty())
                url = new URL("https://flights.ctrip.com/itinerary/oneway/"
                        + departureAirportCode + "-" + arrivalAirportCode + "?date=" + departDate);
            else
                url = new URL("https://flights.ctrip.com/itinerary/roundtrip/"
                        + departureAirportCode + "-" + arrivalAirportCode + "?date=" + departDate + "%2" + returnDate);
            Utils.logTime(logger);
            httpsURLConnection = (HttpsURLConnection) url.openConnection();
            Utils.logTime(logger);
            httpsURLConnection.setRequestMethod("GET");
            httpsURLConnection.setConnectTimeout(5 * 1000);
            httpsURLConnection.setReadTimeout(5 * 1000);
            httpsURLConnection.setDoOutput(true);
            httpsURLConnection.setDoInput(true);
            httpsURLConnection.setRequestProperty(Utils.ACCEPT, Utils.ACCEPT_ALL);
            httpsURLConnection.setRequestProperty(Utils.ACCEPT_ENCODING, Utils.GZIP);
            httpsURLConnection.setRequestProperty(Utils.USER_AGENT, Utils.MOZILLA);
            Utils.addCookiesToConnection(httpsURLConnection, cookieMap);
            httpsURLConnection.connect();

            if (httpsURLConnection.getResponseCode() != HttpURLConnection.HTTP_OK)
                throw new UnsupportedOperationException(
                        "HTTP " + httpsURLConnection.getResponseCode() + " Error\n"
                                + httpsURLConnection.getResponseMessage());
            Utils.saveCookies(httpsURLConnection.getHeaderFields(), cookieMap);
        } catch (Exception e) {
            Utils.handleException(e, logger);
            return false;
        } finally {
            Utils.closeConnection(httpsURLConnection, logger);
        }
        return true;
    }

    private static boolean getProductsJson() {

        HttpsURLConnection httpsURLConnection = null;
        ProductsJsonReturned productsJsonReturned;

        try {
            URL url = new URL("https://flights.ctrip.com/itinerary/api/12808/products");
            Utils.logTime(logger);
            httpsURLConnection = (HttpsURLConnection) url.openConnection();
            Utils.logTime(logger);
            httpsURLConnection.setRequestMethod("POST");
            httpsURLConnection.setUseCaches(false);
            httpsURLConnection.setConnectTimeout(5 * 1000);
            httpsURLConnection.setReadTimeout(5 * 1000);
            httpsURLConnection.setDoOutput(true);
            httpsURLConnection.setDoInput(true);
            httpsURLConnection.setRequestProperty(Utils.ACCEPT, Utils.ACCEPT_ALL);
            httpsURLConnection.setRequestProperty(Utils.ACCEPT_ENCODING, Utils.GZIP);
            httpsURLConnection.setRequestProperty(Utils.USER_AGENT, Utils.MOZILLA);
            httpsURLConnection.setRequestProperty(Utils.CONTENT_TYPE, Utils.APP_JSON);
            Utils.addCookiesToConnection(httpsURLConnection, cookieMap);

            ProductsJsonPost productsJsonPost = new ProductsJsonPost();
            if (returnDate.isEmpty())
                productsJsonPost.setAirportParams(new AirportParamsItem[1]);
            else
                productsJsonPost.setAirportParams(new AirportParamsItem[2]);
            productsJsonPost.getAirportParams()[0] = new AirportParamsItem();
            productsJsonPost.getAirportParams()[0].setDcity(departureAirportCode);
            productsJsonPost.getAirportParams()[0].setAcity(arrivalAirportCode);
            productsJsonPost.getAirportParams()[0].setDate(departDate);
            if (!returnDate.isEmpty()) {
                productsJsonPost.setFlightWay("Roundtrip");
                productsJsonPost.getAirportParams()[1] = new AirportParamsItem();
                productsJsonPost.getAirportParams()[1].setDcity(arrivalAirportCode);
                productsJsonPost.getAirportParams()[1].setAcity(departureAirportCode);
                productsJsonPost.getAirportParams()[1].setDate(returnDate);
            }

            final String json = JSON.toJSONString(
                    productsJsonPost, SerializerFeature.NotWriteDefaultValue);
            logger.info(() -> json);

            httpsURLConnection.setRequestProperty(
                    Utils.CONTENT_LENGTH, Integer.toString(json.length()));
            OutputStream outputStream = httpsURLConnection.getOutputStream();
            outputStream.write(json.getBytes());
            outputStream.close();
            httpsURLConnection.connect();

            final String result = Utils.processJson(httpsURLConnection, logger);
            logger.info(() -> result);
            if (result == null) return false;
            productsJsonReturned = JSON.parseObject(result, ProductsJsonReturned.class);
        } catch (Exception e) {
            Utils.handleException(e, logger);
            return false;
        } finally {
            Utils.closeConnection(httpsURLConnection, logger);
        }

        HSSFSheet sheet;
        if (returnDate.isEmpty())
            sheet = workbook.createSheet(departureAirportCode.toUpperCase()
                    + "->" + arrivalAirportCode.toUpperCase() + "@" + departDate);
        else
            sheet = workbook.createSheet(departureAirportCode.toUpperCase()
                    + "<=>" + arrivalAirportCode.toUpperCase() + "@" + departDate);

        int rowNumber = 0;
        HSSFRow row0 = sheet.createRow(rowNumber);
        HSSFCell r0 = row0.createCell(0);

        r0.setCellValue("邻近城市航班推荐：");
        sheet.addMergedRegionUnsafe(new CellRangeAddress(0, 0, 0, 8));
        r0.setCellStyle(centeredStyle);

        if (productsJsonReturned.getData().getRecommendData() != null) {
            if (returnDate.isEmpty()) {
                FlightsItem recFlight = productsJsonReturned.getData()
                        .getRecommendData().getRedirectSingleProduct().getFlights()[0];
                ++rowNumber;
                parseRecommendData(sheet, recFlight, 0, rowNumber);
            } else {
                FlightsItem[] openJaws = productsJsonReturned.getData()
                        .getRecommendData().getRedirectMRoute().getOpenJaws();
                if (openJaws != null)
                    for (int i = 0; i <= 1; i++) {
                        ++rowNumber;
                        parseRecommendData(sheet, openJaws[i], i, rowNumber);
                    }

                FlightsItem[] roundNears = productsJsonReturned.getData()
                        .getRecommendData().getRedirectMRoute().getRoundNears();
                if (roundNears.length != 0)
                    for (int i = 0; i <= 1; i++) {
                        ++rowNumber;
                        parseRecommendData(sheet, roundNears[i], i, rowNumber);
                    }
            }
        } else
            sheet.addMergedRegionUnsafe(
                    new CellRangeAddress(1, 1, 0, 7));


        rowNumber += 2;
        HSSFRow row2 = sheet.createRow(rowNumber);
        HSSFCell r2 = row2.createCell(0);
        if (returnDate.isEmpty())
            r2.setCellValue(departDate + " 出发，从 " + departureAirportCode.toUpperCase()
                    + " 到 " + arrivalAirportCode.toUpperCase() + " 的所有航班：");
        else
            r2.setCellValue(departDate + " 出发 " + returnDate + " 返回，从 " +
                    departureAirportCode.toUpperCase()
                    + " 往返 " + arrivalAirportCode.toUpperCase() + " 的所有航班：");
        sheet.addMergedRegionUnsafe(new
                CellRangeAddress(rowNumber, rowNumber, 0, 8));
        r2.setCellStyle(centeredStyle);

        RouteListItem[] routeLists = productsJsonReturned.getData().getRouteList();
        Arrays.parallelSort(routeLists);
        if (returnDate.isEmpty()) {
            for (RouteListItem routeList : routeLists) {
                if (!routeList.getRouteType().equals("Flight"))
                    continue;
                LegsItem legs = routeList.getLegs()[0];
                HSSFRow row = sheet.createRow(++rowNumber);

                String airlineName = legs.getFlight().getAirlineName();
                HSSFCell c0 = row.createCell(0);
                c0.setCellValue(airlineName);
                c0.setCellStyle(centeredStyle);

                row.createCell(1).setCellValue(legs.getFlight().getFlightNumber());

                HSSFCell c2 = row.createCell(2);
                c2.setCellValue(legs.getFlight().getDepartureAirportInfo().getAirportName()
                        + legs.getFlight().getDepartureAirportInfo().getTerminal().getName());
                c2.setCellStyle(centeredStyle);

                HSSFCell c3 = row.createCell(3);
                c3.setCellValue(legs.getFlight().getArrivalAirportInfo().getAirportName()
                        + legs.getFlight().getArrivalAirportInfo().getTerminal().getName());
                c3.setCellStyle(centeredStyle);

                HSSFCell c4 = row.createCell(4);
                c4.setCellValue(legs.getFlight().getDepartureDate().substring(11, 16));
                c4.setCellStyle(centeredStyle);

                HSSFCell c5 = row.createCell(5);
                c5.setCellValue(legs.getFlight().getArrivalDate().substring(11, 16));
                c5.setCellStyle(centeredStyle);

                HSSFCell c6 = row.createCell(6);
                c6.setCellValue(Math.min(legs.getCharacteristic().getLowestPrice(),
                        legs.getCabins()[0].getPrice().getPrice()));
                c6.setCellStyle(currencyStyle);
                if (airlineName.equals("东方航空")
                        && legs.getCharacteristic().getLowestPrice()
                        != legs.getCabins()[0].getPrice().getPrice())
                    row.createCell(9).setCellValue("售价为青老年折扣价，原最低价:¥"
                            + legs.getCharacteristic().getLowestPrice());

                HSSFCell c7 = row.createCell(7);
                switch (legs.getFlight().getMealType()) {
                    case "Meal":
                        c7.setCellValue("正餐");
                        break;
                    case "Snack":
                        c7.setCellValue("零食");
                        break;
                    case "None":
                    default:
                        c7.setCellValue("木有");
                }
                c7.setCellStyle(centeredStyle);

                String craftTypeName = legs.getFlight().getCraftTypeName();
                String craftTypeCode = "(" + legs.getFlight().getCraftTypeCode() + ")";
                String craftTypeDisplayName = "\t（" +
                        legs.getFlight().getCraftTypeKindDisplayName() + "）";
                if (craftTypeName == null) {
                    craftTypeName = "";
                    craftTypeCode = legs.getFlight().getCraftTypeCode();
                }
                if (legs.getFlight().getCraftTypeKindDisplayName() == null)
                    craftTypeDisplayName = "\t（中型）";
                row.createCell(8).setCellValue(
                        craftTypeName + craftTypeCode + craftTypeDisplayName);
            }
            sheet.setColumnWidth(0, 11 * 256);
            sheet.setColumnWidth(1, 10 * 256);
            sheet.setColumnWidth(2, 8 * 2 * 256);
            sheet.setColumnWidth(3, 8 * 2 * 256);
            sheet.setColumnWidth(4, 7 * 256);
            sheet.setColumnWidth(5, 7 * 256);
            sheet.setColumnWidth(6, 8 * 256);
            sheet.setColumnWidth(7, 6 * 256);
            sheet.autoSizeColumn(8);
            sheet.setColumnWidth(9, 35 * 256);
        } else {
            for (RouteListItem routeList : routeLists) {
                if (!routeList.getRouteType().equals("Flight"))
                    continue;
                LegsItem legs = routeList.getLegs()[0];
                HSSFRow row = sheet.createRow(++rowNumber);

                String airlineName = legs.getFlight().getAirlineName();
                HSSFCell c0 = row.createCell(0);
                c0.setCellValue(airlineName);
                c0.setCellStyle(centeredStyle);

                row.createCell(1).setCellValue(legs.getFlight().getFlightNumber());

                HSSFCell c2 = row.createCell(2);
                c2.setCellValue(legs.getFlight().getDepartureAirportInfo().getAirportName()
                        + legs.getFlight().getDepartureAirportInfo().getTerminal().getName());
                c2.setCellStyle(centeredStyle);

                HSSFCell c3 = row.createCell(3);
                c3.setCellValue(legs.getFlight().getArrivalAirportInfo().getAirportName()
                        + legs.getFlight().getArrivalAirportInfo().getTerminal().getName());
                c3.setCellStyle(centeredStyle);

                HSSFCell c4 = row.createCell(4);
                c4.setCellValue(legs.getFlight().getDepartureDate().substring(11, 16));
                c4.setCellStyle(centeredStyle);

                HSSFCell c5 = row.createCell(5);
                c5.setCellValue(legs.getFlight().getArrivalDate().substring(11, 16));
                c5.setCellStyle(centeredStyle);

                HSSFCell c6 = row.createCell(6);
                c6.setCellValue(routeList.getCombinedPrice());
                c6.setCellStyle(currencyStyle);
                if (legs.getCharacteristic().isRoundTripDiscounts())
                    row.createCell(9).setCellValue("往返优惠");

                HSSFCell c7 = row.createCell(7);
                switch (legs.getFlight().getMealType()) {
                    case "Meal":
                        c7.setCellValue("正餐");
                        break;
                    case "Snack":
                        c7.setCellValue("零食");
                        break;
                    case "None":
                    default:
                        c7.setCellValue("木有");
                }
                c7.setCellStyle(centeredStyle);

                String craftTypeName = legs.getFlight().getCraftTypeName();
                String craftTypeCode = "(" + legs.getFlight().getCraftTypeCode() + ")";
                String craftTypeDisplayName = "\t（" +
                        legs.getFlight().getCraftTypeKindDisplayName() + "）";
                if (craftTypeName == null) {
                    craftTypeName = "";
                    craftTypeCode = legs.getFlight().getCraftTypeCode();
                }
                if (legs.getFlight().getCraftTypeKindDisplayName() == null)
                    craftTypeDisplayName = "\t（中型）";
                row.createCell(8).setCellValue(
                        craftTypeName + craftTypeCode + craftTypeDisplayName);
            }
            sheet.setColumnWidth(0, 11 * 256);
            sheet.setColumnWidth(1, 10 * 256);
            sheet.setColumnWidth(2, 8 * 2 * 256);
            sheet.setColumnWidth(3, 8 * 2 * 256);
            sheet.setColumnWidth(4, 7 * 256);
            sheet.setColumnWidth(5, 7 * 256);
            sheet.setColumnWidth(6, 8 * 256);
            sheet.setColumnWidth(7, 6 * 256);
            sheet.autoSizeColumn(8);
            sheet.setColumnWidth(9, 35 * 256);

        }
        return true;
    }

    private static boolean getLowestPriceJson() {

        HttpsURLConnection httpsURLConnection = null;
        LowestPriceJsonReturned lowestPriceJsonReturned;

        try {
            URL url = new URL("https://flights.ctrip.com/itinerary/api/12808/lowestPrice");
            httpsURLConnection = (HttpsURLConnection) url.openConnection();
            httpsURLConnection.setRequestMethod("POST");
            httpsURLConnection.setUseCaches(false);
            httpsURLConnection.setConnectTimeout(5 * 1000);
            httpsURLConnection.setReadTimeout(5 * 1000);
            httpsURLConnection.setDoOutput(true);
            httpsURLConnection.setDoInput(true);
            httpsURLConnection.setRequestProperty(Utils.ACCEPT, Utils.ACCEPT_ALL);
            httpsURLConnection.setRequestProperty(Utils.ACCEPT_ENCODING, Utils.GZIP);
            httpsURLConnection.setRequestProperty(Utils.USER_AGENT, Utils.MOZILLA);
            httpsURLConnection.setRequestProperty(Utils.CONTENT_TYPE, Utils.APP_JSON);
            Utils.addCookiesToConnection(httpsURLConnection, cookieMap);

            LowestPriceJsonPost lowestPriceJsonPost = new LowestPriceJsonPost();
            lowestPriceJsonPost.setDcity(Utils.airportCodeToCityCode(departureAirportCode));
            lowestPriceJsonPost.setAcity(Utils.airportCodeToCityCode(arrivalAirportCode));
            if (returnDate.isEmpty())
                lowestPriceJsonPost.setFlightWay("Oneway");
            else
                lowestPriceJsonPost.setFlightWay("Roundtrip");

            final String json = JSON.toJSONString(
                    lowestPriceJsonPost, SerializerFeature.NotWriteDefaultValue);
            logger.info(() -> json);

            httpsURLConnection.setRequestProperty(
                    Utils.CONTENT_LENGTH, Integer.toString(json.length()));
            OutputStream outputStream = httpsURLConnection.getOutputStream();
            outputStream.write(json.getBytes());
            outputStream.close();
            httpsURLConnection.connect();

            final String result = Utils.processJson(httpsURLConnection, logger);
            logger.info(() -> result);
            if (result == null) return false;
            lowestPriceJsonReturned = JSON.parseObject(result, LowestPriceJsonReturned.class);
        } catch (Exception e) {
            Utils.handleException(e, logger);
            return false;
        } finally {
            Utils.closeConnection(httpsURLConnection, logger);
        }

        if (lowestPriceJsonReturned == null)
            return false;
        if (returnDate.isEmpty()) {
            Map<String, Integer> oneWayPrice =
                    lowestPriceJsonReturned.getData().getOneWayPrice()[0];
            FlightLowestPriceInfo[] flightLowestPriceInfos =
                    new FlightLowestPriceInfo[oneWayPrice.size()];
            int index = 0;
            for (Map.Entry<String, Integer> flight : oneWayPrice.entrySet()) {
                flightLowestPriceInfos[index] =
                        new FlightLowestPriceInfo(formatDate(flight.getKey()), flight.getValue());
                index++;
            }
            Arrays.parallelSort(flightLowestPriceInfos);

            HSSFSheet sheet = workbook.createSheet(departureAirportCode.toUpperCase()
                    + "->" + arrivalAirportCode.toUpperCase() + "@最低价");
            int rowNumber = -1;
            HSSFRow row0 = sheet.createRow(++rowNumber);
            row0.createCell(0).setCellValue("出发日期");
            row0.createCell(1).setCellValue("最低价");

            for (FlightLowestPriceInfo flightLowestPriceInfo : flightLowestPriceInfos) {
                HSSFRow row = sheet.createRow(++rowNumber);
                HSSFCell cell0 = row.createCell(0);
                cell0.setCellValue(flightLowestPriceInfo.departureDate);
                cell0.setCellStyle(dateStyle);
                HSSFCell cell1 = row.createCell(1);
                cell1.setCellValue(flightLowestPriceInfo.price);
                cell1.setCellStyle(currencyStyle);
            }
            sheet.setColumnWidth(0, 12 * 256);
            sheet.setColumnWidth(1, 8 * 256);
        } else {
            Map<String, Map<String, Integer>> roundTripPrice =
                    lowestPriceJsonReturned.getData().getRoundTripPrice();
            ArrayList<FlightLowestPriceInfo> flightLowestPriceInfos = new ArrayList<>();
            for (Map.Entry<String, Map<String, Integer>> deptFlight : roundTripPrice.entrySet())
                for (Map.Entry<String, Integer> flight : deptFlight.getValue().entrySet())
                    flightLowestPriceInfos.add(new FlightLowestPriceInfo(
                            formatDate(deptFlight.getKey()),
                            formatDate(flight.getKey()), flight.getValue()));
            Collections.sort(flightLowestPriceInfos);

            HSSFSheet sheet = workbook.createSheet(departureAirportCode.toUpperCase()
                    + "<=>" + arrivalAirportCode.toUpperCase() + "@最低价");
            int rowNumber = -1;
            HSSFRow row0 = sheet.createRow(++rowNumber);
            row0.createCell(0).setCellValue("出发日期");
            row0.createCell(1).setCellValue("返回日期");
            row0.createCell(2).setCellValue("最低价");

            for (FlightLowestPriceInfo flightLowestPriceInfo : flightLowestPriceInfos) {
                HSSFRow row = sheet.createRow(++rowNumber);
                HSSFCell cell0 = row.createCell(0);
                cell0.setCellValue(flightLowestPriceInfo.departureDate);
                cell0.setCellStyle(dateStyle);
                HSSFCell cell1 = row.createCell(1);
                cell1.setCellValue(flightLowestPriceInfo.returnDate);
                cell1.setCellStyle(dateStyle);
                HSSFCell cell2 = row.createCell(2);
                cell2.setCellValue(flightLowestPriceInfo.price);
                cell2.setCellStyle(currencyStyle);
            }
            sheet.setColumnWidth(0, 12 * 256);
            sheet.setColumnWidth(1, 12 * 256);
            sheet.setColumnWidth(2, 8 * 256);
        }
        return true;
    }

    private static String formatDate(final String date) {
        return date.substring(0, 4)
                + "-" + date.substring(4, 6)
                + "-" + date.substring(6);
    }

    private static void parseRecommendData(final HSSFSheet sheet, final FlightsItem item,
                                           final int i, final int rowNumber) {

        HSSFRow row = sheet.createRow(rowNumber);
        row.createCell(1).setCellValue(item.getTransportNo());
        row.createCell(2).setCellValue(item.getDepartureCityName());
        row.createCell(3).setCellValue(item.getArrivalCityName());

        HSSFCell c4 = row.createCell(4);
        c4.setCellValue(item.getDepartureTime().substring(11, 16));
        c4.setCellStyle(centeredStyle);

        HSSFCell c5 = row.createCell(5);
        c5.setCellValue(item.getArrivalTime().substring(11, 16));
        c5.setCellStyle(centeredStyle);

        HSSFCell c6 = row.createCell(6);
        c6.setCellValue(item.getPrice());
        c6.setCellStyle(currencyStyle);

        if (i == 1) {
            c6 = sheet.getRow(rowNumber - 1).getCell(6);
            c6.setCellValue(
                    sheet.getRow(rowNumber - 1)
                            .getCell(6).getNumericCellValue()
                            + sheet.getRow(rowNumber)
                            .getCell(6).getNumericCellValue()
            );
            sheet.addMergedRegionUnsafe(new CellRangeAddress(
                    rowNumber - 1, rowNumber, 6, 6));
            c6.setCellStyle(currencyStyle);
        }
    }

    private static void writeFile() {

        try (FileOutputStream out = new FileOutputStream(excelFilePath)) {
            workbook.write(out);
        } catch (FileNotFoundException fNF) {
            Utils.handleException(fNF, logger);
            excelFilePath += ".xls";
            writeFile();
        } catch (IOException iO) {
            Utils.handleException(iO, logger);
        }
    }
}