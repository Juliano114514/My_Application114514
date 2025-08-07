package com.example.my_application114514

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.my_application114514.adapter.SongAdapter
import com.example.my_application114514.data.SongData
import com.example.my_application114514.util.MediaIOHelper

class MainActivity : AppCompatActivity() {

    var mSongList: ArrayList<SongData> = ArrayList()
    lateinit var mRecyclerView: RecyclerView
    lateinit var mAdapter: SongAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.song_list)

        initView()
        initSongRcv()
        initViewListener() // 确保调用监听器初始化
    }


    // 注册用到的view
    fun initView(){
        mRecyclerView = findViewById(R.id.main_rcv_song_list)
    }


    // 注册用到的监听器
    fun initViewListener(){
        // 设置适配器的点击回调
        mAdapter.setOnItemClickListener { songData ->
            // 点击弹窗
            Toast.makeText(
                this,
                "将播放${songData.songName}",
                Toast.LENGTH_SHORT
            ).show()
            val position = mSongList.indexOf(songData) // 获得点击位置

            var intent : Intent = MediaIOHelper.loadSongList<PlayActivity>(this,mSongList,position)
            startActivity(intent) // 你需要保留一点点风味，才知道你在启动服务
        }

    }


    // 初始化播放列表
    fun initSongRcv(){
        mSongList = MediaIOHelper.loadSongsFromAssets(this)
        mAdapter = SongAdapter(this,mSongList)
        mRecyclerView.layoutManager = LinearLayoutManager(this)
        mRecyclerView.adapter = mAdapter
    }


}