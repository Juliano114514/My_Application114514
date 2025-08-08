package com.example.my_application114514.service.binder

import android.os.Binder
import com.example.my_application114514.data.SongData
import com.example.my_application114514.service.MusicService

class MusicBinder(private val mMusicService : MusicService) : Binder() {
    fun setMusicList(mSonglist: ArrayList<SongData>?){ mMusicService.setMusicList(mSonglist) }
    fun setMusicCurIndex(curIndex:Int){ mMusicService.setMusicCurIndex(curIndex) }
    fun getMusicCurIndex():Int{ return mMusicService.getMusicCurIndex() }
    fun getMusicList():ArrayList<SongData>?{ return mMusicService.getMusicList() }

    fun startPlay(idx : Int){ mMusicService.startPlay(idx) }
    fun startPlay(){ mMusicService.startPlay() }
    fun isMusicPlaying() : Boolean{ return mMusicService.isMusicPlaying() }
    fun pause(){ mMusicService.pause() }
    fun play(){ mMusicService.play() }
    fun previous(){ mMusicService.previous() }
    fun nextSong(){mMusicService.nextSong()}
}