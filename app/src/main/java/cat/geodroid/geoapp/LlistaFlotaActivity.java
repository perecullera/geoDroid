package cat.geodroid.geoapp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import android.content.Intent;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.List;
import java.util.Map;

public class LlistaFlotaActivity extends ListActivity {

    // Progress Dialog
    private ProgressDialog pDialog;

    private static final String URL_DISPOSITIUS = "http://192.168.1.10/html/dispositius.php";

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

    // An array of all of our comments
    private JSONArray mDispositius = null;
    // manages all of our comments in a list.
    private ArrayList<HashMap<String, String>> mDispositiuList;

    private HashMap<Integer, Dispositiu> mDispositiusMapper;

    private Dispositiu dispositiu;

    private Bundle dades;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.llista_dispositus);
        dades = getIntent().getExtras();

    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        // loading dispositius via AsyncTask
        new LoadDispositiusTask().execute();
    }

    /**
     * Retrieves Dispositius data from the server.
     */
    public void updateJSONdata() {

        mDispositiuList = new ArrayList<HashMap<String, String>>();

        mDispositiusMapper = new HashMap<Integer, Dispositiu>();

        JSONParser jsonParser = new JSONParser();

        // Building Parameters
        List<NameValuePair> postValues = new ArrayList<NameValuePair>();
        postValues.add(new BasicNameValuePair("id_empresa", String.valueOf(dades.getInt("empresa"))));

        // getting JSON Object
        // Note that login url accepts POST method
        JSONObject json = jsonParser.makeHttpRequest(URL_DISPOSITIUS, "POST", postValues);

        // check log cat from response
        // Log.d("DispositiusEmpresa Response", json.toString());

        try {

            success = json.getInt(TAG_SUCCESS);

            if (success == 1) {
                // dispositius retrieved successfully

                mDispositius = json.getJSONArray(TAG_DEVICES);

                if (mDispositius != null) {

                    int len = mDispositius.length();
                    for (int i = 0; i < len; i++) {
                        JSONObject obj = mDispositius.getJSONObject(i);

                        // gets the content of each tag
                        String id = String.valueOf(obj.getInt(TAG_DEVICEID));
                        String nom = obj.getString(TAG_DEVICENAME);
                        String flota = obj.getString(TAG_FLOTA);
                        String vehicle = obj.getString(TAG_VEHICLE);

                        // creating new HashMap
                        HashMap<String, String> map = new HashMap<String, String>();

                        map.put(TAG_DEVICEID, id);
                        map.put(TAG_DEVICENAME, nom);
                        map.put(TAG_FLOTA, flota);
                        map.put(TAG_VEHICLE, vehicle);

                        // adding HashList to ArrayList
                        mDispositiuList.add(map);


                        dispositiu = new Dispositiu();
                        dispositiu.setId(obj.getInt(TAG_DEVICEID));
                        dispositiu.setNom(nom);
                        dispositiu.setFlota(flota);
                        dispositiu.setVehicle(vehicle);
                        dispositiu.setPosition(obj.getDouble(TAG_LAT), obj.getDouble(TAG_LNG));

                        mDispositiusMapper.put(dispositiu.getId(), dispositiu);
                    }
                }

            } else {
                // failed to retrieve dispositius
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Inserts the parsed data into the listview.
     */
    private void updateList() {

        if(!mDispositiuList.isEmpty()){
            Collections.sort(mDispositiuList, new Comparator<HashMap<String, String>>() {

                public int compare(HashMap<String, String> object1,
                                   HashMap<String, String> object2) {
                    return ((String) object1.get(TAG_DEVICENAME)).compareTo((String) object2.get(TAG_DEVICENAME));
                }
            });
        }

        ListAdapter adapter = new SimpleAdapter(this,
                mDispositiuList,
                R.layout.dispositiu,
                new String[] { TAG_DEVICENAME, TAG_FLOTA, TAG_VEHICLE },
                new int[] { R.id.nom, R.id.flota, R.id.vehicle });


        setListAdapter(adapter);

        ListView lv = getListView();
        lv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                HashMap<String, String> map = (HashMap<String, String>) parent.getItemAtPosition(position);

                //Toast.makeText(LlistaFlotaActivity.this, map.get(TAG_DEVICEID), Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(LlistaFlotaActivity.this, DispositiuActivity.class);
                intent.putExtra("jsonDispositiu", mDispositiusMapper.get(Integer.parseInt(map.get(TAG_DEVICEID))).toJSON());
                startActivity(intent);
            }
        });
    }

    public class LoadDispositiusTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(LlistaFlotaActivity.this);
            pDialog.setMessage("Carregant dispositius...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... arg0) {
            updateJSONdata();
            return null;

        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            pDialog.dismiss();
            updateList();
        }
    }
}