package easterndroids.xperience;

import android.app.AlertDialog;
import android.content.Context;
import android.location.Criteria;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
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
        if (type.equals("login")){
            try {

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
            //try
            String Latitude="";
            String Longitude="";
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
                return "login success";
                /*URL url = new URL("http://xperience.x10host.com/MomentInsert.php");
                HttpURLConnection httpURLConnection =(HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String post_data = URLEncoder.encode("user_name","UTF-8")+"="+URLEncoder.encode(uname,"UTF-8")+"&"+URLEncoder.encode("latitude","UTF-8")+"="+URLEncoder.encode(Double.toString(Latitude),"UTF-8")+"&"+URLEncoder.encode("longitude","UTF-8")+"="+URLEncoder.encode(Double.toString(Longitude),"UTF-8");
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
                return result;*/
            }
            /*catch (MalformedURLException e)
            {
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            }*/

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
        //alertDialog.setMessage(result);
        //alertDialog.show();

        Log.d("result:",result);
        if(result.equals("login success")){
            Intent XPActivityIntent = new Intent(context, XperienceActivity.class);
            XPActivityIntent.putExtra("uname", uname);
            //context.startActivities(new Intent[]{new Intent(context, XperienceActivity.class)});
            context.startActivity(XPActivityIntent);
        }


    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}
