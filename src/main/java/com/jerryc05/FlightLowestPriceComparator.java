package com.jerryc05;

import java.util.Comparator;

class FlightLowestPriceComparator implements Comparator<FlightLowestPriceInfo> {

    @Override
    public int compare(FlightLowestPriceInfo o1, FlightLowestPriceInfo o2) {
        if (o1.departureDate == null)
            if (o1.price != o2.price)
                return o1.price - o2.price;
            else if (!o1.arrivalDate.substring(0, 4).equals(o2.arrivalDate.substring(0, 4)))
                return Integer.parseInt(o1.arrivalDate.substring(0, 4))
                        - Integer.parseInt(o2.arrivalDate.substring(0, 4));
            else if (!o1.arrivalDate.substring(5, 7).equals(o2.arrivalDate.substring(5, 7)))
                return Integer.parseInt(o1.arrivalDate.substring(5, 7))
                        - Integer.parseInt(o2.arrivalDate.substring(5, 7));
            else
                return Integer.parseInt(o1.arrivalDate.substring(8))
                        - Integer.parseInt(o2.arrivalDate.substring(8));
        else
            return 0;//todo

    }
}
