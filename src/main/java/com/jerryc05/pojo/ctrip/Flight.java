package com.jerryc05.pojo.ctrip;

import com.alibaba.fastjson.annotation.JSONField;

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

}
