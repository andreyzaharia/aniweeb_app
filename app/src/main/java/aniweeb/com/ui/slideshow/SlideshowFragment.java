package aniweeb.com.ui.slideshow;

import android.graphics.Typeface;
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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import aniweeb.com.R;
import aniweeb.com.URLs.URLs;
import aniweeb.com.adapters.PortadaAdapter;
import aniweeb.com.databinding.FragmentSlideshowBinding;
import aniweeb.com.models.Portada;
import aniweeb.com.restapi.RestAPIWebServices;

public class SlideshowFragment extends Fragment implements View.OnClickListener{

    private FragmentSlideshowBinding binding;
    private LinearLayout bt_finalizado, bt_emision, bt_prox;
    private TextView txt_finalizado, txt_emision, txt_prox;

    private RecyclerView recyclerView;
    private ArrayList<Portada> listPortadas;
    private RecyclerView.LayoutManager layoutManager;

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
        bt_finalizado = root.findViewById(R.id.lay_finalizado);
        bt_finalizado.setOnClickListener(this);

        bt_emision = root.findViewById(R.id.lay_emision);
        bt_emision.setOnClickListener(this);

        bt_prox = root.findViewById(R.id.lay_prox);
        bt_prox.setOnClickListener(this);

        txt_finalizado = root.findViewById(R.id.txt_finalizado);
        txt_emision = root.findViewById(R.id.txt_emision);
        txt_prox = root.findViewById(R.id.txt_prox);

        recyclerView = root.findViewById(R.id.grid_animes);
        recyclerView.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(getContext(),2);


        getAnimeRanking("tv");
    }

    private void getAnimeRanking(String ranking_type) {
        HashMap<String, String> params = new HashMap<>();
        params.put("ranking_type", "airing");
        params.put("limit", String.valueOf(10));


        RestAPIWebServices restAPIWebServices = new RestAPIWebServices(getContext(), params, URLs.getRankingAnimeList + "?ranking_type=" + ranking_type + "&limit=10&offset=0&fields=mean,num_scoring_users");
        restAPIWebServices.getResponseWithDataApi(new RestAPIWebServices.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(result);
                    listPortadas = new ArrayList<>();

                    Log.e("jsonObject", jsonObject.toString());
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

                    PortadaAdapter portadaAdapter = new PortadaAdapter(listPortadas,getContext());
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(portadaAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("error", e.toString());
                    Toast.makeText(getContext(),getContext().getString(R.string.general_error), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error", error.toString());
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
                changeOption(1);
                getAnimeRanking("tv");

                break;

            case R.id.lay_emision:
                changeOption(2);
                getAnimeRanking("airing");

                break;

            case R.id.lay_prox:
                changeOption(3);
                getAnimeRanking("upcoming");

                break;
        }
    }


    private void changeOption(int option) {
        if (option == 1) {
            bt_finalizado.setBackground(getContext().getResources().getDrawable(R.drawable.bg_layout_purple));
            txt_finalizado.setTextColor(getContext().getResources().getColor(R.color.white));
            txt_finalizado.setTextSize(14);
            txt_finalizado.setTypeface(null, Typeface.BOLD);

            bt_emision.setBackground(null);
            txt_emision.setTextColor(getContext().getResources().getColor(R.color.black));
            txt_emision.setTextSize(12);
            txt_emision.setTypeface(null, Typeface.NORMAL);

            bt_prox.setBackground(null);
            txt_prox.setTextColor(getContext().getResources().getColor(R.color.black));
            txt_prox.setTextSize(12);
            txt_prox.setTypeface(null, Typeface.NORMAL);
        } else if (option == 2) {

            bt_emision.setBackground(getContext().getResources().getDrawable(R.drawable.bg_layout_purple));
            txt_emision.setTextColor(getContext().getResources().getColor(R.color.white));
            txt_emision.setTextSize(14);
            txt_emision.setTypeface(null, Typeface.BOLD);

            bt_finalizado.setBackground(null);
            txt_finalizado.setTextColor(getContext().getResources().getColor(R.color.black));
            txt_finalizado.setTextSize(12);
            txt_finalizado.setTypeface(null, Typeface.NORMAL);

            bt_prox.setBackground(null);
            txt_prox.setTextColor(getContext().getResources().getColor(R.color.black));
            txt_prox.setTextSize(12);
            txt_prox.setTypeface(null, Typeface.NORMAL);
        } else if (option == 3) {

            bt_prox.setBackground(getContext().getResources().getDrawable(R.drawable.bg_layout_purple));
            txt_prox.setTextColor(getContext().getResources().getColor(R.color.white));
            txt_prox.setTextSize(14);
            txt_prox.setTypeface(null, Typeface.BOLD);

            bt_finalizado.setBackground(null);
            txt_finalizado.setTextColor(getContext().getResources().getColor(R.color.black));
            txt_finalizado.setTextSize(12);
            txt_finalizado.setTypeface(null, Typeface.NORMAL);

            bt_emision.setBackground(null);
            txt_emision.setTextColor(getContext().getResources().getColor(R.color.black));
            txt_emision.setTextSize(12);
            txt_emision.setTypeface(null, Typeface.NORMAL);
        }
    }
}