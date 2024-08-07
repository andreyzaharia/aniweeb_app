package aniweeb.com.ui.random;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;

import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import aniweeb.com.R;
import aniweeb.com.URLs.URLs;
import aniweeb.com.activities.CatalogoActivity;
import aniweeb.com.activities.DetailsActivity;
import aniweeb.com.adapters.EpisodeAdapter;
import aniweeb.com.models.Episode;
import aniweeb.com.restapi.RestAPIWebServices;

public class RandomFragment extends Fragment  implements View.OnClickListener {
    private LinearLayout bt_randomAnime, progressbar, lay_anime, bt_again, lay_scores,lay_status, lay_item;
    private Context mContext;
    private ShapeableImageView im_anime;
    private TextView TXT_Type, txt_episodes, txt_duration, txt_score, txt_title;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

       View root = inflater.inflate(R.layout.fragment_random, container, false);
       mContext = getContext();

       loadElements(root);

        return root;
    }

    private void loadElements(View root) {

        bt_randomAnime = root.findViewById(R.id.bt_getRandom);
        bt_randomAnime.setOnClickListener(this);

        progressbar = root.findViewById(R.id.progressBar);

        lay_anime = root.findViewById(R.id.lay_anime);

        bt_again = root.findViewById(R.id.bt_again);
        bt_again.setOnClickListener(this);
        lay_scores = root.findViewById(R.id.lay_scores);
        lay_status = root.findViewById(R.id.lay_status);

        View includedLayout = root.findViewById(R.id.included);
        im_anime = includedLayout.findViewById(R.id.im_anime);
        TXT_Type = includedLayout.findViewById(R.id.TXT_Type);
        txt_episodes = includedLayout.findViewById(R.id.txt_episodes);
        txt_duration = includedLayout.findViewById(R.id.txt_duration);
        txt_score = includedLayout.findViewById(R.id.txt_score);
        txt_title = includedLayout.findViewById(R.id.txt_title);
        lay_item = includedLayout.findViewById(R.id.lay_item);

    }


    private void getRandomAnime() {
        progressbar.setVisibility(View.VISIBLE);
        bt_randomAnime.setVisibility(View.GONE);

        final RestAPIWebServices restAPIWebServices = new RestAPIWebServices(mContext, URLs.getRandomAnime);
        restAPIWebServices.getResponseApi(new RestAPIWebServices.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                JSONObject jsonObject = null;

                try {
                    jsonObject = new JSONObject(result);
                    lay_anime.setVisibility(View.VISIBLE);

                    //Log.e("jsonObject", jsonObject.toString());
                    JSONObject data = jsonObject.getJSONObject("data");

                        int id = data.getInt("mal_id");

                        JSONObject images = data.getJSONObject("images");
                        JSONObject jpg = images.getJSONObject("jpg");
                        String image_url = jpg.getString("image_url");
                        Picasso.with(mContext).load(image_url).into(im_anime);

                        String title = data.getString("title");
                        txt_title.setText(title);

                        String type = data.getString("type");
                        TXT_Type.setText(type);

                        if (!data.isNull("score")) {
                            lay_scores.setVisibility(View.VISIBLE);
                            double score = data.getDouble("score");
                            txt_score.setText(String.valueOf(score));
                        }

                        String duration = data.getString("duration");
                        txt_episodes.setText(duration);

                        JSONObject broadcast = data.getJSONObject("broadcast");
                        if (!broadcast.isNull("string")) {
                            lay_status.setVisibility(View.VISIBLE);
                            String string = broadcast.getString("string");
                            txt_duration.setText(string);
                        } else {
                            lay_status.setVisibility(View.GONE);
                        }

                    lay_item.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent i = new Intent(mContext, DetailsActivity.class);
                            i.putExtra("id", id);
                            mContext.startActivity(i);
                        }
                    });

                    progressbar.setVisibility(View.GONE);

                } catch (JSONException e) {
                    e.printStackTrace();
                    progressbar.setVisibility(View.GONE);
                    lay_anime.setVisibility(View.GONE);

                    bt_randomAnime.setVisibility(View.VISIBLE);
                    Log.e("error", e.toString());
                    Toast.makeText(getContext(),getContext().getString(R.string.general_error), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error", error.toString());
                progressbar.setVisibility(View.GONE);
                lay_anime.setVisibility(View.GONE);

                bt_randomAnime.setVisibility(View.VISIBLE);
                Toast.makeText(getContext(),getContext().getString(R.string.general_error), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.bt_getRandom) {
            getRandomAnime();
        } else if (id == R.id.bt_again) {
            lay_anime.setVisibility(View.GONE);
            progressbar.setVisibility(View.VISIBLE);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    getRandomAnime();
                }
            }, 1000);
        }
    }
}
