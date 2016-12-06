package easterndroids.xperience;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

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
        /*******************************************************************************************
         *initialize buttons and add listeners
         ******************************************************************************************/
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





    /*******************************************************************************************
     *Extract json, show image and get the location of photos
     ******************************************************************************************/
    private void extractJSON(){
        try {
            JSONObject jsonObject = new JSONObject(imagesJSON);
            arrayImages = jsonObject.getJSONArray(JSON_ARRAY);
        } catch (JSONException e) {
            e.printStackTrace();
        }
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


    /*******************************************************************************************
     *add buttons' fuctions: next, previous and navigate
     ******************************************************************************************/
    private void moveNext(){
        if(arrayImages != null && TRACK < arrayImages.length()){
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
    public void Navigate(View view)
    {
        System.out.println("Null Check: "+latitude+ " "+longitude);
        if (latitude != null && longitude != null) {
            Intent intent = new Intent(this, MapsActivity.class);
            intent.putExtra("uname", uname);
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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
    /*******************************************************************************************
     *using AsyncTask to read our JSON in order to get all images
     ******************************************************************************************/
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




    /*******************************************************************************************
     *This method will take a string as a parameter.
     *The string would have the url to image extracted from json array
     ******************************************************************************************/
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
    /*******************************************************************************************
     *set up buttons' onclick fuctions.(next, previous and navigate)
     ******************************************************************************************/
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
        if(v== buttonNavigate && latitude != null && longitude != null){
            Intent intent = new Intent(this, MapsActivity.class);
            intent.putExtra("uname",uname);
            intent.putExtra("latitude",latitude);
            intent.putExtra("longitude",longitude);
            startActivity(intent);
            System.out.println("In Search - Latitude: "+latitude+" Longitude: "+longitude);
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
                Intent intent = new Intent(SearchActivity.this, XperienceActivity.class);
                intent.putExtra("uname",uname);
                finish();
                startActivity(intent);
            }



            return true;
        }
    }
}