package com.example.my_application114514.util

import android.annotation.SuppressLint
import android.widget.ImageView
import android.widget.TextView
import com.example.my_application114514.R
import com.example.my_application114514.data.SongData
import com.example.my_application114514.service.binder.MusicBinder


// 我简直是解耦大王
object InteractiveEvent {

    fun lastSong(musicBinder: MusicBinder){ musicBinder.previous()}
    fun nextSong(musicBinder: MusicBinder){ musicBinder.nextSong()}
    // 播放暂停
    fun playOrPause(musicBinder: MusicBinder, imageView: ImageView) {
        if (musicBinder.isMusicPlaying()) {
            musicBinder.pause()
            imageView.setImageResource(R.drawable.baseline_play_arrow_24) // 替换为您的播放图标资源
        } else {
            musicBinder.play() // 注意：这里应该是play()而不是pause()
            imageView.setImageResource(R.drawable.baseline_pause_24) // 替换为您的暂停图标资源
        }
    }

    fun stop(musicBinder: MusicBinder, imageView: ImageView){
        musicBinder.stop()
        imageView.setImageResource(R.drawable.baseline_play_arrow_24)
    }

    @SuppressLint("DefaultLocale")
    fun getCurTimeText(sumTime:Int):String{
        var time = sumTime / 1000
        val mins = time / 60
        val seconds = time % 60
        return String.format("%02d:%02d",mins,seconds)
    }

    // 播放模式控制


}