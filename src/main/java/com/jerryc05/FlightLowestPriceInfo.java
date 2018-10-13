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
    public int compareTo(FlightLowestPriceInfo o2) {
        if (departureDate == null)
            if (price != o2.price)
                return price - o2.price;
            else if (!arrivalDate.substring(0, 4).equals(o2.arrivalDate.substring(0, 4)))
                return Integer.parseInt(arrivalDate.substring(0, 4))
                        - Integer.parseInt(o2.arrivalDate.substring(0, 4));
            else if (!arrivalDate.substring(5, 7).equals(o2.arrivalDate.substring(5, 7)))
                return Integer.parseInt(arrivalDate.substring(5, 7))
                        - Integer.parseInt(o2.arrivalDate.substring(5, 7));
            else
                return Integer.parseInt(arrivalDate.substring(8))
                        - Integer.parseInt(o2.arrivalDate.substring(8));
        else
            return 0;//todo
    }
}
