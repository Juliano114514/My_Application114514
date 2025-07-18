package com.example.my_application114514;

import java.util.ArrayList;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;

import com.example.my_application114514.data.GlobalConstants;
import com.example.my_application114514.data.SongData;
import com.example.my_application114514.service.MusicService;

import androidx.appcompat.app.AppCompatActivity;

public class PlayActivity extends AppCompatActivity {

  private ArrayList<SongData> mPlaylist;
  private int curIndex;
  private ServiceConnection mlink = new ServiceConnection() {
    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
      // 通过binder传递信息
      // onBind 返回的 Binder 会传递到这里
      MusicService.MusicBinder musicBinder = (MusicService.MusicBinder)service;
      musicBinder.updateMusicList(mPlaylist);
      musicBinder.updateMusicCurIndex(curIndex);
      musicBinder.startPlay();
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }
  };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.play_layout);

    Intent intent = getIntent();
    curIndex = intent.getIntExtra(GlobalConstants.KEY_SONG_INDEX,0);
    mPlaylist = (ArrayList<SongData>) intent.getSerializableExtra(GlobalConstants.KEY_SONG_LIST);

    startMusicService();
  }

  private void startMusicService() {
    // bind 启动 service
    Intent intent = new Intent(this, MusicService.class);
    bindService(intent,mlink,BIND_AUTO_CREATE);
  }
}