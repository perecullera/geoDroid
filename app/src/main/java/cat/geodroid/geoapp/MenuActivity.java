package cat.geodroid.geoapp;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MenuActivity extends ActionBarActivity {
    Button mapaButton, editaButton, logoutButton;
    TextView menu_title;
    Bundle dades;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        mapaButton = (Button) findViewById(R.id.mapa_button);
        editaButton = (Button) findViewById(R.id.flota_button);
        logoutButton = (Button) findViewById(R.id.logout_button);
        menu_title = (TextView) findViewById(R.id.menu_title);

        dades = getIntent().getExtras();

        /**
         * Si tipusUsuari no es ADMIN, no podra
         * accedir a edicio de flotes
         */
        if(dades.getInt("tipusUsuari")==0){
            editaButton.setVisibility(View.INVISIBLE);
        }

        menu_title.setText("Hola, "+dades.getString("email"));

        /**
         * Carreguem Activity Maps per a visualitzar
         * l'ubicació dels dispositius
         */
        mapaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent intent = new Intent(MenuActivity.this, MapsActivity.class);
            intent.putExtra("empresa", dades.getInt("empresa"));
            startActivity(intent);
            }
        });

        /**
         * Carreguem Activity LlistaFlota i passem les dades de
         *  l'usuari per a fer la cerca dels seus dispositius
         */
        editaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent intent = new Intent(MenuActivity.this, LlistaFlotaActivity.class);
            intent.putExtra("empresa", dades.getInt("empresa"));
            startActivity(intent);
            }
        });

        /**
         * Carreguem Activity Login al fer click al Botó logout
         */
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent intent = new Intent(MenuActivity.this, LoginActivity.class);
            startActivity(intent);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_menu, menu);
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