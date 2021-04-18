package com.see;
import android.Manifest;
import android.app.AlertDialog;
import android.app.KeyguardManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.util.Log;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.listeners.RequestListener;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Locale;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import butterknife.ButterKnife;
import butterknife.BindView;

public class Login extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    RequestListener requestlistener;
    @BindView(R.id.input_phone) EditText _phoneText;
    @BindView(R.id.input_password) EditText _passwordText;
    @BindView(R.id.btn_login) Button _loginButton;
    @BindView(R.id.link_signup) TextView _signupLink;
    @BindView(R.id.imageView) ImageView imageView;
    static ImageView fpicon;

    private static final String KEY_NAME = "yourKey";
    private Cipher cipher;
    private KeyStore keyStore;
    private KeyGenerator keyGenerator;
    private TextView textView;
    private FingerprintManager.CryptoObject cryptoObject;
    private FingerprintManager fingerprintManager;
    private KeyguardManager keyguardManager;
    static AlertDialog.Builder builder;
    static  AlertDialog alertDialog;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();
        SharedPreferences editorr=Login.this.getSharedPreferences("Settings",MODE_PRIVATE);
        if(editorr.getString("My_Lang", "").equalsIgnoreCase("ur"))
        {
            setContentView(R.layout.loginur);
        }
        else
        {
            setContentView(R.layout.activity_login);
        }
        Button demo = findViewById(R.id.demo);
        demo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(Login.this,IntroSlider.class);
                startActivity(in);
                //finish();

            }
        });

        Button changeLang = findViewById(R.id.cahngelanguage);
        changeLang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChangeLanguageDialoge();
            }
        });

        SharedPreferences preferences = Login.this.getSharedPreferences("userdetails", MODE_PRIVATE);
        if(!preferences.getString("name", "").equalsIgnoreCase(""))
        {
            Intent in=new Intent(Login.this,Dashboard.class);
            startActivity(in);
            finish();
            return;
        }
        ButterKnife.bind(this);
        requestlistener = new RequestListener() {

            @Override
            public void onSuccess(String msg,String token,String user_id,String name,String email,String dob,String phone,String cnic,String level,String ad_views) {
                // TODO Auto-generated method stub
                Toast.makeText(Login.this,msg,Toast.LENGTH_LONG).show();

                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("token",token);
                editor.putString("user_id",user_id);
                editor.putString("name",name);
                editor.putString("email",email);
                editor.putString("date_of_birth",dob);
                editor.putString("phone",phone);
                editor.putString("cnic",cnic);
                editor.putString("level",level);
                editor.putString("ad_views",ad_views);
                editor.apply();

                Intent i=new Intent(Login.this,Dashboard.class);
                startActivity(i);
                finish();
            }

            @Override
            public void onError(String result) {
                // TODO Auto-generated method stub
               Toast.makeText(Login.this,result,Toast.LENGTH_LONG).show();


            }

        };
        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), Register.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Declare a string variable for the key we’re going to use in our fingerprint authentication

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    //Get an instance of KeyguardManager and FingerprintManager//
                    keyguardManager =
                            (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
                    fingerprintManager =
                            (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);



                    //Check whether the device has a fingerprint sensor//
                    if (!fingerprintManager.isHardwareDetected()) {
                        // If a fingerprint sensor isn’t available, then inform the user that they’ll be unable to use your app’s fingerprint functionality//
                        Toast.makeText(Login.this, "Your device doesn't support fingerprint authentication", Toast.LENGTH_LONG).show();

                    }
                    //Check whether the user has granted your app the USE_FINGERPRINT permission//
                    else if (ActivityCompat.checkSelfPermission(Login.this, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
                        // If your app doesn't have this permission, then display the following text//
                        Toast.makeText(Login.this, "Please enable the fingerprint permission", Toast.LENGTH_LONG).show();


                    }

                    //Check that the user has registered at least one fingerprint//
                    else  if (!fingerprintManager.hasEnrolledFingerprints()) {
                        // If the user hasn’t configured any fingerprints, then display the following message//
                        Toast.makeText(Login.this, "No fingerprint configured. Please register at least one fingerprint in your device's Settings", Toast.LENGTH_LONG).show();

                    }

                    //Check that the lockscreen is secured//
                    else  if (!keyguardManager.isKeyguardSecure()) {
                        // If the user hasn’t secured their lockscreen with a PIN password or pattern, then display the following text//
                        Toast.makeText(Login.this, "Please enable lockscreen security in your device's Settings", Toast.LENGTH_LONG).show();


                    } else {
                        try {
                            generateKey();
                        } catch (Login.FingerprintException e) {

                        }

                        if (initCipher()) {
                            //If the cipher is initialized successfully, then create a CryptoObject instance//
                            cryptoObject = new FingerprintManager.CryptoObject(cipher);

                            // Here, I’m referencing the FingerprintHandler class that we’ll create in the next section. This class will be responsible
                            // for starting the authentication process (via the startAuth method) and processing the authentication process events//
                            FingerprintHandler helper = new FingerprintHandler(Login.this);
                            helper.startAuth(fingerprintManager, cryptoObject);
                        }
                    }
                }
                builder = new AlertDialog.Builder(Login.this);
                ViewGroup viewGroup = findViewById(android.R.id.content);
                View dialogView = LayoutInflater.from(v.getContext()).inflate(R.layout.customdialog, viewGroup, false);
                builder.setView(dialogView);
                Button dialogButton = (Button) dialogView.findViewById(R.id.buttonOk);
                fpicon= (ImageView) dialogView.findViewById(R.id.fpicon);
                // if button is clicked, close the custom dialog


                alertDialog = builder.create();
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                alertDialog.show();
            }
        });



    }

    private void showChangeLanguageDialoge() {
        final String[] listItems ={"English","اردو"};
        AlertDialog.Builder mBuilder=new AlertDialog.Builder(Login.this);
        mBuilder.setTitle("Choose Language....");
        mBuilder.setSingleChoiceItems(listItems, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                if (i==0){
                    setLocale("en");
                    recreate();
                }
                else if (i==1){
                    setLocale("ur");
                    recreate();

                }
                dialog.dismiss();
            }
        });
        AlertDialog mDialog=mBuilder.create();
        mDialog.show();

    }

    private void setLocale(String lang) {
        Locale locale=new Locale(lang);
        Locale.setDefault(locale);
        Configuration config =new  Configuration();
        config.locale=locale;
        getBaseContext().getResources().updateConfiguration(config,getBaseContext().getResources().getDisplayMetrics());
        SharedPreferences.Editor editor=getSharedPreferences("Settings",MODE_PRIVATE).edit();
        editor.putString("My_Lang",lang);
        editor.apply();

    }
    public void loadLocale(){
        SharedPreferences prefs=getSharedPreferences("Settings",MODE_PRIVATE);
        String language=prefs.getString("My_Lang","");
        setLocale(language);
    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }



        String phone = _phoneText.getText().toString();
        String password = _passwordText.getText().toString();

        // TODO: Implement your own authentication logic here.

        new LoginWebService(Login.this, requestlistener, phone, password).execute();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();

            }
        }
    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        _loginButton.setEnabled(true);
        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String phone = _phoneText.getText().toString();
        String password = _passwordText.getText().toString();

        if (phone.isEmpty() ) {
            _phoneText.setError("enter a valid mobile number");
            valid = false;
        } else {
            _phoneText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }
    private void generateKey() throws Login.FingerprintException {
        try {
            // Obtain a reference to the Keystore using the standard Android keystore container identifier (“AndroidKeystore”)//
            keyStore = KeyStore.getInstance("AndroidKeyStore");

            //Generate the key//
            keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");

            //Initialize an empty KeyStore//
            keyStore.load(null);

            //Initialize the KeyGenerator//
            keyGenerator.init(new

                    //Specify the operation(s) this key can be used for//
                    KeyGenParameterSpec.Builder(KEY_NAME,
                    KeyProperties.PURPOSE_ENCRYPT |
                            KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)

                    //Configure this key so that the user has to confirm their identity with a fingerprint each time they want to use it//
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(
                            KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .build());

            //Generate the key//
            keyGenerator.generateKey();

        } catch (KeyStoreException
                | NoSuchAlgorithmException
                | NoSuchProviderException
                | InvalidAlgorithmParameterException
                | CertificateException
                | IOException exc) {
            exc.printStackTrace();
            throw new Login.FingerprintException(exc);
        }
    }

    //Create a new method that we’ll use to initialize our cipher//
    public boolean initCipher() {
        try {
            //Obtain a cipher instance and configure it with the properties required for fingerprint authentication//
            cipher = Cipher.getInstance(
                    KeyProperties.KEY_ALGORITHM_AES + "/"
                            + KeyProperties.BLOCK_MODE_CBC + "/"
                            + KeyProperties.ENCRYPTION_PADDING_PKCS7);
        } catch (NoSuchAlgorithmException |
                NoSuchPaddingException e) {
            throw new RuntimeException("Failed to get Cipher", e);
        }

        try {
            keyStore.load(null);
            SecretKey key = (SecretKey) keyStore.getKey(KEY_NAME,
                    null);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            //Return true if the cipher has been initialized successfully//
            return true;
        } catch (KeyPermanentlyInvalidatedException e) {

            //Return false if cipher initialization failed//
            return false;
        } catch (KeyStoreException | CertificateException
                | UnrecoverableKeyException | IOException
                | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Failed to init Cipher", e);
        }
    }

    private class FingerprintException extends Exception {
        public FingerprintException(Exception e) {
            super(e);
        }
    }
}