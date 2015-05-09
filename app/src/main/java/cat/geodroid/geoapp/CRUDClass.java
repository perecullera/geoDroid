package cat.geodroid.geoapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joaquim Guinovart, Raul Ortega on 31/3/15.
 */
public class CRUDClass {
    private static DataBaseHelper DBH;
    private static SQLiteDatabase db;

    public CRUDClass(Context context){
        DBH = new DataBaseHelper(context);

    }
    //TODO retorn dels mètodes i paràmetres


    public void open() throws SQLException{
        db = DBH.getWritableDatabase();
    }


    //--------CREATE---------


    /**
     * Mètode per a introduir una nova empresa a la BDD
     * @param empresa que es vol introduir a la BDD
     * @return retorna cert si s'ha pogut introduir fals en cas contrari
     */
    public boolean createEmpresa(Empresa empresa){
        try {
            ContentValues cv = new ContentValues();
            cv.put("id_empresa","id");
        }catch (Exception ex){

        }
        return false;
    }

    /**
     * Mètode per a introduir un nou usuari a la BDD
     * @param usuari usuari a introduir
     * @return retorna cert si s'ha pogut introduir fals en cas contrari
     */
    public boolean createUsuari(Usuari usuari){
        ContentValues cv = new ContentValues();
        try {
            cv.put("id_usuari",usuari.getId());
            cv.put("nom",usuari.getNom());
            cv.put("email",usuari.getEmail());
            cv.put("rol",usuari.getRol());
            cv.put("pwd",usuari.getPwd());
            cv.put("id_usuari_empresa",usuari.getId_empresa());
            db.insert(DBH.TABLE_USUARI, null, cv);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Mètode per a introduir nous detalls a la BDD
     * @param detalls detalls a introduir a la BDD
     * @return retorna cert si s'ha pogut introduir fals en cas contrari
     */
    public boolean createDetalls(Detalls detalls){
        return false;
    }

    /**
     * Mètode per a introduir un nou dispositiu a la BDD
     * @param dispositiu dispositiu a introduir a la BDD
     * @return retorna cert si s'ha pogut introduir fals en cas contrari
     */
    public boolean createDispositiu(Dispositiu dispositiu){
        try{
            ContentValues cv = new ContentValues();
            cv.put("id_dispositiu",dispositiu.getId());
            cv.put("nom",dispositiu.getNom());
            cv.put("flota",dispositiu.getFlota());
            cv.put("vehicle",dispositiu.getVehicle());
            cv.put("longitud",dispositiu.getLong());
            cv.put("latitud",dispositiu.getLat());
            db.insert(DBH.TABLE_DISPOSITIU, null, cv);
            return true;
        }catch(Exception ex){
            System.out.print(ex);
            return false;
        }

    }



    //-------------READ-------------


    /**
     * Mètode per a obtindre una empresa de la bDD donat el seu identificador
     * @param id identificador de l'empresa a retornar
     * @return retorna l'empresa en cas que existeixi, null en cas q no existeixi
     */
    public Empresa getEmpresa(int id){
        Empresa empresa = new Empresa();
        return empresa;
    }



    /**
     * Mètode per a recuperar totes les empreses emmagatzemades a la BDD
     * @return llista amb totes les empreses de la BDD
     */
    public List <Empresa> getEmpreses(){
        List <Empresa> llistaEmpreses = new ArrayList<Empresa>();
        return llistaEmpreses;
    }

    /**
     * Mètode que retorna un usari dónat el seu identificador
     * @param id identificador de l'usuari a buscar
     * @return retorna usuari en cas q existeixi, null en cas q no existeixi
     */
    public Usuari getUsuari(int id){
        Usuari usuari = new Usuari();
        return usuari;

    }

    /**
     *
     * @param nom nom de l'usuari
     * @param pwd password de l'usuari
     * @return 0 no hi ha cap usuari amb aquest password 1 és usuari administrador 2 és usuari conductor
     */
    public Usuari loguejaUsuari(String nom, String pwd){
        Usuari usuari = new Usuari();
        String whereClause ="nom = ? AND pwd = ?";
        String[] whereArgs = {nom,pwd};
        Cursor cursor = db.query(DataBaseHelper.TABLE_USUARI, null, whereClause, whereArgs, null, null, null);
        cursor.moveToFirst();
        if(cursor.getCount()==0){
            return null;
        }else{
            usuari = cursorToUsuari(cursor);
        }
        return usuari;

    }


    /**
     * Mètode que retorna tots els usuaris de la BDD
     * @return llista amb tots els usuaris de la BDD
     */
    public List<Usuari> getUsuaris(){
        List <Usuari> llistaUsuaris = new ArrayList<Usuari>();
        return llistaUsuaris;
    }
    //retorna els detalls d'un dispositiu
    public void getDetalls(){

    }
    //retorna un dispositiu
    public Dispositiu getDispositiu(String id_dispositiu){
        Dispositiu aux;

        String whereClause ="id_dipositiu = ? ";
        String[] whereArgs = {id_dispositiu};
        Cursor cursor = db.query(DataBaseHelper.TABLE_DISPOSITIU, null, whereClause, whereArgs, null, null, null);
        cursor.moveToFirst();
        if(cursor.getCount()==0){
            aux = new Dispositiu();
            aux.setId("-1");
        }else{
            aux = cursorToDis(cursor);
        }
        return aux;
    }
    //retorna la posició d'un dispositiu
    public void getPosition(){

    }
    public void getPositions(){

    }
    //retorna tots els dispositius de la BDD

    /**
     * Retorna tots els dispositius guardats a la BDD
     * @return llista amb tots els dispositius
     */
    public List<Dispositiu> getDispositius(){
        List<Dispositiu> llistaDispositius = new ArrayList<Dispositiu>();
        Cursor cursor = db.query(DBH.TABLE_DISPOSITIU, null, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Dispositiu m = cursorToDis(cursor);
            llistaDispositius.add(m);
            cursor.moveToNext();
        }
        cursor.close();
        return llistaDispositius;
    }
    public List<Dispositiu> getDispositiusEmpresa(String Empresa){
        List<Dispositiu> llistaDispositius = new ArrayList<Dispositiu>();
        String whereClause="id_dispositiu_empresa = ?";
        String[] whereArgs ={Empresa};
        Cursor cursor = db.query(DBH.TABLE_DISPOSITIU, null, whereClause, whereArgs, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Dispositiu m = cursorToDis(cursor);
            llistaDispositius.add(m);
            cursor.moveToNext();
        }
        cursor.close();
        return llistaDispositius;
    }

    //------------UPDATE-----------

    //update empresa
    public void updateEmpresa(){

    }
    //update usuari
    public void updateUsuari(){

    }
    //update detalls
    public void updateDetalls(){

    }
    //update dispositiu
    public boolean updateDispositiu(Dispositiu dis){
        ContentValues cv = new ContentValues();
        String whereClause = "id_dispositiu = ?";
        String[] wwhereArgs ={dis.getId()};
        cv.put("id_dispositiu",dis.getId());
        cv.put("nom",dis.getNom());
        cv.put("flota",dis.getFlota());
        cv.put("vehicle",dis.getVehicle());
        cv.put("longitud",dis.getLong());
        cv.put("latitud",dis.getLat());
        try {
            db.update(DBH.TABLE_DISPOSITIU, cv,whereClause,wwhereArgs );
            return true;
        }catch(Exception ex){
            System.out.println("Error al actualitzar el dispositiu "+ dis +" "+ ex);
            return false;
        }

    }
    //update de la posicio
    public void updatePosicio(Dispositiu dis){

    }


    //------------DELETE------------

    //delete empresa
    public void deleteEmpresa(){

    }
    public void deleteUsuari(){

    }
    public void deleteDetalls(){

    }
    public void deleteDispositiu(){

    }//delete posció
    public void deletePosicio(){

    }

    //------------CLOSE DB----------

    public void closeDB(){
        if (db != null && db.isOpen()){
            db.close();
        }
    }

    //------------ AUXILIAR-----------------

    /**
     * Mètode auxiliar per a passar el cursor de dispositius a Dispositius
     * @param cursor amb els dispositius retornat per getDispositius
     * @return llista amb els dispositius
     */
    public Dispositiu cursorToDis(Cursor cursor){
        Dispositiu dis = new Dispositiu();
        dis.setId(cursor.getString(0));
        dis.setNom(cursor.getString(1));
        dis.setFlota(cursor.getString(2));
        dis.setVehicle(cursor.getString(5));
        dis.setPosition(cursor.getDouble(3),cursor.getDouble(4));

        return dis;
    }
    private Usuari cursorToUsuari(Cursor cursor) {
        Usuari us = new Usuari();
        us.setId(cursor.getString(0));
        us.setNom(cursor.getString(1));
        us.setEmail(cursor.getString(2));
        us.setRol(cursor.getInt(3));
        us.setPwd(cursor.getString(4));
        us.setIdEmpresa(cursor.getString(5));
        return us;
    }

}
