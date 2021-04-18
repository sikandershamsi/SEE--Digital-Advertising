package com.see;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.listeners.CategoriesRequestListener;
import com.listeners.RequestListener;
import com.see.ui.home.HomeFragment;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class UserAds extends AppCompatActivity {
    private GridView gridview1;
    public static String latitude="0.0", longitude="0.0";
    private ArrayList<HashMap<String, Object>> maplist = new ArrayList<>();
    RequestListener requestlistener;
    CategoriesRequestListener crequestListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_ads);
        getSupportActionBar().hide();
        gridview1 = findViewById(R.id.mainGridView1);

        crequestListener = new CategoriesRequestListener() {

            @Override
            public void onSuccess(String result) {
                // TODO Auto-generated method stub
                Toast.makeText(UserAds.this,result,Toast.LENGTH_LONG).show();
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
                    UserAds.GridAdapter adapter = new GridAdapter(UserAds.this);
                    gridview1.setAdapter(adapter);
                }
                catch(Exception e)
                {

                }
            }

            @Override
            public void onError(String result) {
                // TODO Auto-generated method stub
                Toast.makeText(UserAds.this,result,Toast.LENGTH_LONG).show();


            }

        };


        gridview1.setOnItemClickListener(new GridView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> p1, View p2, int p3, long p4){

                Intent i=new Intent(UserAds.this, UsersAdsListing.class);
                i.putExtra("catid", maplist.get(p3).get("id").toString());
                i.putExtra("lat", latitude);
                i.putExtra("lon", longitude);
                startActivity(i);
                finish();
            }
        });
        new GetCategories(UserAds.this, crequestListener).execute();


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
            LayoutInflater _inflater = (LayoutInflater) UserAds.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
            if (maplist.get(position).get("title").toString().equalsIgnoreCase("fashion")) {
                imgview.setImageResource(R.drawable.fashion);
            } else if (maplist.get(position).get("title").toString().equalsIgnoreCase("travel")) {
                imgview.setImageResource(R.drawable.travel);
            } else if (maplist.get(position).get("title").toString().equalsIgnoreCase("food")) {
                imgview.setImageResource(R.drawable.food);
            } else if (maplist.get(position).get("title").toString().equalsIgnoreCase("movies")) {
                imgview.setImageResource(R.drawable.movies);
            } else if (maplist.get(position).get("title").toString().equalsIgnoreCase("music")) {
                imgview.setImageResource(R.drawable.music);
            } else if (maplist.get(position).get("title").toString().equalsIgnoreCase("vehicle")) {
                imgview.setImageResource(R.drawable.vehicle);
            } else if (maplist.get(position).get("title").toString().equalsIgnoreCase("banks")) {
                imgview.setImageResource(R.drawable.banks);
            } else if (maplist.get(position).get("title").toString().equalsIgnoreCase("medicine")) {
                imgview.setImageResource(R.drawable.medcine);
            } else if (maplist.get(position).get("title").toString().equalsIgnoreCase("gift shops")) {
                imgview.setImageResource(R.drawable.giftshops);
            } else if (maplist.get(position).get("title").toString().equalsIgnoreCase("Government Ads")) {
                imgview.setImageResource(R.drawable.govtads);
            } else if (maplist.get(position).get("title").toString().equalsIgnoreCase("electronics")) {
                imgview.setImageResource(R.drawable.electronics);
            } else if (maplist.get(position).get("title").toString().equalsIgnoreCase("interior")) {
                imgview.setImageResource(R.drawable.interior);
            } else if (maplist.get(position).get("title").toString().equalsIgnoreCase("construction")) {
                imgview.setImageResource(R.drawable.construction);
            } else if (maplist.get(position).get("title").toString().equalsIgnoreCase("architects")) {
                imgview.setImageResource(R.drawable.architects);
            } else if (maplist.get(position).get("title").toString().equalsIgnoreCase("events")) {
                imgview.setImageResource(R.drawable.events);
            } else if (maplist.get(position).get("title").toString().equalsIgnoreCase("insurance")) {
                imgview.setImageResource(R.drawable.insurance);
            } else if (maplist.get(position).get("title").toString().equalsIgnoreCase("Cash & Carry")) {
                imgview.setImageResource(R.drawable.cashandcarry);
            } else if (maplist.get(position).get("title").toString().equalsIgnoreCase("Real Estate")) {
                imgview.setImageResource(R.drawable.realestate);
            } else if (maplist.get(position).get("title").toString().equalsIgnoreCase("Apps")) {
                imgview.setImageResource(R.drawable.apps);
            } else if (maplist.get(position).get("title").toString().equalsIgnoreCase("Misc")) {
                imgview.setImageResource(R.drawable.misc);
            } else {
                imgview.setImageResource(R.drawable.logo);
            }

            return v;
        }


    }
}