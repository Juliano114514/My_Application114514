package com.example.my_application114514

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.my_application114514.data.SongData

class MainActivity : AppCompatActivity() {

    var mSongList: MutableList<SongData> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.song_list)
    }


    fun loadSongsFromAssets():List<SongData>{
        val songList: MutableList<SongData> = ArrayList()
        val assetManager = assets

        assetManager.list("")?.forEach{ fileName ->
            if(fileName.endsWith(".mp3")){
                val songName = fileName.removeSuffix(".mp3")
                val assetPath = "${fileName}"

                songList.add(SongData(
                    assetPath = assetPath,
                    songName = songName,
                    totalTime = 0,
                ))
            }
        }
        return songList
    }


}