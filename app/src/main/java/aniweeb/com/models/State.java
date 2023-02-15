package aniweeb.com.models;

/**
 * Created by Cristian Mármol cristian.marmol@occamcomunicacion.com on 15/02/2023.
 */
public class State {
    String name, estado;

    public State(String name, String estado) {
        this.name = name;
        this.estado = estado;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return name;
    }
}
