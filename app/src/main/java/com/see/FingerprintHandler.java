package com.see;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.Manifest;
import android.os.CancellationSignal;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.listeners.RequestListener;
import com.see.ui.GetUserByDevice;

import static android.content.Context.MODE_PRIVATE;

public class FingerprintHandler extends FingerprintManager.AuthenticationCallback {

    // You should use the CancellationSignal method whenever your app can no longer process user input, for example when your app goes
    // into the background. If you don’t use this method, then other apps will be unable to access the touch sensor, including the lockscreen!//

    private CancellationSignal cancellationSignal;
    private Context context;
    RequestListener requestlistener;
    public FingerprintHandler(Context mContext) {
        context = mContext;
    }

    //Implement the startAuth method, which is responsible for starting the fingerprint authentication process//

    public void startAuth(FingerprintManager manager, FingerprintManager.CryptoObject cryptoObject) {

        cancellationSignal = new CancellationSignal();
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        manager.authenticate(cryptoObject, cancellationSignal, 0, this, null);
    }

    @Override
    //onAuthenticationError is called when a fatal error has occurred. It provides the error code and error message as its parameters//

    public void onAuthenticationError(int errMsgId, CharSequence errString) {

        //I’m going to display the results of fingerprint authentication as a series of toasts.
        //Here, I’m creating the message that’ll be displayed if an error occurs//

        Toast.makeText(context, "Authentication error\n" + errString, Toast.LENGTH_LONG).show();
    }

    @Override

    //onAuthenticationFailed is called when the fingerprint doesn’t match with any of the fingerprints registered on the device//

    public void onAuthenticationFailed() {
        Toast.makeText(context, "Authentication failed", Toast.LENGTH_LONG).show();
    }

    @Override

    //onAuthenticationHelp is called when a non-fatal error has occurred. This method provides additional information about the error,
    //so to provide the user with as much feedback as possible I’m incorporating this information into my toast//
    public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
        Toast.makeText(context, "Authentication help\n" + helpString, Toast.LENGTH_LONG).show();
    }@Override

    //onAuthenticationSucceeded is called when a fingerprint has been successfully matched to one of the fingerprints stored on the user’s device//
    public void onAuthenticationSucceeded(
            FingerprintManager.AuthenticationResult result) {

        Login.fpicon.setImageResource(R.drawable.fingerprint_white_24dp);
        Login.alertDialog.dismiss();
        requestlistener = new RequestListener() {

            @Override
            public void onSuccess(String msg,String token,String user_id,String name,String email,String dob,String phone,String cnic,String level,String ad_views) {
                // TODO Auto-generated method stub
                Toast.makeText(context, "Authentication Successful!", Toast.LENGTH_LONG).show();
                SharedPreferences preferences =context.getSharedPreferences("userdetails", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("token",token);
                editor.putString("user_id",user_id);
                editor.putString("name",name);
                editor.putString("email",email);
                editor.putString("dob",dob);
                editor.putString("phone",phone);
                editor.putString("cnic",cnic);
                editor.putString("level",level);
                editor.putString("ad_views",ad_views);
                editor.apply();

              Intent i=new Intent(context,Login.class);
                context.startActivity(i);
            }

            @Override
            public void onError(String result) {
                // TODO Auto-generated method stub
                Toast.makeText(context,result,Toast.LENGTH_LONG).show();


            }

        };
        new GetUserByDevice(context, requestlistener).execute();

    }

}
