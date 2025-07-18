package com.example.my_application114514.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.my_application114514.R;
import com.example.my_application114514.data.SongData;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MySongLIstAdapter extends RecyclerView.Adapter<MySongLIstAdapter.ItemViewHolder> {

  private ArrayList<SongData> mPlayList;
  private Context mContext;
  private OnItemClickListener mItemClickListener;  // 回调用的点击事件监听器

  public MySongLIstAdapter(Context mContext, ArrayList<SongData> mPlayList) {
    this.mContext = mContext;
    this.mPlayList = mPlayList;
  }


  @NonNull
  @Override
  public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_layout,parent,false);
    return new ItemViewHolder(itemView);
  }

  @Override
  public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
    SongData song = mPlayList.get(position);
    holder.bind(song);
  }

  @Override
  public int getItemCount() {
    return mPlayList.isEmpty() ? 0 : mPlayList.size();
  }

  class ItemViewHolder extends RecyclerView.ViewHolder{
    private TextView mTvSongName;
    private LinearLayout llContainer;
    public ItemViewHolder(@NonNull View itemView) {
      super(itemView);
      mTvSongName = itemView.findViewById(R.id.tv_song_name);
      llContainer = itemView.findViewById(R.id.ll_song_item);
      llContainer.setOnClickListener(new View.OnClickListener(){
        @Override
        public void onClick(View v) {
          if(mItemClickListener != null){
            mItemClickListener.onItemClick(getAdapterPosition());
          }
        }
      });

    }

    public void bind(SongData song){
      mTvSongName.setText(song.getSongName());
    }
  }

  // 调用这个，并传入一个监听器
  public void setmItemClickListener(OnItemClickListener itemClickListener) {
    this.mItemClickListener = itemClickListener;
  }

  // 监听器的接口
  public interface OnItemClickListener{
    void onItemClick(int position);

  }

}
