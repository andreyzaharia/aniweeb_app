package aniweeb.com.activities;

import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import aniweeb.com.R;
import aniweeb.com.URLs.URLs;
import aniweeb.com.adapters.GenreAdapter;
import aniweeb.com.adapters.PortadaAdapter;
import aniweeb.com.models.Genero;
import aniweeb.com.models.Portada;
import aniweeb.com.restapi.RestAPIWebServices;

/**
 * Created by Cristian Mármol cristian.marmol@occamcomunicacion.com on 16/02/2023.
 */
public class DetailsActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView imbt_back;
    private ShapeableImageView im_anime;
    private ImageButton im_mas;
    private TextView txt_title, TXT_Type, txt_episodes, txt_status, txt_score, TVDescription,txt_ranking, txt_people_scored, txt_people_add_list, txtpopularity,
            txt_start_date, txt_end_date, txt_Details, txt_Trailer, txt_trailer_title;
    private LinearLayout bt_add_list, lay_status, lay_episodes, lay_scores, lay_type, lay_final,
            lay_inicio, progresBar, lay_ranking, lay_scored, lay_members, lay_popularity, lay_info, lay_details,lay_trailer;
    private RecyclerView recyclerGeneros;
    private RecyclerView.LayoutManager layoutManagerGeneros;
    private ArrayList<Genero> listGeneros;

    private int id_anime;
    private WebView web_trailer;

    private InterstitialAd mInterstitialAd;
    private AdRequest adRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        adRequest = new AdRequest.Builder().build();

        loadElements();
    }

    private void getIntentExtras() {
        if (getIntent().getExtras() != null) {
            id_anime = getIntent().getIntExtra("id",0);
        }
    }

    private void loadElements() {
        getIntentExtras();

        imbt_back = findViewById(R.id.im_back);
        imbt_back.setOnClickListener(this);

        bt_add_list = findViewById(R.id.add_list);
        bt_add_list.setOnClickListener(this);

        im_anime = findViewById(R.id.im_anime);

        im_mas = findViewById(R.id.im_mas);
        im_mas.setOnClickListener(this);

        lay_status = findViewById(R.id.lay_status);
        lay_episodes = findViewById(R.id.lay_episodes);
        lay_scores = findViewById(R.id.lay_scores);
        lay_type = findViewById(R.id.lay_type);
        lay_inicio = findViewById(R.id.lay_inicio);
        lay_final = findViewById(R.id.lay_final);
        lay_ranking = findViewById(R.id.lay_ranking);
        lay_scored = findViewById(R.id.lay_scored);
        lay_members = findViewById(R.id.lay_members);
        lay_popularity = findViewById(R.id.lay_popularity);
        lay_info = findViewById(R.id.lay_info);
        lay_details = findViewById(R.id.lay_details);
        lay_trailer = findViewById(R.id.lay_trailer);

        web_trailer = findViewById(R.id.wb_trailer);
        WebChromeClient mWebChromeClient = new WebChromeClient();

        web_trailer.setWebChromeClient(mWebChromeClient);
        web_trailer.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });
        WebSettings webSettings = web_trailer.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        webSettings.setUseWideViewPort(true);
        webSettings.setMediaPlaybackRequiresUserGesture(true);


        progresBar = findViewById(R.id.progresBar);

        txt_title = findViewById(R.id.txt_title);
        TXT_Type = findViewById(R.id.TXT_Type);
        txt_episodes = findViewById(R.id.txt_episodes);
        txt_status = findViewById(R.id.txt_status);
        txt_score = findViewById(R.id.txt_score);
        TVDescription = findViewById(R.id.TVDescription);
        txt_ranking = findViewById(R.id.txt_ranking);
        txt_people_scored = findViewById(R.id.txt_people_scored);
        txt_people_add_list = findViewById(R.id.txt_people_add_list);
        txtpopularity = findViewById(R.id.txtpopularity);
        txt_start_date = findViewById(R.id.txt_start_date);
        txt_end_date = findViewById(R.id.txt_end_date);
        txt_trailer_title = findViewById(R.id.txt_trailer_title);

        txt_Details = findViewById(R.id.txt_Details);
        txt_Details.setOnClickListener(this);

        txt_Trailer = findViewById(R.id.txt_Trailer);
        txt_Trailer.setOnClickListener(this);

        recyclerGeneros = findViewById(R.id.recyclerGeneros);
        layoutManagerGeneros = new LinearLayoutManager(DetailsActivity.this, LinearLayoutManager.HORIZONTAL,false);

        InterstitialAd.load(this,getString(R.string.adUnitInterstecial_real), adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        mInterstitialAd = null;
                    }
                });

        getAnimeDetails();
    }

    private void getAnimeDetails() {
        Log.e("id", String.valueOf(id_anime));

        progresBar.setVisibility(View.VISIBLE);
        final RestAPIWebServices restAPIWebServices = new RestAPIWebServices(DetailsActivity.this, URLs.getAnimeJikanList+ "/"+ id_anime);
        restAPIWebServices.getResponseApi(new RestAPIWebServices.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(result);
                    JSONObject data = jsonObject.getJSONObject("data");
                    JSONObject images = data.getJSONObject("images");
                    JSONObject jpg = images.getJSONObject("jpg");

                    String img_url = jpg.getString("large_image_url");
                    Picasso.with(DetailsActivity.this).load(img_url).into(im_anime);

                    String title = data.getString("title");
                    txt_title.setText(title);

                    if (!data.isNull("type")) {
                        lay_type.setVisibility(View.VISIBLE);
                        String type = data.getString("type");
                        TXT_Type.setText(type);
                    }

                    if (!data.isNull("status")) {
                        lay_status.setVisibility(View.VISIBLE);
                        String status = data.getString("status");
                        txt_status.setText(status);
                    }

                    if (!data.isNull("episodes")) {
                        lay_episodes.setVisibility(View.VISIBLE);
                        int episodes = data.getInt("episodes");
                        txt_episodes.setText(String.valueOf(episodes) + " episodes");
                    }

                    if (!data.isNull("score")) {
                        lay_scores.setVisibility(View.VISIBLE);
                        double score = data.getDouble("score");
                        txt_score.setText(String.valueOf(score));
                    }

                    if (!data.isNull("rank") && data.getInt("rank") != 0) {
                        lay_ranking.setVisibility(View.VISIBLE);
                        int rank = data.getInt("rank");
                        txt_ranking.setText("#" + String.valueOf(rank));
                    }

                    if (!data.isNull("scored_by") && data.getInt("scored_by") != 0) {
                        lay_scored.setVisibility(View.VISIBLE);
                        int scored_by = data.getInt("scored_by");
                        txt_people_scored.setText(String.valueOf(scored_by));
                    }

                    if (!data.isNull("members") && data.getInt("members") != 0) {
                        lay_members.setVisibility(View.VISIBLE);
                        int members = data.getInt("members");
                        txt_people_add_list.setText(String.valueOf(members));
                    }

                    if (!data.isNull("popularity") && data.getInt("popularity") != 0) {
                        lay_popularity.setVisibility(View.VISIBLE);
                        int popularity = data.getInt("popularity");
                        txtpopularity.setText("#" + String.valueOf(popularity));
                    }

                    if (!data.isNull("synopsis")) {
                        String synopsis = data.getString("synopsis");
                        TVDescription.setText(synopsis);
                        int linecount = TVDescription.getLineCount();

                        if (linecount > 4) {
                            im_mas.setVisibility(View.VISIBLE);
                        } else {
                            im_mas.setVisibility(View.GONE);
                        }
                    }

                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

                    JSONObject aired = data.getJSONObject("aired");
                    if (!aired.isNull("from")) {
                        try {
                            lay_inicio.setVisibility(View.VISIBLE);
                            String from = aired.getString("from");

                            Date f = dateFormat.parse(from);
                            txt_start_date.setText(dateFormat.format(f));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }

                    if (!aired.isNull("to")) {
                        try {
                            lay_final.setVisibility(View.VISIBLE);
                            String to = aired.getString("to");

                            Date t = dateFormat.parse(to);
                            txt_end_date.setText(dateFormat.format(t));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }

                    if (!data.isNull("genres")) {
                        listGeneros = new ArrayList<>();
                        JSONArray genres = data.getJSONArray("genres");
                        for (int i = 0; i < genres.length(); i++){
                            JSONObject ele = genres.getJSONObject(i);
                            int genre_id = ele.getInt("mal_id");
                            String name = ele.getString("name");
                            listGeneros.add(new Genero(genre_id, name, name));
                        }

                        GenreAdapter genreAdapter = new GenreAdapter(listGeneros, DetailsActivity.this);
                        recyclerGeneros.setHasFixedSize(true);
                        recyclerGeneros.setLayoutManager(layoutManagerGeneros);
                        recyclerGeneros.setAdapter(genreAdapter);
                    }

                    JSONObject trailer = data.getJSONObject("trailer");
                    if (!trailer.isNull("youtube_id")) {
                        lay_info.setVisibility(View.VISIBLE);

                        String embed_url = trailer.getString("embed_url");
                        web_trailer.loadUrl(embed_url);
                        txt_trailer_title.setText(title);
                    }


                    progresBar.setVisibility(View.GONE);
                } catch (JSONException e) {
                    e.printStackTrace();
                    progresBar.setVisibility(View.GONE);
                    Log.e("JSONException", e.toString());
                    Toast.makeText(DetailsActivity.this, getString(R.string.general_error), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error", error.toString());
                progresBar.setVisibility(View.GONE);
                Toast.makeText(DetailsActivity.this, getString(R.string.general_error), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.im_back:
                finish();
                break;

            case R.id.im_mas:
                if(TVDescription.getMaxLines() == 100) {
                    im_mas.setImageDrawable(getResources().getDrawable(R.drawable.icons8_doble_abajo_64));
                    TVDescription.setMaxLines(5);
                }else if(TVDescription.getLineCount() > 4){
                    im_mas.setImageDrawable(getResources().getDrawable(R.drawable.icons8_cheur_n_96___));
                    TVDescription.setMaxLines(100);
                }
                break;

            case R.id.txt_Details:
                changeView(1);
                break;

            case R.id.txt_Trailer:

                if (mInterstitialAd != null) {
                    mInterstitialAd.show(DetailsActivity.this);
                    changeView(2);
                } else {
                    Log.d("TAG", "The interstitial ad wasn't ready yet.");
                    //changeView(2);
                    Toast.makeText(DetailsActivity.this, "Ad loading, try again later.", Toast.LENGTH_SHORT).show();

                }

                break;
        }
    }

    private void changeView(int option) {
        if (option == 1) { //vista detalles
            lay_details.setVisibility(View.VISIBLE);
            lay_trailer.setVisibility(View.GONE);

            txt_Details.setTextSize(16);
            txt_Details.setTypeface(null, Typeface.BOLD);
            txt_Details.setBackground(this.getResources().getDrawable(R.drawable.bg_layout_purple));

            txt_Trailer.setTextSize(14);
            txt_Trailer.setTypeface(null, Typeface.NORMAL);
            txt_Trailer.setBackground(this.getResources().getDrawable(R.drawable.bg_no_selected));
            web_trailer.onPause();

        }else if (option == 2) {

            lay_details.setVisibility(View.GONE);
            lay_trailer.setVisibility(View.VISIBLE);

            txt_Trailer.setTextSize(16);
            txt_Trailer.setTypeface(null, Typeface.BOLD);
            txt_Trailer.setBackground(DetailsActivity.this.getResources().getDrawable(R.drawable.bg_layout_purple));

            txt_Details.setTextSize(14);
            txt_Details.setTypeface(null, Typeface.NORMAL);
            txt_Details.setBackground(DetailsActivity.this.getResources().getDrawable(R.drawable.bg_no_selected));

            web_trailer.onResume();

        }
    }
}
