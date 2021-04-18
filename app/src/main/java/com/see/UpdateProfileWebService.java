package com.see;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

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

public class UpdateProfileWebService extends AsyncTask<String, String, String> {
    private ProgressDialog mProgressDialog;
    CategoriesRequestListener _listener;
    @SuppressWarnings("unused")
    private Context context;
    String result="";
    boolean exception=false;
    String exceptionmessage="";
    String name="";
    String email="";
    String phone="";
    String gender="";
    String dob="";
    String city="";
    String country="";
    String cnic="";

    public UpdateProfileWebService(Context context,CategoriesRequestListener listener,String namet,String emailt,String phonet,String gendert,String dobt,String cityt,String countryt,String cnict)
    {
        this.context = context;
        _listener = listener;
        mProgressDialog = new ProgressDialog(context,   R.style.AppTheme_Dark_Dialog);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage(context.getResources().getString(R.string.updatingprofile));
        mProgressDialog.setCancelable(false);
        name=namet;
        phone=phonet;
        email=emailt;
        gender=gendert;
        dob=dobt;
        city=cityt;
        country=countryt;
        cnic=cnict;
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
            HttpPost httppost = new HttpPost(Api.API_URL+Api.update_profile);
            SharedPreferences preferences = context.getSharedPreferences("userdetails", MODE_PRIVATE);
            httppost.addHeader("Authorization",preferences.getString("token", ""));
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(7);
            nameValuePairs.add(new BasicNameValuePair("name", name));
            nameValuePairs.add(new BasicNameValuePair("email", email));
            nameValuePairs.add(new BasicNameValuePair("phone", phone));
            nameValuePairs.add(new BasicNameValuePair("cnic", cnic));
            nameValuePairs.add(new BasicNameValuePair("gender",gender));
            nameValuePairs.add(new BasicNameValuePair("date_of_birth",dob));
            nameValuePairs.add(new BasicNameValuePair("interests","2"));
            nameValuePairs.add(new BasicNameValuePair("country",country));
            nameValuePairs.add(new BasicNameValuePair("city",city));
            nameValuePairs.add(new BasicNameValuePair("address","abc"));
            nameValuePairs.add(new BasicNameValuePair("language","urdu"));
           // nameValuePairs.add(new BasicNameValuePair("photo",photo));


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
        Toast.makeText(context,"Profile Updated",Toast.LENGTH_LONG).show();
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

