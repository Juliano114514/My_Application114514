package com.example.my_application114514

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


import com.example.my_application114514.data.SongData
import com.example.my_application114514.service.MusicService
import com.example.my_application114514.service.binder.MusicBinder
import com.example.my_application114514.util.InteractiveEvent
import com.example.my_application114514.util.MediaIOHelper

class PlayActivity : AppCompatActivity() {

    var mSongList: ArrayList<SongData>? = ArrayList()
    var mSongIndex:Int = 0
    lateinit var mPlayPauseSwitch: ImageView
    lateinit var mPreviousSwitch: ImageView
    lateinit var mNextSongSwitch: ImageView
    lateinit var mAlbumPic : ImageView
    lateinit var mTotalTime : TextView
    lateinit var mPlayTitle : TextView
    lateinit var mMusicBinder : MusicBinder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.play_layout)

        // 舔包时间
        mSongList = MediaIOHelper.readSongList(intent)
        mSongIndex = MediaIOHelper.readSongIndex(intent)

        initEvent()
        initListener()
        startMusicService()
    }

    // ============================== 初始化部分
    fun initEvent(){
        initView()
        updateUI()
    }

    fun initView(){
        mAlbumPic = findViewById(R.id.play_album_pic)
        mTotalTime = findViewById(R.id.play_total_time)
        mPlayTitle = findViewById(R.id.play_title)
        mPlayPauseSwitch = findViewById(R.id.play_pause)
        mPreviousSwitch = findViewById(R.id.play_last_switch)
        mNextSongSwitch = findViewById(R.id.play_next_switch)
    }

    fun initListener(){
        mPlayPauseSwitch.setOnClickListener { InteractiveEvent.playOrPause(mMusicBinder, mPlayPauseSwitch) }

        mPreviousSwitch.setOnClickListener {
            InteractiveEvent.lastSong(mMusicBinder)
            updateUI()
        }

        mNextSongSwitch.setOnClickListener {
            InteractiveEvent.nextSong(mMusicBinder)
            updateUI()
        }
    }

    fun updateUI(){
        if (::mMusicBinder.isInitialized){mSongIndex = mMusicBinder.getMusicCurIndex()}
        val currentSong = mSongList?.get(mSongIndex) ?: return // 或者用其他默认值
        mTotalTime.text = currentSong.displayTotTime
        mPlayTitle.text = currentSong.songName
        MediaIOHelper.loadAlbumPicFromAssets(this,mAlbumPic,currentSong.albumPicPath)
    }



    // ============================== 启动服务部分
    private fun startMusicService(){
        val intent = Intent(this, MusicService::class.java)
        bindService(intent,connection,Context.BIND_AUTO_CREATE)
    }

    private val connection = object : ServiceConnection{
        // 绑定Binder时跳转到该方法
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            mMusicBinder = service as MusicBinder
            mMusicBinder.setMusicList(mSongList);
            mMusicBinder.setMusicCurIndex(mSongIndex);
            mMusicBinder.startPlay();
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            TODO("Not yet implemented")
        }

    }

}