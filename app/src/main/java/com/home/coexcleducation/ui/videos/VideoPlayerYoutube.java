package com.home.coexcleducation.ui.videos;

import static com.home.coexcleducation.utils.Helper.extractYTId;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.home.coexcleducation.R;
import com.home.coexcleducation.ui.adaptar.VideoAdaptar;
import com.home.coexcleducation.utils.CoexclLogs;
import com.home.coexcleducation.utils.Constants;
import com.home.coexcleducation.utils.FirebaseAnalyticsCoexcl;
import com.home.coexcleducation.utils.ViewUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VideoPlayerYoutube extends YouTubeBaseActivity implements
        YouTubePlayer.OnInitializedListener {

    private YouTubePlayerView youTubeView;
    private YouTubePlayer youTubePlayer;
    private static final int RECOVERY_DIALOG_REQUEST = 1;
    private ListView mVideoListView;
    private ImageView mBack;
    private TextView mTopic;
    String mVideoUrl;
    ArrayList<HashMap<String, String>> mListOfVideos = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.youtube_video_layout);

        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.BLACK);

        youTubeView = findViewById(R.id.youtube_view);
        mBack =  findViewById(R.id.back);
        mVideoListView = findViewById(R.id.subject_videos);
        mTopic = findViewById(R.id.topicName);

        mListOfVideos = (ArrayList<HashMap<String, String>>) getIntent().getSerializableExtra("videoList");

        mVideoUrl = mListOfVideos.get(getIntent().getIntExtra("videoIndex", 0)).get("url");
        mTopic.setText(mListOfVideos.get(getIntent().getIntExtra("videoIndex", 0)).get("chaptername"));
        // Initializing video player with developer key
        youTubeView.initialize(Constants.APIKEY_YOUTUBE, this);

        if(mListOfVideos.size() > 0) {
            VideoAdaptar adapter = new VideoAdaptar(this, mListOfVideos, "video");
            mVideoListView.setAdapter(adapter);
        }

        mVideoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mVideoUrl = mListOfVideos.get(position).get("url");
                youTubePlayer.loadVideo(extractYTId(mVideoUrl));
                mTopic.setText(mListOfVideos.get(position).get("chaptername"));
                new FirebaseAnalyticsCoexcl().logFirebaseEvent(VideoPlayerYoutube.this, "", "Home", "Video Played");
            }
        });

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ViewUtils().exitActivityToBottom(VideoPlayerYoutube.this);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        new ViewUtils().exitActivityToBottom(VideoPlayerYoutube.this);
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                        YouTubeInitializationResult errorReason) {
        if (errorReason.isUserRecoverableError()) {
            errorReason.getErrorDialog(this, RECOVERY_DIALOG_REQUEST).show();
        } else {
            String errorMessage = "error";
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                        YouTubePlayer player, boolean wasRestored) {
        if (!wasRestored) {
            youTubePlayer = player;
            player.loadVideo(extractYTId(mVideoUrl));
            player.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
            new FirebaseAnalyticsCoexcl().logFirebaseEvent(this, "", "Home", "Video Played");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECOVERY_DIALOG_REQUEST) {
            getYouTubePlayerProvider().initialize(Constants.APIKEY_YOUTUBE, this);
        }
    }

    private YouTubePlayer.Provider getYouTubePlayerProvider() {
        return (YouTubePlayerView) findViewById(R.id.youtube_view);
    }



}