package easterndroids.xperience;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UserGalleryActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
    Date now = new Date();
    public String fileName = "JPEG_"+formatter.format(now) + ".jpg";
    private String[] FilePathStrings;
    private String[] FileNameStrings;
    private File[] listFile;
    GridView grid;
    GridViewAdapter adapter;
    File file;
    public static String uname = "";
    public ImageView imageView;
    public String  fullPath;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        imageView = (ImageView) findViewById (R.id.image_view_bmp);
        uname = getIntent().getStringExtra("uname");
        setContentView(R.layout.activity_user_gallery);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            Toast.makeText(this, "Error! No SDCARD Found!", Toast.LENGTH_LONG)
                    .show();
        } else {
            fullPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Xperience/";
            boolean success = true;
            try
            {
                file = new File(fullPath);
                if (!file.exists()) {

                    success = file.mkdirs();
                }
                System.out.println("Full Path: "+fullPath);
                if (success) {
                    System.out.println("Full Path: "+fullPath);
                    System.out.println("Success:"+file.exists()+fullPath);
                } else {
                    System.out.println("Failure:"+file.exists());
                }
            }
            catch (Exception e) {
                System.out.println( e.getMessage());
            }

            listFile = file.listFiles();
            int len = listFile.length;
            System.out.println("Length:"+len);
        }

        if (file.isDirectory()) {
            listFile = file.listFiles();
            FilePathStrings = new String[listFile.length];
            FileNameStrings = new String[listFile.length];

            for (int i = 0; i < listFile.length; i++) {
                System.out.println("List File Size: "+listFile[i].length());
                if(listFile[i].length() != 0) {
                    FilePathStrings[i] = listFile[i].getAbsolutePath();
                    FileNameStrings[i] = listFile[i].getName();
                    System.out.println("FilePathStrings: " + FilePathStrings[i]);
                    System.out.println("FileNameStrings: " + FileNameStrings[i]);
                    System.out.println("List File Size 2: "+listFile[i].length());
                }
            }
        }

        grid = (GridView) findViewById(R.id.gridview);
        adapter = new GridViewAdapter(this, FilePathStrings, FileNameStrings);
        grid.setAdapter(adapter);
        grid.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Intent i = new Intent(UserGalleryActivity.this, ViewImage.class);
                i.putExtra("filepath", FilePathStrings);
                i.putExtra("filename", FileNameStrings);
                i.putExtra("position", position);
                i.putExtra("uname", uname);
                startActivity(i);
            }

        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.test_navigation_activty, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(UserGalleryActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println("Request Code: "+data);
        Bitmap bmp = null;
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            setPic();
        }
    }

    public void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, MyFileContentProvider.CONTENT_URI);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override

    protected void onDestroy() {
        super.onDestroy();
    }

    private void setPic() {
        int targetH = 100;
        int targetW = 100;
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        MyFileContentProvider MFCP = new MyFileContentProvider();
        BitmapFactory.decodeFile(MFCP.GlobalPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(MFCP.GlobalPath, bmOptions);
        Intent TagViewIntent = new Intent(this, AddTags.class);
        TagViewIntent.putExtra("bitmap", bitmap);
        TagViewIntent.putExtra("uname", uname);
        startActivity(TagViewIntent);
    }
}
