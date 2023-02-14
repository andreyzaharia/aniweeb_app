package aniweeb.com.activities;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.ns.developer.tagview.entity.Tag;
import com.ns.developer.tagview.widget.TagCloudLinkView;

import aniweeb.com.R;

/**
 * Created by Cristian Mármol cristian.marmol@occamcomunicacion.com on 14/02/2023.
 */
public class CatalogoActivity extends AppCompatActivity implements View.OnClickListener{

    private TagCloudLinkView tagCloudLinkView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalogo);

        loadElements();
    }

    private void loadElements() {
        tagCloudLinkView = findViewById(R.id.tagLink);
        tagCloudLinkView.add(new Tag(1,"TAG TEXT 1"));
        tagCloudLinkView.add(new Tag(1,"TAG TEXT 2"));
        tagCloudLinkView.add(new Tag(1,"TAG TEXT 3"));
        tagCloudLinkView.drawTags();

        tagCloudLinkView.setOnTagSelectListener(new TagCloudLinkView.OnTagSelectListener(){
            @Override
            public void onTagSelected(Tag tag, int i) {
                // write something
            }
        });

        tagCloudLinkView.setOnTagDeleteListener(new TagCloudLinkView.OnTagDeleteListener() {
            @Override
            public void onTagDeleted(Tag tag, int i) {
                // write something
            }
        });
    }

    @Override
    public void onClick(View view) {

    }
}
