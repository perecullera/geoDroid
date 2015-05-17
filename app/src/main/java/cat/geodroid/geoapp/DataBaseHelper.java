package cat.geodroid.geoapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Joaquim Guinovart, Raul Ortega, Victor LluciÃ , Ricard Moya  on 31/3/15.
 */
class DataBaseHelper extends SQLiteOpenHelper {
    /**
    * Logcat tag
    */
    private static final String LOG = "Databasehelper";

    /**
    * Database Version
    */
    private static final int DATABASE_VERSION = 1;

    /**
    * Database Name
    */
    private static final String DATABASE_NAME = "geoDroid";

    /**
    * Table Names
    */
    protected static final String TABLE_EMPRESA = "empresa";
    protected static final String TABLE_USUARI = "usuari";
    private static final String TABLE_DETALLS = "detalls";
    public static final String TABLE_DISPOSITIU = "dispositiu";

    /**
    * Table Create Statements
    * Empresa table create statement
    */
    private static final String CREATE_TABLE_EMPRESA =
            "CREATE TABLE "+ TABLE_EMPRESA +" (" +
                    "id_empresa INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "nom VARCHAR (50) NOT NULL)";

    /**
     * Usuari table create statement
     */
    private static final String CREATE_TABLE_USUARI =
            "CREATE TABLE "+ TABLE_USUARI +" (" +
                    "id_usuari INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "nom VARCHAR (50) NOT NULL," +
                    "email VARCHAR (50) NOT NULL," +
                    "rol INTEGER NOT NULL," +
                    "pwd VARCHAR (20) NOT NULL," +
                    "id_usuari_empresa INTEGER," +
                    "FOREIGN KEY (id_usuari_empresa) REFERENCES empresa (id_empresa))";

    /**
     * detalls table create statement
     */
    private static final String CREATE_TABLE_DETALLS =
            "CREATE TABLE "+ TABLE_DETALLS + " (" +
                    "id_detalls INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "nom VARCHAR (50))";

    /**
     * Dispositiu table create statement
     */
    private static final String CREATE_TABLE_DISPOSITIU =
            "CREATE TABLE "+ TABLE_DISPOSITIU +" (" +
                    "id_dispositiu INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "nom VARCHAR (50) NOT NULL," +
                    "flota VARCHAR(50) NOT NULL," +
                    "latitud DOUBLE," +
                    "longitud DOUBLE," +
                    "vehicle VARCHAR(50)," +
                    "carrega VARCHAR(50)," +
                    "id_dispositiu_empresa INTEGER," +
                    "id_dispositiu_usuari INTEGER," +
                    "FOREIGN KEY (id_dispositiu_empresa) REFERENCES empresa(id_empresa)," +
                    "FOREIGN KEY (id_dispositiu_usuari) REFERENCES usuari(id_usuari))";

    /**
     * Sentencia SQL per inserir dades de simulacio DISPOSITIUS
     */
    private static final String DB_PREPOPULATE_DISPOSITIUS =
            "insert into "+ TABLE_DISPOSITIU
                    + " (nom, flota, latitud, longitud, vehicle, carrega, id_dispositiu_empresa, id_dispositiu_usuari) "
                    + "values "
                    + "('dispositiu1', 'flota1', 41.376800, 2.135913, 'vehicle1', '', 1, 2), "
                    + "('dispositiu2', 'flota2', 41.407504, 2.164091, 'vehicle2', '', 1, 2), "
                    + "('dispositiu3', 'flota2', 41.400529, 2.204363, 'vehicle3', '', 1, 2), "
                    + "('dispositiu4', 'flota2', 41.367781, 2.140033, 'vehicle4', '', 1, 2), "
                    + "('dispositiu5', 'flota1', 41.357781, 2.150033, 'vehicle5', '', 1, 2), "
                    + "('dispositiu6', 'flota1', 41.376800, 2.125913, 'vehicle6', '', 2, 1);"
            ;

    /**
     * Sentencia SQL per inserir dades de simulacio EMPRESA
     */
    private static final String DB_PREPOPULATE_EMPRESA =
            "insert into "+ TABLE_EMPRESA
                    + " (nom) "
                    + "values "
                    + "('empresa1'), "
                    + "('empresa2'), "
                    + "('empresa3'), "
                    + "('empresa4'), "
                    + "('empresa5'), "
                    + "('empresa6');"
            ;

    /**
     * Sentencia SQL per inserir dades de simulacio USUARI
     */
    private static final String DB_PREPOPULATE_USUARI =
            "insert into "+ TABLE_USUARI
                    + " (nom, email, rol, pwd, id_usuari_empresa) "
                    + "values "
                    + "('admin', 'email@email.com', 1, 'password', 1), "
                    + "('laia', 'e@ema.com', 1, 'p', 2), "
                    + "('u', 'mail@mail.cat', 1, 'p', 1);"
                    + "('conductor', 'simple@mail.cat',2,'p',2)";
            ;

    public DataBaseHelper(Context context)  {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Metode per crear les taules de la bdd si es necessari
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        //creating required tables
        db.execSQL(CREATE_TABLE_EMPRESA);
        db.execSQL(CREATE_TABLE_USUARI);
        db.execSQL(CREATE_TABLE_DETALLS);
        db.execSQL(CREATE_TABLE_DISPOSITIU);

        db.execSQL(DB_PREPOPULATE_DISPOSITIUS);
        db.execSQL(DB_PREPOPULATE_EMPRESA);
        db.execSQL(DB_PREPOPULATE_USUARI);
    }

    /**
     * Metode per eliminar les taules al actualitzar
     * @param db
     * @param oldVersion
     * @param newVersion
     */
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