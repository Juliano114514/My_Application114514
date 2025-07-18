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

  void updateMusicCurIndex(int idx){curIndex = idx;}

  void startPlay(){
    if(curIndex<0 || curIndex >= mPlaylist.size()){
      return;
    }

    SongData song = mPlaylist.get(curIndex);
    String songName = song.getSongName();

    AssetManager assetManager = getAssets();

    try{
      mMediaPlayer.stop();
      mMediaPlayer.reset();
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

  public boolean isPlaying() {
    return mMediaPlayer.isPlaying();
  }

  public void pause() {
    if(!mMediaPlayer.isPlaying()){
      return;
    }
    mMediaPlayer.pause();
  }

  public void play() {
    if(mMediaPlayer.isPlaying()){
      return;
    }
    mMediaPlayer.start();
  }

  public void last(){
    int preIdx = (curIndex-1 + mPlaylist.size()) % mPlaylist.size();
    updateMusicCurIndex(preIdx);
    startPlay();
  }

  public void next(){
    int nxtIdx = (curIndex+1) % mPlaylist.size();
    updateMusicCurIndex(nxtIdx);
    startPlay();
  }

  public void stop() {
    mMediaPlayer.stop();
    try{
    mMediaPlayer.prepare();
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

    public boolean isPlaying() {
      return mMusicService.isPlaying();
    }

    public void pause() {
      mMusicService.pause();
    }

    public void play() {
      mMusicService.play();
    }

    public void last(){
      mMusicService.last();
    }

    public void next(){
      mMusicService.next();
    }

    public void stop() {
      mMusicService.stop();
    }

    public SongData getCurSong(){
      return mMusicService.mPlaylist.get(mMusicService.curIndex);
    }
  }
}
