package com.see;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.listeners.AddadRequestListener;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class AddWebService extends AsyncTask<String, String, String> {
    private ProgressDialog mProgressDialog;
    AddadRequestListener _listener;
    @SuppressWarnings("unused")
    private Context context;
    String title="";
   // String type="";
    String fromdate="";
    String todate="";
    String category="";
    String interest="";
    String agegroup="";
    String country="";
    String city="";
    String max_views="";
    String filename="";
    String thumb="";
    String ad_url="";
    boolean exception=false;
    String exceptionmessage="";
    private String result;

    public AddWebService(Context context, AddadRequestListener listener, String titlee, String max_viewss, String fromdatee,  String todatee, String categoryy, String agegroupp, String countryy, String cityy,String ad_urll,String filenamee,String thumbb)
    {
        this.context = context;
        _listener = listener;
        mProgressDialog = new ProgressDialog(context,R.style.AppTheme_Dark_Dialog);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage(context.getResources().getString(R.string.posting_new_add));
        mProgressDialog.setCancelable(false);
        title=titlee;
        max_views=max_viewss;
       // type=typee;
        fromdate=fromdatee;
        todate=todatee;
        category=categoryy;
        agegroup=agegroupp;
        country=countryy;
        city=cityy;
        ad_url=ad_urll;
        filename=filenamee;
        thumb=thumbb;


    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mProgressDialog.show();
    }
    @Override
    protected String doInBackground(String... aurl) {

        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(Api.API_URL+Api.upload_ad);
            SharedPreferences preferences = context.getSharedPreferences("userdetails", MODE_PRIVATE);

            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("title", title));
            nameValuePairs.add(new BasicNameValuePair("type", "4"));
            nameValuePairs.add(new BasicNameValuePair("from_date", fromdate));
            nameValuePairs.add(new BasicNameValuePair("to_date", todate));
            nameValuePairs.add(new BasicNameValuePair("category_id", category));
            nameValuePairs.add(new BasicNameValuePair("date_of_birth", ""));
            //nameValuePairs.add(new BasicNameValuePair("amount",amount));
            nameValuePairs.add(new BasicNameValuePair("token",preferences.getString("token", "")));
            nameValuePairs.add(new BasicNameValuePair("gender", ""));
            nameValuePairs.add(new BasicNameValuePair("device_id", DeviceUtils.getAndroidUniqueID(context)));
            nameValuePairs.add(new BasicNameValuePair("device_type","Mobile"));
            nameValuePairs.add(new BasicNameValuePair("platform", "android"));
            nameValuePairs.add(new BasicNameValuePair("photo", ""));
            nameValuePairs.add(new BasicNameValuePair("language", ""));
            nameValuePairs.add(new BasicNameValuePair("address", ""));
            nameValuePairs.add(new BasicNameValuePair("ad_url", ad_url));
            nameValuePairs.add(new BasicNameValuePair("city",city));
            nameValuePairs.add(new BasicNameValuePair("max_views",max_views));
            nameValuePairs.add(new BasicNameValuePair("filename",filename));
            nameValuePairs.add(new BasicNameValuePair("thumb",thumb));
            nameValuePairs.add(new BasicNameValuePair("country",country));
            nameValuePairs.add(new BasicNameValuePair("interests",interest));
            nameValuePairs.add(new BasicNameValuePair("age_group", agegroup));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            InputStream is = entity.getContent();
            result=FileUtils.convertStream(is);


        } catch (Exception e) {
            Log.e(""," Exception "+ e.getMessage());

            exception=true;
            exceptionmessage=e.getMessage();
        }

        return null;

    }

    protected void onProgressUpdate(String... progress) {
        Log.d(""," Progress "+progress[0]);
        //mProgressDialog.setProgress(Integer.parseInt(progress[0]));
    }
    @Override
    protected void onPostExecute(String unused) {

        mProgressDialog.dismiss();
        Log.e(""," Result "+result);
        Toast.makeText(context,result,Toast.LENGTH_LONG).show();
        if(!result.equals(""))
        {
            result=result.trim().toString();
        }
        try {
            JSONObject jobj = new JSONObject(result);
            String flag=jobj.getString("status");
            String message=jobj.getString("message");
            if(flag.equalsIgnoreCase("true"))
            {
                _listener.onSuccess(message);
            }
            else
            {
                _listener.onError(message);
            }
        }
        catch(Exception a)
        {
            _listener.onError(a.toString());
        }

    }

}