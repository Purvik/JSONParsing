package com.purvik.jsonparsing;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.google.android.youtube.player.YouTubePlayerView;



public class PlayerActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener{
	
	
	 private static final int RECOVERY_REQUEST = 1;
	 String videoTag = "", videoTitle = "", videoDescription = "";
	 Intent mainIntent;

	 private YouTubePlayerView youTubeView;
	 TextView vTitle, vDescription;
	
	    
	    
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player_activity);
        
        youTubeView = (YouTubePlayerView)findViewById(R.id.youtube_View);
        youTubeView.initialize(Config.YOUTUBE_API_KEY, PlayerActivity.this);
        
        vTitle = (TextView)findViewById(R.id.VideoTitle);
        vDescription = (TextView)findViewById(R.id.VideoDescription);
        
        mainIntent = getIntent();
        videoTag = mainIntent.getStringExtra("y_link");
        videoTitle = mainIntent.getStringExtra("name");
        videoDescription = mainIntent.getStringExtra("desc");
        
        vTitle.setText(videoTitle);
    	vDescription.setText(videoDescription);
        
        
        Log.i("vTag", videoTag);
        
	}
	

	//Two Overrided Methods of Interface
    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {

        //https://www.youtube.com/watch?v=8zszQgfmq30
        if(!b){
        	
        	/*mainIntent = getIntent();
            videoTag = mainIntent.getStringExtra("y_link");
            Log.i("vTag", videoTag);*/
        	/*
        	vTitle.setText(videoTitle);
        	vDescription.setText(videoDescription);*/
        	
            youTubePlayer.cueVideo(videoTag);

        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult errorReason) {

        if (errorReason.isUserRecoverableError()) {
            errorReason.getErrorDialog(this, RECOVERY_REQUEST).show();
        } else {
            String error = String.format(getString(R.string.player_error), errorReason.toString());
            Toast.makeText(this, error, Toast.LENGTH_LONG).show();
        }

    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == RECOVERY_REQUEST) {

            // Retry initialization if user performed a recovery action
            getYouTubePlayerProvider().initialize(Config.YOUTUBE_API_KEY, this);
        }

    }

    protected Provider getYouTubePlayerProvider() {
        return youTubeView;
    }
}
