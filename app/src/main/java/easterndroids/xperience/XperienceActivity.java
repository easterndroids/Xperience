package easterndroids.xperience;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import java.io.File;

public class XperienceActivity extends AppCompatActivity  {


    private GestureDetectorCompat gestureObject;
    public String uname = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        uname = getIntent().getStringExtra("uname");
        setContentView(R.layout.activity_xperience);
        gestureObject = new GestureDetectorCompat(this, new LearnGesture());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        //CleanGallery();
        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                NavigateToUserGalleryView(v);
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.gestureObject.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    public void CleanGallery()
    {
        String fullPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Xperience/";
        File file = new File(fullPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        File [] listFile = file.listFiles();
        for(int i=0; i<listFile.length; i++)
        {
            if(listFile[i].length() == 0)
            {
                listFile[i].delete();
            }
        }
    }
    public void NavigateToUserGalleryView(View view)
    {
        Intent intent = new Intent(this, UserGalleryActivity.class);
        intent.putExtra("uname",uname);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.test_navigation_activty, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(XperienceActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
        return super.onOptionsItemSelected(item);
    }

    class LearnGesture extends GestureDetector.SimpleOnGestureListener{
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if(e2.getX() > e1.getX()){
                Intent intent = new Intent(XperienceActivity.this, SearchActivity.class);
                intent.putExtra("uname",uname);
                startActivity(intent);
            }
            return true;
        }
    }
}
