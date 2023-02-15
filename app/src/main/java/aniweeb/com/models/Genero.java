package aniweeb.com.models;

/**
 * Created by Cristian Mármol cristian.marmol@occamcomunicacion.com on 15/02/2023.
 */
public class Genero {
    int id;
    String name, genero;

    public Genero(int id, String name, String genero) {
        this.id = id;
        this.name = name;
        this.genero = genero;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    @Override
    public String toString() {
        return name;
    }
}
