package aniweeb.com.models;

/**
 * Created by Cristian Mármol cristian.marmol@occamcomunicacion.com on 21/02/2023.
 */
public class Trailer {
    private int id_anime;
    private String titulo, yt_url;


    public Trailer(int id_anime, String titulo, String yt_url) {
        this.id_anime = id_anime;
        this.titulo = titulo;
        this.yt_url = yt_url;
    }

    public int getId_anime() {
        return id_anime;
    }

    public void setId_anime(int id_anime) {
        this.id_anime = id_anime;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getYt_url() {
        return yt_url;
    }

    public void setYt_url(String yt_url) {
        this.yt_url = yt_url;
    }
}
