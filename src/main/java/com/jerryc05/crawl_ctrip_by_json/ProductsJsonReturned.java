package com.jerryc05.crawl_ctrip_by_json;

import com.alibaba.fastjson.annotation.JSONField;

@SuppressWarnings("unused")
class ProductsJsonReturned {

    @JSONField(name = "data")
    private Data data;

    class Data {

        @JSONField(name = "recommendData")
        private RecommendData recommendData;

        @JSONField(name = "highFrequencyConfig")
        private Object highFrequencyConfig;

        @JSONField(name = "roundNear")
        private boolean roundNear;

        @JSONField(name = "error")
        private Object error;

        @JSONField(name = "transitCities")
        private Object transitCities;

        @JSONField(name = "transitPoints")
        private Object transitPoints;

        @JSONField(name = "roundNearsTransports")
        private Object roundNearsTransports;

        @JSONField(name = "routeList")
        private RouteListItem[] routeList;

        public RecommendData getRecommendData() {
            return recommendData;
        }

        public Object getHighFrequencyConfig() {
            return highFrequencyConfig;
        }

        public boolean isRoundNear() {
            return roundNear;
        }

        public Object getError() {
            return error;
        }

        public Object getTransitCities() {
            return transitCities;
        }

        public Object getTransitPoints() {
            return transitPoints;
        }

        public Object getRoundNearsTransports() {
            return roundNearsTransports;
        }

        public RouteListItem[] getRouteList() {
            return routeList;
        }

        class RecommendData {

            @JSONField(name = "redirectSingleProduct")
            private RedirectSingleProduct redirectSingleProduct;

            @JSONField(name = "redirectMRoute")
            private Object redirectMRoute;

            @JSONField(name = "singleGroupProduct")
            private Object singleGroupProduct;

            @JSONField(name = "redirectGroupProduct")
            private Object redirectGroupProduct;

            public RedirectSingleProduct getRedirectSingleProduct() {
                return redirectSingleProduct;
            }

            public Object getRedirectMRoute() {
                return redirectMRoute;
            }

            public Object getSingleGroupProduct() {
                return singleGroupProduct;
            }

            public Object getRedirectGroupProduct() {
                return redirectGroupProduct;
            }

            class RedirectSingleProduct {

                @JSONField(name = "buses")
                private Object buses;

                @JSONField(name = "flights")
                private FlightsItem[] flights;

                @JSONField(name = "trains")
                private Object trains;

                public FlightsItem[] getFlights() {
                    return flights;
                }

                public Object getTrains() {
                    return trains;
                }

                class FlightsItem {

                    @JSONField(name = "departureTime")
                    private String departureTime;

                    @JSONField(name = "cabin")
                    private String cabin;

                    @JSONField(name = "weight")
                    private int weight;

                    @JSONField(name = "departureCityEncode")
                    private Object departureCityEncode;

                    @JSONField(name = "transportNo")
                    private String transportNo;

                    @JSONField(name = "departureCityTlc")
                    private String departureCityTlc;

                    @JSONField(name = "deptStationName")
                    private Object deptStationName;

                    @JSONField(name = "segmentNo")
                    private int segmentNo;

                    @JSONField(name = "departureCityName")
                    private String departureCityName;

                    @JSONField(name = "arrivalCityEncode")
                    private Object arrivalCityEncode;

                    @JSONField(name = "priceClass")
                    private String priceClass;

                    @JSONField(name = "departureCityId")
                    private int departureCityId;

                    @JSONField(name = "arrivalCityId")
                    private int arrivalCityId;

                    @JSONField(name = "arrivalTime")
                    private String arrivalTime;

                    @JSONField(name = "price")
                    private int price;

                    @JSONField(name = "arrivalCityName")
                    private String arrivalCityName;

                    @JSONField(name = "arrivalStationName")
                    private Object arrivalStationName;

                    @JSONField(name = "arrivalCityTlc")
                    private String arrivalCityTlc;

                    @JSONField(name = "routeNo")
                    private int routeNo;

