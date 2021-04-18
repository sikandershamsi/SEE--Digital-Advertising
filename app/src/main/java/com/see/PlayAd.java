package com.see;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.listeners.CategoriesRequestListener;
import com.see.R;
import com.see.ui.home.HomeFragment;

import java.util.concurrent.TimeUnit;

import ticker.views.com.ticker.widgets.circular.timer.callbacks.CircularViewCallback;
import ticker.views.com.ticker.widgets.circular.timer.view.CircularView;

public class PlayAd extends AppCompatActivity {
    PlayerView playerView;
    ProgressBar progressBar;
    TextView duraTion;
    ImageView btnFullscreen,sharevia,webView;
    //SimpleExoPlayer simpleExoPlayer;
    boolean flag=false;
    SimpleExoPlayer exoplayer;
    String videourl="";
    String adurl="";
    String ad_url="";
    String filename="";

    String id="";
    long Duration;
    CircularView circularViewWithTimer;
    String title="";
    boolean shareclicked=false;
    CategoriesRequestListener requestListener;
    SimpleExoPlayer simpleExoPlayer;
    boolean iscomplete=false;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         getSupportActionBar().hide();
        setContentView(R.layout.activity_playad);

        playerView = findViewById(R.id.exoplayerview);
        progressBar = findViewById(R.id.progress_bar);
        duraTion = findViewById(R.id.duraTion);

        // btnFullscreen = playerView.findViewById(R.id.full_screen);
        webView = findViewById(R.id.website);

