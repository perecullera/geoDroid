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

    private CRUDClass crud;

    Dispositiu dispositiu = new Dispositiu("prova1","flota1","vehicle1",1);
    Dispositiu dispositiu2 = new Dispositiu("prova2","flota1","vehicle2",1);
    Dispositiu dispositiu3 = new Dispositiu("prova3","flota1","vehicle2",2);

    @Override
    public void setUp() throws Exception {
        super.setUp();
        RenamingDelegatingContext context = new RenamingDelegatingContext(getContext(), "test_");
        crud = new CRUDClass(context);
        crud.open();
    }

    /**
     * Mètode per provar la creació i la conexió a la BDD
     */
    public void testCreateDB() throws SQLException {
        crud.open();
        assertTrue(crud.isOpen());
        //crud.close();
    }


    /**
     * Mètode per provar l'addicció de dispositius a la BDD
     * Insereix el dispositiu dispositiu, i comprova que el mètode createDispositiu retorni true
     */
    public void testAddDispositiu(){
        try {
            crud.open();
            assertEquals(true, crud.createDispositiu(dispositiu));
            //crud.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Mètode per comprovar l'addicció de 2 dispositius i l'obtenció dels mateixos
     * insereix el dispositiu i el dispositiu2
     * i comprova que retorni dos dispositius amb el mateix id que els introduits
     */
    public void testGetDispositius(){

        crud.createDispositiu(dispositiu);
        crud.createDispositiu(dispositiu2);
        List<Dispositiu> llista ;
        llista = crud.getDispositius();
        System.out.print("dispositius: " + llista.size());

        assertEquals(1,llista.get(0).getId() );
        assertEquals(2,llista.get(1).getId() );
    }
    /**
     * Mètode per comprovar l'addicció l'obtenció de dispositius a partir de la id d'empresa
     * insereix el dispositiu, el dispositiu2 i el dispositiu3
     * i comprova que retorni els dispositius
     */
    public void testGetDispositiusPerempresa() {

        List<Dispositiu> llista1 ;
        List<Dispositiu> llista2 ;
        List<Dispositiu> llista3 ;
        llista1 = crud.getDispositiusEmpresa(1);
        llista2 = crud.getDispositiusEmpresa(2);
        llista3 = crud.getDispositiusEmpresa(3);

        //resultat per id empresa = 1 ha de ser 5, afegits al prepopulates
        assertEquals(5,llista1.size());
        // resultat per id empresa = 2 ha de ser 2
        assertEquals(1,llista2.size());
        // resultat per id empresa = 3 ha de ser 0
        assertEquals(0,llista3.size());

    }
    /**
     * Mètode per testejar l'update dispositius i l'obtenció dels mateixos
     * insereix el dispositiu i el dispositiu2
     * i comprova que retorni dos dispositius amb el mateix id que els introduits
     * */
    public void testUpdateDispositiu(){

        //provem l'update del nom
        Dispositiu disPrevi = crud.getDispositiu(1);
        disPrevi.setNom("nom1");
        crud.updateDispositiu(disPrevi);
        Dispositiu disDespres = crud.getDispositiu(1);

        assertEquals("nom1",disDespres.getNom());

        //provem l'update de la flota
        disPrevi = crud.getDispositiu(2);
        disPrevi.setFlota("flota5");
        crud.updateDispositiu(disPrevi);
        disDespres = crud.getDispositiu(2);

        assertEquals("flota5",disDespres.getFlota());

        //provem l'update del vehicle
        disPrevi = crud.getDispositiu(3);
        disPrevi.setVehicle("cotxe");
        crud.updateDispositiu(disPrevi);
        disDespres = crud.getDispositiu(3);

        assertEquals("cotxe",disDespres.getVehicle());

    }

    public void testLoginCorrecte(){
        //test login per usuari admin pwd password
        Usuari usuari = crud.loguejaUsuari("admin","password");

        assertEquals("admin",usuari.getNom());

        //test loguin per a usuari conductor password p
        usuari = crud.loguejaUsuari("conductor","p");

        assertEquals("conductor",usuari.getNom());
    }

    public void testLoginIncorrecte(){
        //test loguin per a usuari incorrecte
        Usuari usuari = crud.loguejaUsuari("usuari1","password");

        assertEquals(null,usuari);

        //test loguin per a password incorrecte
        usuari = crud.loguejaUsuari("admin","pass");

        assertEquals(null,usuari);

    }


    @Override
    public void tearDown() throws Exception{
        crud.close();
        super.tearDown();
    }
}
