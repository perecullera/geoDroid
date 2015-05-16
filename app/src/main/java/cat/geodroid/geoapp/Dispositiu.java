package cat.geodroid.geoapp;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Victor LLuciÃ , Ricard Moya on 31/3/15.
 */
public class Dispositiu {
    //TODO generate atributes, getters and setters
    private int id_dispositiu;
    private String flota;
    private Double longitud;
    private Double latitud;
    private String vehicle;
    private String carrega;
    private int id_empresa;
    private int id_usuari;
    private String nom;

    public Dispositiu() {

    }

    public Dispositiu (String nom, String flota, String vehicle, int empresa) {
        this.nom = nom;
        this.flota = flota;
        this.vehicle = vehicle;
        this.id_empresa = empresa;
    }

    public void setFlota(String flota) {
        this.flota = flota;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPosition(Double longitud, Double latitud) {
        this.longitud = longitud;
        this.latitud = latitud;
    }

    public void setVehicle(String vehicle) {
        this.vehicle=vehicle;
    }

    public void setId(int id) {
        this.id_dispositiu = id;
    }

    public void setCarrega(String Carrega) {
        this.carrega = carrega;
    }

    public void setEmpresa(int empresa) {
        this.id_empresa = empresa;
    }

    public void setUsuari(int usuari) {
        this.id_usuari = usuari;
    }

    public int getId() {
        return id_dispositiu;
    }

    public String getFlota() {
        return flota;
    }

    public Double getLong() {
        return longitud;
    }

    public Double getLat() {
        return latitud;
    }

    public String getVehicle() {
        return vehicle;
    }

    public String getCarrega() {
        return carrega;
    }

    public int getId_empresa() {
        return id_empresa;
    }

    public int getId_usuari() {
        return id_usuari;
    }

    public LatLng getPosition() {
        LatLng latlong = new LatLng(latitud, longitud);
        return latlong;
    }

    public String getNom() {
        return nom;
    }
}