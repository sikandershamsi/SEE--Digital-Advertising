package com;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.listeners.RequestListener;
import com.see.Api;
import com.see.FileUtils;
import com.see.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class GetCoinsWebService extends AsyncTask<String, String, String> {
    private ProgressDialog mProgressDialog;
    RequestListener _listener;
    @SuppressWarnings("unused")
    private Context context;
    String result="";
    boolean exception=false;
    String exceptionmessage="";
    public GetCoinsWebService(Context context,RequestListener listener)
    {

        this.context = context;
        _listener = listener;
        mProgressDialog = new ProgressDialog(context,   R.style.AppTheme_Dark_Dialog);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage(context.getResources().getString(R.string.gettingcoins));

        mProgressDialog.setCancelable(false);

    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //mProgressDialog.show();

    }
    @Override
    protected String doInBackground(String... aurl) {

        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httppost = new HttpGet(Api.API_URL+Api.get_coins);

            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(6);
            SharedPreferences preferences = context.getSharedPreferences("userdetails", MODE_PRIVATE);
            httppost.addHeader("Authorization",preferences.getString("token", ""));

            //  httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

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

        mProgressDialog.dismiss();
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
                String coins_for_withdraw=jobj.getString("coins_for_withdraw");
                String coins_for_purchase=jobj.getString("coins_for_purchase");
                String videos_watch=jobj.getString("videos_watch");
                JSONArray jarr=jobj.getJSONArray("banners");
               // JSONArray cnic=jobj.getJSONArray("cnic");
                //JSONArray dob=jobj.getJSONArray("date_of_birth");
                String lotteries=jobj.getString("lotteries");
                JSONArray popads=jobj.getJSONArray("pop_ads");
                _listener.onSuccess(message,coins_for_withdraw,coins_for_purchase,videos_watch,jarr.toString(),popads.toString(),lotteries,"","","");
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