                    @JSONField(name = "productType")
                    private String productType;

                    public String getDepartureTime() {
                        return departureTime;
                    }

                    public String getCabin() {
                        return cabin;
                    }

                    public int getWeight() {
                        return weight;
                    }

                    public Object getDepartureCityEncode() {
                        return departureCityEncode;
                    }

                    public String getTransportNo() {
                        return transportNo;
                    }

                    public String getDepartureCityTlc() {
                        return departureCityTlc;
                    }

                    public Object getDeptStationName() {
                        return deptStationName;
                    }

                    public int getSegmentNo() {
                        return segmentNo;
                    }

                    public String getDepartureCityName() {
                        return departureCityName;
                    }

                    public Object getArrivalCityEncode() {
                        return arrivalCityEncode;
                    }

                    public String getPriceClass() {
                        return priceClass;
                    }

                    public int getDepartureCityId() {
                        return departureCityId;
                    }

                    public int getArrivalCityId() {
                        return arrivalCityId;
                    }

                    public String getArrivalTime() {
                        return arrivalTime;
                    }

                    public int getPrice() {
                        return price;
                    }

                    public String getArrivalCityName() {
                        return arrivalCityName;
                    }

                    public Object getArrivalStationName() {
                        return arrivalStationName;
                    }

                    public String getArrivalCityTlc() {
                        return arrivalCityTlc;
                    }

                    public int getRouteNo() {
                        return routeNo;
                    }

                    public String getProductType() {
                        return productType;
                    }

                    public Object getBuses() {
                        return buses;
                    }
                }
            }
        }

        class RouteListItem {

            @JSONField(name = "legs")
            private LegsItem[] legs;

            public LegsItem[] getLegs() {
                return legs;
            }

            class LegsItem {

                @JSONField(name = "flight")
                private Flight flight;

                @JSONField(name = "characteristic")
                private Characteristic characteristic;

                public Flight getFlight() {
                    return flight;
                }

                public Characteristic getCharacteristic() {
                    return characteristic;
                }

                class Flight {

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

                    @JSONField(name = "airlineCode")
                    private String airlineCode;

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

                    public String getMealType() {
                        return mealType;
                    }

                    public String getFlightNumber() {
                        return flightNumber;
                    }

                    public String getArrivalDate() {
                        return arrivalDate;
                    }

                    public String getCraftTypeCode() {
                        return craftTypeCode;
                    }

                    public DepartureAirportInfo getDepartureAirportInfo() {
                        return departureAirportInfo;
                    }

                    public String getAirlineCode() {
                        return airlineCode;
                    }

                    public String getDepartureDate() {
                        return departureDate;
                    }

                    public String getCraftTypeKindDisplayName() {
                        return craftTypeKindDisplayName;
                    }

                    public String getAirlineName() {
                        return airlineName;
                    }

                    public String getCraftTypeName() {
                        return craftTypeName;
                    }

                    class DepartureAirportInfo {

                        @JSONField(name = "airportName")
                        private String airportName;

                        @JSONField(name = "terminal")
                        private Terminal terminal;

                        public String getAirportName() {
                            return airportName;
                        }

                        public Terminal getTerminal() {
                            return terminal;
                        }
                    }

                    class ArrivalAirportInfo {

                        @JSONField(name = "airportName")
                        private String airportName;

                        @JSONField(name = "terminal")
                        private Terminal terminal;

                        public String getAirportName() {
                            return airportName;
                        }

                        public Terminal getTerminal() {
                            return terminal;
                        }
                    }

                    class Terminal {

                        @JSONField(name = "name")
                        private String name;

                        @JSONField(name = "shortName")
                        private String shortName;

                        public String getName() {
                            return name;
                        }

                        public String getShortName() {
                            return shortName;
                        }
                    }
                }

                class Characteristic {

                    @JSONField(name = "lowestPrice")
                    private int lowestPrice;

                    public int getLowestPrice() {
                        return lowestPrice;
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