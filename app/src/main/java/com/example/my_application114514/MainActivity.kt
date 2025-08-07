package com.example.my_application114514

import android.content.res.AssetFileDescriptor
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.my_application114514.adapter.SongAdapter
import com.example.my_application114514.data.SongData
import java.io.IOException

class MainActivity : AppCompatActivity() {

    var mSongList: List<SongData> = ArrayList()
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: SongAdapter

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
            // 显示点击的歌曲名称
            Toast.makeText(
                this,
                "点击了${songData.songName}",
                Toast.LENGTH_SHORT
            ).show()
        }

    }

    // 初始化播放列表
    fun initSongRcv(){
        mSongList = loadSongsFromAssets()
        mAdapter = SongAdapter(this,mSongList)
        mRecyclerView.layoutManager = LinearLayoutManager(this)
        mRecyclerView.adapter = mAdapter
    }

    // 动态读取assets目录下的歌曲列表
    fun loadSongsFromAssets():List<SongData>{
        val songList: MutableList<SongData> = ArrayList()
        val assetManager = assets

        assetManager.list("")?.forEach{ fileName ->
            if(fileName.endsWith(".mp3")){
                val songName = fileName.removeSuffix(".mp3")
                val assetPath = "${fileName}"
                val duration = getSongDurationFromAssets(assetPath) // 获取时长
                songList.add(SongData(
                    assetPath = assetPath,
                    songName = songName,
                    totalTime = duration,
                ))
            }
        }
        return songList
    }

    private fun getSongDurationFromAssets(assetPath:String) : Int{
        val mediaplayer = MediaPlayer()
        return try {
            val afd: AssetFileDescriptor = assets.openFd(assetPath)
            mediaplayer.setDataSource(afd.fileDescriptor,afd.startOffset,afd.length)
            mediaplayer.prepare()
            val duration = mediaplayer.duration / 1000
            afd.close()
            duration
        }catch (e: IOException){
            e.printStackTrace()
            0
        }finally {
            mediaplayer.release()
        }
    }
}