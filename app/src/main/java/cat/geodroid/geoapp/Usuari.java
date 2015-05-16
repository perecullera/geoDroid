package cat.geodroid.geoapp;

/**
 * Created by  on 31/3/15.
 */
public class Usuari {
    //TODO generate atributes, getters and setters
    int id;
    String nom;
    int rol;
    String pwd;
    int id_empresa;
    String email;

    /**
     * Getter password Usuari
     * @return
     */
    public String getPwd() {
        return pwd;
    }

    /**
     * Getter id_empresa Usuari
     * @return
     */
    public int getId_empresa() {
        return id_empresa;
    }

    /**
     * Getter email Usuari
     * @return
     */
    public String getEmail() {
        return email;
    }

    /**
     * Setter id Usuari
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Setter nom Usuari
     * @param nom
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * Setter rol Usuari
     * @param rol
     */
    public void setRol(int rol) {
        this.rol = rol;
    }

    /**
     * Setter password Usuari
     * @param pwd
     */
    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    /**
     * Setter id_empresa Usuari
     * @param id_empresa
     */
    public void setIdEmpresa(int id_empresa) {
        this.id_empresa = id_empresa;
    }

    /**
     * Setter email Usuari
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Getter rol Usuari
     * @return
     */
    public int getRol() {
        return rol;
    }

    /**
     * Getter id Usuari
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     * Getter nom Usuari
     * @return
     */
    public String getNom() {
        return nom;
    }
}