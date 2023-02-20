package aniweeb.com.models;

/**
 * Created by Cristian Mármol cristian.marmol@occamcomunicacion.com on 20/02/2023.
 */
public class Days {
    int id;
    String name, day_of_week;

    public Days(int id, String name, String day_of_week) {
        this.id = id;
        this.name = name;
        this.day_of_week = day_of_week;
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

    public String getDay_of_week() {
        return day_of_week;
    }

    public void setDay_of_week(String day_of_week) {
        this.day_of_week = day_of_week;
    }

    @Override
    public String toString() {
        return name;
    }
}
