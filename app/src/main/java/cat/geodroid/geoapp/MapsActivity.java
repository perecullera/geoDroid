package cat.geodroid.geoapp;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import android.app.ProgressDialog;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Marker;

import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;

/**
 * Created by Victor Llucià i Ricard Moya
 *
 *
 * Activitat encarregada de carregar el mapa amb els marcadors que representen
 * els vehicles.
 *
 * En crear-se l'activitat es comprova que l'APK Google Play Services estigui instal·lada.
 * Sense ella no es podrà accedir a l'API de Google Maps.
 * Si no està instal·lada, s'informa a l'usuari que és necessària i se li ofereix anar a
 * la Play Store per a instal·lar-la. *
 *
 * Els marcadors s'obtenen d'una BD SQLite fent servir una classe interna de tasca asíncrona.
 */
public class MapsActivity extends FragmentActivity implements OnInfoWindowClickListener {

    private GoogleMap mGMap; // Might be null if Google Play services APK is not available.

    private Context context;
    private Bundle dades;
    private MarkerOptions markerOptions;

    // Progress Dialog
    private ProgressDialog pDialog;

    // url to fetch dispositius
    //private static final String URL_DISPOSITIUS = "http://192.168.1.10/geodroid/dispositius.php";
    private static final String URL_DISPOSITIUS = "http://serasihay.ddns.net:23080/geodroid/dispositius.php";

    //MOCKING
    //private static final String URL_ONTHEMOVE = "http://192.168.1.10/geodroid/on_the_move.php";
    //private static final String URL_ONTHEMOVE = "http://serasihay.ddns.net:23080/geodroid/on_the_move.php";

    JSONParser jsonParser = new JSONParser();
    private int success; //to determine JSON signal login success/fail

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private static final String TAG_DEVICES = "dispositius";
    private static final String TAG_DEVICEID = "id";
    private static final String TAG_DEVICENAME = "nom";
    private static final String TAG_FLOTA = "flota";
    private static final String TAG_LAT = "latitud";
    private static final String TAG_LNG = "longitud";
    private static final String TAG_VEHICLE = "vehicle";
    private static final String TAG_CARREGA= "carrega";
    private static final String TAG_IDEMPRESA = "id_empresa";
    private static final String TAG_IDUSUARI = "id_usuari";

    private HashMap<Integer, Dispositiu> dispositiusMapper = new HashMap<Integer, Dispositiu>();

    private HashMap<Marker, Dispositiu> dispositiuMarkerMap = new HashMap<Marker, Dispositiu>();

    LatLngBounds bounds; //per calcular la mitjana dels markers

    //MOCKING
    List<Marker> lMarkers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        context = this;

        dades = getIntent().getExtras();

