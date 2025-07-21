package com.example.my_application114514;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

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
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.my_application114514.data.GlobalConstants;
import com.example.my_application114514.data.SongData;
import com.example.my_application114514.listener.PlayerListener;
import com.example.my_application114514.service.MusicService;
import com.example.my_application114514.util.MusicBinder;
import com.example.my_application114514.util.PlayModeHelper;
import com.example.my_application114514.util.TimeUtil;

import androidx.appcompat.app.AppCompatActivity;

public class PlayActivity extends AppCompatActivity {

  private ArrayList<SongData> mPlaylist;
  private int curIndex;
  private MusicBinder mMusicBinder;
  private ServiceConnection mlink = new ServiceConnection() {
    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
      // 通过binder传递信息
      // onBind 返回的 Binder 会传递到这里
      mMusicBinder = (MusicBinder)service;
      mMusicBinder.updateMusicList(mPlaylist);
      mMusicBinder.updateMusicCurIndex(curIndex);
      mMusicBinder.startPlay();
      mMusicBinder.setPlayerListener(new PlayerListener() {

        @Override
        public void onComplete(int songIndex, SongData song) {
          updateTitle(song.getSongName());
          updatePic(song.getSongName());
          updateUI();
          // 如果是顺序播放的最后一首歌，播放键进入暂停模式
          if (!mMusicBinder.isPlaying()) {
            mStartSwitch.setImageResource(R.drawable.baseline_play_arrow_24);
          }
        }

        @Override
        public void onNext(int songIndex, SongData song) {

        }

        @Override
        public void onLast(int songIndex, SongData song) {

        }

        @Override
        public void onPause(int songIndex, SongData song) {

        }

        @Override
        public void onPlay(int songIndex, SongData song) {

        }
      });

      updateUI();
    }
    @Override
    public void onServiceDisconnected(ComponentName name) {}
  };

  // 设定点击事件监听器
  ImageView mStartSwitch; // 播放键
  ImageView mNextSwitch;  // 下一首
  ImageView mLastSwitch;  // 上一首
  ImageView mStopSwitch;  // 停止
  ImageView mModeSwitch;  // 播放模式
  ImageView mAlbumPic;    // 播放模式

  TextView mTitle;    // 标题
  TextView mCurtime;  // 开始时间
  TextView mEndtime;  // 结束时间

  SeekBar mProgress;  // 进度条

  Timer timer = null; // 进度条计时器
  private boolean isSeekBarDragging; // 进度条状态

  int mCurMode = PlayModeHelper.PLAY_ORDERED;

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
    initView();

  }


  private void initView(){
    mStartSwitch = findViewById(R.id.iv_play_pause);  // 播放键
    mNextSwitch = findViewById(R.id.next_switch);  // 下一首
    mLastSwitch = findViewById(R.id.last_switch);  // 上一首
    mStopSwitch = findViewById(R.id.stop_switch);  // 停止
    mModeSwitch = findViewById(R.id.play_mode);  // 播放模式

    mCurtime = findViewById(R.id.cur_time);  // 开始时间
    mEndtime = findViewById(R.id.tot_time); // 总时间

    mProgress = findViewById(R.id.proc_bar); // 进度条

    // ================================================================ 开始/暂停
    mStartSwitch.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
          if (mMusicBinder.isPlaying()) {
            mMusicBinder.pause();
            mStartSwitch.setImageResource(R.drawable.baseline_play_arrow_24);
          } else {
            mMusicBinder.play();
            mStartSwitch.setImageResource(R.drawable.baseline_pause_24);
            updateCurTimeText();
            updateUI();
          }
        }
    });

    // ================================================================ 下一首
    mNextSwitch.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        mMusicBinder.next();
        updateTitle(mMusicBinder.getCurSong().getSongName());
        updatePic(mMusicBinder.getCurSong().getSongName());
        updateUI();
      }
    });

    // ================================================================ 上一首
    mLastSwitch.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        mMusicBinder.last();
        updateTitle(mMusicBinder.getCurSong().getSongName());
        updatePic(mMusicBinder.getCurSong().getSongName());
        updateUI();
      }
    });

    // ================================================================ 停止
    mStopSwitch.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        mMusicBinder.stop();
        mStartSwitch.setImageResource(R.drawable.baseline_play_arrow_24);
        mMusicBinder.seekTo(0);
        updateUI();
      }
    });

    // ================================================================ 切换播放模式
    mModeSwitch.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        mCurMode = (mCurMode+1)%4;
        mModeSwitch.setImageResource(PlayModeHelper.setImage(mCurMode));
        mMusicBinder.setPlayMode(mCurMode);
      }
    });

    // ================================================================ 进度条交互
    mProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
      @Override
      public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        updateCurTimeText(progress);
      }

      @Override
      public void onStartTrackingTouch(SeekBar seekBar) { isSeekBarDragging = true; }

      @Override
      public void onStopTrackingTouch(SeekBar seekBar) {
        isSeekBarDragging = false;
        int progress = seekBar.getProgress();
        mMusicBinder.seekTo(progress);
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

  private void updateUI(){
    // 当前时间更新
    int curProgress = updateCurTimeText();

    // 总时间更新
    int endProgress = mMusicBinder.getEndProgress();
    mEndtime.setText(TimeUtil.msToMMSS(endProgress));

    // 设定进度条
    mProgress.setMax(endProgress-1000);
    mProgress.setProgress(curProgress);

    if(timer!=null){return;}
    timer = new Timer();
    timer.schedule(new TimerTask() {
      @Override
      public void run() {
        runOnUiThread(new Runnable() {
          @Override
          public void run() {
            if(!isSeekBarDragging && mMusicBinder.isPlaying()){
              int curProgress = updateCurTimeText();
              mProgress.setProgress(curProgress);
            }
          }
        });
      }
    },0,1000);
  }

  private int updateCurTimeText(){
    int curProgress = mMusicBinder.getCurProgress();
    mCurtime.setText(TimeUtil.msToMMSS(curProgress));
    return curProgress;
  }

  private int updateCurTimeText(int progress){
    int curProgress = progress;
    mCurtime.setText(TimeUtil.msToMMSS(curProgress));
    return curProgress;
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

  @Override
  protected void onDestroy() {
    super.onDestroy();
    if(timer!=null){
      timer.cancel();
      timer = null;
    }
  }
}