package com.see.ui.home;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.GetCoinsWebService;
import com.listeners.CategoriesRequestListener;
import com.listeners.RequestListener;
import com.see.AddToLotteryWebService;
import com.see.AdsListing;
import com.see.GetCategories;
import com.see.IntroSlider;
import com.see.Login;
import com.see.PlayConsecutiveAds;
import com.see.R;
import com.see.PlayAd;
import com.see.UserAds;
import com.see.ui.LogoutWebservice;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.Context.MODE_PRIVATE;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    String userName="";
    String ad_views="";
    TextView title,videoswatched,points;
    private GridView gridview1;
    ImageView watchad,watchad2,iv;
    static AlertDialog.Builder builder;
    static  AlertDialog AlertDialog;
    RequestListener requestlistener;
    CategoriesRequestListener crequestListener;
    TextView leveltext;
    CategoriesRequestListener logoutrequestListener;
    private static final int REQUEST_LOCATION = 1;
    LocationManager locationManager;
    public static String latitude="0.0", longitude="0.0";
    // Create a new Array of type HashMap
    private ArrayList<HashMap<String, Object>> maplist = new ArrayList<>();
    private ArrayList<String> bannerimageslist = new ArrayList();
    public static float coins=0;
    public static float coinsw=0;
    public static float coinl=0;
