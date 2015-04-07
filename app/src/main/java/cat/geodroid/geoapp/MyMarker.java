package cat.geodroid.geoapp;


public class MyMarker {
    private long id;
    private String title;
    private double lat;
    private double lng;
    private String snippet;

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
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the latitude
     */
    public double getLat() {
        return lat;
    }

    /**
     * @param lat the latitude to set
     */
    public void setLat(double lat) {
        this.lat = lat;
    }

    /**
     * @return the longitude
     */
    public double getLng() {
        return lng;
    }

    /**
     * @param lng the longitude to set
     */
    public void setLng(double lng) {
        this.lng = lng;
    }

    /**
     * @return the snippet
     */
    public String getSnippet() {
        return snippet;
    }

    /**
     * @param snippet the snippet to set
     */
    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }
}
