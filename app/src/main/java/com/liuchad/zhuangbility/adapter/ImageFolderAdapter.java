package com.liuchad.zhuangbility.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.liuchad.zhuangbility.R;
import com.liuchad.zhuangbility.vo.ImageFolder;
import java.util.ArrayList;
import java.util.List;

public class ImageFolderAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;

    private List<ImageFolder> mImageFolders = new ArrayList<>();

    private int lastSelected = 0;

    public ImageFolderAdapter(Context context) {
        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /**
     * 设置数据集
     */
    public void setData(List<ImageFolder> imageFolders) {
        if (imageFolders != null && imageFolders.size() > 0) {
            mImageFolders = imageFolders;
        } else {
            mImageFolders.clear();
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mImageFolders.size() + 1;
    }

    @Override
    public ImageFolder getItem(int i) {
        if (i == 0) return null;
        return mImageFolders.get(i - 1);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            view = mInflater.inflate(R.layout.list_item_folder, viewGroup, false);
            holder = new ViewHolder(view);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        if (holder != null) {
            if (i == 0) {
                holder.name.setText("所有图片");
                holder.size.setText(new StringBuilder().append(String.valueOf(getTotalImageSize())).append("张").toString());
                if (mImageFolders.size() > 0) {
                    ImageFolder f = mImageFolders.get(0);
                    String folderCoverPath = "file://" + f.cover.path;
                    Glide.with(mContext).load(folderCoverPath).into(holder.cover);
                    //ImageLoader.getInstance().displayImage(folderCoverPath, holder.cover, App.DISPLAY_OPTIONS_PHOTO);
                }
            } else {
                holder.bindData(getItem(i));
            }
            if (lastSelected == i) {
                holder.indicator.setVisibility(View.VISIBLE);
            } else {
                holder.indicator.setVisibility(View.INVISIBLE);
            }
        }
        return view;
    }

    private int getTotalImageSize() {
        int result = 0;
        if (mImageFolders != null && mImageFolders.size() > 0) {
            for (ImageFolder f : mImageFolders) {
                result += f.images.size();
            }
        }
        return result;
    }

    public void setSelectIndex(int i) {
        if (lastSelected == i) return;

        lastSelected = i;
        notifyDataSetChanged();
    }

    public int getSelectIndex() {
        return lastSelected;
    }

    public class ViewHolder {
        @Bind(R.id.cover) ImageView cover;
        @Bind(R.id.name) TextView name;
        @Bind(R.id.size) TextView size;
        @Bind(R.id.indicator) ImageView indicator;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
            view.setTag(this);
        }

        void bindData(ImageFolder data) {
            name.setText(data.name);
            size.setText(new StringBuilder().append(data.images.size()).append("张").toString());
            String folderCoverPath = "file://" + data.cover.path;
            Glide.with(mContext).load(folderCoverPath).into(cover);
        }
    }
}
