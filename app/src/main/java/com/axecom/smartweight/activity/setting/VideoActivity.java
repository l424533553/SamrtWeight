package com.axecom.smartweight.activity.setting;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.MediaController;
import android.widget.VideoView;

import com.axecom.smartweight.R;

public class VideoActivity extends AppCompatActivity {
    private void initView() {
        String url = "http://www.anluyun.com:3100/main/smart.mp4";
        VideoView videoView = findViewById(R.id.mVideoView);
        MediaController localMediaController = new MediaController(this);
        videoView.setMediaController(localMediaController);
        videoView.setVideoPath(url);
        videoView.requestFocus();
        videoView.start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        initView();
    }

}
