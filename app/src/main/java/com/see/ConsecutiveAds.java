package com.see;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import androidx.appcompat.app.AppCompatActivity;

import com.listeners.CategoriesRequestListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ConsecutiveAds extends AppCompatActivity {
    CategoriesRequestListener crequestListener;
    private GridView gridview1;
    String latitude="";
    String longitude="";
    ImageView back_btn;
    private ArrayList<HashMap<String, Object>> maplist = new ArrayList<>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        SharedPreferences editorr=ConsecutiveAds.this.getSharedPreferences("Settings",MODE_PRIVATE);
        if(editorr.getString("My_Lang", "").equalsIgnoreCase("ur"))
        {
            setContentView(R.layout.consecutiveadslistingur);
        }
        else
        {
            setContentView(R.layout.consecutiveadslisting);
        }
        Intent i=getIntent();

        latitude=i.getStringExtra("lat");
        longitude=i.getStringExtra("lon");
        gridview1 = (GridView)findViewById(R.id.mainGridView1);
        back_btn = (ImageView) findViewById(R.id.back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
        gridview1.setOnItemClickListener(new GridView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> p1, View p2, int p3, long p4){
                Intent i=new Intent(ConsecutiveAds.this, PlayAd.class);
                i.putExtra("id", maplist.get(p3).get("id").toString());
                i.putExtra("filename",  maplist.get(p3).get("filename").toString());
                i.putExtra("adurl",  maplist.get(p3).get("adurl").toString());
                i.putExtra("title", maplist.get(p3).get("title").toString());
                startActivity(i);
            }
        });
        crequestListener = new CategoriesRequestListener() {

            @Override
            public void onSuccess(String result) {
                // TODO Auto-generated method stub
               // Toast.makeText(ConsecutiveAds.this,result,Toast.LENGTH_LONG).show();
                try {
                    JSONObject jobj = new JSONObject(result);
                    String adurl=jobj.getString("ad_url");
                    for (int i=0;i<jobj.getJSONArray("data").length();i++)
                    {
                        JSONObject jitem=jobj.getJSONArray("data").getJSONObject(i);
                        try {
                            JSONArray ads = jitem.getJSONArray("ads");
                            for (int j = 0; j < ads.length(); j++) {
                                JSONObject jitem2 = ads.getJSONObject(j);
                                {
                                    HashMap<String, Object> _item = new HashMap<>();
                                    _item.put("title", jitem2.getString("title"));
                                    _item.put("id", jitem2.getString("id"));
                                    _item.put("filename", jitem2.getString("filename"));
                                    _item.put("thumb", jitem2.getString("thumb"));
                                    _item.put("adurl", adurl);
                                    _item.put("icon", R.drawable.ic_dashboard_black_24dp);
                                    maplist.add(_item);
                                }
                            }
                        }catch (Exception e){}
                    }
                    GridAdapter adapter = new GridAdapter(ConsecutiveAds.this);
                    gridview1.setAdapter(adapter);

                }
                catch(Exception e)
                {
                   // Toast.makeText(ConsecutiveAds.this,e.toString(),Toast.LENGTH_LONG).show();
                }
            }


            @Override
            public void onError(String result) {
                // TODO Auto-generated method stub
                Toast.makeText(ConsecutiveAds.this,result,Toast.LENGTH_LONG).show();


            }

        };
        new GetConsecutiveAds(ConsecutiveAds.this, crequestListener,"",latitude,longitude).execute();

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
            LayoutInflater _inflater = (LayoutInflater)ConsecutiveAds.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (v == null) {
                v = _inflater.inflate(R.layout.grid_item, null);
            }
            // Get the TextView and ImageView from CustomView for displaying item
            TextView txtview = (TextView) v.findViewById(R.id.listitemTextView1);
            TextView idt = (TextView) v.findViewById(R.id.id);
            TextView filename = (TextView) v.findViewById(R.id.filename);
            TextView baseurl = (TextView) v.findViewById(R.id.baseurl);
            TextView thumb = (TextView) v.findViewById(R.id.thumb);
            thumb.setText(maplist.get(position).get("thumb").toString());
            filename.setText(maplist.get(position).get("filename").toString());
            baseurl.setText(maplist.get(position).get("adurl").toString());
            ImageView imgview = (ImageView) v.findViewById(R.id.listitemImageView1);

            // Set the text and image for current item using data from map list
            txtview.setText(maplist.get(position).get("title").toString());
            idt.setText(maplist.get(position).get("id").toString());

            Picasso.get().load(maplist.get(position).get("thumb").toString()).placeholder(ConsecutiveAds.this.getResources().getDrawable
                    (R.drawable.logo)).into(imgview);


            return v;
        }
    }

}
