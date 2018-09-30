import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.ArrayList;
import java.util.List;
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
        ChromeDriver driver = new ChromeDriver(options);
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
        System.out.println("\n\t====== Showing at most 15 flights sorted by price ======\n");
        System.out.println(driver.getTitle() + "\n");
        ArrayList<Flight> flights = new ArrayList<>();
        String prefix, airlineFlightNumber, model, startTime, endTime, departureAp, arrivalAp, accuracy, discountRate, price;
        for (int i = 1; i <= 15; i++) {
            prefix = "#base_bd > div.base_main > div.searchresult_content > div:nth-child(2) > div:nth-child(2) > div:nth-child(1) > div:nth-child(" + i + ") > div";
            if (driver.findElementsByCssSelector(prefix).size() == 0)
                prefix = "#base_bd > div.base_main > div.searchresult_content > div:nth-child(2) > div:nth-child(2) > div > div:nth-child(" + i + ") > div";
            try {
                airlineFlightNumber = driver.findElementByCssSelector(prefix + " > div.inb.logo > div.logo-item.flight_logo > div > span").getText();
                model = driver.findElementByCssSelector(prefix + " > div.inb.logo > div:nth-child(2)").getText();
                startTime = driver.findElementByCssSelector(prefix + " > div.inb.right > div.time_box > strong").getText();
                endTime = driver.findElementByCssSelector(prefix + " > div.inb.left > div.time_box > strong").getText();
                departureAp = driver.findElementByCssSelector(prefix + " > div.inb.right > div.airport").getText();
                arrivalAp = driver.findElementByCssSelector(prefix + " > div.inb.left > div.airport").getText();
                accuracy = driver.findElementByCssSelector(prefix + " > div.inb.service > div > div > div").getText();
                List<WebElement> list = driver.findElementsByCssSelector(prefix + " > div.inb.price.child_price.lowest_price > div > div > span");
                if (list.size() != 0) {
                    discountRate = list.get(0).getText();
                    price = driver.findElementByCssSelector(prefix + "> div.inb.price.child_price.lowest_price > div > span").getText();
                } else {
                    discountRate = driver.findElementByCssSelector(prefix + " > div.inb.price.child_price > div > div > span").getText();
                    price = driver.findElementByCssSelector(prefix + "> div.inb.price.child_price > div > span").getText();
                }
                Flight flight = new Flight(airlineFlightNumber, model, startTime, endTime,
                        departureAp, arrivalAp, accuracy, discountRate, price,"Ctrip");
                flights.add(flight);
                System.out.println(flight);
            } catch (NoSuchElementException e) {
                System.out.println("Got " + (i - 1) + " flight(s) in total.");
                break;
            }
        }
    }
}
