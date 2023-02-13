package aniweeb.com.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import aniweeb.com.R;
import aniweeb.com.models.Portada;

public class PortadaAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private ArrayList<Portada> listPortadas;
    private Context mContext;

    public PortadaAdapter(ArrayList<Portada> listPortadas, Context mContext) {
        this.listPortadas = listPortadas;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        RecyclerView.ViewHolder viewHolder = new PortadaAdapter.ViewHolder(inflater.inflate(R.layout.item_grid_estrenos,parent,false));
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
            portadaHolder.TVMean.setText(String.valueOf(portada.getPuntuacion()));
            portadaHolder.TVPopularity.setText(String.valueOf(portada.getViewers()));

        } else {
            portadaHolder.lay_puntuacion.setVisibility(View.GONE);
            portadaHolder.TVMean.setVisibility(View.GONE);
            portadaHolder.TVPopularity.setVisibility(View.GONE);

        }
    }

    @Override
    public int getItemCount() {
        return listPortadas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView TVTitle, TVcategories, TVMean, TVPopularity;
        ImageView im_anime;
        LinearLayout lay_puntuacion;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            TVTitle = itemView.findViewById(R.id.txt_title);
            TVcategories = itemView.findViewById(R.id.txt_categories);
            im_anime = itemView.findViewById(R.id.im_anime);
            TVMean = itemView.findViewById(R.id.txt_mean);
            TVPopularity = itemView.findViewById(R.id.txt_popularity);
            lay_puntuacion = itemView.findViewById(R.id.lay_puntuacion);

        }
    }
}
