package easterndroids.xperience;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

public class AddTags extends AppCompatActivity {

    public static String uname="";
    public Bitmap bitmap;
    public String image_str;

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
        bitmap = (Bitmap) getIntent().getParcelableExtra("bitmap");
        imageView.setImageBitmap(bitmap);
        BackgroundWork BGWork = new BackgroundWork(this);
        BGWork.uploadImage = getStringImage(bitmap);
    }

    public void NavigateToGallery()
    {
        EditText TagET = (EditText) findViewById(R.id.tagmoment);
        String Tag = TagET.getText().toString();
        String type = "Moment Insert";
        BackgroundWork backgroundWork = new BackgroundWork(this);
        backgroundWork.execute(type,uname,Tag);
        Intent GalleryIntent = new Intent(this, XperienceActivity.class);
        GalleryIntent.putExtra("uname", uname);
        finish();
        startActivity(GalleryIntent);
        Toast.makeText(getBaseContext(), "Xperience saved", Toast.LENGTH_LONG).show();
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        String result = "";
        if (bmp != null) {
            bmp.compress(Bitmap.CompressFormat.JPEG, 90, baos);
            byte[] imageBytes = baos.toByteArray();
            String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            result =  encodedImage;
        }
        System.out.println("Start Image: "+result+" End Image");
        return result;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.test_navigation_activty, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(AddTags.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
        return super.onOptionsItemSelected(item);
    }
}
