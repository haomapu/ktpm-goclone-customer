package com.example.ktpm_goclone_customer;

import java.io.Serializable;

public class ListHistoryLocation implements Serializable {

    String title, descript, dateTime, vehicleType;

    public ListHistoryLocation(String title, String descript, String dateTime, String vehicleType) {
        this.title = title;
        this.descript = descript;
        this.dateTime = dateTime;
        this.vehicleType = vehicleType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescript() {
        return descript;
    }

    public void setDescript(String descript) {
        this.descript = descript;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }
}
