import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

class CrawlCtripBySelenium {

    static void crawlCtripBySelenium() {/*
        //init
        final long START_TIME = System.currentTimeMillis();
//        System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");
//        Logger.getLogger("com.gargoylesoftware").setLevel(Level.OFF);
        System.setProperty("webdriver.chrome.driver", "D:/Download/chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.setBinary("C:/Program Files (x86)/Google/Chrome Dev/Application/chrome.exe");
        options.setProxy(null);
        options.addArguments("--headless");
        //        options.addArguments("--silent");
        final ChromeDriver driver = new ChromeDriver(options);
//        final HtmlUnitDriver driver = new HtmlUnitDriver(true);
//        final WebClient webClient = new WebClient();
//        HtmlPage htmlPage = null;
//        webClient.getOptions().setThrowExceptionOnScriptError(false);
        System.out.println("Boot time: " + (System.currentTimeMillis() - START_TIME) / 1000d);


        //process
        String departureAirportCode = "", arrivalAirportCode = "", departDate = "", returnDate = "";
        try {
            departureAirportCode = completableFuture.get()[0];
            arrivalAirportCode = completableFuture.get()[1];
            departDate = completableFuture.get()[2];
            returnDate = completableFuture.get()[3];
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("\n\t====== Processing, please wait... ======\n");
        if (returnDate.equals(""))
            driver.get(
//            try {
//                htmlPage = webClient.getPage(
                    "https://flights.ctrip.com/itinerary/oneway/"
                            + departureAirportCode + "-" + arrivalAirportCode + "?date=" + departDate);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
        else {
            returnDate = returnDate.substring(0, 10);
            driver.get("https://flights.ctrip.com/itinerary/roundtrip/"
                    + departureAirportCode + "-" + arrivalAirportCode + "?date=" + departDate + "%2" + returnDate);//,
        }
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight)");
//        ((JavascriptExecutor) webClient).executeScript("window.scrollTo(0, document.body.scrollHeight)");
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
//        webClient.waitForBackgroundJavaScript(10 * 1000);


        //output
        System.out.println("\t====== Showing at most 100 flights sorted by price ======\n");
        System.out.println(driver.getTitle() + "\n");
        //        System.out.println(Objects.requireNonNull(htmlPage).getTitleText() + "\n");
        ArrayList<Flight> flights = new ArrayList<>();
        String prefix, suffix4Price = " > div.inb.price.child_price.lowest_price",
                airlineFlightNumber, model, startTime, endTime, departureAirport, arrivalAirport, accuracy, discountRate, price;
        int divStart = 1;
        XSSFWorkbook wb = new XSSFWorkbook();        //创建一个工作簿 即excel文件,再在该文件中创建一个sheet
        XSSFSheet sheet = wb.createSheet((departureAirportCode + "->" + arrivalAirportCode) + "@" + departDate);
        XSSFRow row = sheet.createRow(0);
        row.createCell(0).setCellValue(driver.getTitle());


        for (int i = 1; i <= 100; i++) {
            prefix = "#base_bd > div.base_main > div.searchresult_content > div:nth-child(2) > div:nth-child("
                    + divStart + ") > div:nth-child(1) > div:nth-child(" + i + ") > div";
            if (i == 1) {
                if (isElementNotPresent(driver, prefix + " > div.inb.logo")) {
                    divStart = 3 - divStart;
                    prefix = "#base_bd > div.base_main > div.searchresult_content > div:nth-child(2) > div:nth-child("
                            + divStart + ") > div:nth-child(1) > div:nth-child(" + i + ") > div";
                    if (isElementNotPresent(driver, prefix + " > div.inb.logo")) {
                        System.out.println("\n\t====== ERROR: Analyze css selector failed! ======\n");
                        break;
                    }
                }
            }
//            System.out.println("\tprefix init finished " + i + " = " + (System.currentTimeMillis() - current) / 1000d);
//            System.out.println(prefix);
            try {
                airlineFlightNumber = driver.findElementByCssSelector(prefix + " > div.inb.logo > div.logo-item.flight_logo > div > span").getText();
            } catch (NoSuchElementException e) {
                System.out.println("\n\t====== Got " + (i - 1) + " flight(s) in total ======");
                break;
            }
//            System.out.println("\tairline finished " + i + " = " + (System.currentTimeMillis() - current) / 1000d);
            model = driver.findElementByCssSelector(prefix + " > div.inb.logo > div:nth-child(2)").getText();
            startTime = driver.findElementByCssSelector(prefix + " > div.inb.right > div.time_box > strong").getText();
            endTime = driver.findElementByCssSelector(prefix + " > div.inb.left > div.time_box > strong").getText();
            departureAirport = driver.findElementByCssSelector(prefix + " > div.inb.right > div.airport").getText();
            arrivalAirport = driver.findElementByCssSelector(prefix + " > div.inb.left > div.airport").getText();
            accuracy = driver.findElementByCssSelector(prefix + " > div.inb.service > div > div > div").getText();
            if (accuracy.equals("-"))
                accuracy = "N/A";
            try {
                discountRate = driver.findElementByCssSelector(prefix + suffix4Price + " > div > div > span").getText();
                price = driver.findElementByCssSelector(prefix + suffix4Price + " >  div > span").getText();
            } catch (NoSuchElementException e) {
                suffix4Price = " > div.inb.price.child_price";
                discountRate = driver.findElementByCssSelector(prefix + suffix4Price + " > div > div > span").getText();
                price = driver.findElementByCssSelector(prefix + suffix4Price + " >  div > span").getText();
            }
            Flight flight = new Flight(airlineFlightNumber, model, startTime, endTime,
                    departureAirport, arrivalAirport, accuracy, discountRate, price, "Ctrip");
            flights.add(flight);
//            System.out.println("\tfinal " + i + " = " + (System.currentTimeMillis() - current) / 1000d);
            System.out.println(flight);
            row = sheet.createRow(i);
            row.createCell(0).setCellValue(airlineFlightNumber);
            row.createCell(1).setCellValue(model);
            row.createCell(2).setCellValue(startTime);
            row.createCell(3).setCellValue(endTime);
            row.createCell(4).setCellValue(departureAirport);
            row.createCell(5).setCellValue(arrivalAirport);
            row.createCell(6).setCellValue(accuracy);
            row.createCell(7).setCellValue(discountRate);
            row.createCell(8).setCellValue(price);
            row.createCell(9).setCellValue("Ctrip.com");
            for (int j = 0; j <= 9; j++) {
                sheet.autoSizeColumn(j);
            }
        }
        try {
            FileOutputStream out = new FileOutputStream("D:/FlightPrices.xlsx");
            wb.write(out);
            out.close();
        } catch (
                IOException e) {
            e.printStackTrace();
        }
        System.out.println("\nTotal exec time: " + (System.currentTimeMillis() - START_TIME) / 1000d);
        driver.quit();*/
    }
    //    driver.findElement(By.linkText("查看365天低价")).click();

    private static boolean isElementNotPresent(WebDriver driver, String string) {
        try {
            ((RemoteWebDriver) driver).findElementByCssSelector(string);
            return false;
        } catch (NoSuchElementException e) {
            return true;
        }
    }
}
