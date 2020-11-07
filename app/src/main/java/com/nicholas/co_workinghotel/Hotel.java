package com.nicholas.co_workinghotel;

public class Hotel {
    private long hotelID, userID;
    private String hotelName, hotelDesc, hotelPrice;

    public Hotel(long hotelID, long userID, String hotelName, String hotelDesc, String hotelPrice) {
        this.hotelID = hotelID;
        this.userID = userID;
        this.hotelName = hotelName;
        this.hotelDesc = hotelDesc;
        this.hotelPrice = hotelPrice;
    }

    public String getHotelPrice() {
        return hotelPrice;
    }

    public void setHotelPrice(String hotelPrice) {
        this.hotelPrice = hotelPrice;
    }

    public long getHotelID() {
        return hotelID;
    }

    public void setHotelID(long hotelID) {
        this.hotelID = hotelID;
    }

    public long getUserID() {
        return userID;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public String getHotelDesc() {
        return hotelDesc;
    }

    public void setHotelDesc(String hotelDesc) {
        this.hotelDesc = hotelDesc;
    }
}
