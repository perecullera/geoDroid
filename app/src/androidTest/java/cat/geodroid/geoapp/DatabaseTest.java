package cat.geodroid.geoapp;

import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Joaquim Guinovart on 7/4/15.
 */
public class DatabaseTest extends AndroidTestCase {
    /*private DataBaseHelper helper;
    RenamingDelegatingContext context;
    CRUDClass crud;
    SQLiteDatabase db;
    Dispositiu dispositiu = new Dispositiu("id1","dispositiu1","flota1","vehicle1");
    Dispositiu dispositiu2 = new Dispositiu("id2","dispositiu2","flota1","vehicle2");

    @Override
    public void setUp(){
        context
                = new RenamingDelegatingContext(getContext(), "test_");
        crud = new CRUDClass(context);
        helper = new DataBaseHelper(context);
        db = helper.getWritableDatabase();

    }*/

    /**
     * Mètode per provar la creació i la conexió a la BDD
     */
    /*public void testCreateDB(){
        db = helper.getWritableDatabase();
        assertTrue(db.isOpen());
        db.close();

    }*/


    /**
     * Mètode per provar l'addicció de dispositius a la BDD
     * Insereix el dispositiu dispositiu, i comprova que el mètode createDispositiu retorni true
     */
    /*public void testAddDispositiu(){
        try {
            crud.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        assertEquals(true,crud.createDispositiu(dispositiu));
        //db.close();
    }*/

    /**
     * Mètode per comprovar l'addicció de 2 dispositius i l'obtenció dels mateixos
     * insereix el dispositiu i el dispositiu2
     * i comprova que retorni dos dispositius amb el mateix id que els introduits
     * @throws SQLException
     */
    /*public void testGetDispositius() throws SQLException {
        crud.open();
        crud.createDispositiu(dispositiu);
        crud.createDispositiu(dispositiu2);
        List <Dispositiu> llista ;
        llista = crud.getDispositius();

        assertEquals(dispositiu.getId(),llista.get(0).getId() );
        assertEquals(dispositiu2.getId(),llista.get(1).getId() );
    }
    @Override
    public void tearDown() throws Exception{
        helper.close();
        super.tearDown();
    }*/
}
