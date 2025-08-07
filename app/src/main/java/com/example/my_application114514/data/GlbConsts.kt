package com.example.my_application114514.data

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.widget.ImageView
import com.example.my_application114514.R
import java.util.ArrayList

object GlbConsts {
    const val SONG_LIST :String = "song_list"
    const val SONG_INDEX :String = "song_index"



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
        mList: ArrayList<SongData>,
        position: Int
    ): Intent {
        return Intent(context, T::class.java).apply {
            putParcelableArrayListExtra(GlbConsts.SONG_LIST, mList)
            putExtra(GlbConsts.SONG_INDEX, position)
        }
    }


    fun readSongIndex(intent: Intent): Int {
        return intent.getIntExtra(GlbConsts.SONG_INDEX, -1)
    }

    @Suppress("DEPRECATION")
    fun readSongList(intent: Intent): ArrayList<SongData>? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableArrayListExtra(GlbConsts.SONG_LIST, SongData::class.java)
        } else {
            intent.getParcelableArrayListExtra<SongData>(GlbConsts.SONG_LIST)
        }
    }

}