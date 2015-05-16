package cat.geodroid.geoapp;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cat.geodroid.geoapp.R;

public class DispositiuActivity extends ActionBarActivity {
    Context context;
    CRUDClass crud;
    Bundle dades;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dispositiu);

        crud = new CRUDClass(context);
        context = this;
        dades = getIntent().getExtras();

        int idDispositiu = dades.getInt("idDispositiu");
        final Dispositiu dis = crud.getDispositiu(idDispositiu);

        final EditText nom = (EditText) findViewById(R.id.nom_dispositiu);
        final EditText flota = (EditText) findViewById(R.id.flota);

        Button actualitzar = (Button) findViewById(R.id.actualitza);

        nom.setText(dis.getNom());
        flota.setText(dis.getFlota());

        actualitzar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dis.setNom(nom.getText().toString());
                dis.setFlota(flota.getText().toString());

                boolean success = crud.updateDispositiu(dis);
                if(success){
                    Toast.makeText(context, "Actualitzat correctament", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context, "Actualitzat erroneament", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Carreguem vista des dels Markers de GMAPS. Anulem la possible edicio dels dispositius
        /*if(dades.getString("ubicacio")=="mapa") {
            //
            nom.setEnabled(false);
            empresa.setEnabled(false);
            actualitzar.setVisibility(View.INVISIBLE);
        }*/
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
