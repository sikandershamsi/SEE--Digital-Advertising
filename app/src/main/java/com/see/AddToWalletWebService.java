package com.see;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.listeners.CategoriesRequestListener;

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

public class AddToWalletWebService extends AsyncTask<String, String, String> {
    private ProgressDialog mProgressDialog;
    CategoriesRequestListener _listener;
    @SuppressWarnings("unused")
    private Context context;
    String result="";
    boolean exception=false;
    String exceptionmessage="";
    String adidd="";
    String latitude="";
    String longitude="";


    public AddToWalletWebService(Context context,CategoriesRequestListener listener,String adid,String lat,String lon)
    {
        this.context = context;
        _listener = listener;
        mProgressDialog = new ProgressDialog(context,   R.style.AppTheme_Dark_Dialog);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage(context.getResources().getString(R.string.updating));

        mProgressDialog.setCancelable(false);
        adidd=adid;
        latitude=lat;
        longitude=lon;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if(!mProgressDialog.isShowing()){
        mProgressDialog.show();}
    }
    @Override
    protected String doInBackground(String... aurl) {

        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(Api.API_URL+Api.add_to_wallet);
            SharedPreferences preferences = context.getSharedPreferences("userdetails", MODE_PRIVATE);
            httppost.addHeader("Authorization",preferences.getString("token", ""));
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
            nameValuePairs.add(new BasicNameValuePair("ad_id", adidd));
            nameValuePairs.add(new BasicNameValuePair("latitude", latitude));
            nameValuePairs.add(new BasicNameValuePair("longitude",longitude));
            nameValuePairs.add(new BasicNameValuePair("location","No Location"));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            InputStream is = entity.getContent();
            result= FileUtils.convertStream(is);


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

        if(mProgressDialog.isShowing()){
        mProgressDialog.dismiss();}
        Log.e(""," Result "+result);
        // Toast.makeText(context,result,Toast.LENGTH_LONG).show();
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
            _listener.onError(result);
        }

    }

}

