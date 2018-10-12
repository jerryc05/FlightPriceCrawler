package com.jerryc05;

class FlightLowestPriceInfo implements Comparable<FlightLowestPriceInfo> {

    String departureDate;//yyyy-mm-dd
    String arrivalDate;
    String departureDate2;
    String arrivalDate2;
    int price;
    boolean isRoundTrip = false;

    private FlightLowestPriceInfo() {
    }

    FlightLowestPriceInfo(String departureDate, String arrivalDate, int price) {
        this.departureDate = departureDate;
        this.arrivalDate = arrivalDate;
        this.price = price;
    }

    FlightLowestPriceInfo(String departureDate, String arrivalDate,
                          String departureDate2, String arrivalDate2,
                          int price) {
        this(departureDate, arrivalDate, price);
        this.departureDate2 = departureDate2;
        this.arrivalDate2 = arrivalDate2;
        this.isRoundTrip = true;
    }

    @Override
    public int compareTo(FlightLowestPriceInfo o) {
        if (price != o.price)
            return price - o.price;
        else if (!departureDate.substring(0, 4).equals(o.departureDate.substring(0, 4)))
            return Integer.parseInt(departureDate.substring(0, 4))
                    - Integer.parseInt(o.departureDate.substring(0, 4));
        else if (!departureDate.substring(5, 7).equals(o.departureDate.substring(5, 7)))
            return Integer.parseInt(departureDate.substring(5, 7))
                    - Integer.parseInt(o.departureDate.substring(5, 7));
        else
            return Integer.parseInt(departureDate.substring(8, 10))
                    - Integer.parseInt(o.departureDate.substring(8, 10));
    }
}
