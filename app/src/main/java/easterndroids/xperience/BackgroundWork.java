package easterndroids.xperience;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.location.Criteria;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by zhuangyuan on 2016-11-12.
 */

public class BackgroundWork extends AsyncTask<String,Void,String> {
    Context context;
    AlertDialog alertDialog;
    BackgroundWork(Context ctx) {
        context = ctx;
    }
    public String uname = "";
    public static String uploadImage = "";

//    private final Activity mActivity;
//
//    public BackgroundWork( final Activity mActivity ) {
//        this.mActivity = mActivity;
//    }

    @Override
    protected String doInBackground(String... params) {
        String type = params[0];
        uname = params[1];
        String login_url = "http://xperience.x10host.com/login.php";
        String register_url = "http://xperience.x10host.com/register.php";
        if (type.equals("login")){
            try
            {
                String user_name = params[1];
                String password = params[2];
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection =(HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String post_data = URLEncoder.encode("user_name","UTF-8")+"="+URLEncoder.encode(user_name,"UTF-8")+"&"+URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(password,"UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String result = "";
                String line;
                while ((line = bufferedReader.readLine())!= null){
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        if (type.equals("Moment Insert"))
        {
            String Tag = params[2];
            String Latitude="";
            String Longitude="";
            try
            {
                GPSTracker gps = new GPSTracker(context);
                if(gps.canGetLocation()){
                    Latitude = Double.toString(gps.getLatitude());
                    Longitude = Double.toString(gps.getLongitude());
                    System.out.println("Can get Location");
                    // \n is for new line
                }else{
                    // can't get location
                    // GPS or Network is not enabled
                    // Ask user to enable GPS/network in settings
                    System.out.println("Can't get Location");
                    gps.showSettingsAlert();
                }
                System.out.println("Latitude: "+Latitude+" Longitude: "+Longitude);
                System.out.println("Upload Image: "+uploadImage);
                URL url = new URL("http://xperience.x10host.com/momentinsert.php");
                HttpURLConnection httpURLConnection =(HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String post_data = URLEncoder.encode("user_name","UTF-8")+"="+URLEncoder.encode(uname,"UTF-8")+"&"+URLEncoder.encode("latitude","UTF-8")+"="+Latitude+"&"+URLEncoder.encode("longitude","UTF-8")+"="+Longitude+"&"+URLEncoder.encode("image","UTF-8")+"="+uploadImage+"&"+URLEncoder.encode("tag","UTF-8")+"="+URLEncoder.encode(Tag,"UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String result = "";
                String line;
                while ((line = bufferedReader.readLine())!= null){
                    result += line;
                }
                System.out.println("Result in Moment Insert: "+result);
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;
            }
            catch (MalformedURLException e)
            {
                e.printStackTrace();
                System.out.println("Error 1: "+e.getMessage());
            }
            catch (IOException e) {
                e.printStackTrace();
                System.out.println("Error 2: "+e.getMessage());
            }
        }else if(type.equals("register")){

            try
            {
                String firstName = params[1];
                String lastName = params[2];
                String email = params[3];
                String mobile = params[4];
                String username = params[5];
                String password = params[6];
                URL url = new URL(register_url);
                HttpURLConnection httpURLConnection =(HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String post_data = URLEncoder.encode("FIRST_NAME","UTF-8")+"="+URLEncoder.encode(firstName,"UTF-8")+"&"
                        + URLEncoder.encode("LAST_NAME","UTF-8")+"="+URLEncoder.encode(lastName,"UTF-8")+"&"
                        + URLEncoder.encode("EMAIL","UTF-8")+"="+URLEncoder.encode(email,"UTF-8")+"&"
                        + URLEncoder.encode("MOBILE","UTF-8")+"="+URLEncoder.encode(mobile,"UTF-8")+"&"
                        + URLEncoder.encode("USERNAME","UTF-8")+"="+URLEncoder.encode(username,"UTF-8")+"&"
                        + URLEncoder.encode("PASSWORD","UTF-8")+"="+URLEncoder.encode(password,"UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String result = "";
                String line = "";
                while ((line = bufferedReader.readLine())!= null){
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return null;
    }

    @Override
    protected void onPreExecute() {
        alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("Status");
    }

    @Override
    protected  void onPostExecute(String result) {
        alertDialog.setMessage(result);
        alertDialog.show();
        if (result != null)
        {
            Log.d("result:", result);
            if (result.equals("login success")) {
                Intent XPActivityIntent = new Intent(context, XperienceActivity.class);
                XPActivityIntent.putExtra("uname", uname);
                //context.startActivities(new Intent[]{new Intent(context, XperienceActivity.class)});
                context.startActivity(XPActivityIntent);
            } else if (result.contains("insertion success")) {
                Intent XPActivityIntent = new Intent(context, UserGalleryActivity.class);
                XPActivityIntent.putExtra("uname", uname);
                //context.startActivities(new Intent[]{new Intent(context, XperienceActivity.class)});
                context.startActivity(XPActivityIntent);
            }
        }
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

}
