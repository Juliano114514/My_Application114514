package com.example.my_application114514.service

import android.app.Service
import android.content.Intent
import android.content.res.AssetFileDescriptor
import android.content.res.AssetManager
import android.media.MediaPlayer
import android.os.IBinder
import android.util.Log
import com.example.my_application114514.R
import com.example.my_application114514.data.GlobalConsts
import com.example.my_application114514.data.SongData
import com.example.my_application114514.listener.MyPlayerListener
import com.example.my_application114514.service.binder.MusicBinder
import com.example.my_application114514.util.InteractiveEvent
import java.io.IOException

class MusicService : Service() {

    private lateinit var mMyPlayerListener : MyPlayerListener
    private lateinit var mMediaPlayer: MediaPlayer
    private var mRamdomPlayList: ArrayList<Int> ?= ArrayList()
    private var mSongList : ArrayList<SongData> ?= ArrayList()
    private var curSongIdx : Int = 0
    private var mPlayMode : Int = GlobalConsts.PLAY_MODE_ORDERED
    private var mRndIdx : Int = 0


    override fun onCreate() {
        super.onCreate()
        mMediaPlayer = MediaPlayer()
        mSongList = ArrayList()

        // 添加播放完成监听器
        mMediaPlayer.setOnCompletionListener {
            onMusicCompletion()
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return MusicBinder(this)
    }

    // 新增：播放完成回调处理
    private fun onMusicCompletion(){
        if (mSongList.isNullOrEmpty()) return
        when(mPlayMode){
            GlobalConsts.PLAY_MODE_CIRCLE  -> { nextSong() }
            GlobalConsts.PLAY_MODE_RANDOM  -> { randomPlay() }
            GlobalConsts.PLAY_MODE_SINGLE  -> { startPlay(curSongIdx,true) }
            GlobalConsts.PLAY_MODE_ORDERED -> {
                if(curSongIdx + 1 != mSongList?.size)nextSong()
                else{
                    mMediaPlayer.stop()
                    mMyPlayerListener?.onStop()
                }
            }
            else -> { nextSong() }
        }
    }

    // ============================== 与binder交互的部分
    fun setMusicList(data: ArrayList<SongData>?) { this.mSongList = data }
    fun setMusicCurIndex(idx: Int) { this.curSongIdx = idx }
    fun setPlayerListener(myPlayerListener: MyPlayerListener){this.mMyPlayerListener = myPlayerListener}
    fun getMusicList():ArrayList<SongData>? { return this.mSongList  }
    fun getMusicCurIndex():Int { return this.curSongIdx }
    fun getCurProgress():Int{return mMediaPlayer.currentPosition}
    fun getPlayMode():Int{return mPlayMode}


    // ==================== 播放模式控制
    fun switchMode(){
        mPlayMode = (mPlayMode+1) % 4
    }

    // 生成随机模式播放列表
    fun setRandonList(){
        mRamdomPlayList?.clear()
        mRamdomPlayList = InteractiveEvent.setRandomList(mSongList)
    }

    // 传入随机序号
    fun randomPlay(){
        if(mRamdomPlayList?.size == 0) { setRandonList() }
        val inputIdx = mRamdomPlayList?.get(mRndIdx) as Int
        startPlay(inputIdx)

        mRndIdx += 1
        if(mRndIdx == mSongList?.size){
            setRandonList()
            mRndIdx = 0
        }
    }


    // ==================== 播放控制
    fun seekTo(progress:Int){ mMediaPlayer.seekTo(progress) }
    fun isMusicPlaying() : Boolean{ return mMediaPlayer.isPlaying() }
    fun pause(){ mMediaPlayer.pause() }
    fun stop(){
        mMediaPlayer.stop()
        mMediaPlayer.prepare()
    }
    fun play(){
        if(mMediaPlayer.isPlaying())return
        mMediaPlayer.start()
    }
    fun previous(){
        var idx = (curSongIdx + (mSongList?.size ?: 1) - 1) % (mSongList?.size ?: 1)
        if(mPlayMode == GlobalConsts.PLAY_MODE_RANDOM) { randomPlay() }
        else { startPlay(idx) }
        mMyPlayerListener?.onPlay()
    }
    fun nextSong(){
        var idx = (curSongIdx + 1) % (mSongList?.size ?: 1)
        if(mPlayMode == GlobalConsts.PLAY_MODE_RANDOM) { randomPlay() }
        else { startPlay(idx) }
        mMyPlayerListener?.onPlay()
    }

    // 播放默认曲目
    fun startPlay(){
        if(mMediaPlayer.isPlaying)return
        val song : SongData = mSongList?.getOrNull(curSongIdx) ?: return
        try {
            val assetManager: AssetManager = this.assets
            val afd: AssetFileDescriptor = assetManager.openFd(song.assetPath)
            mMediaPlayer.setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
            afd.close() // 及时关闭描述符

            mMediaPlayer.prepare()
            mMediaPlayer.start()
            mMediaPlayer.setOnErrorListener { _, what, extra ->
                Log.e("Player", "播放错误: $what | $extra")
                false
            }
        }catch (e: IOException) {
            Log.e("Player", "播放失败", e)
        } catch (e: IllegalArgumentException) {
            Log.e("Player", "无效参数", e)
        }
    }

    // 播放指定曲目
    fun startPlay(idx:Int,forceStopFlag: Boolean = false){
        val needRestart = (idx != curSongIdx) || forceStopFlag  // 判断是否需要暂停播放
        if (needRestart && mMediaPlayer.isPlaying) { mMediaPlayer.stop() }
        if (needRestart) {
            try {
                mMediaPlayer.reset()  // 重置播放器状态
                curSongIdx = idx      // 更新当前曲目索引
            } catch (e: IllegalStateException) {
                Log.e("Player", "重置播放器失败", e)
                return
            }
        }
        startPlay() // 调用无参数版本开始播放
    }

    // 监听并通知


    // 新增：在服务销毁时释放资源
    override fun onDestroy() {
        releaseMediaPlayer()
        super.onDestroy()
    }

    private fun releaseMediaPlayer() {
        if (::mMediaPlayer.isInitialized) {
            try {
                mMediaPlayer.reset()       // 清除播放器状态
                mMediaPlayer.release()     // 释放系统资源
            } catch (e: Exception) {
                Log.e("MusicService", "释放MediaPlayer失败", e)
            }
        }
    }
}




