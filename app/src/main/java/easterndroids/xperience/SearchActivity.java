package easterndroids.xperience;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import easterndroids.xperience.MySQL.SenderReceiver;

public class SearchActivity extends AppCompatActivity {

    private GestureDetectorCompat gestureObject;

    String urlAddress="http://xperience.x10host.com/searcher.php";
    SearchView sv;
    ListView lv;
    ImageView noDataImg,noNetworkImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//
//        setSupportActionBar(toolbar);
        gestureObject = new GestureDetectorCompat(this,new SearchActivity.LearnGesture());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        lv = (ListView) findViewById(R.id.lv);
        sv = (SearchView) findViewById(R.id.sv);
        noDataImg = (ImageView) findViewById(R.id.nodataImg);
        noNetworkImg = (ImageView) findViewById(R.id.noserver);

        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                SenderReceiver sr = new SenderReceiver(SearchActivity.this,urlAddress,query,lv,noDataImg,noNetworkImg);
                sr.execute();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                SenderReceiver sr = new SenderReceiver(SearchActivity.this,urlAddress,query,lv,noDataImg,noNetworkImg);
                sr.execute();

                return false;
            }
        });


    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.gestureObject.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    class LearnGesture extends GestureDetector.SimpleOnGestureListener{

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if(e2.getX() < e1.getX()){
                Intent intent = new Intent(SearchActivity.this, XperienceActivity.class);
                finish();
                startActivity(intent);
            }



            return true;
        }
    }

}
