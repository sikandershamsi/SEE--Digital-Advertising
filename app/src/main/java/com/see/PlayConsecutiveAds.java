package com.see;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
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
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.listeners.CategoriesRequestListener;
import com.see.ui.home.HomeFragment;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import jp.shts.android.storiesprogressview.StoriesProgressView;
import ticker.views.com.ticker.widgets.circular.timer.callbacks.CircularViewCallback;
import ticker.views.com.ticker.widgets.circular.timer.view.CircularView;

import static java.lang.System.currentTimeMillis;

public class PlayConsecutiveAds extends AppCompatActivity {
    PlayerView playerView;
    ProgressBar progressBar;
    ImageView btnFullscreen,sharevia,webView;
    //SimpleExoPlayer simpleExoPlayer;
    boolean flag=false;
    SimpleExoPlayer exoplayer;
    String videourl="";
    String adurl="";
    String ad_url="";
    String filename="";
    String id="";
    String title="";
    TextView duraTion;
    private Uri imageUri;
    CategoriesRequestListener requestListener;
    SimpleExoPlayer simpleExoPlayer;
    boolean iscomplete=false;
    String latitude="";
    String longitude="";
    CategoriesRequestListener crequestListener;
    private ArrayList<HashMap<String, Object>> maplist = new ArrayList<>();
    int adscount=0;
    int currentad=0;
    CircularView circularViewWithTimer;
    // int counter=0;
    //int progressBarValue=0;
    boolean shareclicked=false;
    boolean bufferstate=false;
    long Duration;
    Handler handler=new Handler();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         getSupportActionBar().hide();
        setContentView(R.layout.activity_playconsad);
       // progressBar2 = findViewById(R.id.progressbar2);


        playerView = findViewById(R.id.exoplayerview);
        progressBar = findViewById(R.id.progress_bar);
        sharevia = findViewById(R.id.share);
        webView = findViewById(R.id.website);
        duraTion=findViewById(R.id.duraTion);


        // btnFullscreen = playerView.findViewById(R.id.full_screen);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestListener = new CategoriesRequestListener() {

