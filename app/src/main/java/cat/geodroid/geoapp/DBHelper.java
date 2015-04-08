package cat.geodroid.geoapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Classe per facilitar la creació i gestió de versions de la
 * BD SQLite.
 *
 * També l'aprofitem per precarregar la BD amb algunes files que
 * simulin marcadors/vehicles i permetin fer proves de funcionament.
 *
 */
public class DBHelper extends SQLiteOpenHelper{

    // Nom de la taula que hi haurà a la BD
    public static final String TABLE_NAME = "locations";
    // Camps de la taula
    public static final String ID = "loc_id";
    public static final String TITLE = "loc_title";
    public static final String LAT = "loc_lat";
    public static final String LNG = "loc_lng";
    public static final String SNIPPET = "loc_snippet";

    // Número de versió inicial de la BD
    private static final int DB_VERSION = 1;

    // Nom del fitxer que contindrà la BD
    private static final String DB_NAME = "markerlocations.db";

    // Sentència SQL per a crear la BD
    private static final String DB_CREATE =
        "create table "+ TABLE_NAME + "("
        + ID + " integer primary key autoincrement, "
        + TITLE + " text, "
        + LAT + " double , "
        + LNG + " double , "
        + SNIPPET + " text); "
    ;

    // Sentència SQL per inserir dades de simulació
    private static final String DB_PREPOPULATE =
        "insert into "+ TABLE_NAME
        + " (loc_title, loc_lat, loc_lng, loc_snippet) "
        + "values "
        + "('A', 41.401808, 2.165934, 'Alpha'), "
        + "('B', 41.402581, 2.166106, 'Beta'), "
        + "('C', 41.400658, 2.165655, 'Charlie'); "
    ;

    /**
     * Constructor
     * @param context Context de l'activitat/classe que crida
     *                el constructor
     */
    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    /**
     * Mètode que es crida quan s'instancia la classe.
     *
     * Executarà les sentències de creació de la BD i de precarrega
     * de les dades de simulació
     *
     * @param db BD sobre la qual treballar
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DB_CREATE);
        db.execSQL(DB_PREPOPULATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_NAME);
        onCreate(db);
    }
}
