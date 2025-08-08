package com.example.my_application114514

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


import com.example.my_application114514.data.SongData
import com.example.my_application114514.service.MusicService
import com.example.my_application114514.service.binder.MusicBinder
import com.example.my_application114514.util.InteractiveEvent
import com.example.my_application114514.util.MediaIOHelper
import java.util.Timer
import java.util.TimerTask

class PlayActivity : AppCompatActivity() {

    var mSongList: ArrayList<SongData>? = ArrayList()
    var mSongIndex:Int = 0
    var progressTimer : Timer? = null
    lateinit var mPlayPauseSwitch: ImageView
    lateinit var mPreviousSwitch: ImageView
    lateinit var mNextSongSwitch: ImageView
    lateinit var mStopSwitch: ImageView
    lateinit var mAlbumPic : ImageView
    lateinit var mTotalTime : TextView
    lateinit var mCurTime : TextView
    lateinit var mPlayTitle : TextView
    lateinit var mSeekBar: SeekBar
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
        mCurTime = findViewById(R.id.play_cur_time)
        mTotalTime = findViewById(R.id.play_total_time)
        mPlayTitle = findViewById(R.id.play_title)
        mPlayPauseSwitch = findViewById(R.id.play_pause)
        mPreviousSwitch = findViewById(R.id.play_last_switch)
        mNextSongSwitch = findViewById(R.id.play_next_switch)
        mStopSwitch = findViewById(R.id.play_stop_switch)
        mSeekBar = findViewById(R.id.play_proc_bar)
    }

    fun initListener(){
        mPlayPauseSwitch.setOnClickListener { InteractiveEvent.playOrPause(mMusicBinder, mPlayPauseSwitch) }
        mStopSwitch.setOnClickListener { InteractiveEvent.stop(mMusicBinder, mPlayPauseSwitch) }
        mPreviousSwitch.setOnClickListener {
            InteractiveEvent.lastSong(mMusicBinder)
            updateUI()
        }

        mNextSongSwitch.setOnClickListener {
            InteractiveEvent.nextSong(mMusicBinder)
            updateUI()
        }

        mSeekBar.setOnSeekBarChangeListener( object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if(fromUser){ mCurTime.text = InteractiveEvent.getCurTimeText(progress) }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                progressTimer?.cancel()
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                seekBar?.let {
                    mMusicBinder.seekTo(it.progress)   // 将播放进度设置到拖动位置
                    updateProgress()                   // 恢复进度条自动更新
                }
            }
        })

    }

    fun updateUI(){
        if (::mMusicBinder.isInitialized){mSongIndex = mMusicBinder.getMusicCurIndex()}
        val currentSong = mSongList?.get(mSongIndex) ?: return // 或者用其他默认值
        mTotalTime.text = currentSong.displayTotTime
        mSeekBar.max = (currentSong.totalPlayTime)
        mPlayTitle.text = currentSong.songName
        MediaIOHelper.loadAlbumPicFromAssets(this,mAlbumPic,currentSong.albumPicPath)
    }

    fun updateProgress(){
        progressTimer?.cancel()
        progressTimer = Timer().apply {
            schedule(object : TimerTask(){
                override fun run(){
                    runOnUiThread {
                        val curTimeVal = mMusicBinder.getCurProgress()
                        mSeekBar.progress = curTimeVal
                        mCurTime.text = InteractiveEvent.getCurTimeText(mMusicBinder.getCurProgress())
                    }
                }
            },0,1000)
        }
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
            updateProgress()
        }

        override fun onServiceDisconnected(name: ComponentName?) {}
    }

}