import java.util.concurrent.CompletableFuture;

import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class FlightPriceCrawler {

    public static void main(String[] args) {

        //input
        final JTextField departureAirportField = new JTextField();
        final JTextField arrivalAirportField = new JTextField();
        final JTextField departDateField = new JTextField();
        final JTextField returnDateField = new JTextField();
        Object[] popupWindow = {
                "departure airport code:", departureAirportField,
                "arrival airport code:", arrivalAirportField,
                "departure date(YYYY-MM-DD)", departDateField,
                "return date(YYYY-MM-DD)", returnDateField
        };
        if (JOptionPane.showConfirmDialog(
                null, popupWindow, "Flight Search", JOptionPane.OK_CANCEL_OPTION)
                == JOptionPane.OK_OPTION) {
            if (departureAirportField.getText().equals(""))
                departureAirportField.setText("foc");
            if (arrivalAirportField.getText().equals(""))
                arrivalAirportField.setText("sha");
            if (departDateField.getText().equals(""))
                departDateField.setText("2018-11-20");
        }

        CompletableFuture cfCrip = CompletableFuture.supplyAsync(() ->
                CrawlCtripByJson.crawlCtripByJson(
                        departureAirportField.getText(),
                        arrivalAirportField.getText(),
                        departDateField.getText(),
                        returnDateField.getText().trim())
        );
        try {
            cfCrip.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
