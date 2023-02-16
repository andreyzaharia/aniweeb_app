package aniweeb.com.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import aniweeb.com.R;
import aniweeb.com.models.Genero;
import aniweeb.com.models.Portada;

/**
 * Created by Cristian Mármol cristian.marmol@occamcomunicacion.com on 16/02/2023.
 */
public class GenreAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final ArrayList<Genero> listGenero;
    private final Context mContext;

    public GenreAdapter(ArrayList<Genero> listGenero, Context mContext) {
        this.listGenero = listGenero;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        RecyclerView.ViewHolder viewHolder = new GenreAdapter.ViewHolder(inflater.inflate(R.layout.item_genre, parent, false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        final Genero generos = listGenero.get(position);
        GenreAdapter.ViewHolder genresHolder = (GenreAdapter.ViewHolder) viewHolder;
        genresHolder.TVTitle.setText(generos.getName());
    }

    @Override
    public int getItemCount() {
        return listGenero.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView TVTitle;
        LinearLayout item;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            TVTitle = itemView.findViewById(R.id.txt_genre);
            item = itemView.findViewById(R.id.bt_genre);
        }
    }
}
