import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FlightPriceCollection {
    public static void main(String[] args) {

        Scanner scan = new Scanner(System.in);

        //input
        System.out.print("Input departure airport code in 3 characters: ");
        final String departureAirport = scan.nextLine().trim().substring(0, 3);
        System.out.print("Input   arrival airport code in 3 characters: ");
        final String arrivalAirport = scan.nextLine().trim().substring(0, 3);
        System.out.print("Input departure date in           yyyy-mm-dd: ");
        final String departDate = scan.nextLine().trim().substring(0, 10);
        System.out.print("Input return date in yyyy-mm-dd (blank for one-way trip): ");
        String returnDate = scan.nextLine().trim();
        System.out.println("\n\t====== Processing, please wait... ======\n");

        //init
        System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");
        Logger.getLogger("com.gargoylesoftware").setLevel(Level.OFF);
        System.setProperty("webdriver.chrome.driver", "D:/Download/chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.setBinary("C:/Program Files (x86)/Google/Chrome Dev/Application/chrome.exe");
        options.addArguments("--headless");
        final ChromeDriver driver = new ChromeDriver(options);
//        HtmlUnitDriver driver = new HtmlUnitDriver(true);

        //process
        if (returnDate.equals(""))
            driver.get("https://flights.ctrip.com/itinerary/oneway/"
                    + departureAirport + "-" + arrivalAirport + "?date=" + departDate);
        else {
            returnDate = returnDate.substring(0, 10);
            driver.get("https://flights.ctrip.com/itinerary/roundtrip/"
                    + departureAirport + "-" + arrivalAirport + "?date=" + departDate + "%2" + returnDate);//,
        }

        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        //output
        System.out.println("\n\t====== Showing at most 20 flights sorted by price ======\n");
        System.out.println(driver.getTitle() + "\n");
        ArrayList<Flight> flights = new ArrayList<>();
        String prefix, airlineFlightNumber, model, startTime, endTime, departureAp, arrivalAp, accuracy, discountRate, price;
        long current;//todo
        for (int i = 1; i <= 20; i++) {
            current = System.currentTimeMillis();//todo
            prefix = "#base_bd > div.base_main > div.searchresult_content > div:nth-child(2) > div:nth-child(2) > div:nth-child(1) > div:nth-child(" + i + ") > div";
            if (driver.findElementsByCssSelector(prefix.substring(0, prefix.length() - 6)).size() == 0)
                prefix = "#base_bd > div.base_main > div.searchresult_content > div:nth-child(2) > div:nth-child(2) > div > div:nth-child(" + i + ") > div";
            try {
                airlineFlightNumber = driver.findElementByCssSelector(prefix + " > div.inb.logo > div.logo-item.flight_logo > div > span").getText();
            } catch (NoSuchElementException e) {
                System.out.println("Got " + (i - 1) + " flight(s) in total.");
                break;
            }
            model = driver.findElementByCssSelector(prefix + " > div.inb.logo > div:nth-child(2)").getText();
            startTime = driver.findElementByCssSelector(prefix + " > div.inb.right > div.time_box > strong").getText();
            endTime = driver.findElementByCssSelector(prefix + " > div.inb.left > div.time_box > strong").getText();
            departureAp = driver.findElementByCssSelector(prefix + " > div.inb.right > div.airport").getText();
            arrivalAp = driver.findElementByCssSelector(prefix + " > div.inb.left > div.airport").getText();
            accuracy = driver.findElementByCssSelector(prefix + " > div.inb.service > div > div > div").getText();
            if (accuracy.equals("-"))
                accuracy = "N/A";
            System.out.println("\texec " + i + " =1= " + (System.currentTimeMillis() - current) / 1000d);
            try {
                discountRate = driver.findElementByCssSelector(prefix + " > div.inb.price.child_price.lowest_price > div > div > span").getText();
                price = driver.findElementByCssSelector(prefix + "> div.inb.price.child_price.lowest_price > div > span").getText();
                System.out.println("\texec " + i + " =1.1= " + (System.currentTimeMillis() - current) / 1000d);
            } catch (NoSuchElementException e) {
                discountRate = driver.findElementByCssSelector(prefix + " > div.inb.price.child_price > div > div > span").getText();
                price = driver.findElementByCssSelector(prefix + "> div.inb.price.child_price > div > span").getText();
                System.out.println("\texec " + i + " =1.2= " + (System.currentTimeMillis() - current) / 1000d);
            }
            System.out.println("\texec " + i + " =2= " + (System.currentTimeMillis() - current) / 1000d);
            Flight flight = new Flight(airlineFlightNumber, model, startTime, endTime,
                    departureAp, arrivalAp, accuracy, discountRate, price, "Ctrip");
            flights.add(flight);
            System.out.println("\texec " + i + " =0= " + (System.currentTimeMillis() - current) / 1000d);//todo
            System.out.println(flight);
        }
    }
}
