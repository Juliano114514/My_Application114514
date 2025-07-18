package com.example.my_application114514;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.my_application114514.data.GlobalConstants;
import com.example.my_application114514.data.SongData;
import com.example.my_application114514.service.MusicService;

import androidx.appcompat.app.AppCompatActivity;

public class PlayActivity extends AppCompatActivity {

  private ArrayList<SongData> mPlaylist;
  private int curIndex;
  private MusicService.MusicBinder mMusicBinder;
  private ServiceConnection mlink = new ServiceConnection() {
    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
      // 通过binder传递信息
      // onBind 返回的 Binder 会传递到这里
      mMusicBinder = (MusicService.MusicBinder)service;
      mMusicBinder.updateMusicList(mPlaylist);
      mMusicBinder.updateMusicCurIndex(curIndex);
      mMusicBinder.startPlay();
    }
    @Override
    public void onServiceDisconnected(ComponentName name) {}
  };

  // 设定点击事件监听器
  ImageView mStartSwitch;  // 播放键
  ImageView mNextSwitch;  // 下一首
  ImageView mLastSwitch;  // 上一首
  ImageView mStopSwitch;  // 停止
  ImageView mModeSwitch;  // 播放模式
  ImageView mAlbumPic;  // 播放模式


  TextView mTitle;  // 标题

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.play_layout);

    Intent intent = getIntent();
    curIndex = intent.getIntExtra(GlobalConstants.KEY_SONG_INDEX,0);
    mPlaylist = (ArrayList<SongData>) intent.getSerializableExtra(GlobalConstants.KEY_SONG_LIST);

    updateTitle(mPlaylist.get(curIndex).getSongName());
    updatePic(mPlaylist.get(curIndex).getSongName());
    startMusicService();
    initListener();

  }


  private void initListener(){
    mStartSwitch = findViewById(R.id.iv_play_pause);  // 播放键
    mNextSwitch = findViewById(R.id.next_switch);  // 下一首
    mLastSwitch = findViewById(R.id.last_switch);  // 上一首
    mStopSwitch = findViewById(R.id.stop_switch);  // 停止
    mModeSwitch = findViewById(R.id.play_mode);  // 播放模式

    mStartSwitch.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
          if (mMusicBinder.isPlaying()) {
            mMusicBinder.pause();
            mStartSwitch.setImageResource(R.drawable.baseline_play_arrow_24);
          } else {
            mMusicBinder.play();
            mStartSwitch.setImageResource(R.drawable.baseline_pause_24);
          }
        }
    });

    mNextSwitch.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        mMusicBinder.next();
        updateTitle(mMusicBinder.getCurSong().getSongName());
        updatePic(mMusicBinder.getCurSong().getSongName());
      }
    });

    mLastSwitch.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        mMusicBinder.last();
        updatePic(mMusicBinder.getCurSong().getSongName());
      }
    });

    mStopSwitch.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        mMusicBinder.stop();
        mStartSwitch.setImageResource(R.drawable.baseline_play_arrow_24);
      }
    });

  }

  // 更新标题
  private void updateTitle(String name){
    mTitle = findViewById(R.id.play_titlle);
    mTitle.setText(name);
  }

  private void updatePic(String name){
    Bitmap albumCover = getAlbumCover(name);
    mAlbumPic = findViewById(R.id.albumpic);
    if (albumCover != null) {
      mAlbumPic.setImageBitmap(albumCover);
    }
  }

  private void startMusicService() {
    // bind 启动 service
    Intent intent = new Intent(this, MusicService.class);
    bindService(intent,mlink,BIND_AUTO_CREATE);
  }



  public Bitmap getAlbumCover(String mp3FileName) {
    AssetManager assetManager = getAssets();
    try {
      // 假设专辑封面图片的命名规则是去掉 .mp3 后缀，加上 .jpg
      String coverFileName = mp3FileName.replace(".mp3", ".jpg");
      InputStream inputStream = assetManager.open("" + coverFileName);
      return BitmapFactory.decodeStream(inputStream);
    } catch (IOException e) {
      e.printStackTrace();
      Toast.makeText(this, "Error loading cover image: " + mp3FileName, Toast.LENGTH_SHORT).show();
      return null;
    }
  }

}