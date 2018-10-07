package com.jerryc05;

import java.awt.Button;
import java.awt.Checkbox;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FlightPriceCrawler {

    private static String departureAirportCode;
    private static String arrivalAirportCode;
    private static String departDate;
    private static String returnDate;
    private static Logger logger;
    private static Label error = new Label();
    private static Frame frame;

    public static void main(String[] args) {

        logger = Logger.getLogger(FlightPriceCrawler.class.getName());
        logger.setLevel(Level.OFF);
        for (String arg : args) {
            if (arg.equals("-log"))
                logger.setLevel(Level.WARNING);
            if (arg.equals("--log-level=info"))
                logger.setLevel(Level.INFO);
        }

        getInput();
    }

    private static void getInput() {

        frame = new Frame("Flight Crawler by @jerryc05");
        frame.setSize(400, 500);
        frame.setResizable(false);
        frame.setLayout(null);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent we) {
                frame.dispose();
            }
        });

        Label welcomeLabel = new Label("Welcome to Flight Crawler v" + MyUtils.VERSION);
        welcomeLabel.setFont(new Font(Font.DIALOG, Font.BOLD, 18));
        welcomeLabel.setAlignment(Label.CENTER);
        welcomeLabel.setBounds(0, 50, frame.getWidth(), 50);
        frame.add(welcomeLabel);


        Label departureCityCodeLabel = new Label("Departure airport IATA code:   ");
        departureCityCodeLabel.setAlignment(Label.RIGHT);
        departureCityCodeLabel.setBounds(
                0, 120, frame.getWidth() / 2, 25);
        frame.add(departureCityCodeLabel);
        TextField departureCityCodeField = new TextField();
        departureCityCodeField.setText("foc");//todo
        departureCityCodeField.setFont(new Font(Font.DIALOG, Font.PLAIN, 15));
        departureCityCodeField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                departureCityCodeField.setText(departureCityCodeField.getText().trim());
            }
        });
        departureCityCodeField.setBounds(
                frame.getWidth() / 2, 120, frame.getWidth() / 2 - 50, 25);
        frame.add(departureCityCodeField);


        Label arrivalCityCodeLabel = new Label("Arrival airport IATA code:   ");
        arrivalCityCodeLabel.setAlignment(Label.RIGHT);
        arrivalCityCodeLabel.setBounds(
                0, 170, frame.getWidth() / 2, 25);
        frame.add(arrivalCityCodeLabel);
        TextField arrivalCityCodeField = new TextField();
        arrivalCityCodeField.setText("sha");//todo
        arrivalCityCodeField.setFont(new Font(Font.DIALOG, Font.PLAIN, 15));
        arrivalCityCodeField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                arrivalCityCodeField.setText(arrivalCityCodeField.getText().trim());
            }
        });
        arrivalCityCodeField.setBounds(
                frame.getWidth() / 2, 170, frame.getWidth() / 2 - 50, 25);
        frame.add(arrivalCityCodeField);


        Label departureDateLabel = new Label("Departure Date (YYYY-MM-DD):   ");
        departureDateLabel.setAlignment(Label.RIGHT);
        departureDateLabel.setBounds(
                0, 220, frame.getWidth() / 2, 25);
        frame.add(departureDateLabel);
        TextField departureDateField = new TextField();
        departureDateField.setFont(new Font(Font.DIALOG, Font.PLAIN, 15));
        departureDateField.setText("2018-12-21");//todo
        departureDateField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                departureDateField.setText(departureDateField.getText().trim());
            }
        });
        departureDateField.setBounds(
                frame.getWidth() / 2, 220, frame.getWidth() / 2 - 50, 25);
        frame.add(departureDateField);


        Label returnDateLabel = new Label("Arrival Date (YYYY-MM-DD):   ");
        returnDateLabel.setAlignment(Label.RIGHT);
        returnDateLabel.setBounds(
                0, 270, frame.getWidth() / 2, 25);
        frame.add(returnDateLabel);
        TextField returnDateField = new TextField();
        returnDateField.setFont(new Font(Font.DIALOG, Font.PLAIN, 15));
        returnDateField.setEnabled(false);//todo
        returnDateField.setText("Not yet available ......");//todo
        returnDateField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                returnDateField.setText(returnDateField.getText().trim());
            }
        });
        returnDateField.setBounds(
                frame.getWidth() / 2, 270, frame.getWidth() / 2 - 50, 25);
        frame.add(returnDateField);


        Checkbox ctrip = new Checkbox("Ctrip");
        ctrip.setBounds(30, 310, 50, 50);
        frame.add(ctrip);

        Checkbox fliggy = new Checkbox("Fliggy");
        fliggy.setEnabled(false);
        fliggy.setBounds(30 + (frame.getWidth() - 60) / 5, 310, 50, 50);
        frame.add(fliggy);

        Checkbox qunar = new Checkbox("Qunar");
        qunar.setEnabled(false);
        qunar.setBounds(30 + (frame.getWidth() - 60) / 5 * 2, 310, 50, 50);
        frame.add(qunar);

        Checkbox ly = new Checkbox("Ly");
        ly.setEnabled(false);
        ly.setBounds(30 + (frame.getWidth() - 60) / 5 * 3, 310, 50, 50);
        frame.add(ly);

        Checkbox zhixing = new Checkbox("ZhiXing");
        zhixing.setEnabled(false);
        zhixing.setBounds(30 + (frame.getWidth() - 60) / 5 * 4, 310, 50, 50);
        frame.add(zhixing);

        error.setFont(new Font(Font.DIALOG, Font.BOLD, 14));
        error.setBounds(50, 370, frame.getWidth() - 100, 30);
        error.setAlignment(Label.CENTER);
        frame.add(error);

        Button button = new Button("Crawl it!");
        button.setFont(new Font(Font.DIALOG, Font.BOLD, 16));
        button.setBounds(50, 420, frame.getWidth() - 100, 40);
        frame.add(button);

        frame.setVisible(true);

        button.addActionListener(e -> {
            FlightPriceCrawler.departureAirportCode = departureCityCodeField.getText();
            FlightPriceCrawler.arrivalAirportCode = arrivalCityCodeField.getText();
            FlightPriceCrawler.departDate = departureDateField.getText();
            FlightPriceCrawler.returnDate = "";//todo returnDateField.getText();
            if (ctrip.getState() || fliggy.getState() ||
                    qunar.getState() || ly.getState() || zhixing.getState()) {
                error.setText("Processing, please wait...");
                FlightPriceCrawler.processInput(ctrip.getState(), fliggy.getState(),
                        qunar.getState(), ly.getState(), zhixing.getState());
            } else {
                error.setText("Please check one source to crawl!");
            }
        });
    }

    private static void processInput(final boolean ctrip,
                                     final boolean fliggy,
                                     final boolean qunar,
                                     final boolean ly,
                                     final boolean zhixing) {

        CompletableFuture<Boolean> cfCtrip = null;
        if (ctrip) cfCtrip = CompletableFuture.supplyAsync(() ->
                CrawlCtripByJson.crawlCtripByJson(
                        departureAirportCode.toUpperCase(),
                        arrivalAirportCode.toUpperCase(),
                        departDate, returnDate));
        if (fliggy) {
        }
        if (qunar) {
        }
        if (ly) {
        }
        if (zhixing) {
        }
        try {
            if (cfCtrip != null && cfCtrip.get())
                logger.info(() -> "finished!");
            error.setText("Crawing finished!");
            Thread.sleep(2000);
            frame.dispose();
        } catch (Exception e) {
            MyUtils.handleException(e, logger);
        }
    }
}
