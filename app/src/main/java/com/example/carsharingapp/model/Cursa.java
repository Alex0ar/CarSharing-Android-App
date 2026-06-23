package com.example.carsharingapp.model;

public class Cursa {
    private String userEmail;
    private String vehicul;
    private double tarifInitial;
    private double pretTotal;
    private long durataInSecunde;

    public Cursa(String userEmail, String vehicul, double tarifInitial, double pretTotal, long durataInSecunde) {
        this.userEmail = userEmail;
        this.vehicul = vehicul;
        this.tarifInitial = tarifInitial;
        this.pretTotal = pretTotal;
        this.durataInSecunde = durataInSecunde;
    }

    public long getDurataInSecunde() {
        return durataInSecunde;
    }

    public void setDurataInSecunde(long durataInSecunde) {
        this.durataInSecunde = durataInSecunde;
    }

    public double getPretTotal() {
        return pretTotal;
    }

    public void setPretTotal(double pretTotal) {
        this.pretTotal = pretTotal;
    }

    public double getTarifInitial() {
        return tarifInitial;
    }

    public void setTarifInitial(double tarifInitial) {
        this.tarifInitial = tarifInitial;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getVehicul() {
        return vehicul;
    }

    public void setVehicul(String vehicul) {
        this.vehicul = vehicul;
    }
}

