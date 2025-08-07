package com.example.my_application114514.util

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.widget.ImageView
import com.example.my_application114514.R
import com.example.my_application114514.data.GlobalConsts
import com.example.my_application114514.data.SongData
import java.util.ArrayList

object MediaIOHelper {

    fun loadAlbumPicFromAssets(mContext:Context,imageView: ImageView, assetPath: String) {
        try {
            val inputStream = mContext.assets.open(assetPath)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            imageView.setImageBitmap(bitmap)
        }catch (e:Exception){
            imageView.setImageResource(R.mipmap.ic_launcher)
            e.printStackTrace()
        }
    }

    // 采用泛型封装
    inline fun <reified T> loadSongList(
        context: Context,
        mList: java.util.ArrayList<SongData>,
        position: Int
    ): Intent {
        return Intent(context, T::class.java).apply {
            putParcelableArrayListExtra(GlobalConsts.SONG_LIST, mList)
            putExtra(GlobalConsts.SONG_INDEX, position)
        }
    }


    fun readSongIndex(intent: Intent): Int {
        return intent.getIntExtra(GlobalConsts.SONG_INDEX, -1)
    }

    @Suppress("DEPRECATION")
    fun readSongList(intent: Intent): ArrayList<SongData>? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableArrayListExtra(GlobalConsts.SONG_LIST, SongData::class.java)
        } else {
            intent.getParcelableArrayListExtra<SongData>(GlobalConsts.SONG_LIST)
        }
    }
}