ViewPager mvieww;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SharedPreferences editorr=getActivity().getSharedPreferences("Settings",MODE_PRIVATE);
        loadLocale();
        View root=null;
        setHasOptionsMenu(true);
        if(editorr.getString("My_Lang", "").equalsIgnoreCase("ur"))
        {
         //show urdu xml

             root=inflater.inflate(R.layout.fragmenthomeur, container, false);

        }


        else {
            //shown english xml
             root = inflater.inflate(R.layout.fragment_home, container, false);
        }
       Toolbar toolbar=root.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        logoutrequestListener= new CategoriesRequestListener() {

            @Override
            public void onSuccess(String result) {
                // TODO Auto-generated method stub
                if(HomeFragment.timer==null)
                {}
                else
                {
                    HomeFragment.timer.cancel();
                }
                SharedPreferences preferences = getActivity().getSharedPreferences("userdetails", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("token","");
                editor.putString("user_id","");
                editor.putString("name","");
                editor.putString("email","");
                editor.putString("date_of_birth","");
                editor.putString("phone","");
                editor.putString("cnic","");
                editor.putString("level","");
               // editor.putString("offset","");
                editor.putString("ad_views","");
                editor.apply();
             //    Toast.makeText(getActivity(),preferences.getString("offset",""),Toast.LENGTH_LONG).show();
                Intent i=new Intent(getActivity(), Login.class);
                startActivity(i);
                getActivity().finish();
            }


            @Override
            public void onError(String result) {
                // TODO Auto-generated method stub
                Toast.makeText(getActivity(),result,Toast.LENGTH_LONG).show();


            }

        };



        if(HomeFragment.timer==null)
        {}
        else
        {
            HomeFragment.timer.cancel();
        }
        ActivityCompat.requestPermissions( getActivity(),
                new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        mvieww=(ViewPager) root.findViewById(R.id.mvieww);
        //leveltext=(TextView) root.findViewById(R.id.leveltext);
        SharedPreferences preferences = getActivity().getSharedPreferences("userdetails", MODE_PRIVATE);
       //videoswatched .setText(preferences.getString("offset", ""));
        points=(TextView)root.findViewById(R.id.points);
        requestlistener = new RequestListener() {

            @Override
            public void onSuccess(String msg,String cw,String cp,String param,String param1,String param2,String param3,String param4,String param5,String param6) {
                // TODO Auto-generated method stub
              // Toast.makeText(getActivity(),(String.valueOf(param1)),Toast.LENGTH_LONG).show();
                 coins=Float.parseFloat(cw)+Float.parseFloat(cp);
                coinsw=Float.parseFloat(cw);
                try {
                    JSONArray jarr = new JSONArray(param3);
                    for(int i=0;i<jarr.length();i++)
                    {

                        String total=jarr.getJSONObject(i).getString("total");
                        Log.e("Total ",total);
                        coinl=Float.parseFloat(total);

                    }
                }
                catch (Exception a){

                }

                points.setText(String.valueOf(coins));
              videoswatched.setText(String.valueOf(param));
                try {
                    JSONArray jarr = new JSONArray(param1);
                    for(int i=0;i<jarr.length();i++)
                    {

                        String filename=jarr.getJSONObject(i).getString("filename");
                        Log.e("FILE NAME ",filename);
                        bannerimageslist.add(filename);
                    }
                    page=bannerimageslist.size();
                }
                catch (Exception a){}
                ImageAdapter adapter = new ImageAdapter(getActivity());
                mvieww.setAdapter(adapter);
                pageSwitcher(1);
                locationManager= (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    OnGPS();
                } else {
                    getLocation();
                }
                try {
                    JSONArray popad = new JSONArray(param2);
                    String filename=popad.getJSONObject(0).getString("filename");
                    Dialog dialog = new Dialog(getActivity());
                    dialog.setCancelable(true);

                    View view  = getActivity().getLayoutInflater().inflate(R.layout.popupwindow, null);
                    ImageView imageView=(ImageView)view.findViewById(R.id.imagebg);
                    Picasso.get().load("https://seee.tech/uploads/ads/"+filename).placeholder(getActivity().getResources().getDrawable
                            (R.drawable.logo)).into(imageView);
                    dialog.setContentView(view);
                    dialog.show();
                    ImageView iv=dialog.findViewById(R.id.close);
                    iv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();

                        }
                    });
                }
                catch (Exception a){}

            }

            @Override
            public void onError(String result) {
                // TODO Auto-generated method stub
                Toast.makeText(getActivity(),result,Toast.LENGTH_LONG).show();

                locationManager= (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    OnGPS();
                } else {
                    getLocation();
                }
            }

        };
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
                            HashMap<String, Object> _item = new HashMap<>();
                            _item.put("title", jitem.getString("title"));
                            _item.put("id", jitem.getString("id"));
                            _item.put("icon", R.drawable.ic_dashboard_black_24dp);
                            maplist.add(_item);
                        }
                    }
                    GridAdapter adapter = new GridAdapter(getActivity());
                    gridview1.setAdapter(adapter);
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
        new GetCoinsWebService(getActivity(), requestlistener).execute();
        //new AddToLotteryWebService(getActivity(), crequestListener).execute();
        new GetCategories(getActivity(), crequestListener).execute();
         userName = preferences.getString("name", "");
        ad_views = preferences.getString("ad_views", "");
  //      title=(TextView)root.findViewById(R.id.title);
