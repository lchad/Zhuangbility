package com.liuchad.zhuangbility.ui;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.liuchad.zhuangbility.R;
import com.liuchad.zhuangbility.base.BaseActivity;
import com.liuchad.zhuangbility.event.SelectPicEvent;

import butterknife.BindView;
import in.workarounds.bundler.annotations.RequireBundler;
import org.greenrobot.eventbus.EventBus;

@RequireBundler
public class SelectPicActivity extends BaseActivity {

    @BindView(R.id.recyclerview) RecyclerView mRecyclerView;
    @BindView(R.id.back) ImageView mBack;

    @Override protected int getLayoutId() {
        return R.layout.activity_select_pic;
    }

    @Override protected void initView() {
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);
        final PicAdapter adapter = new PicAdapter(null);
        mRecyclerView.setAdapter(adapter);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                finish();
            }
        });
    }

    @Override protected void initData() {

    }

    @Override protected void initInjector() {

    }

    class PicAdapter extends RecyclerView.Adapter<PicViewHolder> {
        private int[] picIds;

        public PicAdapter(int[] ids) {
            picIds = ids;
        }

        @Override
        public PicViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemPostView = LayoutInflater.from(SelectPicActivity.this).inflate(R.layout.original_emoji_item, parent, false);
            return new PicViewHolder(itemPostView);
        }

        @Override
        public void onBindViewHolder(final PicViewHolder holder, final int position) {
            holder.mView.setImageResource(picIds[position]);
            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EventBus.getDefault().post(new SelectPicEvent(picIds[holder.getAdapterPosition()]));
                    finish();
                }
            });
        }

        @Override
        public int getItemCount() {
            return picIds.length;
        }
    }

    class PicViewHolder extends RecyclerView.ViewHolder {
        ImageView mView;

        PicViewHolder(View itemView) {
            super(itemView);
            mView = (ImageView) itemView.findViewById(R.id.pic);
        }
    }


}
