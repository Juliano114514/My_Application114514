package com.example.my_application114514.data;

import java.io.Serializable;

import androidx.annotation.NonNull;

public class SongData implements Serializable {
  private String songName;

  public SongData() {}
  public SongData(String songName) {
    this.songName = songName;
  }

  public String getSongName() {
    return songName;
  }

  public void setSongName(String songName) {
    this.songName = songName;
  }

  @NonNull
  @Override
  public String toString() {
    return super.toString();
  }
}
