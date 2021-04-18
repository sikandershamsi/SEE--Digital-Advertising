package com.see.ui.dashboard;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.listeners.CategoriesRequestListener;
import com.see.AdsListing;
import com.see.Dashboard;
import com.see.GetAds;
import com.see.IntroSlider;
import com.see.Login;
import com.see.R;
import com.see.Register;
import com.see.UpdateProfileWebService;
import com.see.ui.LogoutWebservice;
import com.see.ui.home.HomeFragment;
import com.squareup.picasso.Picasso;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickCancel;
import com.vansuita.pickimage.listeners.IPickResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;

public class ProfileFragment extends Fragment {
    EditText nametext,emailtext,phonetext,city,country,cnic;
    TextView leveltext,dob_edit;
    RadioGroup gender_radio_group;
    RadioButton gender_radio;
    RadioButton RadioMale;
    RadioButton RadioFemale;
    Button btn_updatep;
    String getGender;
    RadioButton male_radio,female_radio;
    String gender="";
    Calendar myCalendar;
    de.hdodenhof.circleimageview.CircleImageView edit_profile_profile_image;
    com.google.android.material.floatingactionbutton.FloatingActionButton edit_profile_change_image_btn;
    CategoriesRequestListener crequestListener;
    CategoriesRequestListener logoutrequestListener;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SharedPreferences editorr = getActivity().getSharedPreferences("Settings", MODE_PRIVATE);
        View root = null;
        setHasOptionsMenu(true);
        if (editorr.getString("My_Lang", "").equalsIgnoreCase("ur")) {
            //show urdu xml

            root = inflater.inflate(R.layout.fragmentprofileur, container, false);

        } else {
            //shown english xml
                root = inflater.inflate(R.layout.fragment_profile, container, false);
        }
        Toolbar toolbar = root.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        logoutrequestListener = new CategoriesRequestListener() {

            @Override
            public void onSuccess(String result) {
                // TODO Auto-generated method stub
                SharedPreferences preferences = getActivity().getSharedPreferences("userdetails", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("token", "");
                editor.putString("user_id", "");
                editor.putString("name", "");
                editor.putString("email", "");
                editor.putString("cnic", "");
                editor.putString("phone", "");
                editor.putString("level", "");
                editor.putString("ad_views", "");
                editor.apply();
               // Toast.makeText(getActivity(),result,Toast.LENGTH_LONG).show();
               //Toast.makeText(getActivity(),preferences.getString("cnic",""),Toast.LENGTH_LONG).show();
                Intent i = new Intent(getActivity(), Login.class);
                startActivity(i);
                getActivity().finish();
            }


            @Override
            public void onError(String result) {
                // TODO Auto-generated method stub
                Toast.makeText(getActivity(), result, Toast.LENGTH_LONG).show();


            }

        };
        crequestListener = new CategoriesRequestListener() {

            @Override
            public void onSuccess(String result) {
                // TODO Auto-generated method stub
             //   Toast.makeText(getActivity(), result, Toast.LENGTH_LONG).show();
                try {
                    JSONObject jsonobj = new JSONObject(result);
                    String name = jsonobj.getJSONArray("data").getJSONObject(0).getString("name");
                    String email = jsonobj.getJSONArray("data").getJSONObject(0).getString("email");
                    String gender = jsonobj.getJSONArray("data").getJSONObject(0).getString("gender");
                    String phone = jsonobj.getJSONArray("data").getJSONObject(0).getString("phone");
                    String cnic = jsonobj.getJSONArray("data").getJSONObject(0).getString("cnic");
                    String city = jsonobj.getJSONArray("data").getJSONObject(0).getString("city");
                    String country = jsonobj.getJSONArray("data").getJSONObject(0).getString("country");
                    String dob = jsonobj.getJSONArray("data").getJSONObject(0).getString("date_of_birth");
                    String photo = jsonobj.getJSONArray("data").getJSONObject(0).getString("photo");
                    SharedPreferences preferences = getActivity().getSharedPreferences("userdetails", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("name", name);
                    editor.putString("email", email);
                    editor.putString("phone", phone);
                    editor.putString("cnic", cnic);
                    editor.putString("city", city);
                    editor.putString("country", country);
                    editor.putString("date_of_birth", dob);
                    editor.putString("gender", gender);
                    editor.putString("photo", photo);
                    editor.apply();
                } catch (Exception a) {


                }


            }


            @Override
            public void onError(String result) {
                // TODO Auto-generated method stub
                Toast.makeText(getActivity(), result, Toast.LENGTH_LONG).show();


            }

        };
        if (HomeFragment.timer == null) {
        } else {
            HomeFragment.timer.cancel();
        }
        myCalendar = Calendar.getInstance();
        edit_profile_change_image_btn = (com.google.android.material.floatingactionbutton.FloatingActionButton) root.findViewById(R.id.edit_profile_change_image_btn);
        edit_profile_profile_image = (de.hdodenhof.circleimageview.CircleImageView) root.findViewById(R.id.edit_profile_profile_image);
        city = (EditText) root.findViewById(R.id.edit_city_edit);
        country = (EditText) root.findViewById(R.id.edit_country_edit);
        cnic = (EditText) root.findViewById(R.id.edit_cnic_edit);
        btn_updatep = (Button) root.findViewById(R.id.btn_updatep);
        gender_radio_group = (RadioGroup) root.findViewById(R.id.gender_radio_group);
        male_radio=root.findViewById(R.id.male_radio);
        female_radio=root.findViewById(R.id.female_radio);
        nametext = (EditText) root.findViewById(R.id.edit_fName_edit);
        emailtext = (EditText) root.findViewById(R.id.edit_lName_edit);
        phonetext = (EditText) root.findViewById(R.id.edit_phone_edit);
        dob_edit = (TextView) root.findViewById(R.id.dob_edit);
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "yyyy-MM-dd"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                dob_edit.setText(sdf.format(myCalendar.getTime()));
            }

        };
        dob_edit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getActivity(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });
        leveltext = (TextView) root.findViewById(R.id.leveltext);
        SharedPreferences preferences = getActivity().getSharedPreferences("userdetails", MODE_PRIVATE);
