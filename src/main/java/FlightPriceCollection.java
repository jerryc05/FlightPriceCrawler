import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FlightPriceCollection {
    public static void main(String[] args) {

        Scanner scan = new Scanner(System.in);

        //input
        System.out.print("Input departure airport code in 3 characters: ");
        final String departureAirport = scan.nextLine().trim().substring(0,3);
        System.out.print("Input   arrival airport code in 3 characters: ");
        final String arrivalAirport = scan.nextLine().trim().substring(0,3);
        System.out.print("Input departure date in           yyyy-mm-dd: ");
        final String departDate =scan.nextLine().trim().substring(0,10);
        System.out.print("Input return date in yyyy-mm-dd (blank for one-way trip): ");
        String returnDate = scan.nextLine().trim();
        System.out.println("====== Processing, please wait... ======");

        //init
        System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");
        Logger.getLogger("com.gargoylesoftware").setLevel(Level.OFF);
        System.setProperty("webdriver.chrome.driver", "D:/Download/chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.setBinary("C:/Program Files (x86)/Google/Chrome Dev/Application/chrome.exe");
        options.addArguments("--headless");
        ChromeDriver driver = new ChromeDriver(options);
//        HtmlUnitDriver driver = new HtmlUnitDriver(true);
        if (returnDate.equals(""))
            driver.get("https://flights.ctrip.com/itinerary/oneway/"
                    + departureAirport + "-" + arrivalAirport + "?date=" + departDate);
        else {
            returnDate = returnDate.substring(0, 10);
            driver.get("https://flights.ctrip.com/itinerary/roundtrip/"
                    + departureAirport + "-" + arrivalAirport + "?date=" + departDate + "%2" + returnDate);//,
        }
        System.out.println("Title: " + driver.getTitle());

        //process
        WebDriverWait wait = new WebDriverWait(driver, 10);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
//        System.out.println("start");
//        try {
//            Thread.sleep(20000);
//        } catch (InterruptedException e) {
//            e.getMessage();
//        }
//        System.out.println("end");
//        int indexCount = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
//                By.cssSelector("#base_bd > div.base_main > div.searchresult_content > div:nth-child(2) > div:nth-child(2) > div"))).size();
//        System.out.println(driver.findElementsByCssSelector("#base_bd > div.base_main > div.searchresult_content > div:nth-child(2) > div:nth-child(2) > div"));
//        System.out.println(indexCount);
//        String[] selectorArray = new String[indexCount];
//        for (int i = 1; i <= indexCount; i++) {
//            selectorArray[i - 1] =
//                    "#base_bd > div.base_main > div.searchresult_content > div:nth-child(2) > div:nth-child(2) > div > div:nth-child(" + i
//                            + ") > div > div.inb.price.child_price > div > span";
//            System.out.println(selectorArray[i - 1]);
//        }
//        for (String selector : selectorArray) {
//            System.out.println(wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(selector))).getText());
//    }
        for (int i = 1; i <= 10; i++) {
            try {
                System.out.println(wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(
                        "#base_bd > div.base_main > div.searchresult_content > div:nth-child(2) > div:nth-child(2) > div > div:nth-child(" + i
                                + ") > div > div.inb.price.child_price > div > span"))).getText());
            } catch (NoSuchElementException e) {
                e.printStackTrace();
                break;
            }
        }
    }
}
