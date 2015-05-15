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

    public String getPwd() {
        return pwd;
    }

    public int getId_empresa() {
        return id_empresa;
    }

    public String getEmail() {
        return email;
    }

    public void setId (int id){
        this.id = id

        ;    }
    public void setNom(String nom){
        this.nom = nom;
    }
    public void setRol(int rol){
        this.rol = rol;
    }
    public void setPwd (String pwd){
        this.pwd = pwd;
    }
    public void setIdEmpresa(int id_empresa){
        this.id_empresa = id_empresa;
    }
    public void setEmail(String email){
        this.email = email;
    }

    public int getRol() {
        return rol;
    }

    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }
}