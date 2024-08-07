package aniweeb.com.ui.gallery;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

import aniweeb.com.R;
import aniweeb.com.URLs.URLs;
import aniweeb.com.adapters.EpisodeAdapter;
import aniweeb.com.adapters.PortadaAdapter;
import aniweeb.com.databinding.FragmentGalleryBinding;
import aniweeb.com.models.Days;
import aniweeb.com.models.Episode;
import aniweeb.com.models.Portada;
import aniweeb.com.restapi.RestAPIWebServices;

public class GalleryFragment extends Fragment implements View.OnClickListener {

    private FragmentGalleryBinding binding;
    private LinearLayout bt_verMas, bt_verMenos, progressbar, lay_paginacion;
    private Context mContext;
    private Spinner sp_day;
    private ArrayList<Days> arrayDay;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Episode> listEpisodes;
    private int current_page = 1;

    private String date = "";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        GalleryViewModel galleryViewModel =
                new ViewModelProvider(this).get(GalleryViewModel.class);

        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        mContext = getContext();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            date = LocalDate.now().getDayOfWeek().name().toLowerCase();

            Log.e("date", date);
        }

        loadElements(root);

        return root;
    }

    private void loadElements(View root) {

        sp_day = root.findViewById(R.id.sp_day);

        arrayDay = new ArrayList<>();
        arrayDay.add(new Days(1, "Monday", "monday"));
        arrayDay.add(new Days(2, "Tuesday", "tuesday"));
        arrayDay.add(new Days(3, "Wednesday", "wednesday"));
        arrayDay.add(new Days(4, "Thursday", "thursday"));
        arrayDay.add(new Days(5, "Friday", "friday"));
        arrayDay.add(new Days(6, "Saturday", "saturday"));
        arrayDay.add(new Days(7, "Sunday", "sunday"));

        final ArrayAdapter<Days> adapterSpinnerGenres = new ArrayAdapter<Days>(mContext, R.layout.spinner_item_custom, arrayDay);
        sp_day.setAdapter(adapterSpinnerGenres);

        sp_day.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Days day = arrayDay.get(i);
                date = day.getDay_of_week();

                getEpisodes(date, current_page);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
/*
        for (Days day : arrayDay) {
            if (day.getDay_of_week().equalsIgnoreCase(date)) {
                sp_day.setSelection(day.getId() - 1);

            }
        }
        */
        for (int i = 0; i < arrayDay.size(); i++) {
            if (arrayDay.get(i).getDay_of_week().equalsIgnoreCase(date)) {
                sp_day.setSelection(arrayDay.get(i).getId() - 1);
            }
        }


        recyclerView = root.findViewById(R.id.recyclerView);

        bt_verMas = root.findViewById(R.id.bt_verMas);
        bt_verMas.setOnClickListener(this);

        bt_verMenos = root.findViewById(R.id.bt_verMenos);
        bt_verMenos.setOnClickListener(this);

        progressbar = root.findViewById(R.id.progresBar);
        lay_paginacion = root.findViewById(R.id.lay_paginacion);

        getEpisodes(date, current_page);
    }


    private void getEpisodes(String day, int page) {
        progressbar.setVisibility(View.VISIBLE);

        HashMap<String, String> params = new HashMap<>();
        params.put("filter", day);
        params.put("kids", "false");
        params.put("page", String.valueOf(page));
        //params.put("limit", String.valueOf(40));

        Log.e("params", params.toString());

        RestAPIWebServices restAPIWebServices = new RestAPIWebServices(getContext(), params, URLs.getEpisodes + "?filter=" + day + "&kids=false&page=" + String.valueOf(page));
        restAPIWebServices.getResponseWithDataApi(new RestAPIWebServices.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                JSONObject jsonObject = null;
                listEpisodes = new ArrayList<>();
                try {
                    jsonObject = new JSONObject(result);
                    //listEpisodes = new ArrayList<>();

                    JSONObject pagination = jsonObject.getJSONObject("pagination");
                    boolean has_next_page = pagination.getBoolean("has_next_page");
                    if (!has_next_page) { //si es false
                        bt_verMas.setVisibility(View.GONE);
                        lay_paginacion.setVisibility(View.GONE);

                    } else {
                        lay_paginacion.setVisibility(View.VISIBLE);
                        bt_verMas.setVisibility(View.VISIBLE);
                    }

                    int current_page = pagination.getInt("current_page");
                    if (current_page > 1) {
                        bt_verMenos.setVisibility(View.VISIBLE);
                        lay_paginacion.setVisibility(View.VISIBLE);

                    } else {
                        bt_verMenos.setVisibility(View.GONE);
                        lay_paginacion.setVisibility(View.GONE);

                    }


                    //Log.e("jsonObject", jsonObject.toString());
                    JSONArray data = jsonObject.getJSONArray("data");
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject element = data.getJSONObject(i);
                        int id = element.getInt("mal_id");

                        JSONObject images = element.getJSONObject("images");
                        JSONObject jpg = images.getJSONObject("jpg");
                        String image_url = jpg.getString("image_url");

                        String title = element.getString("title");
                        String type = element.getString("type");

                        double score = 0;
                        if (!element.isNull("score")) {
                            score = element.getDouble("score");
                        }

                        String duration = element.getString("duration");

                        JSONObject broadcast = element.getJSONObject("broadcast");
                        String string = broadcast.getString("string");

                        listEpisodes.add(new Episode(id, score, title, image_url, type, duration, string));
                    }

                    EpisodeAdapter portadaAdapter = new EpisodeAdapter(listEpisodes, getContext());
                    layoutManager = new LinearLayoutManager(mContext);
                    recyclerView.setHasFixedSize(true);

                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(portadaAdapter);

                    progressbar.setVisibility(View.GONE);

                } catch (JSONException e) {
                    e.printStackTrace();
                    progressbar.setVisibility(View.GONE);
                    bt_verMas.setVisibility(View.GONE);
                    lay_paginacion.setVisibility(View.GONE);
                    Log.e("error", e.toString());
                    Toast.makeText(getContext(),getContext().getString(R.string.general_error), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error", error.toString());
                progressbar.setVisibility(View.GONE);
                bt_verMas.setVisibility(View.GONE);
                lay_paginacion.setVisibility(View.GONE);

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
        int id = view.getId();
        if (id == R.id.bt_verMas) {//recoger filtros
            current_page += 1;
            getEpisodes(date, current_page);
        } else if (id == R.id.bt_verMenos) {//recoger filtros
            current_page -= 1;
            getEpisodes(date, current_page);
        }
    }

    private void alertValoraApp() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage("Rate the app on Google Play!")
                .setCancelable(false)
                .setPositiveButton("Go to Google Play", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        final String appPackageName = "aniweeb.com";

                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                        } catch (android.content.ActivityNotFoundException anfe) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appPackageName)));
                        }
                    }
                })
                .setNegativeButton("Not now", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
