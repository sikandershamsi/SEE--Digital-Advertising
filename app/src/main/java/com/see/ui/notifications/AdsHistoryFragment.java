package com.see.ui.notifications;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.GetAdsHistory;
import com.listeners.CategoriesRequestListener;
import com.see.AdsListing;
import com.see.IntroSlider;
import com.see.Login;
import com.see.R;
import com.see.ui.LogoutWebservice;
import com.see.ui.home.HomeFragment;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;

public class AdsHistoryFragment extends Fragment {
    CategoriesRequestListener crequestListener;
    String ad_url="";
    EditText nametext,emailtext,phonetext,city,country,cnic;
    private ArrayList<HashMap<String, Object>> maplist = new ArrayList<>();
    private ListView listView1;
    CategoriesRequestListener logoutrequestListener;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SharedPreferences editorr=getActivity().getSharedPreferences("Settings",MODE_PRIVATE);
        View root=null;
        setHasOptionsMenu(true);
        if(editorr.getString("My_Lang", "").equalsIgnoreCase("ur"))
        {
            //show urdu xml

            root=inflater.inflate(R.layout.fragmentadshistoryur, container, false);
        }
        else {
            //shown english xml
            root = inflater.inflate(R.layout.fragment_adshistory, container, false);
        }
        Toolbar toolbar= root.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        logoutrequestListener= new CategoriesRequestListener() {

            @Override
            public void onSuccess(String result) {
                // TODO Auto-generated method stub
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
                // Toast.makeText(getActivity(),result,Toast.LENGTH_LONG).show();
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
        crequestListener = new CategoriesRequestListener() {

            @Override
            public void onSuccess(String result) {
                // TODO Auto-generated method stub
                Toast.makeText(getActivity(),result,Toast.LENGTH_LONG).show();
                SharedPreferences preferences = getActivity().getSharedPreferences("userdetails", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();

                editor.putString("name",nametext.getText().toString());
                editor.putString("email",emailtext.getText().toString());
                editor.putString("phone",phonetext.getText().toString());
                editor.apply();

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
        listView1 = (ListView)root.findViewById(R.id.mainListView1);
        /***   ImageView logout_btn=(ImageView) root.findViewById(R.id.logout_btn);
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
        crequestListener = new CategoriesRequestListener() {

            @Override
            public void onSuccess(String result) {
                // TODO Auto-generated method stub
                //Toast.makeText(getActivity(),result,Toast.LENGTH_LONG).show();
                try {
                    JSONObject jobj = new JSONObject(result);
                    String adurl=jobj.getString("ad_url");
                    for (int i=0;i<jobj.getJSONArray("data").length();i++)
                    {

                        JSONObject jitem=jobj.getJSONArray("data").getJSONObject(i);
                        // Log.e("Thumb",jitem.getString("thumb"));
                        {
                            HashMap<String, Object> _item = new HashMap<>();
                            _item.put("coins", jitem.getString("coins"));
                            _item.put("title", jitem.getString("title"));
                            _item.put("ad_url", jitem.getString("ad_url"));
                            _item.put("filename", jitem.getString("filename"));
                            _item.put("date", jitem.getString("created_at"));
                            try {

                                _item.put("thumb", jitem.getString("thumb"));

                            }catch (Exception a){ _item.put("thumb","");}
                            _item.put("adurl",adurl);
                            _item.put("icon", R.drawable.ic_dashboard_black_24dp);
                            maplist.add(_item);
                        }
                    }
                    ListvAdapter adapter = new ListvAdapter(getActivity());
                    listView1.setAdapter(adapter);

                }
                catch(Exception e)
                {
                    Toast.makeText(getActivity(),e.toString(),Toast.LENGTH_LONG).show();
                }
            }


            @Override
            public void onError(String result) {
                // TODO Auto-generated method stub
                Toast.makeText(getActivity(),result,Toast.LENGTH_LONG).show();


            }

        };
        new GetAdsHistory(getActivity(), crequestListener).execute();
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
        {
            final String[] listItems = {"English", "اردو"};
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
            mBuilder.setTitle("Choose Language....");
            mBuilder.setSingleChoiceItems(listItems, -1, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int i) {
                    if (i == 0) {
                        setLocale("en");
                        getActivity().recreate();
                    } else if (i == 1) {
                        setLocale("ur");
                        getActivity().recreate();

                    }
                    dialog.dismiss();
                }
            });
            AlertDialog mDialog = mBuilder.create();
            mDialog.show();

        }
    }

    public class ListvAdapter extends BaseAdapter {
        private Context mContext;
        public ListvAdapter(Context c) {
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
            SharedPreferences editorr=getActivity().getSharedPreferences("Settings",MODE_PRIVATE);
            LayoutInflater _inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if(editorr.getString("My_Lang", "").equalsIgnoreCase("ur"))
            {
                //show urdu xml

                v = _inflater.inflate(R.layout.list_item_ur, null);

            }
            else {
                //shown english xml
                v = _inflater.inflate(R.layout.list_item, null);

            }
            // if (v == null) {
            // v = _inflater.inflate(R.layout.list_item, null);
            // }
            // Get the TextView and ImageView from CustomView for displaying item
            TextView titletext = (TextView) v.findViewById(R.id.titletext);
            TextView datetime = (TextView) v.findViewById(R.id.datetime);
            TextView coinstext = (TextView) v.findViewById(R.id.coinstext);

            titletext.setText(maplist.get(position).get("title").toString());
            datetime.setText(maplist.get(position).get("date").toString());
            coinstext.setText(maplist.get(position).get("coins").toString());
            ImageView imgview = (ImageView) v.findViewById(R.id.icon);

            titletext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(maplist.get(position).get("ad_url").toString()));
                    startActivity(browserIntent);
                }
            });
            try {
                Picasso.get().load(maplist.get(position).get("thumb").toString()).placeholder(mContext.getResources().getDrawable
                        (R.drawable.logo)).into(imgview);
            }catch (Exception a){}

            return v;
        }
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
