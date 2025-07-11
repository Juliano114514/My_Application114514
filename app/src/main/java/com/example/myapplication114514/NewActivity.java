package com.example.myapplication114514;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

public class NewActivity extends AppCompatActivity {
  private final int[] imageResIds = {R.mipmap.chunping, R.mipmap.truemusic}; // 替换为你的图片资源ID
  private final int[] audioResIds = {R.raw.senpai, R.raw.truemusic}; // 替换为你的音频资源ID
  private ImagePagerAdapter adapter;
  private int currentPosition = -1; // 用于记录当前页面的位置


  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_new);

    ViewPager2 viewPager2 = findViewById(R.id.viewPager2);
    adapter = new ImagePagerAdapter(this, imageResIds, audioResIds);
    viewPager2.setAdapter(adapter);

    viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
      @Override
      public void onPageSelected(int position) {
        super.onPageSelected(position);

        // 停止当前页面的音乐
        ImageFragment currentFragment = null;
        if (currentPosition >= 0 && currentPosition < adapter.getItemCount()) {
          currentFragment = (ImageFragment) adapter.getFragment(currentPosition);
          if (currentFragment != null) {
            currentFragment.stopAndReleaseMediaPlayer();
          }
        }

        // 更新当前页面的位置
        currentPosition = position;
        currentFragment = (ImageFragment) adapter.getFragment(currentPosition);
        currentFragment.onResume();

      }
    });

  }
}