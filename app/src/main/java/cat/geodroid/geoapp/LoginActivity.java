package cat.geodroid.geoapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import android.widget.CheckBox;
import android.content.SharedPreferences;

import android.os.AsyncTask;
import android.app.ProgressDialog;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class LoginActivity extends ActionBarActivity {

    private String username, password;

    private Button loginButton;
    private EditText email, contrasenya;
    Context context;

    protected static ConnectivityManager connMgr;

    // Progress Dialog
    private ProgressDialog pDialog;

    JSONParser jsonParser = new JSONParser();
    private int success; //to determine JSON signal login success/fail

    // url to select the user (change accordingly)
    //private static final String URL_LOGIN = "http://192.168.1.10/geodroid/login.php";
    private static final String URL_LOGIN = "http://serasihay.ddns.net:23080/geodroid/login.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private static final String TAG_USERID = "id";
    private static final String TAG_USERNAME = "nom";
    private static final String TAG_EMAIL = "email";
    private static final String TAG_ROLE = "rol";
    private static final String TAG_PASSWD = "pwd";
    private static final String TAG_IDEMPRESA = "id_empresa";

    private CheckBox saveLoginCheckBox;
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    private Boolean saveLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton = (Button) findViewById(R.id.login_button);
        email = (EditText) findViewById(R.id.email_text);
        contrasenya = (EditText) findViewById(R.id.contrasenya_text);
        context = getApplicationContext();

        saveLoginCheckBox = (CheckBox)findViewById(R.id.saveLoginCheckBox);
        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();

        saveLogin = loginPreferences.getBoolean("saveLogin", false);
        if (saveLogin == true) {
            email.setText(loginPreferences.getString("username", ""));
            contrasenya.setText(loginPreferences.getString("password", ""));
            saveLoginCheckBox.setChecked(true);
        }

        // Inicialitzem el connectivity manager.
		connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);


        /**
         * Comprovem que les credencials existeixen a la BBDD.
         * En cas satisfactori, carreguem Menu Activity
         */
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                username = email.getText().toString();
                password = contrasenya.getText().toString();

                if (!username.matches("") || !password.matches("")) {

                    if (comprovaConnexio()) {
                        //call a new Login thread
                        new LoginTask().execute();

			        } else {
				        // No tenim connexió, error
                        //Toast.makeText(context, "No hi ha connexió de dades...", Toast.LENGTH_LONG).show();

                        showNoConnectionDialog(LoginActivity.this);
                    }

                    if (saveLoginCheckBox.isChecked()) {
                        loginPrefsEditor.putBoolean("saveLogin", true);
                        loginPrefsEditor.putString("username", username);
                        loginPrefsEditor.putString("password", password);
                        loginPrefsEditor.commit();
                    } else {
                        loginPrefsEditor.clear();
                        loginPrefsEditor.commit();
                    }

                } else {
                    Toast.makeText(context, "Cal omplir els dos camps per a fer login", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    // True si tenim connexió, False en cas contrari.
	public static Boolean comprovaConnexio() {

		// Obtenim l'estat de la xarxa mòbil
		NetworkInfo networkInfo = connMgr
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		boolean connectat3G = networkInfo.isConnected();

		// Obtenim l'estat de la xarxaWi-Fi
		networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		boolean connectatWifi = networkInfo.isConnected();

		// O bé hem de tenir 3G o bé wifi
		return connectat3G || connectatWifi;
	}

    /**
     * Display a dialog that user has no internet connection
     * @param ctx1
     *
     * Code from: http://osdir.com/ml/Android-Developers/2009-11/msg05044.html
     */
    public static void showNoConnectionDialog(Context ctx1) {
        final Context ctx = ctx1;
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setCancelable(true);
        builder.setMessage("Cal accés a Internet...\n" +
                            "Activa la connexió de dades mòbils o el Wi-Fi.");
        builder.setTitle("Connexió fallida");
        builder.setPositiveButton("Configurar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                ctx.startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
            }
        });
        builder.setNegativeButton("Cancel·lar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface dialog) {
                return;
            }
        });
        builder.show();
    }

    /**
     * Background Async Task to Login with username and password
     *
     * param params Void, no se li envia res en cridar-la (doInBackground no rep res)
     * param Progress Object, tipus de la unitat de progrés
     *                 que es genera durant el processament en 2n pla i que es
     *                 passa al mètode onProgressUpdate() que interactua amb la UI.
     * param Result, Void, el procés en 2n pla retorna null.
     */
    class LoginTask extends AsyncTask<String, String, String> {

        Usuari usuari;

        /**
         * Before starting background thread Show Progress Dialog
         **/
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(LoginActivity.this);
            pDialog.setMessage("Fent login amb "+ username +"...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Selecting the user
         **/
        @Override
        protected String doInBackground(String... params) {

            // Building Parameters
            List<NameValuePair> postValues = new ArrayList<NameValuePair>();
            postValues.add(new BasicNameValuePair("usuari", username));
            postValues.add(new BasicNameValuePair("contrasenya", password));


            // getting JSON Object
            // Note that login url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(URL_LOGIN , "POST", postValues);

            // check log cat from response
            //Log.d("Login Response", json.toString());

            // check for success tag
            try {
                success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // successfully logged in

                    usuari = new Usuari(json.getInt(TAG_USERID),
                            json.getString(TAG_USERNAME),
                            json.getString(TAG_EMAIL),
                            json.getInt(TAG_ROLE),
                            json.getString(TAG_PASSWD),
                            json.getInt(TAG_IDEMPRESA));

                } else {
                    // failed to login
                    usuari = null;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         *
         **/
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            // dismiss the dialog once done
            if (pDialog.isShowing()) {
                pDialog.dismiss();
            }

            if ((usuari != null) && (usuari.getId()) >= 0) {

                Toast.makeText(getApplicationContext(), "Login correcte!", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
                intent.putExtra("email", email.getText().toString());
                intent.putExtra("contrasenya", email.getText().toString());
                intent.putExtra("tipusUsuari", usuari.getRol());
                intent.putExtra("idUsuari", usuari.getId());
                intent.putExtra("empresa", usuari.getId_empresa());

                startActivity(intent);

            } else {
                Toast.makeText(getApplicationContext(), "Login fallit!", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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