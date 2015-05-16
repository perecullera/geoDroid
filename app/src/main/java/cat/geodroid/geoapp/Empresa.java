package cat.geodroid.geoapp;

/**
 * Created by Victor LLucia , Raul Ortega on 31/3/15.
 */
public class Empresa {
    public int id;
    public String nom;
    //TODO generate atributes, getters and setters

    /**
     * Cosntructor d'Empresa buit
     */
    public Empresa(){}

    /**
     * Constructor d'Empresa amb elements
     * @param id
     * @param nom
     */
    public Empresa(int id, String nom){
        this.id = id;
        this.nom = nom;
    }

    /**
     * Setter id Empresa
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Getter id Empresa
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     * Setter nom Empresa
     * @param nom
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * Getter nom Empresa
     * @return
     */
    public String getNom() {
        return nom;
    }
}