//       Picasso.get().load(preferences.getString("photo","")).into(edit_profile_profile_image);
        nametext.setText(preferences.getString("name", ""));
        emailtext.setText(preferences.getString("email", ""));
        dob_edit.setText(preferences.getString("date_of_birth", ""));
        phonetext.setText(preferences.getString("phone", ""));
       cnic.setText(preferences.getString("cnic", ""));
       city.setText(preferences.getString("city", ""));
        country.setText(preferences.getString("country", ""));
        leveltext.setText(R.string.level_1); //+preferences.getString("level", ""))
        gender=preferences.getString("gender","");
        if(gender.equals("1")){
            male_radio.setChecked(true);
        }else if(gender.equals("2")){
            female_radio.setChecked(true);
        }
      // gender_radio_group.setSelected(preferences.getString("gender",));
       // Toast.makeText(getActivity(),preferences.getString("photo",""),Toast.LENGTH_LONG).show();
        DatePickerDialog.OnDateSetListener datee = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "yyyy/MM/dd"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                dob_edit.setText(sdf.format(myCalendar.getTime()));
            }

        };
        /**      ImageView logout_btn=(ImageView) root.findViewById(R.id.logout_btn);
         logout_btn.setOnClickListener(new View.OnClickListener() {

        @Override public void onClick(View v) {
        // TODO Auto-generated method stub
        new AlertDialog.Builder(getActivity())
        .setTitle(getResources().getString(R.string.logout))
        .setMessage(getResources().getString(R.string.dailog_logout))


        // Specifying a listener allows you to take an action before dismissing the dialog.
        // The dialog is automatically dismissed when a dialog button is clicked.
        .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
        // Continue with delete operation

        new LogoutWebservice(getActivity(),logoutrequestListener).execute();

        }
        })

        // A null listener allows the button to dismiss the dialog and take no further action.
        .setNegativeButton(getResources().getString(R.string.no), null)
        .setIcon(android.R.drawable.ic_dialog_alert)
        .show();
        }
        });

         dob_edit.setOnClickListener(new View.OnClickListener() {

        @Override public void onClick(View v) {
        // TODO Auto-generated method stub

        new DatePickerDialog(getActivity(), datee, myCalendar
        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
        myCalendar.get(Calendar.DAY_OF_MONTH)).show();

        }
        });***/
        edit_profile_change_image_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 121);
                } else {
                    //val cameraIntent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
                    //startActivityForResult(cameraIntent, CAMERA_REQUEST)


                    PickImageDialog.build(new PickSetup().setTitle("Choose One")
                            .setCameraButtonText("Take Photo")
                            .setGalleryButtonText("Pick from gallery")
                            .setIconGravity(Gravity.LEFT)
                            .setButtonOrientation(LinearLayout.HORIZONTAL)
                            .setSystemDialog(false))
                            .setOnPickResult(new IPickResult() {
                                @Override
                                public void onPickResult(PickResult r) {
                                    //TODO: do what you have to...
                                    Bitmap bitmapImage = r.getBitmap();
                                    edit_profile_profile_image.setImageBitmap(bitmapImage);

                                }
                            })
                            .setOnPickCancel(new IPickCancel() {
                                @Override
                                public void onCancelClick() {
                                    //TODO: do what you have to if user clicked cancel
                                }
                            }).show(getFragmentManager());
                }
            }
        });
        btn_updatep.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (nametext.getText().length() < 1) {
                    nametext.setError("This field is required");
                } else if (emailtext.getText().length() < 1) {
                    emailtext.setError("This field is required");
                } else if (dob_edit.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(getActivity(), "Please select Date of Birth", Toast.LENGTH_LONG).show();
                } else if (phonetext.getText().length() < 1) {
                    phonetext.setError("This field is required");
                } else if (cnic.getText().length() < 1) {
                    cnic.setError("This field is required");
                } else if (city.getText().length() < 1) {
                    city.setError("This field is required");
                } else if (country.getText().length() < 1) {
                    country.setError("This field is required");
                } else if (gender.equalsIgnoreCase("")) {
                    Toast.makeText(getActivity(), "Please Choose Gender", Toast.LENGTH_LONG).show();
                }else {
                    new UpdateProfileWebService(getActivity(), crequestListener, nametext.getText().toString(), emailtext.getText().toString(), phonetext.getText().toString(), gender, dob_edit.getText().toString(), city.getText().toString(), country.getText().toString(), cnic.getText().toString()).execute();

                }
            }


        });

        gender_radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.male_radio:
                        // do operations specific to this selection
                        gender="1";
                        break;
                    case R.id.female_radio:
                        // do operations specific to this selection
                        gender="2";
                        break;

                }
            }
        });

        return root;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.common_menu, menu);
        return ;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.demo) {
            Intent i=new Intent(getActivity(), IntroSlider.class);
            startActivity(i);

        } else if (id == R.id.changelnguage) {
            showChangeLanguageDialoge();

        } else if (id == R.id.logout) {
            new AlertDialog.Builder(getActivity())
                    .setTitle(getResources().getString(R.string.logout))
                    .setMessage(getResources().getString(R.string.dailog_logout))

                    // Specifying a listener allows you to take an action before dismissing the dialog.
                    // The dialog is automatically dismissed when a dialog button is clicked.
                    .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {


                            // Continue with delete operation
                            new LogoutWebservice(getActivity(),logoutrequestListener).execute();

                        }
                    })

                    // A null listener allows the button to dismiss the dialog and take no further action.
                    .setNegativeButton(getResources().getString(R.string.no), null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();


        }

        return true;
    }

    private void showChangeLanguageDialoge() {
        final String[] listItems ={"English","اردو"};
        AlertDialog.Builder mBuilder=new AlertDialog.Builder(getActivity());
        mBuilder.setTitle("Choose Language....");
        mBuilder.setSingleChoiceItems(listItems, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                if (i==0){
                    setLocale("en");
                    getActivity().recreate();
                }
                else if (i==1){
                    setLocale("ur");
                    getActivity().recreate();

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
        getActivity().getResources().updateConfiguration(config,getActivity().getResources().getDisplayMetrics());
        SharedPreferences.Editor editor=getActivity().getSharedPreferences("Settings",MODE_PRIVATE).edit();
        editor.putString("My_Lang",lang);
        editor.apply();

    }
    public void loadLocale(){
        SharedPreferences prefs=getActivity().getSharedPreferences("Settings",MODE_PRIVATE);
        String language=prefs.getString("My_Lang","");
        setLocale(language);
    }



    public  void disableSoftInputFromAppearing(EditText editText) {
        if (Build.VERSION.SDK_INT >= 11) {
            editText.setRawInputType(InputType.TYPE_CLASS_TEXT);
            editText.setTextIsSelectable(true);
        } else {
            editText.setRawInputType(InputType.TYPE_NULL);
            editText.setFocusable(true);
        }
    }
    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            String permissions[],
            int[] grantResults) {
        switch (requestCode) {
            case 121:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getActivity(), "Permission Granted!", Toast.LENGTH_SHORT).show();
                    PickImageDialog.build(new PickSetup().setTitle("Choose One")
                            .setCameraButtonText("Take Photo")
                            .setGalleryButtonText("Pick from gallery")
                            .setIconGravity(Gravity.LEFT)
                            .setButtonOrientation(LinearLayout.HORIZONTAL)
                            .setSystemDialog(false))
                            .setOnPickResult(new IPickResult() {
                                @Override
                                public void onPickResult(PickResult r) {
                                    //TODO: do what you have to...
                                    Bitmap bitmapImage = r.getBitmap();


                                    edit_profile_profile_image.setImageBitmap(bitmapImage);
                                }
                            })
                            .setOnPickCancel(new IPickCancel() {
                                @Override
                                public void onCancelClick() {
                                    //TODO: do what you have to if user clicked cancel
                                }
                            }).show(getFragmentManager());
                } else {
                    Toast.makeText(getActivity(), "Permission Denied!", Toast.LENGTH_SHORT).show();
                }
        }
    }

}