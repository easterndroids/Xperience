package easterndroids.xperience;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener {
    private String imagesJSON;

    private static final String JSON_ARRAY ="result";
    private static final String IMAGE_URL = "url";
    private EditText et;


    private JSONArray arrayImages= null;

    private int TRACK = 0;

    private  String IMAGES_URL;

    private FloatingActionButton buttonFetchImages;
    private Button buttonMoveNext;
    private Button buttonMovePrevious;
    private Button buttonNavigate;
    private ImageView imageView;
    private GestureDetectorCompat gestureObject;
    public String uname = "";
    public String latitude;
    public String longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        uname = getIntent().getStringExtra("uname");
        setContentView(R.layout.activity_search);
        gestureObject = new GestureDetectorCompat(this, new LearnGesture());
        imageView = (ImageView) findViewById(R.id.imageView);
        et = (EditText) findViewById(R.id.sv);
        buttonFetchImages = (FloatingActionButton) findViewById(R.id.fab);
        buttonMoveNext = (Button) findViewById(R.id.buttonNext);
        buttonMovePrevious = (Button) findViewById(R.id.buttonPrev);
        buttonFetchImages.setOnClickListener(this);
        buttonMoveNext.setOnClickListener(this);
        buttonMovePrevious.setOnClickListener(this);

        buttonNavigate = (Button) findViewById(R.id.buttonNav);
        buttonNavigate.setOnClickListener(this);
    }


    private void extractJSON(){
        try {
            JSONObject jsonObject = new JSONObject(imagesJSON);
            arrayImages = jsonObject.getJSONArray(JSON_ARRAY);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void Navigate(View view)
    {
        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra("uname",uname);
        startActivity(intent);
    }

    private void showImage(){
        try {
            JSONObject jsonObject = arrayImages.getJSONObject(TRACK);
            getImage(jsonObject.getString(IMAGE_URL));
            DecimalFormat df = new DecimalFormat();
            df.setMaximumFractionDigits(15);
            latitude = jsonObject.getString("latitude");
            longitude = jsonObject.getString("longitude");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void moveNext(){
        if(TRACK < arrayImages.length()){
            TRACK++;
            showImage();
        }
    }

    private void movePrevious(){
        if(TRACK>0){
            TRACK--;
            showImage();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.test_navigation_activty, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(SearchActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
        return super.onOptionsItemSelected(item);
    }

    private void getAllImages() {
        class GetAllImages extends AsyncTask<String,Void,String>{
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(SearchActivity.this, "Fetching Data...","Please Wait...",true,true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                imagesJSON = s;
                extractJSON();
                showImage();
            }

            @Override
            protected String doInBackground(String... params) {
                String uri = params[0];
                BufferedReader bufferedReader = null;
                try {
                    URL url = new URL(uri);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();

                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String json;
                    while((json = bufferedReader.readLine())!= null){
                        sb.append(json+"\n");
                    }

                    return sb.toString().trim();

                }catch(Exception e){
                    return null;
                }
            }
        }
        GetAllImages gai = new GetAllImages();
        gai.execute(IMAGES_URL);
    }

    private void getImage(String urlToImage){
        class GetImage extends AsyncTask<String,Void,Bitmap>{
            ProgressDialog loading;
            @Override
            protected Bitmap doInBackground(String... params) {
                URL url = null;
                Bitmap image = null;

                String urlToImage = params[0];
                try {
                    url = new URL(urlToImage);
                    image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return image;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(SearchActivity.this,"Downloading Image...","Please wait...",true,true);
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                super.onPostExecute(bitmap);
                loading.dismiss();
                imageView.setImageBitmap(bitmap);
            }
        }
        GetImage gi = new GetImage();
        gi.execute(urlToImage);
    }

    @Override
    public void onClick(View v) {
        if(v == buttonFetchImages) {
            String tags = et.getText().toString();
            IMAGES_URL = "http://xperience.x10host.com/searcher.php?q=";
            IMAGES_URL += tags;
            getAllImages();
        }
        if(v == buttonMoveNext){
            moveNext();
        }
        if(v== buttonMovePrevious){
            movePrevious();
        }
        if(v== buttonNavigate){
            Intent intent = new Intent(this, MapsActivity.class);
            intent.putExtra("uname",uname);
            intent.putExtra("latitude",latitude);
            intent.putExtra("longitude",longitude);
            startActivity(intent);
            System.out.println("In Search - Latitude: "+latitude+" Longitude: "+longitude);
            /*Uri gmmIntentUri = Uri.parse("google.navigation:latitude,longitude&avoid=tf");
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            if (mapIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(mapIntent);
            }*/
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.gestureObject.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    class LearnGesture extends GestureDetector.SimpleOnGestureListener{

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if(e2.getX() > e1.getX()){
                //left to right swipe navigate to search activity
                Intent intent = new Intent(SearchActivity.this, XperienceActivity.class);
                intent.putExtra("uname",uname);
                finish();
                startActivity(intent);
            }



            return true;
        }
    }
}






























//
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.design.widget.FloatingActionButton;
//import android.support.design.widget.Snackbar;
//import android.support.v4.view.GestureDetectorCompat;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.SearchView;
//import android.support.v7.widget.Toolbar;
//import android.view.GestureDetector;
//import android.view.MotionEvent;
//import android.view.View;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.ListView;
//
//import java.io.InputStream;
//import java.net.HttpURLConnection;
//import java.net.URL;
//
//import android.app.Activity;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.view.View;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//
//public class SearchActivity extends Activity {
//
//    //声明地址
//    private ImageView img;
//    private EditText sv;
//    private String url;
//
//    //在消息队列中实现对控件的更改
//    private Handler handle = new Handler() {
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//                case 0:
//                    System.out.println("111");
//                    Bitmap bmp=(Bitmap)msg.obj;
//                    img.setImageBitmap(bmp);
//                    break;
//            }
//        };
//    };
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_search);
//
//        FloatingActionButton btn = (FloatingActionButton) findViewById(R.id.fab);
//        img = (ImageView) findViewById(R.id.imageView);
//
//        btn.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                //新建线程加载图片信息，发送到消息队列中
//                new Thread(new Runnable() {
//
//                    @Override
//                    public void run() {
//                        // TODO Auto-generated method stub
//                        sv = (EditText) findViewById(R.id.sv);
//                        String tags = sv.getText().toString();
//                        url = "http://xperience.x10host.com/searcher.php?q=";
//                        url += tags;
//                        Bitmap bmp = getURLimage(url);
//                        Message msg = new Message();
//                        msg.what = 0;
//                        msg.obj = bmp;
//                        System.out.println("000");
//                        handle.sendMessage(msg);
//                    }
//                }).start();
//            }
//        });
//    }
//
//    //加载图片
//    public Bitmap getURLimage(String url) {
//        Bitmap bmp = null;
//        try {
//            URL myurl = new URL(url);
//            // 获得连接
//            HttpURLConnection conn = (HttpURLConnection) myurl.openConnection();
//            conn.setConnectTimeout(6000);//设置超时
//            conn.setDoInput(true);
//            conn.setUseCaches(false);//不缓存
//            conn.connect();
//            InputStream is = conn.getInputStream();//获得图片的数据流
//            bmp = BitmapFactory.decodeStream(is);
//            is.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return bmp;
//    }
//}
//
//
//
//
//









//
//import easterndroids.xperience.MySQL.SenderReceiver;
//
//public class SearchActivity extends AppCompatActivity {
//
//    private GestureDetectorCompat gestureObject;
//
//    String urlAddress="http://xperience.x10host.com/searcher.php";
//    SearchView sv;
////    ListView lv;
//    ImageView noDataImg,noNetworkImg;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_search);
////        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
////
////        setSupportActionBar(toolbar);
//        gestureObject = new GestureDetectorCompat(this,new SearchActivity.LearnGesture());
//
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
//
////        lv = (ListView) findViewById(R.id.lv);
//        sv = (SearchView) findViewById(R.id.sv);
////        noDataImg = (ImageView) findViewById(R.id.nodataImg);
////        noNetworkImg = (ImageView) findViewById(R.id.noserver);
//
//        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
////                SenderReceiver sr = new SenderReceiver(SearchActivity.this,urlAddress,query,lv,noDataImg,noNetworkImg);
////                sr.execute();
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String query) {
////                SenderReceiver sr = new SenderReceiver(SearchActivity.this,urlAddress,query,lv,noDataImg,noNetworkImg);
////                sr.execute();
//
//                return false;
//            }
//        });
//
//
//    }
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        this.gestureObject.onTouchEvent(event);
//        return super.onTouchEvent(event);
//    }
//
//    class LearnGesture extends GestureDetector.SimpleOnGestureListener{
//
//        @Override
//        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
//            if(e2.getX() < e1.getX()){
//                Intent intent = new Intent(SearchActivity.this, XperienceActivity.class);
//                finish();
//                startActivity(intent);
//            }
//
//
//
//            return true;
//        }
//    }
//
//}