            @Override
            public void onSuccess(String result) {
                // TODO Auto-generated method stub
                Toast.makeText(PlayConsecutiveAds.this,result,Toast.LENGTH_LONG).show();

                if (adscount == currentad) {
                    Toast.makeText(PlayConsecutiveAds.this, "Ads Completed", Toast.LENGTH_LONG).show();
                } else {
                    iscomplete = false;
                    adscount--;
                    currentad++;
                    title = maplist.get(currentad).get("title").toString();
                    adurl = maplist.get(currentad).get("adurl").toString();
                    filename = maplist.get(currentad).get("filename").toString();
                    id = maplist.get(currentad).get("id").toString();
                    videourl = adurl + filename;
                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    Uri videoUrl = Uri.parse(videourl);
                    LoadControl loadControl = new DefaultLoadControl();
                    BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
                    TrackSelector trankSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
                    simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(PlayConsecutiveAds.this, trankSelector, loadControl);
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
                                //Toast.makeText(PlayConsecutiveAds.this,"On Buffering",Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.VISIBLE);
                            } else if (playbackState == Player.STATE_READY) {
                                //Toast.makeText(PlayConsecutiveAds.this,"On Ready",Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.GONE);
                                //progressBar2.setVisibility(View.VISIBLE);
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
//if(!running)break;
                                        }
                                        while (simpleExoPlayer.getCurrentPosition()<Duration);
                                    }
                                }).start();
                      /**          CircularView.OptionsBuilder builderWithTimer =new CircularView.OptionsBuilder()
                                        .shouldDisplayText(true)
                                        .setCounterInSeconds(Duration/1000)
                                        .setCircularViewCallback(new CircularViewCallback() {
                                            @Override
                                            public void onTimerFinish() {


                                            }

                                            @Override
                                            public void onTimerCancelled() {


                                            }
                                        });circularViewWithTimer.setOptions(builderWithTimer);
                                circularViewWithTimer.startTimer(); ***/


                            } else if (playbackState == Player.STATE_ENDED) {
                                iscomplete = true;
                              //  circularViewWithTimer.stopTimer();
                              //  progressBar2.setVisibility(View.GONE);

                                new AddToWalletWebService(PlayConsecutiveAds.this, requestListener, id, HomeFragment.latitude, HomeFragment.longitude).execute();

                            }
                            else if (playbackState == Player.STATE_IDLE) {
                           //     Toast.makeText(PlayConsecutiveAds.this,"IDLE",Toast.LENGTH_LONG).show();
                                Log.e("","IDLE");
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


                }
            }


            @Override
            public void onError(String result) {
                // TODO Auto-generated method stub
                Toast.makeText(PlayConsecutiveAds.this, result, Toast.LENGTH_LONG).show();


            }

        };
        Intent i = getIntent();
        latitude = i.getStringExtra("lat");
        longitude = i.getStringExtra("lon");
        crequestListener = new CategoriesRequestListener() {

            @Override
            public void onSuccess(String result) {
                // TODO Auto-generated method stub
                // Toast.makeText(ConsecutiveAds.this,result,Toast.LENGTH_LONG).show();
                try {
                    JSONObject jobj = new JSONObject(result);
                    String adurll = jobj.getString("ad_url");
                    adscount = jobj.getJSONArray("data").length();
                    for (int i = 0; i < jobj.getJSONArray("data").length(); i++) {
                        JSONObject jitem = jobj.getJSONArray("data").getJSONObject(i);
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
                                    _item.put("adurl", adurll);
                                    _item.put("ad_url", jitem2.getString("ad_url"));
                                    _item.put("icon", R.drawable.ic_dashboard_black_24dp);
                                    maplist.add(_item);
                                }
                            }
                        } catch (Exception e) {
                        }
                    }
                    title = maplist.get(currentad).get("title").toString();
                    adurl = maplist.get(currentad).get("adurl").toString();
                    ad_url = maplist.get(currentad).get("ad_url").toString();
                    filename = maplist.get(currentad).get("filename").toString();
                    id = maplist.get(currentad).get("id").toString();
                    videourl = adurl + filename;
                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    Uri videoUrl = Uri.parse(videourl);

                    LoadControl loadControl = new DefaultLoadControl();
                    BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
                    TrackSelector trankSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
                    simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(PlayConsecutiveAds.this, trankSelector, loadControl);
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
                            //  progressBar2.setMax(Player.getDuration());
                            if (playbackState == Player.STATE_BUFFERING) {
                                //Toast.makeText(PlayConsecutiveAds.this,"buffertrue",Toast.LENGTH_LONG).show();
                               // long currentDuration = simpleExoPlayer.getCurrentPosition();
                                //Toast.makeText(PlayConsecutiveAds.this,String.valueOf(currentDuration),Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.VISIBLE);
                               // circularViewWithTimer.pauseTimer();
                               // Toast.makeText(PlayConsecutiveAds.this,"On Buffering",Toast.LENGTH_LONG).show();
                            } else if (playbackState == Player.STATE_READY) {

                                //Toast.makeText(PlayConsecutiveAds.this,"On Ready",Toast.LENGTH_LONG).show();
                                //bufferstate=false;
                                //circularViewWithTimer.resumeTimer();
                                //Toast.makeText(PlayConsecutiveAds.this,"Bufferstop",Toast.LENGTH_LONG).show();
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
//if(!running)break;
                                        }
                                        while (simpleExoPlayer.getCurrentPosition()<Duration);
                                    }
                                }).start();
                             /**   CircularView.OptionsBuilder builderWithTimer =new CircularView.OptionsBuilder()
                                        .shouldDisplayText(true)
                                        .setCounterInSeconds(Duration/1000)
                                        .setCircularViewCallback(new CircularViewCallback() {
                                            @Override
                                            public void onTimerFinish() {
                                             //   Toast.makeText(PlayConsecutiveAds.this,"on fnish",Toast.LENGTH_LONG).show();


                                            }

                                            @Override
                                            public void onTimerCancelled() {
                                               // Toast.makeText(PlayConsecutiveAds.this,"on cancel",Toast.LENGTH_LONG).show();
                                            }
                                        });
                                circularViewWithTimer.setOptions(builderWithTimer);
                                circularViewWithTimer.startTimer(); ***/


                              //  progressBar2.setVisibility(View.VISIBLE);

                            } else if (playbackState == Player.STATE_ENDED) {
                              //  Toast.makeText(PlayConsecutiveAds.this,"On Ended",Toast.LENGTH_LONG).show();
                              //  iscomplete = true;
                                //circularViewWithTimer.stopTimer();
                               // progressBar2.setVisibility(View.GONE);
                                new AddToWalletWebService(PlayConsecutiveAds.this, requestListener, id, HomeFragment.latitude, HomeFragment.longitude).execute();

                            } else if (playbackState == Player.STATE_IDLE) {
                                // Toast.makeText(PlayConsecutiveAds.this,"IDLE",Toast.LENGTH_LONG).show();
                                //Log.e("", "IDLE");

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
                    //  setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

                } catch (Exception e) {
                    // Toast.makeText(ConsecutiveAds.this,e.toString(),Toast.LENGTH_LONG).show();
                }
            }


            @Override
            public void onError(String result) {
                // TODO Auto-generated method stub
              //  Toast.makeText(PlayConsecutiveAds.this, result, Toast.LENGTH_LONG).show();


            }

        };
        new GetConsecutiveAds(PlayConsecutiveAds.this, crequestListener, "", latitude, longitude).execute();
       /* title=i.getStringExtra("title");
        adurl=i.getStringExtra("adurl");
        filename=i.getStringExtra("filename");
        id=i.getStringExtra("id");
        videourl=adurl+filename;
        Log.e("vidurl ",videourl);*/
        ImageView back_btn = (ImageView) findViewById(R.id.back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    if (!iscomplete) {
                        simpleExoPlayer.setPlayWhenReady(false);
                        simpleExoPlayer.getPlaybackState();
                       // circularViewWithTimer.stopTimer();
                    }
                } catch (Exception u) {
                }
                finish();
                Intent in = new Intent(PlayConsecutiveAds.this, Login.class);
                startActivity(in);

            }
        });
        webView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  Toast.makeText(PlayConsecutiveAds.this,ad_url,
                //        Toast.LENGTH_SHORT).show();
                shareclicked=true;

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(maplist.get(currentad).get("ad_url").toString()));
                startActivity(browserIntent);
            }
        });
        sharevia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareclicked=true;
                Intent sendintent = new Intent(Intent.ACTION_SEND);
                //  Uri imageUri = Uri.parse("android.resource://" + getPackageName()
                //        + "/drawable/" +getResources().getResourcePackageName( R.drawable.appicon));


                //  sendintent.putExtra(Intent.EXTRA_STREAM, imageUri);
                sendintent.putExtra(Intent.EXTRA_TEXT, "Hey!  I've found this amazing ad on see Mobile Application. Please go checkout, Click the link below!" + "\n" + title + "\n" + videourl);
                sendintent.setType("text/plain");
                //  sendintent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                // sendintent.setType("text/plain");
                startActivity(Intent.createChooser(sendintent,"share using"));



            }
        });

       /* btnFullscreen.setOnClickListener(new View.OnClickListener() {
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

                Dialog rankDialog = new Dialog(PlayConsecutiveAds.this, R.style.FullHeightDialog);
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
                        new RateWebService(PlayConsecutiveAds.this, requestListener,id,String.valueOf(ratingBar.getRating()),etcomments.getText().toString()).execute();

                    }
                });
                //now that the dialog is set up, it's time to show it
                rankDialog.show();
            }
        });
        playerView = findViewById(R.id.exoplayerview);
        progressBar = findViewById(R.id.progress_bar);
        // btnFullscreen = playerView.findViewById(R.id.full_screen);


    }
   // private final Runnable updateProgressAction = new Runnable() {
     //   @Override
       // public void run() {
         //   updateProgressBar();
     //   }
    //};



    // @Override
    //  public boolean onCreateOptionsMenu(Menu menu) {
    //     getMenuInflater().inflate(R.menu.menu,menu);
    //     return true;
    //  }

    // @Override
    // public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    // int id=item.getItemId();
    //      if(id==R.id.share){
    //       ApplicationInfo
    //  }
    // }

    /* =   @Override
                public void onPause() {
                    super.onPause();
                   exoplayer.stop();
                }***/
    @Override
    protected  void onResume() {
        super.onResume();

        if (!iscomplete&&shareclicked) {
           // Toast.makeText(PlayConsecutiveAds.this,"on resume",Toast.LENGTH_LONG).show();

            simpleExoPlayer.setPlayWhenReady(true);
            simpleExoPlayer.getPlaybackState();
           // circularViewWithTimer.resumeTimer();
            shareclicked=false;
        }
}

    @Override
    protected void onPause() {
        super.onPause();

        if(!iscomplete){
           // Toast.makeText(PlayConsecutiveAds.this,"on paused",Toast.LENGTH_LONG).show();
         // circularViewWithTimer.pauseTimer();
            simpleExoPlayer.setPlayWhenReady(false);
            simpleExoPlayer.getPlaybackState();



        }

    }


    @Override
    protected void onRestart(){
        super.onRestart();
        if(!iscomplete){
         //   Toast.makeText(PlayConsecutiveAds.this,"on restart",Toast.LENGTH_LONG).show();
            simpleExoPlayer.setPlayWhenReady(true);
            simpleExoPlayer.getPlaybackState();
            //circularViewWithTimer.resumeTimer();
        }




    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Set your locale here...

        loadLocale();

    }
    private void setLocale(String lang) {
        Locale locale=new Locale(lang);
        Locale.setDefault(locale);
        Configuration config =new  Configuration();
        config.locale=locale;
        getBaseContext().getResources().updateConfiguration(config,getBaseContext().getResources().getDisplayMetrics());
        SharedPreferences.Editor editor=getSharedPreferences("Settings",MODE_PRIVATE).edit();
        editor.putString("My_Lang",lang);
        editor.apply();

    }
    public void loadLocale(){
        SharedPreferences prefs=getSharedPreferences("Settings",MODE_PRIVATE);
        String language=prefs.getString("My_Lang","");
        setLocale(language);
    }
    @Override
    public void onBackPressed() {

        try {
            if(!iscomplete){
           //     Toast.makeText(PlayConsecutiveAds.this,"on backpress",Toast.LENGTH_LONG).show();
                simpleExoPlayer.setPlayWhenReady(false);
                simpleExoPlayer.getPlaybackState();
         //   circularViewWithTimer.stopTimer();
                }
        }catch (Exception u){}
        finish();
        Intent in=new Intent(PlayConsecutiveAds.this,Dashboard.class);
        startActivity(in);
        //exoplayer.release();
    }

