package aniweeb.com.ui.slideshow;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import aniweeb.com.R;
import aniweeb.com.adapters.PortadaAdapter;
import aniweeb.com.databinding.FragmentSlideshowBinding;
import aniweeb.com.models.Portada;

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

        listPortadas = new ArrayList<>();
        listPortadas.add(new Portada(1, 890988,9.89, "Naruto Shippuden", "Action, romance"));
        listPortadas.add(new Portada(2, 890988,9.89, "Naruto Shippuden 2", "Action, romance"));
        listPortadas.add(new Portada(3, 890988,9.89, "Naruto Shippuden 3", "Action, romance"));
        listPortadas.add(new Portada(4, 890988,9.89, "Naruto Shippuden 4", "Action, romance"));
        listPortadas.add(new Portada(5, 890988,9.89, "Naruto Shippuden 5", "Action, romance"));
        listPortadas.add(new Portada(6, 890988,9.89, "Naruto Shippuden 6", "Action, romance"));

        PortadaAdapter portadaAdapter = new PortadaAdapter(listPortadas,getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(portadaAdapter);
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
                break;

            case R.id.lay_emision:
                changeOption(2);

                break;

            case R.id.lay_prox:
                changeOption(3);

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