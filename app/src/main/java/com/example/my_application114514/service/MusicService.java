package com.example.my_application114514.service;

import java.io.IOException;
import java.util.ArrayList;

import android.app.Service;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;

import com.example.my_application114514.data.SongData;

import androidx.annotation.Nullable;

public class MusicService extends Service {

  private MediaPlayer mMediaPlayer;
  private ArrayList<SongData> mPlaylist;
  private int curIndex;


  @Override
  public void onCreate() {
    super.onCreate();
    mMediaPlayer = new MediaPlayer();
    mPlaylist = new ArrayList<>();

  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    if(mMediaPlayer!=null){
      mMediaPlayer.stop();
      mMediaPlayer.release();
      mMediaPlayer = null;
    }
  }

  @Override
  public boolean onUnbind(Intent intent) {
    return super.onUnbind(intent);
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    return super.onStartCommand(intent, flags, startId);
  }

  @Nullable
  @Override
  public IBinder onBind(Intent intent) {
    return new MusicBinder(this); // 返回binder的同时，通过binder的构造函数与service绑定
  }

  void updateMusicList(ArrayList<SongData> mList){
    mPlaylist = mList;
  }

  void updateMusicCurIndex(int idx){
    curIndex = idx;
  }

  void startPlay(){
    if(curIndex<0 || curIndex >= mPlaylist.size()){
      return;
    }

    SongData song = mPlaylist.get(curIndex);
    String songName = song.getSongName();

    AssetManager assetManager = getAssets();

    try{
      AssetFileDescriptor fileDescriptor = assetManager.openFd(songName);
      mMediaPlayer.setDataSource(fileDescriptor.getFileDescriptor(),
          fileDescriptor.getStartOffset(),
          fileDescriptor.getLength());
      mMediaPlayer.prepare();
      mMediaPlayer.start();

    } catch (IOException e) {
      throw new RuntimeException(e);
    }

  }

  // =================定义binder实现====================
  public class MusicBinder extends Binder {
    MusicService mMusicService;

    public MusicBinder(MusicService mMusicService) {
      this.mMusicService = mMusicService;
    }


    public void startPlay(){
      mMusicService.startPlay();
    }

    public void updateMusicList(ArrayList<SongData> mList){
      mMusicService.updateMusicList(mList);
    }

    public void updateMusicCurIndex(int idx){
      mMusicService.updateMusicCurIndex(idx);
    }

  }
}
