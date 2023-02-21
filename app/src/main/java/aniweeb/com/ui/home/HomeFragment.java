package aniweeb.com.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import aniweeb.com.R;
import aniweeb.com.URLs.URLs;
import aniweeb.com.adapters.EpisodeAdapter;
import aniweeb.com.adapters.TrailerAdapter;
import aniweeb.com.databinding.FragmentHomeBinding;
import aniweeb.com.models.Episode;
import aniweeb.com.models.Trailer;
import aniweeb.com.restapi.RestAPIWebServices;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Trailer> listTrailers;
    private LinearLayout progressbar;
    private Context mContext;
    private AdView mAdView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        mContext = getContext();
        loadElements(root);

        return root;
    }

    private void loadElements(View root) {
        mAdView = root.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        recyclerView = root.findViewById(R.id.recyclerView);
        progressbar = root.findViewById(R.id.progressBar);

        getTrailers();
    }


    private void getTrailers() {
        progressbar.setVisibility(View.VISIBLE);

        RestAPIWebServices restAPIWebServices = new RestAPIWebServices(getContext(), URLs.getNewsPromos);
        restAPIWebServices.getResponseWithDataApi(new RestAPIWebServices.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                JSONObject jsonObject = null;
                listTrailers = new ArrayList<>();
                try {
                    jsonObject = new JSONObject(result);
                    //listEpisodes = new ArrayList<>();
/*
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

                    }*/


                    //Log.e("jsonObject", jsonObject.toString());
                    JSONArray data = jsonObject.getJSONArray("data");
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject element = data.getJSONObject(i);
                        JSONObject entry = element.getJSONObject("entry");
                        int id = entry.getInt("mal_id");
                        String title = entry.getString("title");

                        JSONObject trailer = element.getJSONObject("trailer");
                        String yt_url = trailer.getString("embed_url");
                        listTrailers.add(new Trailer(id, title, yt_url));
                    }

                    TrailerAdapter trailerAdapter = new TrailerAdapter(listTrailers, getContext());
                    layoutManager = new LinearLayoutManager(mContext);

                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(trailerAdapter);

                    progressbar.setVisibility(View.GONE);

                } catch (JSONException e) {
                    e.printStackTrace();
                    progressbar.setVisibility(View.GONE);
                    Log.e("error", e.toString());
                    Toast.makeText(getContext(),getContext().getString(R.string.general_error), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error", error.toString());
                progressbar.setVisibility(View.GONE);

                Toast.makeText(getContext(),getContext().getString(R.string.general_error), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}