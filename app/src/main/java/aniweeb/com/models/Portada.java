package aniweeb.com.models;

public class Portada {
    private int id, viewers;
    private double puntuacion;
    private String titulo, categorias;

    public Portada(int id, int viewers, double puntuacion, String titulo, String categorias) {
        this.id = id;
        this.viewers = viewers;
        this.puntuacion = puntuacion;
        this.titulo = titulo;
        this.categorias = categorias;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getViewers() {
        return viewers;
    }

    public void setViewers(int viewers) {
        this.viewers = viewers;
    }

    public double getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(double puntuacion) {
        this.puntuacion = puntuacion;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getCategorias() {
        return categorias;
    }

    public void setCategorias(String categorias) {
        this.categorias = categorias;
    }
}
