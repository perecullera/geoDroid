package cat.geodroid.geoapp;


import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.os.AsyncTask;

import android.content.Context;
import android.app.Dialog;

import android.database.Cursor;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import android.net.Uri;

import android.util.Log;

import java.util.List;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.UiSettings;


public class MapsActivity extends FragmentActivity {

    private GoogleMap mGMap; // Might be null if Google Play services APK is not available.

    private Context context = this;
    private MarkersDataSource data;
    private MarkerOptions markerOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Getting Google Play availability status
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());

        // Showing status
        if (status != ConnectionResult.SUCCESS) { // Google Play Services are not available

            int requestCode = 666;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
            dialog.show();

        } else { // Google Play Services are available

            setUpMapIfNeeded();
            SetMarkersTask markersTask = new SetMarkersTask();
            markersTask.execute();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mGMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mGMap == null) {

            // Getting reference to the SupportMapFragment of activity_main.xml
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
        mGMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
    }

    /*
    Do all of your database lookups and such in an AsyncTask.
    You can still add the markers on the UI thread, but all of the data crunching will
    be done on a background thread.
    onPreExeecute(), onProgressUpdate(), and onPostExecute() all execute on the UI thread, so it's safe to do UI operations there.
     */
    private class SetMarkersTask extends AsyncTask<Void, MarkerOptions, Void> {

        protected void onPreExecute() {
            // Turn progress spinner on
            setProgressBarIndeterminateVisibility(false);
        }

        protected Void doInBackground(Void... params) {
            // Does NOT run on UI thread
            // Long-running operations go here;

            data = new MarkersDataSource(context);
            try {
                data.open();

            } catch (Exception e) {
                Log.i("hello", "hello");
            }

            List<MyMarker> m = data.getMyMarkers();

            if (!m.isEmpty()) {

                for (int i = 0; i < m.size(); i++) {
                    double lat = m.get(i).getLat();
                    double lng = m.get(i).getLng();
                    LatLng latlong = new LatLng(lat, lng);
                    markerOptions = new MarkerOptions()
                            .title(m.get(i).getTitle())
                            .position(latlong)
                            .snippet(m.get(i).getSnippet()
                            );

                }
                publishProgress(markerOptions);
            }
            return null;
        }

        protected void onProgressUpdate(MarkerOptions... params) {
            // Add newly-created marker to map
            mGMap.addMarker(params[0]);
        }

        protected void onPostExecute(Void result) {
            // Turn off progress spinner
            setProgressBarIndeterminateVisibility(false);
        }
    }
}
