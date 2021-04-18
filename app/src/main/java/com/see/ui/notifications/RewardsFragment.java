package com.see.ui.notifications;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.listeners.CategoriesRequestListener;
import com.see.GetCurrentLevel;
import com.see.IntroSlider;
import com.see.Login;
import com.see.LoteryConsectiveAds;
import com.see.PlayConsecutiveAds;
import com.see.R;
import com.see.ui.LogoutWebservice;
import com.see.ui.home.HomeFragment;

import org.json.JSONObject;

import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;

public class RewardsFragment extends Fragment {
    TextView leveltext,congotext,bottomtext;
    ImageView lotery;
    CategoriesRequestListener crequestListener;
    CategoriesRequestListener logoutrequestListener;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SharedPreferences editorr=getActivity().getSharedPreferences("Settings",MODE_PRIVATE);
        View root=null;
        setHasOptionsMenu(true);
        if(editorr.getString("My_Lang", "").equalsIgnoreCase("ur"))
        {
            //show urdu xml

            root=inflater.inflate(R.layout.fragamentrewardur, container, false);

        }


        else {
            //shown english xml
            root = inflater.inflate(R.layout.fragment_rewards, container, false);
        }
        Toolbar toolbar= root.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);


        leveltext=(TextView) root.findViewById(R.id.leveltext);
        congotext=(TextView) root.findViewById(R.id.congotext);
        bottomtext=(TextView) root.findViewById(R.id.bottomtext);
        lotery=(ImageView) root.findViewById(R.id.get_lotery);

        if(HomeFragment.timer==null)
        {}

        else
        {
            HomeFragment.timer.cancel();
        }
        /**  ImageView logout_btn=(ImageView) root.findViewById(R.id.logout_btn);
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

        new LogoutWebservice(getActivity(),logoutrequestListener).execute();
        }
        })

        // A null listener allows the button to dismiss the dialog and take no further action.
        .setNegativeButton(getResources().getString(R.string.no), null)
        .setIcon(android.R.drawable.ic_dialog_alert)
        .show();
        }
        });***/
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
        lotery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getActivity(), LoteryConsectiveAds.class);
                startActivity(i);
                getActivity().finish();
            }
        });
        crequestListener = new CategoriesRequestListener() {

            @Override
            public void onSuccess(String result) {
                // TODO Auto-generated method stub
                //Toast.makeText(getActivity(),result,Toast.LENGTH_LONG).show();
                try

                {
                    JSONObject jobj = new JSONObject(result);
                    String ads_remaining=jobj.getString("ads_remaining");
                    String current_level=jobj.getString("current_level");
                    SharedPreferences preferences = getActivity().getSharedPreferences("userdetails", MODE_PRIVATE);
                    leveltext.setText(R.string.level_1);
                    congotext.setText(R.string.congotext);
                    bottomtext.setText(R.string.bottontext);
                    SharedPreferences.Editor editor = preferences.edit();

                    editor.putString("level",current_level);
                }
                catch (Exception e)
                {

                }
            }

            @Override
            public void onError(String result) {
                // TODO Auto-generated method stub
                Toast.makeText(getActivity(),result,Toast.LENGTH_LONG).show();


            }

        };
        new GetCurrentLevel(getActivity(), crequestListener).execute();
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
}


