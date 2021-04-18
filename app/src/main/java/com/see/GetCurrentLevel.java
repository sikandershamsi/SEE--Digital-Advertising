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
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class GetCurrentLevel extends AsyncTask<String, String, String> {
    private ProgressDialog mProgressDialog;
    CategoriesRequestListener _listener;
    @SuppressWarnings("unused")
    private Context context;
    String result="";
    boolean exception=false;
    String exceptionmessage="";
    public GetCurrentLevel(Context context,CategoriesRequestListener listener)
    {
        this.context = context;
        _listener = listener;
        mProgressDialog = new ProgressDialog(context,   R.style.AppTheme_Dark_Dialog);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage(context.getResources().getString(R.string.loadingrewards));

        mProgressDialog.setCancelable(false);

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
            HttpPost httppost = new HttpPost(Api.API_URL+Api.level);

            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(6);
            SharedPreferences preferences = context.getSharedPreferences("userdetails", MODE_PRIVATE);
            httppost.addHeader("Authorization",preferences.getString("token", ""));

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

                _listener.onSuccess(result);
            }
            else
            {
                _listener.onError(result);
            }
        }
        catch(Exception a)
        {
            _listener.onError(result);
        }

    }

}

