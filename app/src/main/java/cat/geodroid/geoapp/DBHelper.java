package cat.geodroid.geoapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper{

    public static final String TABLE_NAME = "locations";
    public static final String ID = "loc_id";
    public static final String TITLE = "loc_title";
    public static final String LAT = "loc_lat";
    public static final String LNG = "loc_lng";
    public static final String SNIPPET = "loc_snippet";

    private static final int DB_VERSION = 1;

    private static final String DB_NAME = "markerlocations.db";

    private static final String DB_CREATE =
        "create table "+ TABLE_NAME + "("
        + ID + " integer primary key autoincrement, "
        + TITLE + " text, "
        + LAT + " double , "
        + LNG + " double , "
        + SNIPPET + " text); "
    ;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DB_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_NAME);
        onCreate(db);
    }
}
