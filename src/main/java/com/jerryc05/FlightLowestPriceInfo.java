package com.jerryc05;

class FlightLowestPriceInfo implements Comparable<FlightLowestPriceInfo> {

    String departureDate;//yyyy-mm-dd
    String returnDate;
    int price;
    boolean isRoundTrip = false;

    private FlightLowestPriceInfo() {
    }

    FlightLowestPriceInfo(String departureDate, int price) {
        this.departureDate = departureDate;
        this.price = price;
    }

    FlightLowestPriceInfo(String departureDate, String returnDate, int price) {
        this(departureDate, price);
        this.returnDate = returnDate;
        this.isRoundTrip = true;
    }

    @Override
    public int compareTo(FlightLowestPriceInfo o2) {
        if (returnDate == null)
            if (price != o2.price)
                return price - o2.price;
            else if (!departureDate.substring(0, 4).equals(o2.departureDate.substring(0, 4)))
                return Integer.parseInt(departureDate.substring(0, 4))
                        - Integer.parseInt(o2.departureDate.substring(0, 4));
            else if (!departureDate.substring(5, 7).equals(o2.departureDate.substring(5, 7)))
                return Integer.parseInt(departureDate.substring(5, 7))
                        - Integer.parseInt(o2.departureDate.substring(5, 7));
            else
                return Integer.parseInt(departureDate.substring(8))
                        - Integer.parseInt(o2.departureDate.substring(8));
        else
            return 0;//todo
    }
}
