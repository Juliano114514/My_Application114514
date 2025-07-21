package com.example.my_application114514.util;

import com.example.my_application114514.R;

public class PlayModeHelper {

  public final static int PLAY_ORDERED = 0;
  public final static int PLAY_CIRCLE = 1;
  public final static int PLAY_SINGLE = 2;
  public final static int PLAY_RANDOM = 3;

  public static int setImage(int mCurMode) {
    switch (mCurMode){
      case PLAY_ORDERED:
        return R.drawable.outline_playlist_play_24;
      case PLAY_CIRCLE:
        return R.drawable.baseline_repeat_24;
      case PLAY_RANDOM:
        return R.drawable.baseline_shuffle_24;
      case PLAY_SINGLE:
        return R.drawable.baseline_repeat_one_24;
      default:
        return R.drawable.baseline_repeat_one_24;
    }
}

}
