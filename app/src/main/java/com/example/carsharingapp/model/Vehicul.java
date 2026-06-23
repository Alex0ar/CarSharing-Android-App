package com.example.carsharingapp.model;
//
//public class Vehicul {
//    private int id;
//    private String marca;
//    private String model;
//    private int numar_locuri;
//    private int motorizare;
//    private String tip_combustibil;
//    private String tip_caroserie;
//    private double latitudine;
//    private double longitudine;
//
//    public String getTitlu() {
//        return marca + " " + model;
//    }
//
//    // Getteri și setteri
//    public int getId() { return id; }
//    public String getMarca() { return marca; }
//    public String getModel() { return model; }
//    public int getNumarLocuri() { return numar_locuri; }
//    public int getMotorizare() { return motorizare; }
//    public String getTipCombustibil() { return tip_combustibil; }
//    public String getTipCaroserie() { return tip_caroserie; }
//    public double getLatitudine() { return latitudine; }
//    public double getLongitudine() { return longitudine; }
//
//    @Override
//    public String toString() {
//        return "Vehicul{" +
//                "id=" + id +
//                ", marca='" + marca + '\'' +
//                ", model='" + model + '\'' +
//                ", numar_locuri=" + numar_locuri +
//                ", motorizare=" + motorizare +
//                ", tip_combustibil='" + tip_combustibil + '\'' +
//                ", tip_caroserie='" + tip_caroserie + '\'' +
//                ", latitudine=" + latitudine +
//                ", longitudine=" + longitudine +
//                '}';
//    }
//}

import com.google.gson.annotations.SerializedName;

public class Vehicul {

    private String marca;
    private String model;

    @SerializedName("numar_locuri")
    private int numarLocuri;

    private int motorizare;

    @SerializedName("combustibil")
    private String combustibil;

    @SerializedName("caroserie")
    private String caroserie;

    private double latitudine;
    private double longitudine;

    private int autonomie;

    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public int getNumarLocuri() { return numarLocuri; }
    public void setNumarLocuri(int numarLocuri) { this.numarLocuri = numarLocuri; }

    public int getMotorizare() { return motorizare; }
    public void setMotorizare(int motorizare) { this.motorizare = motorizare; }

    public String getCombustibil() { return combustibil; }
    public void setCombustibil(String combustibil) { this.combustibil = combustibil; }

    public String getCaroserie() { return caroserie; }
    public void setCaroserie(String caroserie) { this.caroserie = caroserie; }

    public double getLatitudine() { return latitudine; }
    public void setLatitudine(double latitudine) { this.latitudine = latitudine; }

    public double getLongitudine() { return longitudine; }
    public void setLongitudine(double longitudine) { this.longitudine = longitudine; }

    public int getAutonomie() {
        return autonomie;
    }

    public void setAutonomie(int autonomie) {
        this.autonomie = autonomie;
    }

    @Override
    public String toString() {
        return "Vehicul{" +
                ", marca='" + marca + '\'' +
                ", model='" + model + '\'' +
                ", numar_locuri=" + numarLocuri +
                ", motorizare=" + motorizare +
                ", tip_combustibil='" + combustibil + '\'' +
                ", tip_caroserie='" + caroserie + '\'' +
                ", latitudine=" + latitudine +
                ", longitudine=" + longitudine +
                ", autonomie=" + autonomie +
                '}';
    }

}

