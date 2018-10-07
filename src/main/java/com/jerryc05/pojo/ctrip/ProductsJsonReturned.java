package com.jerryc05.pojo.ctrip;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

public class ProductsJsonReturned {

    @JSONField(name = "data")
    private Data data;

    public class Data {

        @JSONField(name = "recommendData")
        private RecommendData recommendData;

        @JSONField(name = "routeList")
        private List<RouteListItem> routeList;

        public void setRecommendData(RecommendData recommendData) {
            this.recommendData = recommendData;
        }

        public void setRouteList(List<RouteListItem> routeList) {
            this.routeList = routeList;
        }

        public RecommendData getRecommendData() {
            return recommendData;
        }

        public List<RouteListItem> getRouteList() {
            return routeList;
        }

        public class RecommendData {

            @JSONField(name = "redirectSingleProduct")
            private RedirectSingleProduct redirectSingleProduct;

            public RedirectSingleProduct getRedirectSingleProduct() {
                return redirectSingleProduct;
            }

            public void setRedirectSingleProduct(RedirectSingleProduct redirectSingleProduct) {
                this.redirectSingleProduct = redirectSingleProduct;
            }

            public class RedirectSingleProduct {

                @JSONField(name = "flights")
                private List<FlightsItem> flights;

                public List<FlightsItem> getFlights() {
                    return flights;
                }

                public void setFlights(List<FlightsItem> flights) {
                    this.flights = flights;
                }

                public class FlightsItem {

                    @JSONField(name = "departureTime")
                    private String departureTime;

                    @JSONField(name = "transportNo")
                    private String transportNo;

                    @JSONField(name = "departureCityName")
                    private String departureCityName;

                    @JSONField(name = "arrivalTime")
                    private String arrivalTime;

                    @JSONField(name = "price")
                    private int price;

                    @JSONField(name = "arrivalCityName")
                    private String arrivalCityName;

                    public String getDepartureTime() {
                        return departureTime;
                    }

                    public void setDepartureTime(String departureTime) {
                        this.departureTime = departureTime;
                    }

                    public String getTransportNo() {
                        return transportNo;
                    }

                    public void setTransportNo(String transportNo) {
                        this.transportNo = transportNo;
                    }

                    public String getDepartureCityName() {
                        return departureCityName;
                    }

                    public void setDepartureCityName(String departureCityName) {
                        this.departureCityName = departureCityName;
                    }

                    public String getArrivalTime() {
                        return arrivalTime;
                    }

                    public void setArrivalTime(String arrivalTime) {
                        this.arrivalTime = arrivalTime;
                    }

                    public int getPrice() {
                        return price;
                    }

                    public void setPrice(int price) {
                        this.price = price;
                    }

                    public String getArrivalCityName() {
                        return arrivalCityName;
                    }

                    public void setArrivalCityName(String arrivalCityName) {
                        this.arrivalCityName = arrivalCityName;
                    }
                }
            }
        }

        public class RouteListItem {

            @JSONField(name = "legs")
            private List<LegsItem> legs;

            public List<LegsItem> getLegs() {
                return legs;
            }

            public void setLegs(List<LegsItem> legs) {
                this.legs = legs;
            }

            public class LegsItem {

                @JSONField(name = "flight")
                private Flight flight;

                @JSONField(name = "characteristic")
                private Characteristic characteristic;

                public Flight getFlight() {
                    return flight;
                }

                public void setFlight(Flight flight) {
                    this.flight = flight;
                }

                public Characteristic getCharacteristic() {
                    return characteristic;
                }

                public void setCharacteristic(Characteristic characteristic) {
                    this.characteristic = characteristic;
                }

                public class Flight {

                    @JSONField(name = "arrivalAirportInfo")
                    private ArrivalAirportInfo arrivalAirportInfo;

                    @JSONField(name = "mealType")
                    private String mealType;

                    @JSONField(name = "flightNumber")
                    private String flightNumber;

                    @JSONField(name = "arrivalDate")
                    private String arrivalDate;

                    @JSONField(name = "craftTypeCode")
                    private String craftTypeCode;

                    @JSONField(name = "departureAirportInfo")
                    private DepartureAirportInfo departureAirportInfo;

