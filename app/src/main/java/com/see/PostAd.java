package com.see;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.listeners.AddadRequestListener;
import com.listeners.CategoriesRequestListener;
import com.manager.RetrofitClient;
import com.model.AddClass;
import com.see.ui.LogoutWebservice;
import com.service.PostAdService;

import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostAd extends AppCompatActivity {
    AddadRequestListener requestlistener;
    private ProgressDialog mProgressDialog;
    private Context context;
    EditText _titleText,_typeText,_interestText,_countryText,_cityText,_amountText,_ad_urlText;
    Spinner _categoryText,_ageText;
    public static final int RequestPermissionCode = 1;
    public static final int RequestSelectFileCode = 961;
    Button _AddadButton,uploadVideo,uploadThumbnail;
    TextView _fromText,_toText,videopathtxt,imagepathtxt;
    Calendar myCalendar;
    ImageView imageView;

    File file,imgfile;

    AddClass addClass;

    private Uri selectedVideoUri;
    private Uri selectedImageUri;
    private String selectedPath;
    private String imagePath;
    boolean fromdate=false;
    boolean todate=false;
    private static final int SELECT_IMAGE = 2;
    private static final int SELECT_VIDEO = 1;
    ArrayList<HashMap<String, String>> catlist = new ArrayList<>();
    ArrayList<String> clistvals = new ArrayList();
    ArrayList<HashMap<String, String>> agelist = new ArrayList<>();
    ArrayList<String> arrayList = new ArrayList();
    CategoriesRequestListener crequestListener;
    CategoriesRequestListener logoutrequestListener;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        SharedPreferences editorr=PostAd.this.getSharedPreferences("Settings",MODE_PRIVATE);
        if(editorr.getString("My_Lang", "").equalsIgnoreCase("ur"))
        {
            setContentView(R.layout.postaddur);
        }
        else
        {
            setContentView(R.layout.activity_postad);
        }
        askForPermission(Manifest.permission.READ_EXTERNAL_STORAGE, RequestPermissionCode);
        logoutrequestListener= new CategoriesRequestListener() {

            @Override
            public void onSuccess(String result) {
                // TODO Auto-generated method stub
                SharedPreferences preferences = PostAd.this.getSharedPreferences("userdetails", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("token","");
                editor.putString("user_id","");
                editor.putString("name","");
                editor.putString("email","");
                editor.putString("phone","");
                editor.putString("level","");
                editor.putString("ad_views","");
                editor.apply();
                // Toast.makeText(getActivity(),result,Toast.LENGTH_LONG).show();
                Intent i=new Intent(PostAd.this, Login.class);
                startActivity(i);
                finish();
            }


            @Override
            public void onError(String result) {
                // TODO Auto-generated method stub
                Toast.makeText(PostAd.this,result,Toast.LENGTH_LONG).show();


            }

        };
        crequestListener = new CategoriesRequestListener() {

            @Override
            public void onSuccess(String result) {
                // TODO Auto-generated method stub
                //Toast.makeText(PostAd.this,result,Toast.LENGTH_LONG).show();
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

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(PostAd.this, android.R.layout.simple_spinner_item, clistvals);
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
                Toast.makeText(PostAd.this,result,Toast.LENGTH_LONG).show();


            }

        };
        new GetCategories(PostAd.this, crequestListener).execute();
        myCalendar = Calendar.getInstance();
        _titleText=(EditText)findViewById(R.id.input_title);
        _typeText=(EditText)findViewById(R.id.input_views);
       // _typeText=(EditText)findViewById(R.id.input_type);
        videopathtxt=(TextView)findViewById(R.id.selectvideo);
      //  imageView=(ImageView)findViewById(R.id.imageViewad) ;
        imagepathtxt=(TextView)findViewById(R.id.selectimage);
        _fromText=(TextView)findViewById(R.id.input_fromdate);
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
      /* uploadVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //uploadFile();//kia h yek? mjhy func dekhao jaha filename ly rhy ap?

            }*/
     //   });
        _fromText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(PostAd.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                fromdate=true;
                todate=false;

            }
        });
        imagepathtxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                chooseImage();
            }
        });
     videopathtxt.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
          chooseVideo();
         }
     });

        _toText=(TextView)findViewById(R.id.input_todate);
        _toText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(PostAd.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                fromdate=false;
                todate=true;

            }
        });
        _categoryText=(Spinner)findViewById(R.id.input_category);
       // _interestText=(EditText)findViewById(R.id.input_intrest);
        _ageText=(Spinner)findViewById(R.id.input_age);
        _countryText=(EditText)findViewById(R.id.input_country);
        _cityText=(EditText)findViewById(R.id.input_city);
       // _amountText=(EditText)findViewById(R.id.input_amount);
        uploadThumbnail=(Button)findViewById(R.id.upload_thumbnail);
        _ad_urlText=(EditText)findViewById(R.id.input_adurl);
        _AddadButton=(Button)findViewById(R.id.btn_Addad);
        _AddadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddAd();
            }
        });
        _categoryText.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                // sometimes you need nothing here
            }
        });
        _ageText.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                // sometimes you need nothing here
            }
        });
        requestlistener = new AddadRequestListener() {

            @Override
            public void onSuccess(String msg) {
                // TODO Auto-generated method stub
                Toast.makeText(PostAd.this,msg,Toast.LENGTH_LONG).show();
            }


            @Override
            public void onError(String result) {
                // TODO Auto-generated method stub
                Toast.makeText(PostAd.this,result,Toast.LENGTH_LONG).show();


            }

        };
        ImageView back_btn = (ImageView) findViewById(R.id.back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
        ImageView logout_btn=(ImageView)findViewById(R.id.logout_btn);
        logout_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new AlertDialog.Builder(PostAd.this)
                        .setTitle(getResources().getString(R.string.logout))
                        .setMessage(getResources().getString(R.string.dailog_logout))

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Continue with delete operation
                                new LogoutWebservice(PostAd.this,logoutrequestListener).execute();

                                SharedPreferences preferences = PostAd.this.getSharedPreferences("userdetails", MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString("token","");
                                editor.putString("user_id","");
                                editor.putString("name","");
                                editor.putString("email","");
                                editor.putString("phone","");
                                editor.putString("level","");
                                editor.putString("ad_views","");
                                editor.apply();

                                Intent i=new Intent(PostAd.this, Login.class);
                                startActivity(i);
                                finishAffinity();
                            }
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(getResources().getString(R.string.no), null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });
    }

 /*   private void uploadFile() {
        if(selectedVideoUri==null||selectedVideoUri.equals("")){
            Toast.makeText(PostAd.this,"Please Select a video",Toast.LENGTH_LONG).show();
return;
        }else
            Map<String, RequestBody> map=new HashMap<>();
            File file = new File(selectedPath);
            RequestBody requestBody =RequestBody.create(MediaType.parse(""),file);
            map.Put()
    }*/

    private void chooseImage() {
        if (askForPermission(Manifest.permission.READ_EXTERNAL_STORAGE, RequestPermissionCode)) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select an Image "), SELECT_IMAGE);
        }


    }


    public String getPathh(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }


    private void chooseVideo() {
        if (askForPermission(Manifest.permission.READ_EXTERNAL_STORAGE, RequestPermissionCode)) {
            Intent intent = new Intent();
            intent.setType("video/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select a Video "), SELECT_VIDEO);
        }
    }

    private boolean askForPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(PostAd.this, permission) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(PostAd.this, permission)) {

                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(PostAd.this, new String[]{permission}, requestCode);

            } else {

                ActivityCompat.requestPermissions(PostAd.this, new String[]{permission}, requestCode);
            }
        } else {

            return true;
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_VIDEO) {
                System.out.println("SELECT_VIDEO");
                 selectedVideoUri = data.getData();
                selectedPath = getPath(selectedVideoUri);
                videopathtxt.setText(selectedPath);

            }else if(requestCode==SELECT_IMAGE){

                System.out.println("SELECT_IMAGE");
                 selectedImageUri = data.getData();
                selectedPath = getPathh(selectedImageUri);
                imagepathtxt.setText(selectedPath);
            }
        }
    }

        public String getPath(Uri uri) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            cursor.moveToFirst();
            String document_id = cursor.getString(0);
            document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
            cursor.close();

            cursor = getContentResolver().query(
                    android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                    null, MediaStore.Video.Media._ID + " = ? ", new String[]{document_id}, null);
            cursor.moveToFirst();
            String path = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
            cursor.close();

            return path;
        }
    private boolean askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(PostAd.this, permission) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(PostAd.this, permission)) {

                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(PostAd.this, new String[]{permission}, requestCode);

            } else {

                ActivityCompat.requestPermissions(PostAd.this, new String[]{permission}, requestCode);
            }
        } else {

            return true;
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int RC, String per[], int[] PResult) {
        switch (RC) {

            case RequestPermissionCode:

                if (PResult.length > 0 && PResult[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(PostAd.this, "Permission Granted, Application can access External Storage.", Toast.LENGTH_LONG).show();
                } else {

                    Toast.makeText(PostAd.this, "Permission Canceled,Application cannot access External Storage.", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }




    @Override
    public void onBackPressed() {
        finish();
    }
    public void AddAd() {


        if (!validate()) {
            onAddAdFailed();
            return;
        }

    /* String title = _titleText.getText().toString();
        String type = _typeText.getText().toString();
        String fromdate = _fromText.getText().toString();
        String todate = _toText.getText().toString();
        String category = catlist.get(_categoryText.getSelectedItemPosition()).get("id");
//        String interests = _interestText.getText().toString();
     //  String age = agelist.get(_ageText.getSelectedItemPosition()).get("id");
        String country = _countryText.getText().toString();
        String city = _cityText.getText().toString();
       // String amount = _amountText.getText().toString();
        String ad_url = _ad_urlText.getText().toString();

*/
       /* mProgressDialog = new ProgressDialog(context,   R.style.AppTheme_Dark_Dialog);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage(context.getResources().getString(R.string.processing));
*/
        if(selectedVideoUri==null) {
            Log.d("VIDEOURL", "No Video Url");
        }else{
            file=new File(getPath(selectedVideoUri));
        }

        if(selectedImageUri==null){
            Toast.makeText(this, "no image", Toast.LENGTH_SHORT).show();
        }else{
            imgfile=new File(getPathh(selectedImageUri));
        }
        SharedPreferences preferences = PostAd.this.getSharedPreferences("userdetails", MODE_PRIVATE);
       // RequestBody token=RequestBody.cre("Authorization",preferences.getString("token", ""));
        RequestBody token = RequestBody.create(MediaType.parse("text/plain"), (Objects.requireNonNull(preferences.getString("token", ""))));
       // RequestBody ad_title = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(_titleText.getText().toString()));
        RequestBody ad_title = RequestBody.create(MediaType.parse("text/plain"), "add");
        RequestBody ad_type = RequestBody.create(MediaType.parse("text/plain"), "4");
      //  RequestBody ad_views = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(_typeText.getText().toString()));
        RequestBody ad_views = RequestBody.create(MediaType.parse("text/plain"), "1");
     //   RequestBody ad_start = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(_fromText.getText().toString()));
        RequestBody ad_start = RequestBody.create(MediaType.parse("text/plain"), "2020-7-08");
       // RequestBody ad_end = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(_toText.getText().toString()));
        RequestBody ad_end = RequestBody.create(MediaType.parse("text/plain"), "2020-17-22");
    //    RequestBody ad_categoryid = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(catlist.get(_categoryText.getSelectedItemPosition()).get("id")));
        RequestBody ad_categoryid = RequestBody.create(MediaType.parse("text/plain"), String.valueOf("1"));

        RequestBody ad_age = RequestBody.create(MediaType.parse("text/plain"), "18-25");
      //  RequestBody ad_country = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(_countryText.getText().toString()));
        RequestBody ad_country = RequestBody.create(MediaType.parse("text/plain"), "PAKISTAN");
       // RequestBody ad_city = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(_cityText.getText().toString()));
        RequestBody ad_city = RequestBody.create(MediaType.parse("text/plain"), "islamabad");
      //  RequestBody ad_urll = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(_ad_urlText.getText().toString()));
        RequestBody ad_urll = RequestBody.create(MediaType.parse("text/plain"), "www.gulahmed.com");
        RequestBody requestFile = RequestBody.create(MediaType.parse("video/*"), file);
        RequestBody imagerequestFile = RequestBody.create(MediaType.parse("image/*"), imgfile);

        MultipartBody.Part filename = MultipartBody.Part.createFormData("filename", file.getName(), requestFile);
        MultipartBody.Part thumb = MultipartBody.Part.createFormData("thumb", imgfile.getName(), imagerequestFile);


        PostAdService service= RetrofitClient.getClient().create(PostAdService.class);
        Call<AddClass> call=service.postadd(token,ad_title,ad_type,ad_start,ad_end,ad_categoryid,ad_age,ad_views,ad_country,ad_city,ad_urll,filename,thumb);
        call.enqueue(new Callback<AddClass>() {
            @Override
            public void onResponse(Call<AddClass> call, Response<AddClass> response) {
                if(response.isSuccessful()){
                    addClass=response.body();
                   // mProgressDialog.dismiss();
                    if(addClass!=null){
                     //   mProgressDialog.show();
                        Toast.makeText(PostAd.this,
                                addClass.getMessage(), Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(PostAd.this,
                                addClass.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }else{
                    Toast.makeText(PostAd.this,
                            addClass.getMessage(), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<AddClass> call, Throwable t) {
                Log.e("lol: ", String.valueOf(t));
                Toast.makeText(PostAd.this,
                        t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    //   new AddWebService(PostAd.this, requestlistener, title,type,fromdate, todate, category,"18",country,city,ad_url,"","").execute();
    }


    public void onAddAdSuccess() {
        _AddadButton.setEnabled(true);
        //setResult(RESULT_OK, null);
    }

    public void onAddAdFailed() {
        Toast.makeText(PostAd.this, "Unable to post", Toast.LENGTH_LONG).show();

        _AddadButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;


        String title = _titleText.getText().toString();
        String type = _typeText.getText().toString();
        String fromdate = _fromText.getText().toString();
        String todate = _toText.getText().toString();
        //String category = _categoryText.getText().toString();
//        String interests = _interestText.getText().toString();

        String country = _countryText.getText().toString();
        String city = _cityText.getText().toString();
//        String amount = _amountText.getText().toString();

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
