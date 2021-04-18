package com.see;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.listeners.CategoriesRequestListener;
import com.see.ui.SendFirebaseTokentoServer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class Dashboard extends AppCompatActivity {
    CategoriesRequestListener requestListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences editorr=Dashboard.this.getSharedPreferences("Settings",MODE_PRIVATE);
        if(editorr.getString("My_Lang", "").equalsIgnoreCase("ur"))
        {
            setContentView(R.layout.dashboardur);
        }
        else
        {
            setContentView(R.layout.activity_dashboard);
        }
        requestListener = new CategoriesRequestListener() {

            @Override
            public void onSuccess(String result) {
                // TODO Auto-generated method stub
                //Toast.makeText(Dashboard.this,"Success "+result,Toast.LENGTH_LONG).show();

            }


            @Override
            public void onError(String result) {
                // TODO Auto-generated method stub
                Toast.makeText(Dashboard.this,"Error "+result,Toast.LENGTH_LONG).show();


            }

        };
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(Dashboard.this, "getInstanceId failed "+task.getException(), Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();

                        // Log and toast
Log.e("Token ",token);
                        //Toast.makeText(Dashboard.this, token, Toast.LENGTH_SHORT).show();
                        new SendFirebaseTokentoServer(Dashboard.this,requestListener,token).execute();
                    }
                });
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_profile, R.id.navigation_adshistory,R.id.navigation_rewards,R.id.navigation_wallet)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
       // NavigationUI.setupActionBarWithNavController(Dashboard.this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }
    @Override
    public void onBackPressed() {
        // Do Here what ever you want do on back press;
    }
}
