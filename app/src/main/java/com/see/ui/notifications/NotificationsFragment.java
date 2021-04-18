package com.see.ui.notifications;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import com.listeners.AddadRequestListener;
import com.listeners.CategoriesRequestListener;
import com.see.AddAd;
import com.see.AddWebService;
import com.see.GetCategories;
import com.see.Login;
import com.see.R;
import com.see.Register;
import com.see.ui.home.HomeFragment;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;

public class NotificationsFragment extends Fragment {

    AddadRequestListener requestlistener;
    EditText _titleText,_typeText,_interestText,_countryText,_cityText,_amountText;
    Spinner _categoryText,_ageText;
    Button _AddadButton;
    TextView _fromText,_toText;
    Calendar myCalendar;
    boolean fromdate=false;
    boolean todate=false;
     ArrayList<HashMap<String, String>> catlist = new ArrayList<>();
     ArrayList<String> clistvals = new ArrayList();
    CategoriesRequestListener crequestListener;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.addad, container, false);
        if(HomeFragment.timer==null)
        {}
        else
        {
        HomeFragment.timer.cancel();
        }
        crequestListener = new CategoriesRequestListener() {

            @Override
            public void onSuccess(String result) {
                // TODO Auto-generated method stub
                //Toast.makeText(getActivity(),result,Toast.LENGTH_LONG).show();
                try {
                    JSONObject jobj = new JSONObject(result);
                    for (int i=0;i<jobj.getJSONArray("data").length();i++)
                    {
                        JSONObject jitem=jobj.getJSONArray("data").getJSONObject(i);
                        {
                            HashMap<String, String> _item = new HashMap<>();
                            _item.put("title", jitem.getString("title"));
                            _item.put("id", jitem.getString("id"));
                            catlist.add(_item);
                            clistvals.add(jitem.getString("title"));

                        }
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, clistvals);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    _categoryText.setAdapter(adapter);

                }
                catch(Exception e)
                {

                }
            }

            @Override
            public void onError(String result) {
                // TODO Auto-generated method stub
                Toast.makeText(getActivity(),result,Toast.LENGTH_LONG).show();


            }

        };
        new GetCategories(getActivity(), crequestListener).execute();
        ImageView logout_btn=(ImageView) root.findViewById(R.id.logout_btn);
        logout_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new AlertDialog.Builder(getActivity())
                        .setTitle(getResources().getString(R.string.logout))
                        .setMessage(getResources().getString(R.string.dailog_logout))

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Continue with delete operation
                                SharedPreferences preferences = getActivity().getSharedPreferences("userdetails", MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString("token","");
                                editor.putString("user_id","");
                                editor.putString("name","");
                                editor.putString("email","");
                                editor.putString("phone","");
                                editor.putString("level","");
                                editor.putString("ad_views","");
                                editor.apply();

                                Intent i=new Intent(getActivity(), Login.class);
                                startActivity(i);
                                getActivity().finish();
                            }
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(getResources().getString(R.string.no), null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });
        myCalendar = Calendar.getInstance();
        _titleText=(EditText)root.findViewById(R.id.input_title);
        _typeText=(EditText)root.findViewById(R.id.input_type);
        _fromText=(TextView)root.findViewById(R.id.input_fromdate);
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "MM/dd/yy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                if(fromdate)
                {
                    _fromText.setText(sdf.format(myCalendar.getTime()));
                }
                if(todate)
                {
                    _toText.setText(sdf.format(myCalendar.getTime()));
                }
            }

        };
        _fromText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getActivity(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                fromdate=true;
                todate=false;

            }
        });

        _toText=(TextView)root.findViewById(R.id.input_todate);
        _toText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getActivity(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                fromdate=false;
                todate=true;

            }
        });
        _categoryText=(Spinner)root.findViewById(R.id.input_category);
        _interestText=(EditText)root.findViewById(R.id.input_intrest);
        _ageText=(Spinner)root.findViewById(R.id.input_age);
        _countryText=(EditText)root.findViewById(R.id.input_country);
        _cityText=(EditText)root.findViewById(R.id.input_city);
        _amountText=(EditText)root.findViewById(R.id.input_amount);
        _AddadButton=(Button)root.findViewById(R.id.btn_Addad);
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
                Toast.makeText(getActivity(),msg,Toast.LENGTH_LONG).show();
            }


            @Override
            public void onError(String result) {
                // TODO Auto-generated method stub
                Toast.makeText(getActivity(),result,Toast.LENGTH_LONG).show();


            }

        };
        return root;
    }
    public void AddAd() {


        if (!validate()) {
            onAddAdFailed();
            return;
        }

        String title = _titleText.getText().toString();
        String type = _typeText.getText().toString();
        String fromdate = _fromText.getText().toString();
        String todate = _toText.getText().toString();
       String category = catlist.get(_categoryText.getSelectedItemPosition()).get("id");
        String interests = _interestText.getText().toString();
       // String age = _ageText.getText().toString();
        String country = _countryText.getText().toString();
        String city = _cityText.getText().toString();
        String amount = _amountText.getText().toString();

        new AddWebService(getActivity(), requestlistener, title, type , fromdate, todate, _ageText.getSelectedItem().toString(),category,country,city,amount,"","").execute();

    }


    public void onAddAdSuccess() {
        _AddadButton.setEnabled(true);
        //setResult(RESULT_OK, null);

    }

    public void onAddAdFailed() {
        Toast.makeText(getActivity(), "Enable to post", Toast.LENGTH_LONG).show();

        _AddadButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;


        String title = _titleText.getText().toString();
        String type = _typeText.getText().toString();
        String fromdate = _fromText.getText().toString();
        String todate = _toText.getText().toString();
        //String category = _categoryText.getText().toString();
        String interests = _interestText.getText().toString();

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

        return valid;
    }
}
