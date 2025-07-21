package com.example.my_application114514.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import android.app.Service;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;

import com.example.my_application114514.data.SongData;
import com.example.my_application114514.listener.PlayerListener;
import com.example.my_application114514.util.MusicBinder;
import com.example.my_application114514.util.PlayModeHelper;

import androidx.annotation.Nullable;

public class MusicService extends Service {

  private MediaPlayer mMediaPlayer;
  public ArrayList<SongData> mPlaylist;
  public int curIndex;

  int mCurMode = PlayModeHelper.PLAY_ORDERED;  // 播放模式
  private int randomCnt = -1;  // 随机计数器
  private int[] randomOrder;  // 随机播放顺序数组

  private PlayerListener mPlayerListener;

  @Override
  public void onCreate() {
    super.onCreate();
    mMediaPlayer = new MediaPlayer();
    mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
      @Override
      public void onCompletion(MediaPlayer mp) {
        switch (mCurMode) {
          case PlayModeHelper.PLAY_ORDERED:
            orderedMode();break;
          case PlayModeHelper.PLAY_CIRCLE:
            circleMode();break;
          case PlayModeHelper.PLAY_SINGLE:
            singleMode();break;
          case PlayModeHelper.PLAY_RANDOM:
            randomMode();break;
          default:break;
        }

        // 自然播放结束到下一首后，通知主活动更新界面
        if(mPlayerListener != null){
          mPlayerListener.onComplete(curIndex,mPlaylist.get(curIndex));
        }

      }
    });

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

  public void updateMusicList(ArrayList<SongData> mList){
    mPlaylist = mList;
  }

  public void updateMusicCurIndex(int idx){curIndex = idx;}

  public void startPlay(){
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
    playIdx(preIdx);
  }

  public void next(){
    int nxtIdx = (curIndex+1) % mPlaylist.size();
    playIdx(nxtIdx);
  }

  public void playIdx(int position){
    updateMusicCurIndex(position);
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

  public int getCurProgress() {
    return mMediaPlayer.getCurrentPosition();
  }

  public int getEndProgress(){
    return mMediaPlayer.getDuration();
  }

  public void seekTo(int progress) {
    mMediaPlayer.seekTo(progress);
  }

  public void setPlayMode(int mode) {
    mCurMode = mode;
  }

  public void setPlayerListener(PlayerListener playerListener){
    this.mPlayerListener = playerListener;
  }

  // ====================定义播放模式方法====================

  void orderedMode(){ if(curIndex+1 != mPlaylist.size()){next();} }
  void circleMode(){ next(); }
  void singleMode(){ playIdx(curIndex); }
  void randomMode(){
    // 更新随机播放列表
    if(randomCnt == -1 || randomCnt == mPlaylist.size()){
      randomCnt = 0;
      randomOrder = getRandomPermutation(mPlaylist.size());
    }

    int nextIdx = randomOrder[randomCnt++];
    playIdx(nextIdx);
  }


  public static int[] getRandomPermutation(int n) {
    // 创建一个包含 0 到 n-1 的列表
    List<Integer> list = IntStream.range(0, n).boxed().collect(Collectors.toList());
    // 使用 Collections.shuffle 方法对列表进行随机排序
    Collections.shuffle(list);
    // 将列表转换为 int 数组
    return list.stream().mapToInt(Integer::intValue).toArray();
  }




}
