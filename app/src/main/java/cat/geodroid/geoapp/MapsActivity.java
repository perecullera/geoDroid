package cat.geodroid.geoapp;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

    private Context context ;
    private Bundle dades;
    private CRUDClass data;
    private MarkerOptions markerOptions;

    private HashMap<Marker, Dispositiu> eventMarkerMap = new HashMap<Marker, Dispositiu>();

    LatLngBounds bounds; //per calcular la mitjana dels markers

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        context = this;

        dades = getIntent().getExtras();

        // Comprovem si els Google Play Services estan disponibles.
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());

        // Avisem a l'usuari que no hi ha Google Play Services
        if (status != ConnectionResult.SUCCESS) { // Google Play Services are not available

            int requestCode = 666;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
            dialog.show();

        } else { // Google Play Services are available

            setUpMapIfNeeded();
            SetMarkersTask markersTask = new SetMarkersTask();
            markersTask.execute();

            //centre el mapa segons mitjana dels punts en el mapa
            mGMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                @Override
                public void onMapLoaded() {
                    int padding = 150; // offset from edges of the map in pixels
                    CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                    mGMap.animateCamera(cu);
                }
            });

            mGMap.setOnInfoWindowClickListener(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
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
     * param Progress MarkerOptions, tipus de la unitat de progrés
     *                 que es genera durant el processament en 2n pla i que es
     *                 passa al mètode onProgressUpdate() que interactua amb la UI.
     * param Result, Void, el procés en 2n pla retorna null.
     */
    private class SetMarkersTask extends AsyncTask<Void, Object, Void> {

        protected void onPreExecute() {
            // Turn progress spinner on
            setProgressBarIndeterminateVisibility(false);
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

            // Instanciem la classe auxiliar que permet obtenir els marcadors
            // a partir de la informació de la BD
            data = new CRUDClass(context);
            try {
                data.open();

            } catch (Exception e) {
                Log.i("ERROR", "Marcador no carregat");
            }

            //DEBUG per inserció de valors a la BDD
            debugging();
            // Carreguem en una llista els marcadors obtinguts de la BD
            //List<Dispositiu> m = data.getDispositius();
            List<Dispositiu> m = data.getDispositiusEmpresa(dades.getInt("empresa"));

            // Si obtenim marcadors
            if (!m.isEmpty()) {

                //Per calcular la mitjana dels punts en el mapa
                LatLngBounds.Builder builder = new LatLngBounds.Builder();

                // Obtenim les característiques de cada marcador i les
                // passem a l'API de GMaps
                for (int i = 0; i < m.size(); i++) {

                    Dispositiu d = m.get(i);
                    builder.include(d.getPosition());

                    double lat = d.getLat();
                    double lng = d.getLong();
                    LatLng latlong = new LatLng(lat, lng);
                    String snippet = d.getVehicle()+" \n "+ d.getFlota();
                    markerOptions = new MarkerOptions()
                            .title(d.getNom())
                            .position(latlong)
                            .snippet(snippet);

                    // Enviem a onProgressUpdate les característiques
                    // del marcador
                    publishProgress(markerOptions, d);

                }
                bounds = builder.build();
            }
            return null;
        }
        /**
         * Mètode que corre al fil de la UI
         *
         * @param params MarkerOptions, tipus de la unitat de progrés
         */
        protected void onProgressUpdate(Object... params) {

            // Add newly-created marker to map
            Marker m = mGMap.addMarker((MarkerOptions) params[0]);

            eventMarkerMap.put(m, (Dispositiu) params[1]);

            /*
            mGMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick(Marker marker) {

                        Toast.makeText(context, String.valueOf(eventMarkerMap.get(params[0]).getId()), Toast.LENGTH_SHORT).show();


                        Intent intent = new Intent(MapsActivity.this, DispositiuActivity.class);
                        intent.putExtra("ubicacio", "mapa");
                        intent.putExtra("idDispositiu", eventMarkerMap.get(params[0]).getId());
                        startActivity(intent);

                    }
            });
            */

        }

        protected void onPostExecute(Void result) {
            // Turn off progress spinner
            setProgressBarIndeterminateVisibility(false);

        }

    }

    @Override
    public void onInfoWindowClick(Marker marker) {

        //Toast.makeText(context, String.valueOf(eventMarkerMap.get(marker).getId()), Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(MapsActivity.this, DispositiuActivity.class);
        intent.putExtra("ubicacio", "mapa");
        intent.putExtra("idDispositiu", String.valueOf(eventMarkerMap.get(marker).getId()));
        startActivity(intent);
     }

    /**
     * Mètode per a insertar dispositius posicionats per a debugging
     */
    protected void debugging(){
    }

}
