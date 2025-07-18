package com.example.my_application114514;

import java.io.IOException;
import java.util.ArrayList;

import android.content.Intent;
import android.content.res.AssetManager;
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
    AssetManager assetManager = getAssets();
    try {
      // 读取 songs 文件夹中的 MP3 文件列表
      String[] songFiles = assetManager.list("");
      for (String songFile : songFiles) {
        if (songFile.endsWith(".mp3")) {
          mPlaylist.add(new SongData(songFile));
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
      Toast.makeText(this, "Error loading songs from assets", Toast.LENGTH_SHORT).show();
    }
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