package cat.geodroid.geoapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.List;
import java.util.ArrayList;

public class DispositiuActivity extends ActionBarActivity {
    Context context;
    Bundle dades;
    Intent intent;
    Dispositiu dis;

    // Progress Dialog
    private ProgressDialog pDialog;

    private static final String URL_DISPOSITIU = "http://192.168.1.10/geodroid/upd_dispositiu.php";
    //private static final String URL_DISPOSITIU = "http://serasihay.ddns.net:23080/geodroid/upd_dispositiu.php";

    private int success; //to determine JSON signal login success/fail
    private String message; //to capture JSON message node text

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private static final String TAG_DEVICEID = "id";
    private static final String TAG_DEVICENAME = "nom";
    private static final String TAG_FLOTA = "flota";
    private static final String TAG_LAT = "latitud";
    private static final String TAG_LNG = "longitud";
    private static final String TAG_VEHICLE = "vehicle";
    private static final String TAG_CARREGA = "carrega";
    private static final String TAG_IDEMPRESA = "id_empresa";
    private static final String TAG_IDUSUARI = "id_usuari";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dispositiu);

        context = this;
        intent = getIntent();
        dades = intent.getExtras();

        final EditText nom = (EditText) findViewById(R.id.nom_dispositiu);
        final EditText flota = (EditText) findViewById(R.id.flota);
        final Button actualitzar = (Button) findViewById(R.id.actualitza);

        if (dades != null) {
            /**
             * Carreguem vista des dels Markers de GMAPS. Anulem la possible edicio dels dispositius
             */
            if (dades.containsKey("ubicacio")) {

                if (dades.getString("ubicacio").equals("mapa")) {

                    TextView tv1 = (TextView)findViewById(R.id.tvDispositiu);
                    tv1.setText("Info Dispositiu");

                    nom.setKeyListener(null);
                    //nom.setEnabled(false);
                    //nom.setFocusable(false);
                    //nom.setClickable(false);

                    flota.setKeyListener(null);
                    //flota.setEnabled(false);
                    //flota.setFocusable(false);
                    //flota.setClickable(false);
                    actualitzar.setVisibility(View.INVISIBLE);
                }
            }

            /**
             * Cerquem el dispositiu a la BBDD amb les
             * dades passades al Bundle
             */
            try {

                JSONObject json = new JSONObject(dades.getString("jsonDispositiu"));

                dis = new Dispositiu();
                dis.setId(json.getInt(TAG_DEVICEID));
                dis.setNom(json.getString(TAG_DEVICENAME));
                dis.setFlota(json.getString(TAG_FLOTA));
                dis.setVehicle(json.getString(TAG_VEHICLE));
                dis.setPosition(json.getDouble(TAG_LAT), json.getDouble(TAG_LNG));

            } catch (JSONException e) {
                e.printStackTrace();
            }

            nom.setText(dis.getNom());
            flota.setText(dis.getFlota());

            /**
             * Boto que gestiona l'actualitzacio del dispositiu
             */
            actualitzar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dis.setNom(nom.getText().toString());
                    dis.setFlota(flota.getText().toString());

                     // updating dispositiu via AsyncTask
                    new UpdateDispositiuTask().execute();
                }
            });
        }
    }

    /**
     *
     */
    public void updateDispositiu() {

        JSONParser jsonParser = new JSONParser();

        // Building Parameters
        List<NameValuePair> postValues = new ArrayList<NameValuePair>();
        postValues.add(new BasicNameValuePair("id_dispositiu", String.valueOf(dis.getId())));
        postValues.add(new BasicNameValuePair("nom", dis.getNom()));
        postValues.add(new BasicNameValuePair("flota", dis.getFlota()));
        //postValues.add(new BasicNameValuePair("vehicle", dis.getVehicle()));
        //postValues.add(new BasicNameValuePair("latitud", String.valueOf(dis.getLat())));
        //postValues.add(new BasicNameValuePair("longitud", String.valueOf(dis.getLong())));

        // getting JSON Object
        // Note that login url accepts POST method
        JSONObject json = jsonParser.makeHttpRequest(URL_DISPOSITIU, "POST", postValues);

        // check log cat from response
        //Log.d("Update Response", json.toString());

        // check for success tag
        try {
            success = json.getInt(TAG_SUCCESS);
            message = json.getString(TAG_MESSAGE);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public class UpdateDispositiuTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(DispositiuActivity.this);
            pDialog.setMessage("Actualitzant les dades...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... arg0) {
            updateDispositiu();
            return null;

        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            // dismiss the dialog once done
            if (pDialog.isShowing()) {
                pDialog.dismiss();
            }

            if (success == 1) {
                // dispositiu updated successfully

                Toast.makeText(context, "Actualitzat correctament, prem enrere per tornar a la llista", Toast.LENGTH_SHORT).show();

            } else {
                // failed to update dispositiu
                Toast.makeText(context, "Actualització fallida!", Toast.LENGTH_SHORT).show();
                Log.e("Update Response", message);
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_llista_flota, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
