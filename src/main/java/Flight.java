class Flight implements Comparable<Flight> {
    private final String airlineFlightNumber, model, startTime, endTime, departureAirport, arrivalAirport, accuracy, discountRate, price, proxy;

    Flight(String airlineFlightNumber, String model, String startTime, String endTime, String departureAirport,
           String arrivalAirport, String accuracy, String discountRate, String price, String proxy) {
        this.airlineFlightNumber = airlineFlightNumber;
        this.model = model;
        this.startTime = startTime;
        this.endTime = endTime;
        this.departureAirport = departureAirport;
        this.arrivalAirport = arrivalAirport;
        this.accuracy = accuracy;
        this.discountRate = discountRate;
        this.price = price;
        this.proxy = proxy;
    }

    @Override
    public String toString() {
        String space = "\t\t";
        if (airlineFlightNumber.contains("共享"))
            space = "\t";
        return airlineFlightNumber + space + startTime + "->" + endTime
                + "\t" + departureAirport + " -> " + arrivalAirport + "\t\tAccu: " + accuracy + "\t"
                + price + "\t" + discountRate + "\t [" + proxy + ".com]\t" + model;
    }

    @Override
    public int compareTo(Flight flight) {
        return Integer.parseInt(price.substring(1)) - Integer.parseInt(flight.price.substring(1));
    }
}
