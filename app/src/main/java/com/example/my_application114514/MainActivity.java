package com.example.my_application114514;

import java.io.IOException;
import java.util.ArrayList;

import android.animation.ObjectAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetManager;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.my_application114514.adapter.MySongLIstAdapter;
import com.example.my_application114514.data.GlobalConstants;
import com.example.my_application114514.data.SongData;
import com.example.my_application114514.service.MusicService;
import com.example.my_application114514.util.MusicBinder;
import com.example.my_application114514.util.PlayModeHelper;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {

  private MySongLIstAdapter mSongListAdapter = null;
  private ArrayList<SongData> mPlaylist = null;
  private MusicBinder mMusicBinder = null;

  private ObjectAnimator mRotationAnimator = null;
  private LinearLayout mStateBar = null; // 播放状态栏
  private RecyclerView mRcvSongList = null;
  private TextView mSongName = null;

  private ImageView mBarNextSwitch = null;
  private ImageView mBarLastSwitch = null;
  private ImageView mBarStartSwitch = null;
  private ImageView mIcon = null;

  private int mCurMode = PlayModeHelper.PLAY_ORDERED;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.song_list);
    initView();
    initData();
    initSongList();

    // 注册 OnBackPressedCallback，返回键默认挂起
    OnBackPressedCallback callback = new OnBackPressedCallback(true) {
      @Override
      public void handleOnBackPressed() { moveTaskToBack(true); }  // 将 Activity 移到后台
    };
    getOnBackPressedDispatcher().addCallback(this, callback);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    Intent intent = new Intent(MainActivity.this, MusicService.class);
    stopService(intent); // 应用销毁时结束播放服务
  }

  @Override
  protected void onResume() {
    super.onResume();
    IntentFilter filter = new IntentFilter(GlobalConstants.KEY_SONG_BINDER);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      registerReceiver(myBroadcastReceiver, filter,Context.RECEIVER_EXPORTED);
    }
  }

  protected void onPause() {
    super.onPause();
    // 选择在 onPause 中取消注册
    unregisterReceiver(myBroadcastReceiver);
  }

  private void initData() {
    mPlaylist = new ArrayList<>();
    AssetManager assetManager = getAssets();
    try {
      // 读取 songs 文件夹中的 MP3 文件列表
      String[] songFiles = assetManager.list("");
      for (String songFile : songFiles) {
        if (songFile.endsWith(".mp3")) {
          mPlaylist.add(new SongData(songFile));
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
      Toast.makeText(this, "Error loading songs from assets", Toast.LENGTH_SHORT).show();
    }
  }

  private void initSongList() {
    mSongListAdapter = new MySongLIstAdapter(this,mPlaylist);
    mSongListAdapter.setmItemClickListener(new MySongLIstAdapter.OnItemClickListener() {
      @Override
      public void onItemClick(int position) {
        goToPlay(position);
      }
    });
    mRcvSongList.setAdapter(mSongListAdapter);
    mRcvSongList.setLayoutManager(new LinearLayoutManager(this));
  }

  private void initView(){
    mRcvSongList = findViewById(R.id.rcv_song_list);
    mStateBar = findViewById(R.id.state_bar);
    mStateBar.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if(mMusicBinder == null){
          Toast.makeText(MainActivity.this, "当前未播放任何曲目！", Toast.LENGTH_SHORT).show();
        }
        else{
          goToPlay();
        }
      }
    });

    mSongName = findViewById(R.id.bar_song_name);
    mSongName.setSelected(true);

    mBarStartSwitch = findViewById(R.id.bar_play_pause);
    mBarLastSwitch = findViewById(R.id.bar_last_switch);
    mBarNextSwitch = findViewById(R.id.bar_next_switch);

    mBarStartSwitch.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if(mMusicBinder == null){
          Toast.makeText(MainActivity.this, "当前未播放任何曲目！", Toast.LENGTH_SHORT).show();
          return;
        }
        if(mMusicBinder.isPlaying()){
          mMusicBinder.pause();
          mBarStartSwitch.setImageResource(R.drawable.baseline_play_arrow_24);
          mRotationAnimator.pause();
        }
        else{
          mMusicBinder.play();
          mBarStartSwitch.setImageResource(R.drawable.baseline_pause_24);
          mRotationAnimator.resume();
        }
      }
    });

    mBarNextSwitch.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if(mMusicBinder == null){
          Toast.makeText(MainActivity.this, "当前未播放任何曲目！", Toast.LENGTH_SHORT).show();
          return;
        }
        mMusicBinder.next();
        updateBar();
      }
    });

    mBarLastSwitch.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if(mMusicBinder == null){
          Toast.makeText(MainActivity.this, "当前未播放任何曲目！", Toast.LENGTH_SHORT).show();
          return;
        }
        mMusicBinder.last();
        updateBar();
      }
    });

    mIcon = findViewById(R.id.bar_bottom_icon);
    mRotationAnimator = ObjectAnimator.ofFloat(mIcon, "rotation", 0f, 360f);
    mRotationAnimator.setDuration(8000); // 动画持续时间为 10 秒
    mRotationAnimator.setRepeatCount(ObjectAnimator.INFINITE); // 无限循环
    mRotationAnimator.setRepeatMode(ObjectAnimator.RESTART); // 重复模式为重新开始
  }


  private BroadcastReceiver myBroadcastReceiver = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
      ArrayList<MusicBinder> mBinder = (ArrayList<MusicBinder>)intent.getSerializableExtra(GlobalConstants.KEY_SONG_BINDER);
      mCurMode = intent.getIntExtra(GlobalConstants.KEY_SONG_PLAY_MODE,0);
      if (mBinder != null && !mBinder.isEmpty()) {
        mMusicBinder = mBinder.get(0);
        updateBar();
      }
    }
  };

  private void updateBar(){
    mSongName.setText(mMusicBinder.getCurSong().getSongName()); // 更新播放标题
    mRotationAnimator.start();
    if(mMusicBinder.isPlaying()){
      mBarStartSwitch.setImageResource(R.drawable.baseline_pause_24);
    }else{
      mBarStartSwitch.setImageResource(R.drawable.baseline_play_arrow_24);
    }
  }


  private void goToPlay(int position){
    Intent intent = new Intent(MainActivity.this, PlayActivity.class);
    intent.putExtra(GlobalConstants.KEY_SONG_LIST,mPlaylist);
    intent.putExtra(GlobalConstants.KEY_SONG_INDEX,position);
    startActivity(intent);
  }

  private void goToPlay(){
    int position = mMusicBinder.getCurIdx();
    ArrayList<MusicBinder> tmp = new ArrayList<>();
    tmp.add(mMusicBinder);
    Intent intent = new Intent(MainActivity.this, PlayActivity.class);
    intent.putExtra(GlobalConstants.KEY_SONG_LIST,mPlaylist);
    intent.putExtra(GlobalConstants.KEY_SONG_INDEX,position);
    intent.putExtra(GlobalConstants.KEY_SONG_BINDER,tmp);

    startActivity(intent);
  }
}