package com.example.my_application114514.data

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable
import com.example.my_application114514.util.InteractiveEvent.getCurTimeText


class SongData : Parcelable {
    val assetPath: String  // 资源路径
    val albumPicPath: String
        get() = assetPath.substringBeforeLast('.')+".jpg"

    var songName:String = "歌曲名字"
    var displayTotTime:String = "00:00"
        private set
    var totalPlayTime: Int = 0
        set(value){
            field = value
            updateDisplay()
        }


    //  谁家好人忘记初始化了
    constructor(assetPath:String, songName:String,totalTime:Int) {
        this.assetPath = assetPath
        this.songName = songName
        this.totalPlayTime = totalTime
        // 初始化时更新显示时间
        updateDisplay()
    }

    private constructor(parcel: Parcel){
        assetPath = parcel.readString()!!
        songName = parcel.readString()!!
        totalPlayTime = parcel.readInt()
        updateDisplay()
    }

    @SuppressLint("DefaultLocale")
    fun updateDisplay(){
        displayTotTime = getCurTimeText(totalPlayTime)
    }

    override fun describeContents():Int = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(assetPath)
        dest.writeString(songName)
        dest.writeInt(totalPlayTime)
    }

    companion object CREATOR : Parcelable.Creator<SongData>{
        override fun createFromParcel(source: Parcel): SongData? {
            return SongData(source)
        }

        override fun newArray(size: Int): Array<out SongData?>? {
            return arrayOfNulls(size)
        }
    }
}