package com.example.my_application114514

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.my_application114514.data.GlbConsts


import com.example.my_application114514.data.SongData

class PlayActivity : AppCompatActivity() {

    var mSongList: ArrayList<SongData>? = ArrayList()
    var mSongIndex:Int = 0
    lateinit var mAlbumPic : ImageView
    lateinit var mTotalTime : TextView
    lateinit var mPlayTitle : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.play_layout)

        // 舔包时间
        mSongList = GlbConsts.readSongList(intent)
        mSongIndex = GlbConsts.readSongIndex(intent)

        initView()
        initUI()
    }

    fun initView(){
        mAlbumPic = findViewById(R.id.play_album_pic)
        mTotalTime = findViewById(R.id.play_total_time)
        mPlayTitle = findViewById(R.id.play_title)
    }

    fun initUI(){
        val currentSong = mSongList?.get(mSongIndex) ?: return // 或者用其他默认值
        mTotalTime.text = currentSong.displayTotTime
        mPlayTitle.text = currentSong.songName
        GlbConsts.loadAlbumPicFromAssets(this,mAlbumPic,currentSong.albumPicPath)
    }
}