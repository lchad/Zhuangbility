package com.liuchad.zhuangbility.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.liuchad.zhuangbility.R;
import com.liuchad.zhuangbility.vo.RemoteImage;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ZhuangbiListAdapter extends RecyclerView.Adapter {
    private List<RemoteImage> mImages = new ArrayList<>();

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item, parent, false);
        return new PicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        PicViewHolder mPicViewHolder = (PicViewHolder) holder;
        RemoteImage image = mImages.get(position);
        mPicViewHolder.bind(image);
    }

    @Override
    public int getItemCount() {
        return mImages == null ? 0 : mImages.size();
    }

    public void setData(List<RemoteImage> images) {
        if (images == null) {
            return;
        }
        if (mImages != null) {
            mImages.clear();
        } else {
            mImages = new ArrayList<>();
        }
        mImages.addAll(images);
        notifyDataSetChanged();
    }

    static class PicViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.imageIv) ImageView imageIv;
        @BindView(R.id.descriptionTv) TextView descriptionTv;

        private RemoteImage mRemoteImage;

        PicViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(RemoteImage image) {
            mRemoteImage = image;
            Glide.with(imageIv.getContext()).load(image.image_url).into(imageIv);
            descriptionTv.setText(image.description);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
        }
    }
}
