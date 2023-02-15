package aniweeb.com.models;

import java.util.ArrayList;

public class Portada {
    private int id, viewers;
    private double puntuacion;
    private String titulo, categorias, img_url, season, status;

    public Portada() {

    }

    public Portada(int id, int viewers, double puntuacion, String titulo, String img_url) {
        this.id = id;
        this.viewers = viewers;
        this.puntuacion = puntuacion;
        this.titulo = titulo;
        this.img_url = img_url;
    }

    public Portada(int id, int viewers, double puntuacion, String titulo, String img_url, String season, String status) {
        this.id = id;
        this.viewers = viewers;
        this.puntuacion = puntuacion;
        this.titulo = titulo;
        this.img_url = img_url;
        this.season = season;
        this.status = status;


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

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
