package com.example.ktpm_goclone_customer;

public class ListLocation {
    String title, descript;

    public ListLocation(String title, String descript) {
        this.title = title;
        this.descript = descript;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescript(String descript) {
        this.descript = descript;
    }

    public String getTitle() {
        return title;
    }

    public String getDescript() {
        return descript;
    }
}
