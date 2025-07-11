package com.example.myapplication114514;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class ImageFragment extends Fragment {
  private MediaPlayer mediaPlayer;
  private int imageResId;
  private int audioResId;
  private boolean isPlaying = false;

  public static ImageFragment newInstance(int imageResId, int audioResId) {
    ImageFragment fragment = new ImageFragment();
    Bundle args = new Bundle();
    args.putInt("imageResId", imageResId);
    args.putInt("audioResId", audioResId);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      imageResId = getArguments().getInt("imageResId");
      audioResId = getArguments().getInt("audioResId");
    }
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_music, container, false);
    ImageView imageView = view.findViewById(R.id.imageView3);
    imageView.setImageResource(imageResId);

    mediaPlayer = MediaPlayer.create(getContext(), audioResId);
    mediaPlayer.setOnCompletionListener(mp -> {
      mp.release();
      mediaPlayer = null;
      isPlaying = false;
    });

    mediaPlayer.start();
    isPlaying = true;

    return view;
  }

  @Override
  public void onResume() {
    super.onResume();
    if (isPlaying && mediaPlayer != null && !mediaPlayer.isPlaying()) {
      mediaPlayer.start();
    }
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    stopAndReleaseMediaPlayer();
  }

  public void stopAndReleaseMediaPlayer() {
    if (mediaPlayer != null) {
      if (mediaPlayer.isPlaying()) {
        mediaPlayer.stop();
      }
      mediaPlayer.release();
      mediaPlayer = null;
      isPlaying = false;
    }
  }
}