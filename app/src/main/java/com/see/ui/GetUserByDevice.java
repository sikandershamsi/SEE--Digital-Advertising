package com.see.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.listeners.RequestListener;
import com.see.Api;
import com.see.DeviceUtils;
import com.see.FileUtils;
import com.see.R;

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

public class GetUserByDevice extends AsyncTask<String, String, String> {
    private ProgressDialog mProgressDialog;
    RequestListener _listener;
    @SuppressWarnings("unused")
    private Context context;
    String result="";
    boolean exception=false;
    String exceptionmessage="";
    public GetUserByDevice(Context context,RequestListener listener)
    {
        this.context = context;
        _listener = listener;
        mProgressDialog = new ProgressDialog(context,   R.style.AppTheme_Dark_Dialog);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage(context.getResources().getString(R.string.authanticaion));

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
            HttpPost httppost = new HttpPost(Api.API_URL+Api.user_by_device);

            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(6);


            nameValuePairs.add(new BasicNameValuePair("device_id", DeviceUtils.getAndroidUniqueID(context)));
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
        if(!result.equals(""))
        {
            result=result.trim().toString();
        }
        try {
            JSONObject jobj = new JSONObject(result);
            String flag=jobj.getString("status");
            String message=jobj.getString("message");
            JSONObject dataobj=jobj.getJSONObject("data");
            if(flag.equalsIgnoreCase("true"))
            {
                String token=jobj.getString("token");
                String user_id=dataobj.getString("user_id");
                String name=dataobj.getString("name");
                String email=dataobj.getString("email");
                String dob=dataobj.getString("dob");
                String phone=dataobj.getString("phone");
                String cnic=dataobj.getString("cnic");
                String level=dataobj.getString("level");
                String ad_views=jobj.getString("ad_views");
                _listener.onSuccess(message,token,user_id,name,email,dob,phone,cnic,level,ad_views);
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

