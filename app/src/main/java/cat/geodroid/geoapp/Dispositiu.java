package cat.geodroid.geoapp;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Victor LLucià, Ricard Moya on 31/3/15.
 */
public class Dispositiu {
    //TODO generate atributes, getters and setters
    private String id_dispositiu;
    private String id_flota;
    private Double longitud;
    private Double latitud;
    private String vehicle;
    private String carrega;
    private String id_empresa;
    private String id_usuari;
    private String nom;


    public Dispositiu(){

    }

    public Dispositiu(String id, String nom,String flota, String vehicle){
        this.id_dispositiu = id;
        this.id_flota = flota;
        this.vehicle = vehicle;
        this.nom = nom;
    }
    public void setId(String id){
        this.id_dispositiu=id;
    }
    public void setFlota(String flota){
        this.id_flota = flota;
    }
    public void setNom(String nom){
        this.nom = nom;
    }
    public void setPosition(Double longitud, Double latitud){
        this.longitud = longitud;
        this.latitud = latitud;
    }
    public void setVehicle(String vehicle){
        this.vehicle=vehicle;
    }

    public void setCarrega(String Carrega){
        this.carrega = carrega;
    }
    public void setEmpresa(String empresa){
        this.id_empresa = empresa;
    }
    public void setUsuari(String usuari){
        this.id_usuari = usuari;
    }

    public String getId(){
        return id_dispositiu;
    }
    public String getFlota(){
        return id_flota;
    }
    public Double getLong(){
        return longitud;
    }
    public Double getLat(){
        return latitud;
    }
    public String getVehicle(){
        return vehicle;
    }
    public String getCarrega(){
        return carrega;
    }
    public String getId_empresa(){
        return id_empresa;
    }
    public String getId_usuari(){
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