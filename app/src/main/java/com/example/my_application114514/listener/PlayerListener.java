package com.example.my_application114514.listener;

import com.example.my_application114514.data.SongData;

public interface PlayerListener {
  void onComplete(int songIndex, SongData song);
  void onNext(int songIndex, SongData song);
  void onLast(int songIndex, SongData song);
  void onPause(int songIndex, SongData song);
  void onPlay(int songIndex, SongData song);
}
