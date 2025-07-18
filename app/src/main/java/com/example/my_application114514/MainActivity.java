package com.example.my_application114514;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.my_application114514.adapter.MySongLIstAdapter;
import com.example.my_application114514.data.GlobalConstants;
import com.example.my_application114514.data.SongData;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {

  private RecyclerView mRcvSongList = null;
  private MySongLIstAdapter mSongListAdapter = null;
  private ArrayList<SongData> mPlaylist = null;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.song_list);

    initView();
    initData();
    initSongList();
  }

  private void initData() {
    mPlaylist = new ArrayList<>();
    mPlaylist.add(new SongData("general_kim.mp3"));
    mPlaylist.add(new SongData("general_kim.mp3"));
    mPlaylist.add(new SongData("general_kim.mp3"));
    mPlaylist.add(new SongData("general_kim.mp3"));
  }

  private void initSongList() {
    mSongListAdapter = new MySongLIstAdapter(this,mPlaylist);
    mSongListAdapter.setmItemClickListener(new MySongLIstAdapter.OnItemClickListener() {
      @Override
      public void onItemClick(int position) {
        Toast.makeText(MainActivity.this,"点击了"+(position+1),Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MainActivity.this, PlayActivity.class);
        intent.putExtra(GlobalConstants.KEY_SONG_LIST,mPlaylist);
        intent.putExtra(GlobalConstants.KEY_SONG_INDEX,position);
        startActivity(intent);
      }
    });
    mRcvSongList.setAdapter(mSongListAdapter);
    mRcvSongList.setLayoutManager(new LinearLayoutManager(this));
  }

  private void initView(){
    mRcvSongList = findViewById(R.id.rcv_song_list);
  }

}