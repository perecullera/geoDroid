package cat.geodroid.geoapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe auxiliar per treballar amb la BD.
 *
 * Conté mètodes que permeten, per exemple, obtenir una llista de
 * tots els marcadors (objectes MyMarker), cadascún dels quals és
 * construit a partir dels camps que conté una fila de la BD.
 *
 */
public class MarkersDataSource {

    // Classes per accedir a la BD
    DBHelper dbhelper;
    SQLiteDatabase db;

    // Columnes que es volen obtenir de la consulta a la BD
    String[] cols = {DBHelper.TITLE, DBHelper.LAT, DBHelper.LNG, DBHelper.SNIPPET};

    /**
     * Constructor de la classe
     * @param c Context de l'activitat/classe que crida
     *          el constructor
     */
    public MarkersDataSource(Context c) {
        dbhelper = new DBHelper(c);
    }

    /**
     * Classe que obre la BD en mode de lectura/escriptura.
     *
     * @throws SQLException
     */
    public void open() throws SQLException {
         db = dbhelper.getWritableDatabase();
    }

    /**
     * Classe que tanca la BD.
     *
     */
    public void close(){
        db.close();
    }

    /**
     * Classe que permet afegir un marcador a la taula de la BD.
     * @param m
     */
    public void addMarker(MyMarker m){

        ContentValues cv = new ContentValues();

        cv.put(DBHelper.TITLE, m.getTitle());
        cv.put(DBHelper.LAT, m.getLat());
        cv.put(DBHelper.LNG, m.getLng());
        cv.put(DBHelper.SNIPPET, m.getSnippet());

        db.insert(DBHelper.TABLE_NAME, null, cv);
    }

    /**
     * Classe que permet obtenir una llista de marcadors construida
     * a partir de la consulta a la BD que recupera totes les files que
     * conté la taula de dispositius.
     *
     * @return markers Llista amb els marcadors
     */
    public List<MyMarker> getMyMarkers(){

        List<MyMarker> markers = new ArrayList<MyMarker>();

        Cursor cursor = db.query(DBHelper.TABLE_NAME, cols, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            MyMarker m = cursorToMarker(cursor);
            markers.add(m);
            cursor.moveToNext();
        }
        cursor.close();

        return markers;
    }

    /**
     * Classe que permet eliminar un marcador/fila de la taula a partir
     * del seu identificador.
     *
     * @param m Marcador a eliminar de la taula
     */
    public void deleteMarker(MyMarker m){
        db.delete(DBHelper.TABLE_NAME, DBHelper.ID + " = '" + m.getId() + "'", null);
    }

    /**
     * Classe que permet transformar una fila de la taula de dispsitius (cursor)
     * en un marcador (MyMarker) amb els atributs necessaris.
     *
     * @param cursor Fila de la taula de la BD
     * @return m Objecte MyMarker (marcador)
     */
    private MyMarker cursorToMarker(Cursor cursor) {
        MyMarker m = new MyMarker();
        m.setTitle(cursor.getString(0)); //L'índex surt de l'array "cols"
        m.setLat(cursor.getDouble(1));
        m.setLng(cursor.getDouble(2));
        m.setSnippet(cursor.getString(3));
        return m;
    }
}
