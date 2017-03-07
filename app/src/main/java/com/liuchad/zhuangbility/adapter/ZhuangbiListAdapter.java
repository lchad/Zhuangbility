package com.liuchad.zhuangbility.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.liuchad.zhuangbility.R;
import com.liuchad.zhuangbility.vo.ZhuangbiImage;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ZhuangbiListAdapter extends RecyclerView.Adapter {
    private List<ZhuangbiImage> mImages;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item, parent, false);
        return new PicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        PicViewHolder mPicViewHolder = (PicViewHolder) holder;
        ZhuangbiImage image = mImages.get(position);
        Glide.with(holder.itemView.getContext()).load(image.image_url).into(mPicViewHolder.imageIv);
        mPicViewHolder.descriptionTv.setText(image.description);
    }

    @Override
    public int getItemCount() {
        return mImages == null ? 0 : mImages.size();
    }

    public void setData(List<ZhuangbiImage> images) {
        this.mImages = images;
        notifyDataSetChanged();
    }

    static class PicViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.imageIv) ImageView imageIv;
        @BindView(R.id.descriptionTv) TextView descriptionTv;
        PicViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
