package com.example.carsharingapp.model;

public class Vehicul {
    private int id;
    private String marca;
    private String model;
    private int numar_locuri;
    private int motorizare;
    private String tip_combustibil;
    private String tip_caroserie;
    private double latitudine;
    private double longitudine;

    public String getTitlu() {
        return marca + " " + model;
    }

    // Getteri și setteri
    public int getId() { return id; }
    public String getMarca() { return marca; }
    public String getModel() { return model; }
    public int getNumarLocuri() { return numar_locuri; }
    public int getMotorizare() { return motorizare; }
    public String getTipCombustibil() { return tip_combustibil; }
    public String getTipCaroserie() { return tip_caroserie; }
    public double getLatitudine() { return latitudine; }
    public double getLongitudine() { return longitudine; }

    @Override
    public String toString() {
        return "Vehicul{" +
                "id=" + id +
                ", marca='" + marca + '\'' +
                ", model='" + model + '\'' +
                ", numar_locuri=" + numar_locuri +
                ", motorizare=" + motorizare +
                ", tip_combustibil='" + tip_combustibil + '\'' +
                ", tip_caroserie='" + tip_caroserie + '\'' +
                ", latitudine=" + latitudine +
                ", longitudine=" + longitudine +
                '}';
    }
}
