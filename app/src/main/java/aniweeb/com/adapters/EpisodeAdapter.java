package aniweeb.com.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import aniweeb.com.R;
import aniweeb.com.activities.DetailsActivity;
import aniweeb.com.models.Episode;
import aniweeb.com.models.Portada;

/**
 * Created by Cristian Mármol cristian.marmol@occamcomunicacion.com on 20/02/2023.
 */
public class EpisodeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final ArrayList<Episode> listEpisodes;
    private final Context mContext;

    public EpisodeAdapter(ArrayList<Episode> listEpisodes, Context mContext) {
        this.listEpisodes = listEpisodes;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        RecyclerView.ViewHolder viewHolder = new EpisodeAdapter.ViewHolder(inflater.inflate(R.layout.item_episodes, parent, false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final Episode episode = listEpisodes.get(position);
        EpisodeAdapter.ViewHolder episodeHolder = (EpisodeAdapter.ViewHolder) holder;

        episodeHolder.txt_title.setText(episode.getTitulo());
        Picasso.with(mContext).load(episode.getImg_url()).into(episodeHolder.im_anime);

        episodeHolder.TXT_Type.setText(episode.getType());
        episodeHolder.txt_episodes.setText(episode.getDuration());
        episodeHolder.txt_duration.setText(episode.getBroadcast());

        if (episode.getScore() != 0) {
            episodeHolder.lay_scores.setVisibility(View.VISIBLE);
            episodeHolder.txt_score.setText(String.valueOf(episode.getScore()));
        }

        episodeHolder.lay_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(mContext, DetailsActivity.class);
                i.putExtra("id", episode.getId());
                mContext.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listEpisodes.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView txt_title, TXT_Type, txt_episodes, txt_duration, txt_score;
        ShapeableImageView im_anime;
        LinearLayout lay_item, lay_scores;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_title = itemView.findViewById(R.id.txt_title);
            TXT_Type = itemView.findViewById(R.id.TXT_Type);
            txt_episodes = itemView.findViewById(R.id.txt_episodes);
            txt_duration = itemView.findViewById(R.id.txt_duration);
            txt_score = itemView.findViewById(R.id.txt_score);

            im_anime = itemView.findViewById(R.id.im_anime);
            lay_item = itemView.findViewById(R.id.lay_item);
            lay_scores = itemView.findViewById(R.id.lay_scores);

        }
    }
}
