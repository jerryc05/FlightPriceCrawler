package com.jerryc05;

class FlightLowestPriceInfo {

    String departureDate;//yyyy-mm-dd
    String arrivalDate;
    String departureDate2;
    String arrivalDate2;
    int price;
    boolean isRoundTrip = false;

    private FlightLowestPriceInfo() {
    }

    FlightLowestPriceInfo(String arrivalDate, int price) {
        this.arrivalDate = arrivalDate;
        this.price = price;
    }

    FlightLowestPriceInfo(String departureDate, String arrivalDate,
                          String departureDate2, String arrivalDate2,
                          int price) {
        this(arrivalDate, price);
        this.departureDate = departureDate;
        this.departureDate2 = departureDate2;
        this.arrivalDate2 = arrivalDate2;
        this.isRoundTrip = true;
    }

    @Override
    public String toString() {
        return departureDate + "\t" + price;
    }
}
