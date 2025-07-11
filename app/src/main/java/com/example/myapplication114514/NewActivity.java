package com.example.myapplication114514;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

public class NewActivity extends AppCompatActivity {
  private ViewPager2 viewPager2;
  private int[] imageResIds = {R.mipmap.chunping, R.mipmap.truemusic}; // 替换为你的图片资源ID
  private int[] audioResIds = {R.raw.senpai, R.raw.truemusic}; // 替换为你的音频资源ID
  private ImagePagerAdapter adapter;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_new);

    viewPager2 = findViewById(R.id.viewPager2);
    adapter = new ImagePagerAdapter(this, imageResIds, audioResIds);
    viewPager2.setAdapter(adapter);

    viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
      @Override
      public void onPageSelected(int position) {
        super.onPageSelected(position);
        // 停止上一个页面的音乐
        if (position > 0) {
          ImageFragment previousFragment = (ImageFragment) adapter.getFragment(position - 1);
          if (previousFragment != null) {
            previousFragment.stopAndReleaseMediaPlayer();
          }
        }
      }
    });
  }
}