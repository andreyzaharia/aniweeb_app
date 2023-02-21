package aniweeb.com.ui.slideshow;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import aniweeb.com.R;
import aniweeb.com.URLs.URLs;
import aniweeb.com.activities.CatalogoActivity;
import aniweeb.com.adapters.PortadaAdapter;
import aniweeb.com.databinding.FragmentSlideshowBinding;
import aniweeb.com.models.Portada;
import aniweeb.com.restapi.RestAPIWebServices;

public class SlideshowFragment extends Fragment implements View.OnClickListener{

    private FragmentSlideshowBinding binding;
    private LinearLayout bt_finalizado, bt_emision, bt_prox, bt_verMas;
    private TextView txt_finalizado, txt_emision, txt_prox;
    private EditText ed_search;

    private RecyclerView recyclerView;
    private ArrayList<Portada> listPortadas;
    private RecyclerView.LayoutManager layoutManager;

    private View progressbar;
    private AdView mAdView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SlideshowViewModel slideshowViewModel =
                new ViewModelProvider(this).get(SlideshowViewModel.class);

        binding = FragmentSlideshowBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        loadElements(root);
        return root;
    }

    private void loadElements(View root) {

        mAdView = root.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        bt_finalizado = root.findViewById(R.id.lay_finalizado);
        bt_finalizado.setOnClickListener(this);

        bt_emision = root.findViewById(R.id.lay_emision);
        bt_emision.setOnClickListener(this);

        bt_prox = root.findViewById(R.id.lay_prox);
        bt_prox.setOnClickListener(this);

        bt_verMas = root.findViewById(R.id.bt_verMas);
        bt_verMas.setOnClickListener(this);

        txt_finalizado = root.findViewById(R.id.txt_finalizado);
        txt_emision = root.findViewById(R.id.txt_emision);
        txt_prox = root.findViewById(R.id.txt_prox);

        recyclerView = root.findViewById(R.id.grid_animes);

        progressbar = root.findViewById(R.id.spin_kit);

        ed_search = root.findViewById(R.id.ed_search);
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
                    getAnimeRanking("airing",2);

                }
            }
        });

        getAnimeRanking("airing",2);
    }

    private void getAnimeRanking(String ranking_type, int option) {
        progressbar.setVisibility(View.VISIBLE);
        changeOption(option);

        HashMap<String, String> params = new HashMap<>();
        params.put("ranking_type", ranking_type);
        params.put("limit", String.valueOf(40));

        RestAPIWebServices restAPIWebServices = new RestAPIWebServices(getContext(), params, URLs.getRankingAnimeList + "?ranking_type=" + ranking_type + "&limit=40&offset=0&fields=mean,num_scoring_users");
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

                    PortadaAdapter portadaAdapter = new PortadaAdapter(listPortadas, getContext());
                    layoutManager = new GridLayoutManager(getContext(),2);
                    recyclerView.setHasFixedSize(true);

                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(portadaAdapter);

                    progressbar.setVisibility(View.GONE);
                    bt_verMas.setVisibility(View.VISIBLE);
                } catch (JSONException e) {
                    e.printStackTrace();
                    progressbar.setVisibility(View.GONE);
                    bt_verMas.setVisibility(View.GONE);
                    Log.e("error", e.toString());
                    Toast.makeText(getContext(),getContext().getString(R.string.general_error), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error", error.toString());
                progressbar.setVisibility(View.GONE);
                bt_verMas.setVisibility(View.GONE);

                Toast.makeText(getContext(),getContext().getString(R.string.general_error), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lay_finalizado:
                getAnimeRanking("tv", 1);
                break;

            case R.id.lay_emision:
                getAnimeRanking("airing", 2);
                break;

            case R.id.lay_prox:
                getAnimeRanking("upcoming", 3);
                break;

            case R.id.bt_verMas:
                Intent i = new Intent(getContext(), CatalogoActivity.class);
                startActivity(i);
                break;
        }
    }


    private void changeOption(int option) {

        if (option == 1) {
            bt_finalizado.setBackground(getContext().getResources().getDrawable(R.drawable.bg_layout_purple));
            //txt_finalizado.setTextColor(getContext().getResources().getColor(R.color.morado_oscuro));
            txt_finalizado.setTextSize(14);
            txt_finalizado.setTypeface(null, Typeface.BOLD);

            bt_emision.setBackground(getContext().getResources().getDrawable(R.drawable.bg_no_selected));
            //txt_emision.setTextColor(getContext().getResources().getColor(R.color.black));
            txt_emision.setTextSize(12);
            txt_emision.setTypeface(null, Typeface.NORMAL);

            bt_prox.setBackground(getContext().getResources().getDrawable(R.drawable.bg_no_selected));
            //txt_prox.setTextColor(getContext().getResources().getColor(R.color.black));
            txt_prox.setTextSize(12);
            txt_prox.setTypeface(null, Typeface.NORMAL);
        } else if (option == 2) {

            bt_emision.setBackground(getContext().getResources().getDrawable(R.drawable.bg_layout_purple));
            //txt_emision.setTextColor(getContext().getResources().getColor(R.color.white));
            txt_emision.setTextSize(14);
            txt_emision.setTypeface(null, Typeface.BOLD);

            bt_finalizado.setBackground(getContext().getResources().getDrawable(R.drawable.bg_no_selected));
            //txt_finalizado.setTextColor(getContext().getResources().getColor(R.color.black));
            txt_finalizado.setTextSize(12);
            txt_finalizado.setTypeface(null, Typeface.NORMAL);

            bt_prox.setBackground(getContext().getResources().getDrawable(R.drawable.bg_no_selected));
            //txt_prox.setTextColor(getContext().getResources().getColor(R.color.black));
            txt_prox.setTextSize(12);
            txt_prox.setTypeface(null, Typeface.NORMAL);
        } else if (option == 3) {

            bt_prox.setBackground(getContext().getResources().getDrawable(R.drawable.bg_layout_purple));
            //txt_prox.setTextColor(getContext().getResources().getColor(R.color.mor));
            txt_prox.setTextSize(14);
            txt_prox.setTypeface(null, Typeface.BOLD);

            bt_finalizado.setBackground(getContext().getResources().getDrawable(R.drawable.bg_no_selected));
            //txt_finalizado.setTextColor(getContext().getResources().getColor(R.color.black));
            txt_finalizado.setTextSize(12);
            txt_finalizado.setTypeface(null, Typeface.NORMAL);

            bt_emision.setBackground(getContext().getResources().getDrawable(R.drawable.bg_no_selected));
            //txt_emision.setTextColor(getContext().getResources().getColor(R.color.black));
            txt_emision.setTextSize(12);
            txt_emision.setTypeface(null, Typeface.NORMAL);
        }
    }


    private void getAnimeSearch(String search) {
        progressbar.setVisibility(View.VISIBLE);

        HashMap<String, String> params = new HashMap<>();
        params.put("q", search);
        params.put("limit", String.valueOf(10));
        params.put("offset", String.valueOf(0));
        params.put("fields", "mean,num_scoring_users");

        RestAPIWebServices restAPIWebServices = new RestAPIWebServices(getContext(), params, URLs.getAnimeList + "?q=" + search + "&limit=10&offset=0&fields=mean,num_scoring_users");
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

                    PortadaAdapter portadaAdapter = new PortadaAdapter(listPortadas, getContext());
                    layoutManager = new GridLayoutManager(getContext(),2);
                    recyclerView.setHasFixedSize(true);

                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(portadaAdapter);

                    progressbar.setVisibility(View.GONE);
                    bt_verMas.setVisibility(View.VISIBLE);
                } catch (JSONException e) {
                    e.printStackTrace();
                    progressbar.setVisibility(View.GONE);
                    bt_verMas.setVisibility(View.GONE);

                    Log.e("error", e.toString());
                    Toast.makeText(getContext(),getContext().getString(R.string.general_error), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error", error.toString());
                progressbar.setVisibility(View.GONE);
                bt_verMas.setVisibility(View.GONE);

                Toast.makeText(getContext(),getContext().getString(R.string.general_error), Toast.LENGTH_SHORT).show();
            }
        });
    }
}