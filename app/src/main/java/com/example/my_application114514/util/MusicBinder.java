package com.example.my_application114514.util;

import java.util.ArrayList;

import android.os.Binder;

import com.example.my_application114514.data.SongData;
import com.example.my_application114514.listener.PlayerListener;
import com.example.my_application114514.service.MusicService;

public class MusicBinder extends Binder {
  MusicService mMusicService;

  public MusicBinder(MusicService mMusicService) {
    this.mMusicService = mMusicService;
  }

  public void startPlay() {
    mMusicService.startPlay();
  }

  public void updateMusicList(ArrayList<SongData> mList) {
    mMusicService.updateMusicList(mList);
  }

  public void updateMusicCurIndex(int idx) {
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

  public void last() {
    mMusicService.last();
  }

  public void next() {
    mMusicService.next();
  }

  public void stop() {
    mMusicService.stop();
  }

  public SongData getCurSong() {
    return mMusicService.mPlaylist.get(mMusicService.curIndex);
  }

  public int getCurProgress() {
    return mMusicService.getCurProgress();
  }

  public int getEndProgress() {
    return mMusicService.getEndProgress();
  }

  public void seekTo(int progress) {
    mMusicService.seekTo(progress);
  }

  public void setPlayMode(int mode) {
    mMusicService.setPlayMode(mode);
  }

  public void setPlayerListener(PlayerListener playerListener) {
    mMusicService.setPlayerListener(playerListener);
  }
}
