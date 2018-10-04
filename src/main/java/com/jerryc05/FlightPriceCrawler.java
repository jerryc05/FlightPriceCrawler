package com.jerryc05;

import com.jerryc05.crawl_ctrip_by_json.CrawlCtripByJson;

import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class FlightPriceCrawler {

    private static String departureAirportCode;
    private static String arrivalAirportCode;
    private static String departDate;
    private static String returnDate;
    private static Logger logger;

    public static void main(String[] args) {

        logger = Logger.getLogger(FlightPriceCrawler.class.getName());

        getInput();
        if (crawlCtripByJson())
            logger.info(CrawlCtripByJson.class.getName() + " finished!");
    }

    private static void getInput() {

        final JTextField departureAirportField = new JTextField();
        final JTextField arrivalAirportField = new JTextField();
        final JTextField departDateField = new JTextField();
        final JTextField returnDateField = new JTextField();
        Object[] popupWindow = {
                "departure airport code:", departureAirportField,
                "arrival airport code:", arrivalAirportField,
                "departure date(YYYY-MM-DD)", departDateField,
                "return date(YYYY-MM-DD)", returnDateField};
        if (JOptionPane.showConfirmDialog(
                null, popupWindow, "com.jerryc05.Flight Search", JOptionPane.DEFAULT_OPTION)
                == JOptionPane.OK_OPTION) {
            if (departureAirportField.getText().trim().equals(""))
                departureAirportCode = "foc";
            if (arrivalAirportField.getText().trim().equals(""))
                arrivalAirportCode = "sha";
            if (departDateField.getText().trim().equals(""))
                departDate = "2018-11-20";
            returnDate = returnDateField.getText().trim();
        }
    }

    private static boolean crawlCtripByJson() {
        CompletableFuture<Boolean> cfCtrip = CompletableFuture.supplyAsync(() ->
                CrawlCtripByJson.crawlCtripByJson(
                        departureAirportCode, arrivalAirportCode, departDate, returnDate));
        try {
            return cfCtrip.get();
        } catch (Exception e) {
            logger.warning(e.getMessage());
            return false;
        }
    }
}
