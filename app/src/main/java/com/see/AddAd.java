package com.see;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.listeners.AddadRequestListener;
import com.listeners.CategoriesRequestListener;
import com.listeners.RequestListener;
import com.see.ui.home.HomeFragment;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
public class AddAd extends AppCompatActivity {
    private static final String TAG = "AddAd Activity";
    AddadRequestListener requestlistener;
    CategoriesRequestListener crequestListener;
    @BindView(R.id.input_title) EditText _titleText;
    @BindView(R.id.input_type) EditText _typeText;
    @BindView(R.id.input_fromdate) TextView _fromText;
    @BindView(R.id.input_todate) TextView _toText;
    @BindView(R.id.input_category) Spinner _categoryText;
    @BindView(R.id.input_intrest) EditText _interestText;
    @BindView(R.id.input_age) EditText _ageText;
    @BindView(R.id.input_country) EditText _countryText;
    @BindView(R.id.input_city) EditText _cityText;
    @BindView(R.id.input_amount) EditText _amountText;
    @BindView(R.id.btn_Addad) Button _AddadButton;
    private ArrayList<HashMap<String, Object>> catlist = new ArrayList<>();
    private ArrayList<String> clistvals = new ArrayList();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences editorr=AddAd.this.getSharedPreferences("Settings",MODE_PRIVATE);
        if(editorr.getString("My_Lang", "").equalsIgnoreCase("ur"))
        {
            setContentView(R.layout.addadur);
        }
        else
        {
            setContentView(R.layout.addad);
        }

        ButterKnife.bind(this);



        _AddadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddAd();
            }
        });

        requestlistener = new AddadRequestListener() {

            @Override
            public void onSuccess(String msg) {
                // TODO Auto-generated method stub
                Toast.makeText(AddAd.this,msg,Toast.LENGTH_LONG).show();
            }


            @Override
            public void onError(String result) {
                // TODO Auto-generated method stub
                Toast.makeText(AddAd.this,result,Toast.LENGTH_LONG).show();


            }

        };


    }

    public void AddAd() {
        Log.d(TAG, "AddAd");

        if (!validate()) {
            onAddAdFailed();
            return;
        }

        String title = _titleText.getText().toString();
        String type = _typeText.getText().toString();
        String fromdate = _fromText.getText().toString();
        String todate = _toText.getText().toString();
        //String category = _categoryText.getText().toString();
        String interests = _interestText.getText().toString();
        String age = _ageText.getText().toString();
        String country = _countryText.getText().toString();
        String city = _cityText.getText().toString();
        String amount = _amountText.getText().toString();
       // String amount = _amountText.getText().toString();

        new AddWebService(AddAd.this, requestlistener, title, type , fromdate, todate, "",age,country,city,amount,"","").execute();

    }


    public void onAddAdSuccess() {
        _AddadButton.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    public void onAddAdFailed() {
        Toast.makeText(getBaseContext(), "Unable to post", Toast.LENGTH_LONG).show();

        _AddadButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;


        String title = _titleText.getText().toString();
        String type = _typeText.getText().toString();
        String fromdate = _fromText.getText().toString();
        String todate = _toText.getText().toString();
       // String category = _categoryText.getText().toString();
        String interests = _interestText.getText().toString();
        String age = _ageText.getText().toString();
        String country = _countryText.getText().toString();
        String city = _cityText.getText().toString();
        String amount = _amountText.getText().toString();

        if (title.isEmpty() || title.length() < 3) {
            _titleText.setError("at least 3 characters");
            valid = false;
        } else {
            _titleText.setError(null);
        }

        if (fromdate.isEmpty()) {
            _fromText.setError("Enter Valid Date");
            valid = false;
        } else {
            _fromText.setError(null);
        }

        if (todate.isEmpty()) {
            _toText.setError("Enter Valid Date");
            valid = false;
        } else {
            _toText.setError(null);
        }


        //  if (todate.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
        //   _emailText.setError("enter a valid email address");
        //    valid = false;
        //  } else {
        //     _emailText.setError(null);
        //  }



        // if (country.isEmpty() || password.length() < 4 || password.length() > 10) {
        //   _passwordText.setError("between 4 and 10 alphanumeric characters");
        // valid = false;
        //} else {
        //  _passwordText.setError(null);
        //}

        //if (amount.isEmpty() || cnic.length() < 13) {
        //  _cnicText.setError("CNIC cannot be less than 13 digits");
        //   valid = false;
        // } else {
        //    _cnicText.setError(null);
        // }
        if (amount.isEmpty() ) {
            _amountText.setError("Enter an amount");
            valid = false;
        } else {

        }
        return valid;
    }
}
