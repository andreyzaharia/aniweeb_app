package aniweeb.com.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import aniweeb.com.R;
import aniweeb.com.activities.DetailsActivity;
import aniweeb.com.models.Genero;
import aniweeb.com.models.Trailer;

/**
 * Created by Cristian Mármol cristian.marmol@occamcomunicacion.com on 21/02/2023.
 */
public class TrailerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private final ArrayList<Trailer> listTrailers;
    private final Context mContext;

    public TrailerAdapter(ArrayList<Trailer> listTrailers, Context mContext) {
        this.listTrailers = listTrailers;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        RecyclerView.ViewHolder viewHolder = new TrailerAdapter.ViewHolder(inflater.inflate(R.layout.item_trailer, parent, false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        final Trailer trailer = listTrailers.get(position);
        TrailerAdapter.ViewHolder trailerHolder = (TrailerAdapter.ViewHolder) viewHolder;

        trailerHolder.TVTitle.setText(trailer.getTitulo());
        trailerHolder.web_trailer.loadUrl(trailer.getYt_url());

        trailerHolder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(mContext, DetailsActivity.class);
                i.putExtra("id", trailer.getId_anime());
                mContext.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listTrailers.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView TVTitle;
        private WebView web_trailer;
        private LinearLayout item;
        private WebChromeClient.CustomViewCallback mCustomViewCallback;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            TVTitle = itemView.findViewById(R.id.txt_title);
            item = itemView.findViewById(R.id.lay_item);

            web_trailer = itemView.findViewById(R.id.wb_trailer);
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

            //web_trailer.onPause();
        }
    }
}
