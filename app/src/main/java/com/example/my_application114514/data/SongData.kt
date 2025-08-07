package com.example.my_application114514.data

import android.annotation.SuppressLint

class SongData(assetPath: String, songName: String, totalTime: Int) {
    val assetPath: String = ""  // 资源路径
    val albumPicPath: String
        get() = assetPath.replaceAfterLast('.',"jpg").replaceAfterLast('.',"png")

    var songName:String = "歌曲名字"
    var displayTotTime:String = "00:00"
        private set
    var totalPlayTime: Int = 0
        set(value){
            field = value
            updateDisplay()
        }


    init {
        // 初始化时更新显示时间
        updateDisplay()
    }

    @SuppressLint("DefaultLocale")
    fun updateDisplay(){
        val mins = totalPlayTime / 60
        val seconds = totalPlayTime % 60
        displayTotTime = String.format("%02d:%02d",mins,seconds)
    }
}