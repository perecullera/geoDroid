package cat.geodroid.geoapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import cat.geodroid.geoapp.R;

import org.w3c.dom.Text;

import java.util.List;

public class LlistaFlotaActivity extends ActionBarActivity {
    TextView info;
    Context context;
    Bundle dades;
    CRUDClass crud;
    ListView llista_dispositius;
    DispositiusAdapter dispositiusAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_llista_flota);

        context = getApplicationContext();
        dades = getIntent().getExtras();
        crud = new CRUDClass(context);

        List dispositius = crud.getDispositiusEmpresa(dades.getString("empresa"));
        Log.d("" + dispositius, "ricard");

        dispositiusAdapter = new DispositiusAdapter(LlistaFlotaActivity.this, dispositius);
        llista_dispositius.setAdapter(dispositiusAdapter);
        llista_dispositius = (ListView) findViewById(R.id.dispositius_listView);

        llista_dispositius.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView id_dispositiu = (TextView) view.findViewById(R.id.id_dispositiu);

                Intent intent = new Intent(LlistaFlotaActivity.this, DispositiuActivity.class);
                intent.putExtra("idDispositiu", id_dispositiu.getText());
                startActivity(intent);
            }
        });
        llista_dispositius.setClickable(true);

        info = (TextView) findViewById(R.id.info_list);
        info.setText("Aqui cal llistar les flotes que hi haura al resultat de la consulta 'SELECT * FROM flotes WHERE propietari="+dades.getString("empresa"));
    }

    private class DispositiusAdapter extends BaseAdapter {
        List<Dispositiu> llistat;
        Context context;
        LayoutInflater inflater;

        public DispositiusAdapter(Context context, List llistat){
            this.llistat = llistat;
            this.context = context;
        }

        @Override
        public int getCount() {
            return llistat.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View dispositiusView = convertView;
            dispositiusView = inflater.inflate(R.layout.dispositius_listview, parent, false);

            TextView id_dispositiu = (TextView) dispositiusView.findViewById(R.id.id_dispositiu);
            TextView nom_dispositiu = (TextView) dispositiusView.findViewById(R.id.nom_dispositiu);

            Dispositiu dispositiu = llistat.get(position);

            id_dispositiu.setText(dispositiu.getId().toString());
            nom_dispositiu.setText(dispositiu.getNom().toString());

            return dispositiusView;
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