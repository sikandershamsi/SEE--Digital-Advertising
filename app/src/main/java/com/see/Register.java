package com.see;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.listeners.RequestListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Register extends AppCompatActivity {
    private static final String TAG = "SignupActivity";
    Calendar myCalendar;
    RequestListener requestlistener;
    @BindView(R.id.input_name) EditText _nameText;
    @BindView(R.id.input_dob) TextView _dobText;
    @BindView(R.id.input_email) EditText _emailText;
    @BindView(R.id.input_mobile) EditText _mobileText;
    @BindView(R.id.input_password) EditText _passwordText;
    @BindView(R.id.input_cnic) EditText _cnicText;
    @BindView(R.id.btn_signup) Button _signupButton;
    @BindView(R.id.link_login) TextView _loginLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences editorr=Register.this.getSharedPreferences("Settings",MODE_PRIVATE);
        if(editorr.getString("My_Lang", "").equalsIgnoreCase("ur"))
        {
            setContentView(R.layout.signupur);
        }
        else
        {
            setContentView(R.layout.activity_signup);;
        }
        SharedPreferences preferences = Register.this.getSharedPreferences("userdetails", MODE_PRIVATE);
        ButterKnife.bind(this);
          myCalendar = Calendar.getInstance();
        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                Intent intent = new Intent(getApplicationContext(),Login.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });

        requestlistener = new RequestListener() {

            @Override
            public void onSuccess(String msg,String token,String user_id,String name,String email,String dob,String phone,String cnic,String level,String ad_views) {
                // TODO Auto-generated method stub

                Toast.makeText(Register.this,msg,Toast.LENGTH_LONG).show();

                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("token",token);
                editor.putString("user_id",user_id);
                editor.putString("name",name);
                editor.putString("email",email);
                editor.putString("date_of_birth",dob);
                editor.putString("phone",phone);
                editor.putString("cnic",phone);
                editor.putString("level",level);
                //editor.putString("ad_views",ad_views);
                editor.apply();
                Intent i=new Intent(Register.this,Dashboard.class);
                startActivity(i);
                finish();
            }

            @Override
            public void onError(String result) {
                // TODO Auto-generated method stub
                Toast.makeText(Register.this,result,Toast.LENGTH_LONG).show();


            }

        };




        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        _dobText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(Register.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });
    }
    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        _dobText.setText(sdf.format(myCalendar.getTime()));
    }
    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        String name = _nameText.getText().toString();
        String dob = _dobText.getText().toString();
        String email = _emailText.getText().toString();
        String mobile = _mobileText.getText().toString();
        String password = _passwordText.getText().toString();
        String cnic = _cnicText.getText().toString();
        new RegisterWebService(Register.this, requestlistener, email, password, name, mobile,cnic,dob).execute();

    }


    public void onSignupSuccess() {
        _signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = _nameText.getText().toString();
        String dob = _dobText.getText().toString();
        String email = _emailText.getText().toString();
        String mobile = _mobileText.getText().toString();
        String password = _passwordText.getText().toString();
        String cnic = _cnicText.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            _nameText.setError("at least 3 characters");
            valid = false;
        } else {
            _nameText.setError(null);
        }

        if (dob.isEmpty()) {
            _dobText.setError("Enter Valid Date of Birth");
            valid = false;
        } else {
            _dobText.setError(null);
        }


        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (mobile.isEmpty() ) {
            _mobileText.setError("Enter Valid Mobile Number");
            valid = false;
        } else {
            _mobileText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        if (cnic.isEmpty() || cnic.length() < 13) {
            _cnicText.setError("CNIC cannot be less than 13 digits");
            valid = false;
        } else {
            _cnicText.setError(null);
        }

        return valid;
    }

}
