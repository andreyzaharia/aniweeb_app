package aniweeb.com.models;

/**
 * Created by Cristian Mármol cristian.marmol@occamcomunicacion.com on 20/02/2023.
 */
public class Episode {
    private int id;
    private double score;
    private String titulo, img_url, type, duration, broadcast;

    public Episode(int id, double score, String titulo, String img_url, String type, String duration, String broadcast) {
        this.id = id;
        this.score = score;
        this.titulo = titulo;
        this.img_url = img_url;
        this.type = type;
        this.duration = duration;
        this.broadcast = broadcast;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getBroadcast() {
        return broadcast;
    }

    public void setBroadcast(String broadcast) {
        this.broadcast = broadcast;
    }
}
