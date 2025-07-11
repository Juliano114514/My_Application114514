package com.example.myapplication114514;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;
import java.util.List;

public class ImagePagerAdapter extends FragmentStateAdapter {
  private final int[] imageResIds;
  private final int[] audioResIds;
  private List<Fragment> fragments = new ArrayList<>();

  public ImagePagerAdapter(@NonNull FragmentActivity fragmentActivity, int[] imageResIds, int[] audioResIds) {
    super(fragmentActivity);
    this.imageResIds = imageResIds;
    this.audioResIds = audioResIds;
  }

  @NonNull
  @Override
  public Fragment createFragment(int position) {
    ImageFragment fragment = ImageFragment.newInstance(imageResIds[position], audioResIds[position]);
    fragments.add(fragment);
    return fragment;
  }

  @Override
  public int getItemCount() {
    return imageResIds.length;
  }

  public Fragment getFragment(int position) {
    if (position >= 0 && position < fragments.size()) {
      return fragments.get(position);
    }
    return null;
  }
}