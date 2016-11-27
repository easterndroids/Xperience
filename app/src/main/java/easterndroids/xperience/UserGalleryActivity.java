package easterndroids.xperience;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
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


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        uname = getIntent().getStringExtra("uname");
        /*Intent intent = getIntent();
        finish();
        startActivity(intent);*/
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
            String fullPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Xperience/";
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
            // Create a String array for FilePathStrings
            FilePathStrings = new String[listFile.length];
            // Create a String array for FileNameStrings
            FileNameStrings = new String[listFile.length];

            for (int i = 0; i < listFile.length; i++) {
                // Get the path of the image file
                FilePathStrings[i] = listFile[i].getAbsolutePath();
                // Get the name image file
                FileNameStrings[i] = listFile[i].getName();
                System.out.println("FilePathStrings: "+FilePathStrings[i]);
                System.out.println("FileNameStrings: "+FileNameStrings[i]);
            }
        }

        // Locate the GridView in gridview_main.xml
        grid = (GridView) findViewById(R.id.gridview);
        // Pass String arrays to LazyAdapter Class
        adapter = new GridViewAdapter(this, FilePathStrings, FileNameStrings);
        // Set the LazyAdapter to the GridView
        grid.setAdapter(adapter);

        // Capture gridview item click
        grid.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Intent i = new Intent(UserGalleryActivity.this, ViewImage.class);
                // Pass String arrays FilePathStrings
                i.putExtra("filepath", FilePathStrings);
                // Pass String arrays FileNameStrings
                i.putExtra("filename", FileNameStrings);
                // Pass click position
                i.putExtra("position", position);
                i.putExtra("uname", uname);
                startActivity(i);
            }

        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println("Request Code: "+requestCode);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Intent GalleryIntent = new Intent(this, UserGalleryActivity.class);
            GalleryIntent.putExtra("uname", uname);
            finish();
            //GalleryIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(GalleryIntent);
        }
    }

    public void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        MyFileContentProvider MFCP = new MyFileContentProvider();
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, MyFileContentProvider.CONTENT_URI);
        //takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, MFCP.getContentURI());
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override

    protected void onDestroy() {
        super.onDestroy();
    }
}