        // Comprovem si els Google Play Services estan disponibles.
        if (checkGPlayServices()) {

            setUpMapIfNeeded();

            //REAL
            SetMarkersTask markersTask = new SetMarkersTask();
            markersTask.execute();

            //MOCKING
            //callAsynchronousTask();

            //centre el mapa segons mitjana dels punts en el mapa
            mGMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                @Override
                public void onMapLoaded() {
                    int padding = 150; // offset from edges of the map in pixels
                    CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                    mGMap.animateCamera(cu);
                    //mGMap.moveCamera(cu);
                }
            });

            mGMap.setOnInfoWindowClickListener(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (checkGPlayServices()) {

            setUpMapIfNeeded();
        }
    }

    /**
     * Comprova que els Google Play Services estan disponibles.
     * Són imprescindibles per accedir a l'API de Google Maps....
     *
     */
    private boolean checkGPlayServices() {
        // Comprovem si els Google Play Services estan disponibles.
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());

        // Avisem a l'usuari que no hi ha Google Play Services
        if (status != ConnectionResult.SUCCESS) { // Google Play Services are not available

            int requestCode = 666;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
            dialog.show();
            return false;

        } else { // Google Play Services are available
            return true;
        }
    }

    /**
     * Carrega i configura el mapa en cas que no s'hagi fet anteriorment.
     *
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mGMap == null) {

            // Getting reference to the SupportMapFragment of activity_loginn.xml
            SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

            // Getting GoogleMap object from the fragment
            mGMap = fm.getMap();
            mGMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);

            mGMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                @Override
                public View getInfoWindow(Marker arg0) {
                    return null;
                }

                @Override
                public View getInfoContents(Marker marker) {

                    View v = getLayoutInflater().inflate(R.layout.marker, null);

                    TextView info = (TextView) v.findViewById(R.id.info);

                    info.setText((Html.fromHtml("<b>" + marker.getTitle() + "</b>" + "<br />" +
                            "<small>" + marker.getSnippet() + "</small>" + "<br />" +
                            "<small> <b>Posició: </b>" + marker.getPosition().toString() + "</small>")));

                    return v;
                }
            });

            UiSettings uiSettings = mGMap.getUiSettings();
            uiSettings.setCompassEnabled(true);
            uiSettings.setZoomControlsEnabled(true);
            uiSettings.setMyLocationButtonEnabled(true);

            // Check if we were successful in obtaining the map.
            if (mGMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mGMap} is not null.
     */
    private void setUpMap() {
        //mGMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
    }

    /**
     * Classe interna de tasca asíncrona per obtenir de la BD la
     * informació necessaria per construir tots els marcadors que
     * representaran els vehicles.
     *
     * La consulta a la BD i la construcció de cada marcador es fa
     * al mètode doInBackground() que corre en un fil que no interfereix
     * amb la UI. Així no es penalitza l'experiència de l'usuari en cas
     * que hi hagi molts marcadors.
     *
     * La construcció dels marcadors es fa a partir de la classe auxiliar
     * MarkersDataSource amb la qual obtindrem una List amb els marcadors.
     *
     * Les característiques de cada marcador les traspassem a l'API de Google MAps
     * a partir de la classe MarkerOptions.
     *
     * Col·loquem al mapa cada marcador amb les seves característiques a través
     * del mètode addMarker de l'objecte que representa el mapa.
     *
     * param params Void, no se li envia res en cridar-la
     * param Progress Object, tipus de la unitat de progrés
     *                 que es genera durant el processament en 2n pla, que es
     *                 passa al mètode onProgressUpdate() que interactua amb la UI.
     * param Result, Void, el procés en 2n pla retorna null.
     */
    private class SetMarkersTask extends AsyncTask<Void, Object, Void> {

        Dispositiu dispositiu;

        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MapsActivity.this);
            pDialog.setMessage("Carregant dispositius...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Mètode que corre en un fil en 2n pla: no interfereix amb la UI
         *
         * @param params
         * @return null
         */
        protected Void doInBackground(Void... params) {
            // Does NOT run on UI thread
            // Long-running operations go here;

            // Building Parameters
            List<NameValuePair> postValues = new ArrayList<NameValuePair>();
            postValues.add(new BasicNameValuePair("id_empresa", String.valueOf(dades.getInt("empresa"))));

            // getting JSON Object
            // Note that login url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(URL_DISPOSITIUS, "POST", postValues);

            // check log cat from response
            //Log.d("DispositiusEmpresa Response", json.toString());

            // check for success tag
            try {
                success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // dispositius retrieved successfully

                    JSONArray jsonArray = json.getJSONArray(TAG_DEVICES);

                    if (jsonArray != null) {
                       int len = jsonArray.length();
                       for (int i = 0; i < len; i++) {

                           JSONObject obj = jsonArray.getJSONObject(i);

                           dispositiu = new Dispositiu();
                           dispositiu.setId(obj.getInt(TAG_DEVICEID));
                           dispositiu.setNom(obj.getString(TAG_DEVICENAME));
                           dispositiu.setFlota(obj.getString(TAG_FLOTA));
                           dispositiu.setVehicle(obj.getString(TAG_VEHICLE));
                           dispositiu.setPosition(obj.getDouble(TAG_LAT), obj.getDouble(TAG_LNG));

                           dispositiusMapper.put(dispositiu.getId(), dispositiu);
                       }
                    }

                } else {
                    // failed to retrieve dispositius
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            // Si hem obtingut dispositius
            if (!dispositiusMapper.isEmpty()) {

                //Per calcular la mitjana dels punts en el mapa
                LatLngBounds.Builder builder = new LatLngBounds.Builder();

                // Obtenim les característiques de cada marcador i les
                // passem a l'API de GMaps
                Iterator it = dispositiusMapper.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry pair = (Map.Entry)it.next();

                    Dispositiu d = (Dispositiu) pair.getValue();
                    builder.include(d.getPosition());

                    double lat = d.getLat();
                    double lng = d.getLong();
                    LatLng latlong = new LatLng(lat, lng);
                    String snippet = "<b>Vehicle: </b>"+ d.getVehicle() +"<br /> <b>Flota: </b>"+ d.getFlota();
                    markerOptions = new MarkerOptions()
                            .title(d.getNom())
                            .position(latlong)
                            .snippet(snippet);

                    // Enviem a onProgressUpdate les característiques
                    // del marcador i el dispositiu que té les dades a associar-hi
                    publishProgress(markerOptions, d);

                }
                bounds = builder.build();
            }
            return null;
        }

        /**
         * Mètode que corre al fil de la UI
         *
         * @param params MarkerOptions i Dispositiu, tipus de la unitat de progrés
         */
        protected void onProgressUpdate(Object... params) {

            if (!(params[0] instanceof MarkerOptions)) {
                throw new IllegalArgumentException("Wrong type for argument 1! " +
                        "MarkerOptions expected, instead got "+params[0].getClass().getCanonicalName());
            }

            if (!(params[1] instanceof Dispositiu)) {
                throw new IllegalArgumentException("Wrong type for argument 2! " +
                        "Dispositiu expected, instead got "+params[1].getClass().getCanonicalName());
            }

            Marker m;
            // Add newly-created marker to map
            m = mGMap.addMarker((MarkerOptions) params[0]);

            //MOCKING
            //lMarkers.add(m);

            dispositiuMarkerMap.put(m, (Dispositiu) params[1]);
        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // dismiss the dialog once done
            if (pDialog.isShowing()) {
                pDialog.dismiss();
		    }
        }
    }

    //MOCKING
    private void clearMarkers() {
        if(!lMarkers.isEmpty()){
            for (Marker m : lMarkers){
                if (m != null) {
                    m.remove();
                }
            }
            lMarkers.clear();
        }
    }


    //MOCKING
    /*
    public void callAsynchronousTask() {
        final Handler handler = new Handler();
        Timer timer = new Timer();
        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            HttpClient client = new DefaultHttpClient();
                            try {
                              client.execute(new HttpGet(URL_ONTHEMOVE));
                            } catch(IOException e) {
                              //do something here
                            }
                            Log.e("UEP! ", "Repetint...");
                            //borra markers
                            //clearMarkers();

                            //SetMarkersTask performSetMarkersTask = new SetMarkersTask();
                            // PerformBackgroundTask this class is the class that extends AsynchTask
                            //performSetMarkersTask.execute();
                            //Toast.makeText(getApplicationContext(), "Actualitzant mapa", Toast.LENGTH_LONG).show();

                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                        }
                    }
                });
            }
        };
        timer.schedule(doAsynchronousTask, 0, 3000); //execute in every 50000 ms
    }
    */

    @Override
    public void onInfoWindowClick(Marker marker) {

        //Toast.makeText(context, String.valueOf(eventMarkerMap.get(marker).getId()), Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(MapsActivity.this, DispositiuActivity.class);
        intent.putExtra("ubicacio", "mapa");
        //intent.putExtra("idDispositiu", dispositiuMarkerMap.get(marker).getId());
        intent.putExtra("jsonDispositiu", dispositiusMapper.get(dispositiuMarkerMap.get(marker).getId()).toJSON());
        startActivity(intent);
     }
}