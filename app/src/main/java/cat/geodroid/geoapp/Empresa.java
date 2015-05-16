package cat.geodroid.geoapp;

/**
 * Created by Victor LLucia , Raul Ortega on 31/3/15.
 */
public class Empresa {
    public int id;
    public String nom;
    //TODO generate atributes, getters and setters

    public Empresa(){}

    public Empresa(int id, String nom){
        this.id = id;
        this.nom = nom;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getNom() {
        return nom;
    }
}