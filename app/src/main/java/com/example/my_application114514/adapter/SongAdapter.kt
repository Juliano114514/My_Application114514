package com.example.my_application114514.adapter

import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import com.example.my_application114514.R
import com.example.my_application114514.data.SongData

import com.bumptech.glide.Glide
import com.example.my_application114514.data.GlbConsts

class SongAdapter (
    private val mContext: Context,
    private val songList:List<SongData>,
):RecyclerView.Adapter<SongAdapter.SongViewHolder>(){

    private var onItemClickListener:((SongData) -> Unit)? = null

    fun setOnItemClickListener(listener:(SongData) -> Unit){
        onItemClickListener = listener
    }

    inner class SongViewHolder(itemView:View): RecyclerView.ViewHolder(itemView){
        val albumPic:ImageView = itemView.findViewById(R.id.item_album_pic)
        val songName:TextView = itemView.findViewById(R.id.item_song_name)
        val totalTime:TextView = itemView.findViewById(R.id.item_total_time)
        init {
         itemView.setOnClickListener {
             val position = adapterPosition
             if(position != RecyclerView.NO_POSITION){
                 onItemClickListener?.invoke(songList[position])
             }
         }
        }
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
        GlbConsts.loadAlbumPicFromAssets(mContext,holder.albumPic,currentSong.albumPicPath)
    }


    override fun getItemCount() = songList.size
}