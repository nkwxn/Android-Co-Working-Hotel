package com.nicholas.co_workinghotel;

public class History {
    private String hotelName, hotelHourlyRate, inDate, outDate, tranDate;

    public History(String hotelName, String hotelHourlyRate, String inDate, String outDate, String tranDate) {
        this.hotelName = hotelName;
        this.hotelHourlyRate = hotelHourlyRate;
        this.inDate = inDate;
        this.outDate = outDate;
        this.tranDate = tranDate;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public String getHotelHourlyRate() {
        return hotelHourlyRate;
    }

    public void setHotelHourlyRate(String hotelHourlyRate) {
        this.hotelHourlyRate = hotelHourlyRate;
    }

    public String getInDate() {
        return inDate;
    }

    public void setInDate(String inDate) {
        this.inDate = inDate;
    }

    public String getOutDate() {
        return outDate;
    }

    public void setOutDate(String outDate) {
        this.outDate = outDate;
    }

    public String getTranDate() {
        return tranDate;
    }

    public void setTranDate(String tranDate) {
        this.tranDate = tranDate;
    }
}
