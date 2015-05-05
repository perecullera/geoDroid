package cat.geodroid.geoapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Joaquim Guinovart, Raul Ortega, Victor Llucià, Ricard Moya  on 31/3/15.
 */
class DataBaseHelper extends SQLiteOpenHelper {
    // Logcat tag
    private static final String LOG = "Databasehelper";

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "geoDroid";

    // Table Names
    private static final String TABLE_EMPRESA = "empresa";
    protected static final String TABLE_USUARI = "usuari";
    private static final String TABLE_DETALLS = "detalls";
    public static final String TABLE_DISPOSITIU = "dispositiu";

    // Table Create Statements
    // Empresa table create statement
    private static final String CREATE_TABLE_EMPRESA="CREATE TABLE "+ TABLE_EMPRESA +" (" +
            "id VARCHAR (50) NOT NULL PRIMARY KEY," +
            "nom VARCHAR (50) NOT NULL)";

    //Usuari table create statement
    //

    private static final String CREATE_TABLE_USUARI = "CREATE TABLE "+ TABLE_USUARI +" (" +
            "id_usuari VARCHAR(20) PRIMARY KEY," +
            "nom VARCHAR (50) NOT NULL," +
            "email VARCHAR (50) NOT NULL," +
            "rol INTEGER NOT NULL," +
            "pwd VARCHAR (20) NOT NULL," +
            "id_usuari_empresa VARCHAR (50)," +
            "FOREIGN KEY (id_usuari_empresa) REFERENCES empresa (id_empresa))";

    //detalls table create statement
    private static final String CREATE_TABLE_DETALLS = "CREATE TABLE "+ TABLE_DETALLS + " (" +
            "id_detalls VARCHAR(20)," +
            "nom VARCHAR (50))";

    //Dispositiu table create statement
    private static final String CREATE_TABLE_DISPOSITIU = "CREATE TABLE "+ TABLE_DISPOSITIU +" (" +
            "id_dispositiu VARCHAR(20) NOT NULL PRIMARY KEY," +
            "nom VARCHAR (50) NOT NULL," +
            "flota VARCHAR(50) NOT NULL," +
            "longitud DOUBLE," +
            "latitud DOUBLE," +
            "vehicle VARCHAR(50)," +
            "carrega VARCHAR(50),"
            +"id_dispositiu_empresa VARCHAR(20)," +
            "id_dispositiu_usuari VARCHAR(20)," +
            "FOREIGN KEY (id_dispositiu_empresa) REFERENCES empresa(id_empresa)," +
            "FOREIGN KEY (id_dispositiu_usuari) REFERENCES usuari (id_usuari))";



    public DataBaseHelper(Context context)  {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Mètode per crear les taules de la bdd si és necessari
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        //creating required tables
        db.execSQL(CREATE_TABLE_EMPRESA);
        db.execSQL(CREATE_TABLE_USUARI);
        db.execSQL(CREATE_TABLE_DETALLS);
        db.execSQL(CREATE_TABLE_DISPOSITIU);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_EMPRESA);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USUARI);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DETALLS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DISPOSITIU);
        onCreate(db);
    }
}