                    @JSONField(name = "departureDate")
                    private String departureDate;

                    @JSONField(name = "craftTypeKindDisplayName")
                    private String craftTypeKindDisplayName;

                    @JSONField(name = "airlineName")
                    private String airlineName;

                    @JSONField(name = "craftTypeName")
                    private String craftTypeName;

                    public ArrivalAirportInfo getArrivalAirportInfo() {
                        return arrivalAirportInfo;
                    }

                    public void setArrivalAirportInfo(ArrivalAirportInfo arrivalAirportInfo) {
                        this.arrivalAirportInfo = arrivalAirportInfo;
                    }

                    public String getMealType() {
                        return mealType;
                    }

                    public void setMealType(String mealType) {
                        this.mealType = mealType;
                    }

                    public String getFlightNumber() {
                        return flightNumber;
                    }

                    public void setFlightNumber(String flightNumber) {
                        this.flightNumber = flightNumber;
                    }

                    public String getArrivalDate() {
                        return arrivalDate;
                    }

                    public void setArrivalDate(String arrivalDate) {
                        this.arrivalDate = arrivalDate;
                    }

                    public String getCraftTypeCode() {
                        return craftTypeCode;
                    }

                    public void setCraftTypeCode(String craftTypeCode) {
                        this.craftTypeCode = craftTypeCode;
                    }

                    public DepartureAirportInfo getDepartureAirportInfo() {
                        return departureAirportInfo;
                    }

                    public void setDepartureAirportInfo(DepartureAirportInfo departureAirportInfo) {
                        this.departureAirportInfo = departureAirportInfo;
                    }

                    public String getDepartureDate() {
                        return departureDate;
                    }

                    public void setDepartureDate(String departureDate) {
                        this.departureDate = departureDate;
                    }

                    public String getCraftTypeKindDisplayName() {
                        return craftTypeKindDisplayName;
                    }

                    public void setCraftTypeKindDisplayName(String craftTypeKindDisplayName) {
                        this.craftTypeKindDisplayName = craftTypeKindDisplayName;
                    }

                    public String getAirlineName() {
                        return airlineName;
                    }

                    public void setAirlineName(String airlineName) {
                        this.airlineName = airlineName;
                    }

                    public String getCraftTypeName() {
                        return craftTypeName;
                    }

                    public void setCraftTypeName(String craftTypeName) {
                        this.craftTypeName = craftTypeName;
                    }

                    public class DepartureAirportInfo {

                        @JSONField(name = "airportName")
                        private String airportName;

                        @JSONField(name = "terminal")
                        private Terminal terminal;

                        public String getAirportName() {
                            return airportName;
                        }

                        public void setAirportName(String airportName) {
                            this.airportName = airportName;
                        }

                        public Terminal getTerminal() {
                            return terminal;
                        }

                        public void setTerminal(Terminal terminal) {
                            this.terminal = terminal;
                        }

                        public class Terminal {

                            @JSONField(name = "name")
                            private String name;

                            public String getName() {
                                return name;
                            }

                            public void setName(String name) {
                                this.name = name;
                            }
                        }
                    }

                    public class ArrivalAirportInfo {

                        @JSONField(name = "airportName")
                        private String airportName;

                        @JSONField(name = "terminal")
                        private Terminal terminal;

                        public String getAirportName() {
                            return airportName;
                        }

                        public void setAirportName(String airportName) {
                            this.airportName = airportName;
                        }

                        public Terminal getTerminal() {
                            return terminal;
                        }

                        public void setTerminal(Terminal terminal) {
                            this.terminal = terminal;
                        }

                        public class Terminal {

                            @JSONField(name = "name")
                            private String name;

                            public String getName() {
                                return name;
                            }

                            public void setName(String name) {
                                this.name = name;
                            }
                        }
                    }
                }

                public class Characteristic {

                    @JSONField(name = "lowestPrice")
                    private int lowestPrice;

                    public int getLowestPrice() {
                        return lowestPrice;
                    }

                    public void setLowestPrice(int lowestPrice) {
                        this.lowestPrice = lowestPrice;
                    }
                }
            }
        }
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
}