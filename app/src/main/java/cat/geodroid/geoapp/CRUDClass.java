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

    //TODO retorn dels mÃ¨todes i parÃ metres

    public void open() throws SQLException{
        db = DBH.getWritableDatabase();
    }

    //--------CREATE---------

    /**
     * MÃ¨tode per a introduir una nova empresa a la BDD
     * @param empresa que es vol introduir a la BDD
     * @return retorna cert si s'ha pogut introduir fals en cas contrari
     */
    public boolean createEmpresa(Empresa empresa){
        try {
            ContentValues cv = new ContentValues();
            cv.put("id_empresa","id");
        } catch (Exception ex) {

        }
        return false;
    }

    /**
     * MÃ¨tode per a introduir un nou usuari a la BDD
     * @param usuari usuari a introduir
     * @return retorna cert si s'ha pogut introduir fals en cas contrari
     */
    public boolean createUsuari(Usuari usuari){
        ContentValues cv = new ContentValues();
        try {
            //cv.put("id_usuari", usuari.getId());
            cv.put("nom", usuari.getNom());
            cv.put("email", usuari.getEmail());
            cv.put("rol", usuari.getRol());
            cv.put("pwd", usuari.getPwd());
            cv.put("id_usuari_empresa", usuari.getId_empresa());
            db.insert(DBH.TABLE_USUARI, null, cv);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * MÃ¨tode per a introduir nous detalls a la BDD
     * @param detalls detalls a introduir a la BDD
     * @return retorna cert si s'ha pogut introduir fals en cas contrari
     */
    public boolean createDetalls(Detalls detalls){
        return false;
    }

    /**
     * MÃ¨tode per a introduir un nou dispositiu a la BDD
     * @param dispositiu dispositiu a introduir a la BDD
     * @return retorna cert si s'ha pogut introduir fals en cas contrari
     */
    public boolean createDispositiu(Dispositiu dispositiu){
        try{
            ContentValues cv = new ContentValues();
            //cv.put("id_dispositiu", dispositiu.getId());
            cv.put("nom", dispositiu.getNom());
            cv.put("flota", dispositiu.getFlota());
            cv.put("vehicle", dispositiu.getVehicle());
            cv.put("latitud", dispositiu.getLat());
            cv.put("longitud", dispositiu.getLong());
            cv.put("id_dispositiu_empresa", dispositiu.getId_empresa());
            cv.put("id_dispositiu_usuari", dispositiu.getId_usuari());
            db.insert(DBH.TABLE_DISPOSITIU, null, cv);
            return true;
        } catch(Exception ex) {
            System.out.print(ex);
            return false;
        }
    }

    //-------------READ-------------

    /**
     * MÃ¨tode per a obtindre una empresa de la bDD donat el seu identificador
     * @param id_empresa identificador de l'empresa a retornar
     * @return retorna l'empresa en cas que existeixi, null en cas q no existeixi
     */
    public Empresa getEmpresa(int id_empresa){
        Empresa emp;

        String whereClause = "id_empresa = ? ";
        String[] whereArgs = {String.valueOf(id_empresa)};
        Cursor cursor = db.query(DataBaseHelper.TABLE_EMPRESA, null, whereClause, whereArgs, null, null, null);
        cursor.moveToFirst();
        if (cursor.getCount() == 0) {
            emp = new Empresa();
            emp.setId(-1);
        } else {
            emp = cursorToEmp(cursor);
        }
        return emp;
    }

    /**
     * MÃ¨tode per a recuperar totes les empreses emmagatzemades a la BDD
     * @return llista amb totes les empreses de la BDD
     */
    public List <Empresa> getEmpreses(){
        List <Empresa> llistaEmpreses = new ArrayList<Empresa>();
        return llistaEmpreses;
    }

    /**
     * MÃ¨tode que retorna un usari dÃ³nat el seu identificador
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
     * @return 0 no hi ha cap usuari amb aquest password 1 Ã©s usuari administrador 2 Ã©s usuari conductor
     */
    public Usuari loguejaUsuari(String nom, String pwd){
        Usuari usuari = new Usuari();
        String whereClause ="nom = ? AND pwd = ?";
        String[] whereArgs = {nom, pwd};
        Cursor cursor = db.query(DataBaseHelper.TABLE_USUARI, null, whereClause, whereArgs, null, null, null);
        cursor.moveToFirst();
        if (cursor.getCount()==0) {
            return null;
        } else {
            usuari = cursorToUsuari(cursor);
        }
        return usuari;
    }

    /**
     * Metode que retorna tots els usuaris de la BDD
     * @return llista amb tots els usuaris de la BDD
     */
    public List<Usuari> getUsuaris(){
        List <Usuari> llistaUsuaris = new ArrayList<Usuari>();
        return llistaUsuaris;
    }

    //retorna els detalls d'un dispositiu
    public void getDetalls(){

    }

    /**
     * Retorna un dispositiu
     * @param id_dispositiu
     * @return aux Dispositiu
     */
    public Dispositiu getDispositiu(int id_dispositiu){
        Dispositiu aux;

        String whereClause = "id_dispositiu = ? ";
        String[] whereArgs = {String.valueOf(id_dispositiu)};
        Cursor cursor = db.query(DataBaseHelper.TABLE_DISPOSITIU, null, whereClause, whereArgs, null, null, null);
        cursor.moveToFirst();
        if (cursor.getCount() == 0) {
            aux = new Dispositiu();
            aux.setId(-1);
        } else {
            aux = cursorToDis(cursor);
        }
        return aux;
    }
    //retorna la posiciÃ³ d'un dispositiu
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

    /**
     * Retorna un llistat de dispositius filtrats per empresa
     * @param empresa id de l'empresa
     * @return List<Dispositius>
     */
    public List<Dispositiu> getDispositiusEmpresa(int empresa){
        List<Dispositiu> llistaDispositius = new ArrayList<Dispositiu>();
        String whereClause="id_dispositiu_empresa = ?";
        String[] whereArgs ={String.valueOf(empresa)};
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

    /**
     * Actualitzem el dispositiu segons Dispositiu
     */

    public boolean updateDispositiu(Dispositiu dis){
        ContentValues cv = new ContentValues();
        String whereClause = "id_dispositiu = ?";
        String[] whereArgs ={String.valueOf(dis.getId())};
        //cv.put("id_dispositiu", dis.getId());
        cv.put("nom", dis.getNom());
        cv.put("flota", dis.getFlota());
        cv.put("vehicle", dis.getVehicle());
        cv.put("latitud", dis.getLat());
        cv.put("longitud", dis.getLong());
        //cv.put("id_dispositiu_empresa", dis.getId_empresa());
        try {
            db.update(DBH.TABLE_DISPOSITIU, cv, whereClause, whereArgs );
            return true;
        }catch(Exception ex){
            System.out.println("Error al actualitzar el dispositiu "+ dis +" "+ ex);
            return false;
        }

    }
    //update de la posicio
    public void updatePosicio(Dispositiu dis){}

    //------------DELETE------------

    //delete empresa
    public void deleteEmpresa(){

    }
    public void deleteUsuari(){

    }
    public void deleteDetalls(){

    }
    public void deleteDispositiu(){

    }//delete posciÃ³
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
     * Metode auxiliar per a passar el cursor de dispositiu a Dispositiu
     * @param cursor amb el dispositiu a retornar
     * @return dispositiu
     */
    public Dispositiu cursorToDis(Cursor cursor){
        Dispositiu dis = new Dispositiu();

        dis.setId(cursor.getInt(0));
        dis.setNom(cursor.getString(1));
        dis.setFlota(cursor.getString(2));
        dis.setVehicle(cursor.getString(5));
        dis.setPosition(cursor.getDouble(3), cursor.getDouble(4));

        return dis;
    }

    /**
     * Metode auxiliar per a passar el cursor d'usuari a Usuari
     * @param cursor amb l'usuari a retornar
     * @return usuari
     */
    private Usuari cursorToUsuari(Cursor cursor) {
        Usuari us = new Usuari();

        us.setId(cursor.getInt(0));
        us.setNom(cursor.getString(1));
        us.setEmail(cursor.getString(2));
        us.setRol(cursor.getInt(3));
        us.setPwd(cursor.getString(4));
        us.setIdEmpresa(cursor.getInt(5));

        return us;
    }

    /**
     * Metode auxiliar per a passar el cursor d'empresa a Empresa
     * @param cursor amb l'empresa a retornar
     * @return empresa
     */
    private Empresa cursorToEmp(Cursor cursor) {
        Empresa emp = new Empresa();

        emp.setId(cursor.getInt(0));
        emp.setNom(cursor.getString(1));

        return emp;
    }
}
