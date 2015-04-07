package cat.geodroid.geoapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.List;

public class MarkersDataSource {

    DBHelper dbhelper;
    SQLiteDatabase db;

    String[] cols = {DBHelper.TITLE, DBHelper.LAT, DBHelper.LNG, DBHelper.SNIPPET};

    public MarkersDataSource(Context c) {
        dbhelper = new DBHelper(c);

    }

    public void open() throws SQLException{
         db = dbhelper.getWritableDatabase();
    }

    public void close(){
        db.close();
    }

    public void addMarker(MyMarker m){
        ContentValues v = new ContentValues();

        v.put(DBHelper.TITLE, m.getTitle());
        v.put(DBHelper.LAT, m.getLat());
        v.put(DBHelper.LNG, m.getLng());
        v.put(DBHelper.SNIPPET, m.getSnippet());

        db.insert(DBHelper.TABLE_NAME, null, v);

    }

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

    public void deleteMarker(MyMarker m){
        db.delete(DBHelper.TABLE_NAME, DBHelper.ID + " = '" + m.getId() + "'", null);
    }


    private MyMarker cursorToMarker(Cursor cursor) {
        MyMarker m = new MyMarker();
        m.setTitle(cursor.getString(0)); //L'índex surt de l'array "cols"
        m.setLat(1);
        m.setLng(2);
        m.setSnippet(cursor.getString(3));
        return m;
    }
}
