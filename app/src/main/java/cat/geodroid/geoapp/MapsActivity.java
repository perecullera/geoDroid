package cat.geodroid.geoapp;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.os.AsyncTask;

import android.content.Context;
import android.app.Dialog;

import android.util.Log;

import java.util.List;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.UiSettings;

/**
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
public class MapsActivity extends FragmentActivity {

    private GoogleMap mGMap;
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Comprovem si els Google Play Services estan disponibles.
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());

        // Avisem a l'usuari que no hi ha Google Play Services
        if (status != ConnectionResult.SUCCESS) {

            int requestCode = 666;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
            dialog.show();

        // Si hi ha els Play Services, preparem el mapa i carreguem els marcadors
        } else {

            setUpMapIfNeeded();
            SetMarkersTask markersTask = new SetMarkersTask();
            markersTask.execute();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
        SetMarkersTask markersTask = new SetMarkersTask();
        markersTask.execute();
    }

    /**
     * Carrega i configura el mapa en cas que no s'hagi fet anteriorment.
     *
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mGMap == null) {

            // Getting reference to the SupportMapFragment of activity_main.xml
            SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

            // Getting GoogleMap object from the fragment
            mGMap = fm.getMap();

            // Check if we were successful in obtaining the map.
            if (mGMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * Configura característiques del mapa com ara el tipus de vista i els
     * controls que es mostraran.
     */
    private void setUpMap() {

        mGMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);

        UiSettings uiSettings = mGMap.getUiSettings();
        uiSettings.setCompassEnabled(true);
        uiSettings.setZoomControlsEnabled(true);
        uiSettings.setMyLocationButtonEnabled(true);
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
     * @param Params Void, no se li envia res en cridar-la
     * @param Progress MarkerOptions, tipus de la unitat de progrés
     *                 que es genera durant el processament en 2n pla i que es
     *                 passa al mètode onProgressUpdate() que interactua amb la UI.
     * @param Result, Void, el procés en 2n pla retorna null.
     */
    private class SetMarkersTask extends AsyncTask<Void, MarkerOptions, Void> {

        private MarkersDataSource data;
        private MarkerOptions markerOptions;

        /**
         * Mètode que corre en un fil en 2n pla: no interfereix amb la UI
         *
         * @param params
         * @return null
         */
        @Override
        protected Void doInBackground(Void... params) {
            // Does NOT run on UI thread
            // Long-running operations go here;

            // Instanciem la classe auxiliar que permet obtenir els marcadors
            // a partir de la informació de la BD
            data = new MarkersDataSource(context);
            try {
                data.open();

            } catch (Exception e) {
                Log.i("MarkersDS", "No s'ha pogut obrir la BD...");
            }

            // Carreguem en una llista els marcadors obtinguts de la BD
            List<MyMarker> m = data.getMyMarkers();

            // Si obtenim marcadors
            if (!m.isEmpty()) {

                // Obtenim les característiques de cada marcador i les
                // passem a l'API de GMaps
                for (int i = 0; i < m.size(); i++) {
                    double lat = m.get(i).getLat();
                    double lng = m.get(i).getLng();
                    LatLng latlong = new LatLng(lat, lng);
                    markerOptions = new MarkerOptions()
                            .title(m.get(i).getTitle())
                            .position(latlong)
                            .snippet(m.get(i).getSnippet()
                            );

                    // Enviem a onProgressUpdate les característiques
                    // del marcador
                    publishProgress(markerOptions);
                }
            }
            return null;
        }

        /**
         * Mètode que corre al fil de la UI
         *
         * @param params MarkerOptions, tipus de la unitat de progrés
         */
        @Override
        protected void onProgressUpdate(MarkerOptions... params) {
            // Afegeix al mapa el marcador rebut
            mGMap.addMarker(params[0]);
        }
    }
}
