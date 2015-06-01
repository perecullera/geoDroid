package cat.geodroid.geoapp;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Victor LLuciÃ , Ricard Moya on 31/3/15.
 */
public class Dispositiu  {
    //TODO generate atributes, getters and setters
    private int id_dispositiu;
    private String nom;
    private String flota;
    private Double latitud;
    private Double longitud;
    private String vehicle;
    private String carrega;
    private int id_empresa;
    private int id_usuari;

    /**
     * Constructor buit de Dispositiu
     */
    public Dispositiu() {}

    public Dispositiu (int id_dispositiu, String nom, String flota, Double latitud,
                       Double longitud, String vehicle, String carrega, int empresa, int id_usuari) {

        this.id_dispositiu = id_dispositiu;
        this.nom = nom;
        this.flota = flota;
        this.latitud = latitud;
        this.longitud = longitud;
        this.vehicle = vehicle;
        this.carrega = carrega;
        this.id_empresa = empresa;
        this.id_usuari = id_usuari;
    }

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

    /**
     *
     * @return
     */
    public String toJSON() {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", getId());
            jsonObject.put("nom", getNom());
            jsonObject.put("flota", getFlota());
            jsonObject.put("latitud", getLat());
            jsonObject.put("longitud", getLong());
            jsonObject.put("vehicle", getVehicle());
            jsonObject.put("carrega", getCarrega());
            jsonObject.put("id_empresa", getId_empresa());
            jsonObject.put("id_usuari", getId_usuari());

            return jsonObject.toString();

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "";
        }
    }
}