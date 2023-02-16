package aniweeb.com.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import aniweeb.com.R;
import aniweeb.com.URLs.URLs;
import aniweeb.com.adapters.PortadaAdapter;
import aniweeb.com.models.Genero;
import aniweeb.com.models.Portada;
import aniweeb.com.models.Season;
import aniweeb.com.models.State;
import aniweeb.com.restapi.RestAPIWebServices;

/**
 * Created by Cristian Mármol cristian.marmol@occamcomunicacion.com on 14/02/2023.
 */
public class CatalogoActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout bt_Filters, lay_filtros, progresBar, bt_filtrar, bt_verMas, bt_verMenos;
    private boolean toggle = false;
    private boolean filtrar = false;
    private RecyclerView recyclerView;

    private EditText ed_search;
    private Spinner sp_genre, sp_state, sp_season;

    private ArrayList<Genero> arrayGenre;
    private ArrayList<Season> arraySeason;
    private ArrayList<State> arrayState;

    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Portada> listPortadas;

    private String estado = "", temporada = "", genero = "";
    private int genre_id, current_page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalogo);

        loadElements();
    }

    private void loadElements() {
        bt_Filters = findViewById(R.id.bt_Filters);
        bt_Filters.setOnClickListener(this);

        bt_filtrar = findViewById(R.id.bt_filtrar);
        bt_filtrar.setOnClickListener(this);

        bt_verMas = findViewById(R.id.bt_verMas);
        bt_verMas.setOnClickListener(this);

        bt_verMenos = findViewById(R.id.bt_verMenos);
        bt_verMenos.setOnClickListener(this);

        lay_filtros = findViewById(R.id.lay_filtros);

        progresBar = findViewById(R.id.progresBar);

        sp_genre = findViewById(R.id.sp_genre);

        sp_state = findViewById(R.id.sp_state);

        sp_season = findViewById(R.id.sp_season);


        ed_search = findViewById(R.id.ed_search);
        ed_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().isEmpty() && editable.toString().length() >= 3) {
                    getAnimeSearch(editable.toString());

                } else {
                    getAnime(current_page);
                }
            }
        });

        recyclerView = findViewById(R.id.recyclerView);

        fillSpinners();
        getGenres();
        getAnime(current_page);
    }

    private void toggleFiltersLayout(boolean toggle) {
        if (toggle) {
            lay_filtros.setVisibility(View.VISIBLE);
        } else {
            lay_filtros.setVisibility(View.GONE);
        }
    }


    private void getAnime(int page) {
        progresBar.setVisibility(View.VISIBLE);

        HashMap<String, Integer> params = new HashMap<>();
        if (genre_id > 0) {
            params.put("genres", genre_id);
        }
        params.put("page", page);


        Log.e("params", params.toString());

        final RestAPIWebServices restAPIWebServices = new RestAPIWebServices(CatalogoActivity.this,URLs.getAnimeJikanList + "?genres=" + genre_id + "&page="+ current_page);
        restAPIWebServices.getResponseApi(new RestAPIWebServices.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                JSONObject jsonObject = null;
                listPortadas = new ArrayList<>();
                try {
                    jsonObject = new JSONObject(result);
                    listPortadas = new ArrayList<>();
                    JSONObject pagination = jsonObject.getJSONObject("pagination");
                    boolean has_next_page = pagination.getBoolean("has_next_page");
                    if (!has_next_page) {
                        bt_verMas.setVisibility(View.GONE);
                    } else {
                        bt_verMas.setVisibility(View.VISIBLE);
                    }
                    int current_page = pagination.getInt("current_page");
                    if (current_page > 1) {
                        bt_verMenos.setVisibility(View.VISIBLE);
                    } else {
                        bt_verMenos.setVisibility(View.GONE);

                    }

                    double score = 0;
                    int scored_by = 0;
                    String season = "";
                    JSONArray data = jsonObject.getJSONArray("data");
                    for (int i = 0; i< data.length(); i++) {
                        JSONObject element = data.getJSONObject(i);

                        int id = element.getInt("mal_id");

                        JSONObject images = element.getJSONObject("images");
                        JSONObject jpg = images.getJSONObject("jpg");
                        String image = jpg.getString("large_image_url");

                        String title = element.getString("title");

                        String status = element.getString("status");

                        if (!element.isNull("score")) {
                            score = element.getDouble("score");
                        }

                        if (!element.isNull("scored_by")) {
                            scored_by = element.getInt("scored_by");
                        }

                        if (!element.isNull("season")) {
                            season = element.getString("season");
                        }

                        listPortadas.add(new Portada(id,scored_by, score, title, image, season, status));

                    }

                        PortadaAdapter portadaAdapter = new PortadaAdapter(listPortadas, CatalogoActivity.this);
                        layoutManager = new GridLayoutManager(CatalogoActivity.this,2);
                        recyclerView.setHasFixedSize(true);

                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.setAdapter(portadaAdapter);

                    if (listPortadas.size() == 0) {
                        Toast.makeText(CatalogoActivity.this, R.string.no_resultados, Toast.LENGTH_SHORT).show();

                    }

                    progresBar.setVisibility(View.GONE);
                    bt_filtrar.setEnabled(true);
                } catch (JSONException e) {
                    e.printStackTrace();
                    bt_filtrar.setEnabled(true);
                    progresBar.setVisibility(View.GONE);
                    Log.e("JSONException", e.toString());
                    Toast.makeText(CatalogoActivity.this, getString(R.string.general_error), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error", error.toString());
                progresBar.setVisibility(View.GONE);
                bt_filtrar.setEnabled(true);
                Toast.makeText(CatalogoActivity.this, getString(R.string.general_error), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void getAnimeSearch(String search) {
        progresBar.setVisibility(View.VISIBLE);
        HashMap<String, String> params = new HashMap<>();
        params.put("q", search);
        params.put("limit", String.valueOf(500));
        params.put("offset", String.valueOf(0));
        params.put("fields", "mean,num_scoring_users");

        RestAPIWebServices restAPIWebServices = new RestAPIWebServices(CatalogoActivity.this, params, URLs.getAnimeList + "?q=" + search + "&limit=10&offset=0&fields=mean,num_scoring_users");
        restAPIWebServices.getResponseWithDataApi(new RestAPIWebServices.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                JSONObject jsonObject = null;
                listPortadas = new ArrayList<>();
                try {
                    jsonObject = new JSONObject(result);
                    listPortadas = new ArrayList<>();

                    //Log.e("jsonObject", jsonObject.toString());
                    JSONArray data = jsonObject.getJSONArray("data");
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject element = data.getJSONObject(i);
                        JSONObject node = element.getJSONObject("node");
                        int id_anime = node.getInt("id");
                        String title = node.getString("title");
                        JSONObject main_picture = node.getJSONObject("main_picture");
                        String url_img_m = main_picture.getString("large");

                        if (!node.isNull("mean")) {
                            int num_scoring_users = node.getInt("num_scoring_users");
                            double mean = node.getDouble("mean");

                            listPortadas.add(new Portada(id_anime, num_scoring_users, mean, title, url_img_m));
                        }else {
                            listPortadas.add(new Portada(id_anime,0,0, title, url_img_m));

                        }
                    }

                    PortadaAdapter portadaAdapter = new PortadaAdapter(listPortadas, CatalogoActivity.this);
                    layoutManager = new GridLayoutManager(CatalogoActivity.this,2);
                    recyclerView.setHasFixedSize(true);

                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(portadaAdapter);

                    progresBar.setVisibility(View.GONE);
                    bt_filtrar.setEnabled(true);
                } catch (JSONException e) {
                    e.printStackTrace();
                    progresBar.setVisibility(View.GONE);
                    bt_filtrar.setEnabled(true);
                    Log.e("error", e.toString());
                    Toast.makeText(CatalogoActivity.this, getString(R.string.general_error), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error", error.toString());
                progresBar.setVisibility(View.GONE);
                bt_filtrar.setEnabled(true);
                Toast.makeText(CatalogoActivity.this, getString(R.string.general_error), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getFilters() {
        Genero genre = (Genero) sp_genre.getSelectedItem();
        genero = genre.getGenero();
        genre_id = genre.getId();

        State state = (State) sp_state.getSelectedItem();
        estado = state.getEstado();

        Season season = (Season) sp_season.getSelectedItem();
        temporada = season.getTemporada();

        filtrar = !genero.isEmpty() || !estado.isEmpty() || !temporada.isEmpty();
        bt_filtrar.setEnabled(false);

        current_page = 1;

        getAnime(current_page);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_Filters:
                toggle = !toggle;
                toggleFiltersLayout(toggle);
                break;

            case R.id.bt_filtrar:
                //recoger filtros
                getFilters();
                break;

            case R.id.bt_verMas:
                //recoger filtros
                current_page += 1;
                getAnime(current_page);
                break;

            case R.id.bt_verMenos:
                //recoger filtros
                current_page -= 1;
                getAnime(current_page);
                break;
        }
    }

    private void getGenres() {
        RestAPIWebServices restAPIWebServices = new RestAPIWebServices(CatalogoActivity.this, URLs.getGenres);
        restAPIWebServices.getResponseApi(new RestAPIWebServices.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(result);
                    arrayGenre = new ArrayList<>();
                    arrayGenre.add(new Genero(1,"Selecciona Genero", ""));

                    JSONArray data = jsonObject.getJSONArray("data");
                    for (int i =0; i < data.length(); i++) {
                        JSONObject element = data.getJSONObject(i);
                        int id = element.getInt("mal_id");
                        String name = element.getString("name");

                        arrayGenre.add(new Genero(id, name, name));
                    }

                    final ArrayAdapter<Genero> adapterSpinnerGenres = new ArrayAdapter<Genero>(CatalogoActivity.this,R.layout.spinner_item_custom,arrayGenre);
                    sp_genre.setAdapter(adapterSpinnerGenres);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("JSONException", e.toString());
                    progresBar.setVisibility(View.GONE);
                    Toast.makeText(CatalogoActivity.this, getString(R.string.general_error), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error", error.toString());
                progresBar.setVisibility(View.GONE);
                Toast.makeText(CatalogoActivity.this, getString(R.string.general_error), Toast.LENGTH_SHORT).show();
            }
        });
    }




    private void fillSpinners() {
        arrayState = new ArrayList<>();
        arraySeason = new ArrayList<>();

        arrayState.add(new State("Selecciona Estado", ""));
        arrayState.add(new State("En emisión", "Currently Airing"));
        arrayState.add(new State("Finalizado", "Finished Airing"));
        //arrayState.add(new State("Próximamente", "not_yet_aired"));
        final ArrayAdapter<State> adapterSpinnerStates = new ArrayAdapter<State>(CatalogoActivity.this,R.layout.spinner_item_custom,arrayState);
        sp_state.setAdapter(adapterSpinnerStates);


        arraySeason.add(new Season("Selecciona Temporada", ""));
        arraySeason.add(new Season("Invierno", "winter"));
        arraySeason.add(new Season("Otoño", "fall"));
        arraySeason.add(new Season("Verano", "summer"));
        arraySeason.add(new Season("Primavera", "spring"));
        final ArrayAdapter<Season> adapterSpinnerSeason = new ArrayAdapter<Season>(CatalogoActivity.this,R.layout.spinner_item_custom,arraySeason);
        sp_season.setAdapter(adapterSpinnerSeason);
    }
}
