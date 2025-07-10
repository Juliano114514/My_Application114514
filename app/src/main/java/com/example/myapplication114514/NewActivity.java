package com.example.myapplication114514;

import android.os.Bundle;
import android.media.MediaPlayer;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class NewActivity extends AppCompatActivity{
  private MediaPlayer mediaPlayer;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState){
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_new);

    mediaPlayer = MediaPlayer.create(this,R.raw.true_music);
    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
      @Override
      public void onCompletion(MediaPlayer mediaPlayer) {
        mediaPlayer.release();
        mediaPlayer = null;
      }
    });

    mediaPlayer.start();
  }

  //@Override
  protected void onDestry(){
    super.onDestroy();
    if(mediaPlayer != null){
      if(mediaPlayer.isPlaying()){
        mediaPlayer.stop();
      }
      mediaPlayer.release();
      mediaPlayer = null;
    }
  }
}
