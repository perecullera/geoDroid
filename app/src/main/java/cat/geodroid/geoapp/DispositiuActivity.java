package cat.geodroid.geoapp;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
        final Dispositiu dis = crud.getDispositiu(dades.getString(""));

        final EditText nom = (EditText) findViewById(R.id.nom_dispositiu);
        final EditText empresa = (EditText) findViewById(R.id.empresa);

        Button actualitzar = (Button) findViewById(R.id.actualitza);

        nom.setText(dis.getNom());
        empresa.setText(dis.getId_empresa());

        actualitzar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dispositiu aux = dis;
                aux.setNom(nom.getText().toString());
                aux.setEmpresa(empresa.getText().toString());

                crud.updateDispositiu(aux);
            }
        });

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
