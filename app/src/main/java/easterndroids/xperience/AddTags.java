package easterndroids.xperience;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class AddTags extends AppCompatActivity {

    public static String uname="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_tags);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        uname = getIntent().getStringExtra("uname");
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {NavigateToGallery();
            }
        });
        setPicture();
    }

    public void setPicture()
    {
        ImageView imageView = (ImageView) findViewById(R.id.image_view_bmp);
        Bitmap bitmap = (Bitmap) getIntent().getParcelableExtra("bitmap");
        imageView.setImageBitmap(bitmap);
    }

    public void NavigateToGallery()
    {
        Intent GalleryIntent = new Intent(this, UserGalleryActivity.class);
        GalleryIntent.putExtra("uname", uname);
        finish();
        startActivity(GalleryIntent);
    }
}
