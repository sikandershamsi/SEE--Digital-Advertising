package com.see;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ViewFlipper;

public class IntroSlider extends  AppCompatActivity {
    ViewFlipper v_flipper;
    Button skip;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_intro_slider);
        int images []={R.drawable.profilescreen,R.drawable.homescreen,R.drawable.homescren,R.drawable.rewardscreen,R.drawable.widraw};
        v_flipper= findViewById(R.id.v_flipper);
        skip=findViewById(R.id.skip);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(IntroSlider.this,Login.class);
                startActivity(in);

            }
        });

for (int image:images){
    flipperImages(image);


}

    }




    public void flipperImages(int image){
        ImageView imageView=new ImageView(this);
        imageView.setBackgroundResource(image);
        v_flipper.addView(imageView);
        v_flipper.setFlipInterval(4000);
        v_flipper.setAutoStart(true);

        v_flipper.setInAnimation(this,android.R.anim.slide_in_left);
        v_flipper.setOutAnimation(this,android.R.anim.slide_out_right);
    }
}