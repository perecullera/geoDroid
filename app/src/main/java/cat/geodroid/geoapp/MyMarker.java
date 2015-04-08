package cat.geodroid.geoapp;

/**
 * Classe que representa un Marker d'acord amb l'API de Google Maps.
 * Conté els atributs típics d'aquest tipus d'objecte.
 *
 * Modelitza un dispositiu/vehicle/marcador.
 *
 * Diposa de constructors i mètodes setters/getters.
 */
public class MyMarker {

    // Atributs
    private long id;
    private String title;
    private double lat;
    private double lng;
    private String snippet;

    //Constructors
    public MyMarker() {
    }

    public MyMarker(long id, String title, double lat, double lng, String snippet) {
        this.id = id;
        this.title = title;
        this.lat = lat;
        this.lng = lng;
        this.snippet = snippet;
    }
    public MyMarker(String title, double lat, double lng, String snippet) {
        this.title = title;
        this.lat = lat;
        this.lng = lng;
        this.snippet = snippet;
    }

    /**
     * @return L'identificador del dispositiu
     */
    public long getId() {
        return id;
    }

    /**
     * @param id L'identificador a assignar al marcador
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * @return El nom del dispositiu
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title El nom a assignar al dispsitiu
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return La darrera latitud coneguda del dispositiu
     */
    public double getLat() {
        return lat;
    }

    /**
     * @param lat La latitud a assignar al dispositiu
     */
    public void setLat(double lat) {
        this.lat = lat;
    }

    /**
     * @return La darrera longitud coneguda del dispositiu
     */
    public double getLng() {
        return lng;
    }

    /**
     * @param lng La longitud a assignar al dispositiu
     */
    public void setLng(double lng) {
        this.lng = lng;
    }

    /**
     * @return L'snippet que es mostra en fer clic al marcador
     */
    public String getSnippet() {
        return snippet;
    }

    /**
     * @param snippet L'snippet que es mostra en fer clic al marcador
     */
    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }
}
