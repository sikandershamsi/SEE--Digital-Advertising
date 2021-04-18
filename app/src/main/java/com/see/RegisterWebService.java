package com.see;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.listeners.RequestListener;

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

/**
 * Created by Saad on 1/28/2019.
 */

public class RegisterWebService extends AsyncTask<String, String, String> {
    private ProgressDialog mProgressDialog;
    RequestListener _listener;
    @SuppressWarnings("unused")
    private Context context;
    String result="";
    String email="";
    String pass="";
    String name="";
    String phone="";
    String dob="";
    String cnic="";
    boolean exception=false;
    String exceptionmessage="";
    public RegisterWebService(Context context,RequestListener listener,String emailid,String password,String namee,String phonee,String cnicc,String dobb)
    {
        this.context = context;
        _listener = listener;
        mProgressDialog = new ProgressDialog(context,R.style.AppTheme_Dark_Dialog);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage(context.getResources().getString(R.string.creatingaccount));
        mProgressDialog.setCancelable(false);
        pass=password;
        email=emailid;
        name=namee;
        phone=phonee;
        cnic=cnicc;
        dob=dobb;

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
            HttpPost httppost = new HttpPost(Api.API_URL+Api.register);

            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
            nameValuePairs.add(new BasicNameValuePair("name", name));
            nameValuePairs.add(new BasicNameValuePair("email", email));
            nameValuePairs.add(new BasicNameValuePair("password", pass));
            nameValuePairs.add(new BasicNameValuePair("phone", phone));
            nameValuePairs.add(new BasicNameValuePair("date_of_birth", dob));
            nameValuePairs.add(new BasicNameValuePair("cnic", cnic));
            nameValuePairs.add(new BasicNameValuePair("gender", ""));
            nameValuePairs.add(new BasicNameValuePair("device_id", DeviceUtils.getAndroidUniqueID(context)));
            nameValuePairs.add(new BasicNameValuePair("device_type","Mobile"));
            nameValuePairs.add(new BasicNameValuePair("platform", "android"));
            nameValuePairs.add(new BasicNameValuePair("photo", ""));
            nameValuePairs.add(new BasicNameValuePair("language", ""));
            nameValuePairs.add(new BasicNameValuePair("address", ""));
            nameValuePairs.add(new BasicNameValuePair("city", ""));
            nameValuePairs.add(new BasicNameValuePair("country", ""));
            nameValuePairs.add(new BasicNameValuePair("interests", ""));
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
        //Toast.makeText(context,result,Toast.LENGTH_LONG).show();
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
                JSONObject dataobj=jobj.getJSONObject("data");
                String token=jobj.getString("token");
                String user_id=dataobj.getString("user_id");
                String name=dataobj.getString("name");
                String email=dataobj.getString("email");
                String dob=dataobj.getString("date_of_birth");
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
