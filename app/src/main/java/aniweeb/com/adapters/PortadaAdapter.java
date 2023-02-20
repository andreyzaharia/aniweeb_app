package aniweeb.com.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import aniweeb.com.R;
import aniweeb.com.activities.DetailsActivity;
import aniweeb.com.models.Portada;

public class PortadaAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final ArrayList<Portada> listPortadas;
    private final Context mContext;


    public PortadaAdapter(ArrayList<Portada> listPortadas, Context mContext) {
        this.listPortadas = listPortadas;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        RecyclerView.ViewHolder viewHolder = new ViewHolder(inflater.inflate(R.layout.item_grid_estrenos, parent, false));
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        final Portada portada = listPortadas.get(position);
        PortadaAdapter.ViewHolder portadaHolder = (PortadaAdapter.ViewHolder) viewHolder;

        portadaHolder.TVTitle.setText(portada.getTitulo());
        Picasso.with(mContext).load(portada.getImg_url()).into(portadaHolder.im_anime);

        if (portada.getPuntuacion() > 0) {
            portadaHolder.lay_puntuacion.setVisibility(View.VISIBLE);
            portadaHolder.TVMean.setText(" " + String.valueOf(portada.getPuntuacion()));
            portadaHolder.TVPopularity.setText(" " + String.valueOf(portada.getViewers()));

        } else {
            portadaHolder.lay_puntuacion.setVisibility(View.GONE);
            portadaHolder.TVMean.setVisibility(View.GONE);
            portadaHolder.TVPopularity.setVisibility(View.GONE);

        }

        portadaHolder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(mContext, DetailsActivity.class);
                i.putExtra("id", portada.getId());
                mContext.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listPortadas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView TVTitle, TVcategories, TVMean, TVPopularity;
        ShapeableImageView im_anime;
        LinearLayout lay_puntuacion;
        RelativeLayout item;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            TVTitle = itemView.findViewById(R.id.txt_title);
            TVcategories = itemView.findViewById(R.id.txt_categories);
            im_anime = itemView.findViewById(R.id.im_anime);
            TVMean = itemView.findViewById(R.id.txt_mean);
            TVPopularity = itemView.findViewById(R.id.txt_popularity);
            lay_puntuacion = itemView.findViewById(R.id.lay_puntuacion);
            item = itemView.findViewById(R.id.relative);
        }
    }
}