        sharevia = findViewById(R.id.share);
        sharevia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareclicked=true;
                Intent sendintent=new Intent();
                sendintent.setAction(Intent.ACTION_SEND);
                sendintent.putExtra(Intent.EXTRA_TEXT, "Hey!  I've found this amazing ad on see Mobile Application. Please go checkout, Click the link below!"+"\n"+title+"\n"+videourl);
                sendintent.setType("text/plain");
                sendintent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(sendintent);
            }
        });

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestListener = new CategoriesRequestListener() {

            @Override
            public void onSuccess(String result) {
                // TODO Auto-generated method stub
                Toast.makeText(PlayAd.this,result,Toast.LENGTH_LONG).show();

            }


            @Override
            public void onError(String result) {
                // TODO Auto-generated method stub
                Toast.makeText(PlayAd.this,result,Toast.LENGTH_LONG).show();


            }

        };
        Intent i=getIntent();
        title=i.getStringExtra("title");
        adurl=i.getStringExtra("adurl");
        ad_url=i.getStringExtra("ad_url");
        filename=i.getStringExtra("filename");
        id=i.getStringExtra("id");
        videourl=adurl+filename;
        Log.e("vidurl ",videourl);
        ImageView back_btn = (ImageView) findViewById(R.id.back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    if(!iscomplete){
                        simpleExoPlayer.setPlayWhenReady(false);
                        simpleExoPlayer.getPlaybackState();}
                }catch (Exception u){}
                finish();

            }
        });
        webView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  Toast.makeText(getApplicationContext(),ad_url,
                //Toast.LENGTH_LONG).show();
                shareclicked=true;
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.gulahmedshop.com/"));
                startActivity(browserIntent);

            }
        });
       /* btnFullscreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag) {
                    btnFullscreen.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_fullscreen_24));
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    flag = false;
                } else {
                    btnFullscreen.setImageDrawable(getResources().getDrawable(R.drawable.ic_exitfullscreen));
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    flag = true;

                }

            }
        });*/

        Button rate_ad = (Button) findViewById(R.id.rate_ad);
        rate_ad.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Dialog rankDialog = new Dialog(PlayAd.this, R.style.FullHeightDialog);
                rankDialog.setContentView(R.layout.rank_dialog);
                rankDialog.setCancelable(true);
                RatingBar ratingBar = (RatingBar)rankDialog.findViewById(R.id.dialog_ratingbar);
                TextView text = (TextView) rankDialog.findViewById(R.id.rank_dialog_text1);
                text.setText(title);
                EditText etcomments= (EditText) rankDialog.findViewById(R.id.etcomments);
                Button updateButton = (Button) rankDialog.findViewById(R.id.rank_dialog_button);
                updateButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        rankDialog.dismiss();
                        new RateWebService(PlayAd.this, requestListener,id,String.valueOf(ratingBar.getRating()),etcomments.getText().toString()).execute();

                    }
                });
                //now that the dialog is set up, it's time to show it
                rankDialog.show();
            }
        });
        playerView = findViewById(R.id.exoplayerview);
        progressBar = findViewById(R.id.progress_bar);
        // btnFullscreen = playerView.findViewById(R.id.full_screen);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Uri videoUrl = Uri.parse(videourl);

        LoadControl loadControl = new DefaultLoadControl();
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelector trankSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
        simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(PlayAd.this, trankSelector, loadControl);
        DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory("exoplayer_video");
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
        MediaSource mediaSource = new ExtractorMediaSource(videoUrl, dataSourceFactory, extractorsFactory, null, null);
        playerView.setPlayer(simpleExoPlayer);
        playerView.setKeepScreenOn(true);
        simpleExoPlayer.prepare(mediaSource);
        simpleExoPlayer.setPlayWhenReady(true);
        simpleExoPlayer.addListener(new Player.EventListener() {
            @Override
            public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

            }

            @Override
            public void onLoadingChanged(boolean isLoading) {

            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                if (playbackState == Player.STATE_BUFFERING) {
                    progressBar.setVisibility(View.VISIBLE);
                } else if (playbackState == Player.STATE_READY) {
                    progressBar.setVisibility(View.GONE);
                    Duration = simpleExoPlayer.getDuration();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            do{
                                duraTion.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        duraTion.setVisibility(View.VISIBLE);
                                        long time = (Duration - simpleExoPlayer.getCurrentPosition())/1000;
                                        if (time<10){
                                        duraTion.setText("0"+time);
                                        }else{
                                            duraTion.setText(time+"");

                                        }
                                    }
                                });
                                try {
                                    Thread.sleep(500);
                                }catch ( InterruptedException e){
                                    e.printStackTrace();
                                }
                            }
                            while (simpleExoPlayer.getCurrentPosition()<Duration);
                        }
                    }).start();
                  /** CircularView.OptionsBuilder builderWithTimer =new CircularView.OptionsBuilder()
                            .shouldDisplayText(true)
                            //.setCustomText(String.format("%d min, %d sec",
                              //      TimeUnit.MILLISECONDS.toMinutes(Duration),
                                //    TimeUnit.MILLISECONDS.toSeconds(Duration) -
                                  //          TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toSeconds(Duration))))
                            .setCustomText(String.duraTion)
                         // .setCounterInSeconds(Duration/1000)
                            .setCircularViewCallback(new CircularViewCallback() {
                                @Override
                                public void onTimerFinish() {
                                    Toast.makeText(PlayAd.this, "CircularCallback: Timer Finished ", Toast.LENGTH_SHORT).show();

                                }

                                @Override
                                public void onTimerCancelled() {
                                    Toast.makeText(PlayAd.this, "CircularCallback: Timer Cancelled ", Toast.LENGTH_SHORT).show();

                                }
                            });circularViewWithTimer.setOptions(builderWithTimer);
                    circularViewWithTimer.startTimer(); ***/



                } else if (playbackState == Player.STATE_ENDED) {
                    iscomplete=true;
                   // circularViewWithTimer.stopTimer();
                    new AddToWalletWebService(PlayAd.this, requestListener,id, HomeFragment.latitude,HomeFragment.longitude).execute();

                }

            }

            @Override
            public void onRepeatModeChanged(int repeatMode) {

            }

            @Override
            public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {

            }

            @Override
            public void onPositionDiscontinuity(int reason) {

            }

            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

            }

            @Override
            public void onSeekProcessed() {

            }
        });
        // setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    /*    @Override
        public void onPause() {
            super.onPause();
           exoplayer.stop();
        }
        @Override
        public void onResume() {
            super.onResume();
            exoPlayerView=(SimpleExoPlayerView)findViewById(R.id.exoplayerview);
            try {
                BandwidthMeter bandwidthMeter=new DefaultBandwidthMeter();
                TrackSelector trankSelector=new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter ));
                exoplayer= ExoPlayerFactory.newSimpleInstance(this,trankSelector);
                Uri Videouri= Uri.parse(videourl);
                DefaultHttpDataSourceFactory dataSourceFactory=new DefaultHttpDataSourceFactory("exoplayer_video");
                ExtractorsFactory extractorsFactory=new DefaultExtractorsFactory();
                MediaSource mediaSource=new ExtractorMediaSource(Videouri,dataSourceFactory,extractorsFactory,null,null);
                exoPlayerView.setPlayer(exoplayer);
                exoplayer.prepare(mediaSource);
                exoplayer.setPlayWhenReady(true);


            } catch (Exception e){
                Log.e("MainActivity","exoplayer error"+ e.toString());
            }
        }*/
    @Override
    protected void onResume() {
        super.onResume();
        if(!iscomplete&&shareclicked){

            simpleExoPlayer.setPlayWhenReady(true);
            simpleExoPlayer.getPlaybackState();
         //   circularViewWithTimer.pauseTimer();
            shareclicked=true;
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        if(!iscomplete){
            simpleExoPlayer.setPlayWhenReady(false);
//            circularViewWithTimer.pauseTimer();
            simpleExoPlayer.getPlaybackState();}

    }
    @Override
    protected void onRestart(){
        super.onRestart();
        if(!iscomplete){
            simpleExoPlayer.setPlayWhenReady(true);
            simpleExoPlayer.getPlaybackState();}




    }
    @Override
    public void onBackPressed() {

        try {
            if(!iscomplete){
                simpleExoPlayer.setPlayWhenReady(false);
              //  circularViewWithTimer.stopTimer();
                simpleExoPlayer.getPlaybackState();}
        }catch (Exception u){}
        finish();

        //exoplayer.release();
    }

}
