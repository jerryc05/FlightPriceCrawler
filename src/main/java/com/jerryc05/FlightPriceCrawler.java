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
            logger.info(() -> CrawlCtripByJson.class.getName() + " finished!");
    }

    private static void getInput() {

        final JTextField departureAirportField = new JTextField();
        final JTextField arrivalAirportField = new JTextField();
        final JTextField departDateField = new JTextField();
        final JTextField returnDateField = new JTextField();
        Object[] popupWindow = {
                "Departure airport IATA code:", departureAirportField,
                "Arrival airport IATA code:", arrivalAirportField,
                "Departure date(YYYY-MM-DD):", departDateField,
                "Return date(YYYY-MM-DD) or blank:", returnDateField};
        returnDateField.setText("Please leave blank, it is not working!");//todo
        if (JOptionPane.showConfirmDialog(
                null, popupWindow, "Flight Search", JOptionPane.DEFAULT_OPTION)
                == JOptionPane.OK_OPTION) {
            if (departureAirportField.getText().trim().equals(""))
                departureAirportCode = "foc";
            if (arrivalAirportField.getText().trim().equals(""))
                arrivalAirportCode = "sha";
            if (departDateField.getText().trim().equals(""))
                departDate = "2018-11-20";
            returnDateField.setText(null);//todo
            returnDate = returnDateField.getText().trim();
        } else {
            System.exit(-2);
        }
    }

    private static boolean crawlCtripByJson() {
        CompletableFuture<Boolean> cfCtrip = CompletableFuture.supplyAsync(() ->
                CrawlCtripByJson.crawlCtripByJson(
                        departureAirportCode.toUpperCase(),
                        arrivalAirportCode.toUpperCase(),
                        departDate, returnDate));
        try {
            return cfCtrip.get();
        } catch (Exception e) {
            MyUtils.handleException(e, logger);
            return false;
        }
    }
}
