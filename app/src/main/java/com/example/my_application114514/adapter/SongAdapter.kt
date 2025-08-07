package com.example.my_application114514.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import com.example.my_application114514.R
import com.example.my_application114514.data.SongData

import com.bumptech.glide.Glide

class SongAdapter (
    private val mContext: Context,
    private val songList:List<SongData>,
    private val onItemClick:((SongData) -> Unit)? = null
):RecyclerView.Adapter<SongAdapter.SongViewHolder>(){
    inner class SongViewHolder(itemView:View): RecyclerView.ViewHolder(itemView){
        val albumPic:ImageView = itemView.findViewById(R.id.item_album_pic)
        val songName:TextView = itemView.findViewById(R.id.item_song_name)
        val totalTime:TextView = itemView.findViewById(R.id.item_total_time)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_layout,parent,false)
        return SongViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        val currentSong = songList[position]
        holder.songName.text = currentSong.songName
        holder.totalTime.text = currentSong.displayTotTime

        // 点击事件
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(currentSong)
        }

        loadAlbumPicFromAssets(holder.albumPic,currentSong.assetPath)
    }


    private fun loadAlbumPicFromAssets(imageView: ImageView, string: String) {
        try {
            Glide.with(mContext)
                .load("file:///android_asset/$string")
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(imageView)
        }catch (e:Exception){
            imageView.setImageResource(R.mipmap.ic_launcher)
        }
    }

    override fun getItemCount() = songList.size
}