//        title.setText(userName);
        videoswatched=(TextView)root.findViewById(R.id.videoswatched);
        videoswatched.setText(ad_views);
        gridview1 = (GridView)root. findViewById(R.id.mainGridView1);
        watchad= (ImageView) root. findViewById(R.id.consecbtn);
        watchad2= (ImageView) root. findViewById(R.id.watchuseradbtn);
        watchad.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(HomeFragment.timer==null)
                {}
                else
                {
                    HomeFragment.timer.cancel();
                }
                Intent i=new Intent(getActivity(), PlayConsecutiveAds.class);
                i.putExtra("lat", latitude);
                i.putExtra("lon", longitude);
                startActivity(i);
                getActivity().finish();
            }
        });
        watchad2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i=new Intent(getActivity(), UserAds.class);
                i.putExtra("lat", latitude);
                i.putExtra("lon", longitude);
                startActivity(i);
              // Toast.makeText(getActivity(),"In Development",Toast.LENGTH_LONG).show();
            }
        });
        gridview1.setOnItemClickListener(new GridView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> p1, View p2, int p3, long p4){
                //Toast.makeText(getActivity(), maplist.get(p3).get("id").toString(), Toast.LENGTH_LONG).show();
                if(HomeFragment.timer==null)
                {}
                else
                {
                    HomeFragment.timer.cancel();
                }
                Intent i=new Intent(getActivity(), AdsListing.class);
                i.putExtra("catid", maplist.get(p3).get("id").toString());
                i.putExtra("lat", latitude);
                i.putExtra("lon", longitude);
                startActivity(i);
                getActivity().finish();
            }
        });
       /* // Add items to the Map list
        {
            HashMap<String, Object> _item = new HashMap<>();
            _item.put("title", "Fashion");
            _item.put("icon", R.drawable.ic_dashboard_black_24dp);
            maplist.add(_item);
        }
        {
            HashMap<String, Object> _item = new HashMap<>();
            _item.put("title", "Travel");
            _item.put("icon",  R.drawable.ic_home_black_24dp);
            maplist.add(_item);
        }
        {
            HashMap<String, Object> _item = new HashMap<>();
            _item.put("title", "Food");
            _item.put("icon",  R.drawable.ic_dashboard_black_24dp);
            maplist.add(_item);
        }
        {
            HashMap<String, Object> _item = new HashMap<>();
            _item.put("title", "Movies");
            _item.put("icon",  R.drawable.ic_notifications_black_24dp);
            maplist.add(_item);
        }
        {
            HashMap<String, Object> _item = new HashMap<>();
            _item.put("title", "Music");
            _item.put("icon",  R.drawable.ic_notifications_black_24dp);
            maplist.add(_item);
        }

        {
            HashMap<String, Object> _item = new HashMap<>();
            _item.put("title", "Vehicles");
            _item.put("icon",  R.drawable.ic_dashboard_black_24dp);
            maplist.add(_item);
        }
        {
            HashMap<String, Object> _item = new HashMap<>();
            _item.put("title", "Banks");
            _item.put("icon",  R.drawable.ic_dashboard_black_24dp);
            maplist.add(_item);
        }
        {
            HashMap<String, Object> _item = new HashMap<>();
            _item.put("title", "Medcine");
            _item.put("icon",  R.drawable.ic_dashboard_black_24dp);
            maplist.add(_item);
        }
        {
            HashMap<String, Object> _item = new HashMap<>();
            _item.put("title", "Gift shops");
            _item.put("icon",  R.drawable.ic_dashboard_black_24dp);
            maplist.add(_item);
        }
        {
            HashMap<String, Object> _item = new HashMap<>();
            _item.put("title", "Govt Ads");
            _item.put("icon",  R.drawable.ic_dashboard_black_24dp);
            maplist.add(_item);
        }
        {
            HashMap<String, Object> _item = new HashMap<>();
            _item.put("title", "Electronics");
            _item.put("icon",  R.drawable.ic_dashboard_black_24dp);
            maplist.add(_item);
        }
        {
            HashMap<String, Object> _item = new HashMap<>();
            _item.put("title", "Interiors");
            _item.put("icon",  R.drawable.ic_dashboard_black_24dp);
            maplist.add(_item);
        }
        {
            HashMap<String, Object> _item = new HashMap<>();
            _item.put("title", "Construction");
            _item.put("icon",  R.drawable.ic_dashboard_black_24dp);
            maplist.add(_item);
        }
        {
            HashMap<String, Object> _item = new HashMap<>();
            _item.put("title", "Architects");
            _item.put("icon",  R.drawable.ic_dashboard_black_24dp);
            maplist.add(_item);
        }
        {
            HashMap<String, Object> _item = new HashMap<>();
            _item.put("title", "Events");
            _item.put("icon",  R.drawable.ic_dashboard_black_24dp);
            maplist.add(_item);
        }
        {
            HashMap<String, Object> _item = new HashMap<>();
            _item.put("title", "Insurances");
            _item.put("icon",  R.drawable.ic_dashboard_black_24dp);
            maplist.add(_item);
        }
        {
            HashMap<String, Object> _item = new HashMap<>();
            _item.put("title", "Cash & carry");
            _item.put("icon",  R.drawable.ic_dashboard_black_24dp);
            maplist.add(_item);
        }
        {
            HashMap<String, Object> _item = new HashMap<>();
            _item.put("title", "Real estate");
            _item.put("icon",  R.drawable.ic_dashboard_black_24dp);
            maplist.add(_item);
        }
        {
            HashMap<String, Object> _item = new HashMap<>();
            _item.put("title", "Apps");
            _item.put("icon",  R.drawable.ic_dashboard_black_24dp);
            maplist.add(_item);
        }
        {
            HashMap<String, Object> _item = new HashMap<>();
            _item.put("title", "Misc");
            _item.put("icon",  R.drawable.ic_dashboard_black_24dp);
            maplist.add(_item);
        }*/

      /*  GridAdapter adapter = new GridAdapter(getActivity());
        gridview1.setAdapter(adapter);*/
   /***     ImageView logout_btn=(ImageView) root.findViewById(R.id.logout_btn);
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
                                new LogoutWebservice(getActivity(),logoutrequestListener).execute();

                            }
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(getResources().getString(R.string.no), null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });***/
        return root;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.common_menu, menu);
        return;
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
    public class GridAdapter extends BaseAdapter {
        private Context mContext;
        public GridAdapter(Context c) {
            mContext = c;
        }
        public int getCount() {
            return maplist.size();
        }
        public Object getItem(int position) {
            return null;
        }
        public long getItemId(int position) {
            return 0;
        }
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            // Inflate the layout for each list item
            LayoutInflater _inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (v == null) {
                v = _inflater.inflate(R.layout.grid_item, null);
            }
            // Get the TextView and ImageView from CustomView for displaying item
            TextView txtview = (TextView) v.findViewById(R.id.listitemTextView1);
            TextView idt = (TextView) v.findViewById(R.id.id);
            ImageView imgview = (ImageView) v.findViewById(R.id.listitemImageView1);

            // Set the text and image for current item using data from map list
            txtview.setText(maplist.get(position).get("title").toString());
            idt.setText(maplist.get(position).get("id").toString());
            if(maplist.get(position).get("title").toString().equalsIgnoreCase("fashion")) {
                imgview.setImageResource(R.drawable.fashion);
            }
            else if(maplist.get(position).get("title").toString().equalsIgnoreCase("travel")) {
                imgview.setImageResource(R.drawable.travel);
            }
            else  if(maplist.get(position).get("title").toString().equalsIgnoreCase("food")) {
                imgview.setImageResource(R.drawable.food);
            }
            else  if(maplist.get(position).get("title").toString().equalsIgnoreCase("movies")) {
                imgview.setImageResource(R.drawable.movies);
            }
            else  if(maplist.get(position).get("title").toString().equalsIgnoreCase("music")) {
                imgview.setImageResource(R.drawable.music);
            }
            else  if(maplist.get(position).get("title").toString().equalsIgnoreCase("vehicle")) {
                imgview.setImageResource(R.drawable.vehicle);
            }
            else  if(maplist.get(position).get("title").toString().equalsIgnoreCase("banks")) {
                imgview.setImageResource(R.drawable.banks);
            }
            else  if(maplist.get(position).get("title").toString().equalsIgnoreCase("medicine")) {
                imgview.setImageResource(R.drawable.medcine);
            }
            else  if(maplist.get(position).get("title").toString().equalsIgnoreCase("gift shops")) {
                imgview.setImageResource(R.drawable.giftshops);
            }
            else  if(maplist.get(position).get("title").toString().equalsIgnoreCase("Government Ads")) {
                imgview.setImageResource(R.drawable.govtads);
            }
            else  if(maplist.get(position).get("title").toString().equalsIgnoreCase("electronics")) {
                imgview.setImageResource(R.drawable.electronics);
            }
            else  if(maplist.get(position).get("title").toString().equalsIgnoreCase("interior")) {
                imgview.setImageResource(R.drawable.interior);
            }
            else  if(maplist.get(position).get("title").toString().equalsIgnoreCase("construction")) {
                imgview.setImageResource(R.drawable.construction);
            }
            else  if(maplist.get(position).get("title").toString().equalsIgnoreCase("architects")) {
                imgview.setImageResource(R.drawable.architects);
            }
            else  if(maplist.get(position).get("title").toString().equalsIgnoreCase("events")) {
                imgview.setImageResource(R.drawable.events);
            }
            else  if(maplist.get(position).get("title").toString().equalsIgnoreCase("insurance")) {
                imgview.setImageResource(R.drawable.insurance);
            }
            else  if(maplist.get(position).get("title").toString().equalsIgnoreCase("Cash & Carry")) {
                imgview.setImageResource(R.drawable.cashandcarry);
            }
            else  if(maplist.get(position).get("title").toString().equalsIgnoreCase("Real Estate")) {
                imgview.setImageResource(R.drawable.realestate);
            }
            else  if(maplist.get(position).get("title").toString().equalsIgnoreCase("Apps")) {
                imgview.setImageResource(R.drawable.apps);
            }
            else  if(maplist.get(position).get("title").toString().equalsIgnoreCase("Misc")) {
                imgview.setImageResource(R.drawable.misc);
            }
            else  {
                imgview.setImageResource(R.drawable.logo);
            }

            return v;
        }
    }
    private void OnGPS() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("Yes", new  DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(
                getActivity(),Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {
            Location locationGPS = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
            if (locationGPS != null) {
                double lat = locationGPS.getLatitude();
                double longi = locationGPS.getLongitude();
                latitude = String.valueOf(lat);
                longitude = String.valueOf(longi);

            } else {
                latitude = String.valueOf(0.0);
                longitude = String.valueOf(0.0);
                Toast.makeText(getActivity(), "Unable to find location.", Toast.LENGTH_SHORT).show();
            }
            //Toast.makeText(getActivity(), "Lat "+latitude+" Lon "+longitude, Toast.LENGTH_SHORT).show();
        }
    }
    public class ImageAdapter extends PagerAdapter {
        private Context context;

        ImageAdapter(Context context)
        {
            this.context=context;
        }

        @Override
        public int getCount() {
            return bannerimageslist.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            String filename=bannerimageslist.get(position);
            ImageView imageView = new ImageView(context);
            int padding = context.getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin);
            imageView.setPadding(padding, padding, padding, padding);
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            Picasso.get().load("https://seee.tech/uploads/ads/"+filename).placeholder(context.getResources().getDrawable
                    (R.drawable.logo)).into(imageView);
            container.addView(imageView, 0);
            return imageView;
        }
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((ImageView) object);
        }
    }
    public static Timer timer;
    int page = 1;

    public void pageSwitcher(int seconds) {
        timer = new Timer(); // At this line a new Thread will be created
        timer.scheduleAtFixedRate(new RemindTask(), 0, seconds * 1000); // delay
        // in
        // milliseconds
    }

    // this is an inner class...
    class RemindTask extends TimerTask {

        @Override
        public void run() {

            // As the TimerTask run on a seprate thread from UI thread we have
            // to call runOnUiThread to do work on UI thread.
            getActivity().runOnUiThread(new Runnable() {
                public void run() {

                    if (page > 4) { // In my case the number of pages are 5
                        page=0;
                        // Showing a toast for just testing purpose

                    } else {
                        mvieww.setCurrentItem(page++);
                    }
                }
            });

        }
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
}
