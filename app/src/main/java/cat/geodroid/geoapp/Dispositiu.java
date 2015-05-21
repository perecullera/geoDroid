package cat.geodroid.geoapp;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

/**
 * Created by Victor LLuciÃ , Ricard Moya on 31/3/15.
 */
public class Dispositiu implements Serializable {
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

    /**
     * Constructor buit de Dispositiu
     */
    public Dispositiu() {}

    /**
     * Constructor amb elements de Dispositiu
     * @param nom
     * @param flota
     * @param vehicle
     * @param empresa
     */
    public Dispositiu (String nom, String flota, String vehicle, int empresa) {
        this.nom = nom;
        this.flota = flota;
        this.vehicle = vehicle;
        this.id_empresa = empresa;
    }

    /**
     * Setter flota Dispositiu
     * @param flota
     */
    public void setFlota(String flota) {
        this.flota = flota;
    }

    /**
     * Setter nom Dispositiu
     * @param nom
     */
    @SuppressWarnings("serial") //with this annotation we are going to hide compiler warning
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * Setter Posicio Dispositiu
     * @param longitud
     * @param latitud
     */
    public void setPosition(Double latitud, Double longitud) {
        this.longitud = longitud;
        this.latitud = latitud;
    }

    /**
     * Setter Vehicle Dispositiu
     * @param vehicle
     */
    public void setVehicle(String vehicle) {
        this.vehicle = vehicle;
    }

    /**
     * Setter id Dispositiu
     * @param id
     */
    public void setId(int id) {
        this.id_dispositiu = id;
    }

    /**
     * Setter carrega Dispositiu
     * @param Carrega
     */
    public void setCarrega(String Carrega) {
        this.carrega = carrega;
    }

    /**
     * Setter empresa Dispositiu
     * @param empresa
     */
    public void setEmpresa(int empresa) {
        this.id_empresa = empresa;
    }

    /**
     * Setter usuari Dispositiu
     * @param usuari
     */
    public void setUsuari(int usuari) {
        this.id_usuari = usuari;
    }

    /**
     * Getter id Dispositiu
     * @return int id_dispositiu
     */
    public int getId() {
        return id_dispositiu;
    }

    /**
     * Getter flota Dispositiu
     * @return String flota
     */
    public String getFlota() {
        return flota;
    }

    /**
     * Getter longitud Dispositiu
     * @return Double longitud
     */
    public Double getLong() {
        return longitud;
    }

    /**
     * Getter latitud Dispositiu
     * @return Double latitud
     */
    public Double getLat() {
        return latitud;
    }

    /**
     * Getter vehicle Dispositiu
     * @return String vehicle
     */
    public String getVehicle() {
        return vehicle;
    }

    /**
     * Getter carrega Dispositiu
     * @return String carrega
     */
    public String getCarrega() {
        return carrega;
    }

    /**
     * Getter id_empresa Dispositiu
     * @return int id_empresa
     */
    public int getId_empresa() {
        return id_empresa;
    }

    /**
     * Getter id_usuari Dispositiu
     * @return int id_usuari
     */
    public int getId_usuari() {
        return id_usuari;
    }

    /**
     * Getter posicio Dispositiu
     * @return LatLong latLong
     */
    public LatLng getPosition() {
        LatLng latlong = new LatLng(latitud, longitud);
        return latlong;
    }

    /**
     * Getter nom Dispositiu
     * @return String nom
     */
    public String getNom() {
        return nom;
    }
}