/**    private void updateProgressBar() {
        long duration = simpleExoPlayer == null ? 0 : simpleExoPlayer.getDuration();
        long position = simpleExoPlayer == null ? 0 : simpleExoPlayer.getCurrentPosition();
        // if (!dragging) {
        progressBar2.setProgress((int) ((simpleExoPlayer.getCurrentPosition()*100)/simpleExoPlayer.getDuration()));
        // }
        long bufferedPosition = simpleExoPlayer == null ? 0 : simpleExoPlayer.getBufferedPosition();
        progressBar2.setProgress((int) ((simpleExoPlayer.getCurrentPosition()*100)/simpleExoPlayer.getDuration()));
        // Remove scheduled updates.
        handler.removeCallbacks(updateProgressAction);
        // Schedule an update if necessary.
        int playbackState = simpleExoPlayer == null ? Player.STATE_IDLE : simpleExoPlayer.getPlaybackState();
        if (playbackState != Player.STATE_IDLE && playbackState != Player.STATE_ENDED) {
            long delayMs;
            if (simpleExoPlayer.getPlayWhenReady() && playbackState == Player.STATE_READY) {
                delayMs = 1000 - (position % 1000);
                if (delayMs < 200) {
                    delayMs += 1000;
                }
            } else {
                delayMs = 1000;
            }
            handler.postDelayed(updateProgressAction, delayMs);
        }
    } ***/

}
