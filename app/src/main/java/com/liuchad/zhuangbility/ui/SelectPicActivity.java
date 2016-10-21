package com.liuchad.zhuangbility.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.example.liuchad.zhuangbility.R;
import com.liuchad.zhuangbility.event.SelectPicEvent;
import in.workarounds.bundler.annotations.RequireBundler;
import org.greenrobot.eventbus.EventBus;

@RequireBundler
public class SelectPicActivity extends AppCompatActivity {

    @Bind(R.id.recyclerview) RecyclerView mRecyclerView;
    @Bind(R.id.back) ImageView mBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_pic);

        ButterKnife.bind(this);
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);
        final PicAdapter adapter = new PicAdapter(picIds);
        mRecyclerView.setAdapter(adapter);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                finish();
            }
        });
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

    private int[] picIds = new int[] {
        R.drawable.bfj2,
        R.drawable.bfj3,
        R.drawable.bfj4,
        R.drawable.bfj5,
        R.drawable.bfj6,
        R.drawable.bfj7,
        R.drawable.kt1,
        R.drawable.kt2,
        R.drawable.kt3,
        R.drawable.kt4,
        R.drawable.kt5,
        R.drawable.kt6,
        R.drawable.kt7,
        R.drawable.kt8,
        R.drawable.kt9,
        R.drawable.kt10,
        R.drawable.kt11,
        R.drawable.kt12,
        R.drawable.kt13,
        R.drawable.kt14,
        R.drawable.kt15,
        R.drawable.kt16,
        R.drawable.kt17,
        R.drawable.kt18,
        R.drawable.kt19,
        R.drawable.mg1,
        R.drawable.mg2,
        R.drawable.mg3,
        R.drawable.mg4,
        R.drawable.mg5,
        R.drawable.mg6,
        R.drawable.mg7,
        R.drawable.mg8,
        R.drawable.mg9,
        R.drawable.mg10,
        R.drawable.mg11,
        R.drawable.mg12,
        R.drawable.mg13,
        R.drawable.mg14,
        R.drawable.mg15,
        R.drawable.mg16,
        R.drawable.rm1,
        R.drawable.rm2,
        R.drawable.rm3,
        R.drawable.rm4,
        R.drawable.rm5,
        R.drawable.rm6,
        R.drawable.rm7,
        R.drawable.rm8,
        R.drawable.rm9,
        R.drawable.rm10,
        R.drawable.rm11,
        R.drawable.rm12,
        R.drawable.rm13,
        R.drawable.rm14,
        R.drawable.rm15,
        R.drawable.xbx1,
        R.drawable.xbx2,
        R.drawable.xbx3,
        R.drawable.xbx4,
        R.drawable.xm1,
        R.drawable.xm2,
        R.drawable.xm3,
        R.drawable.xm4,
        R.drawable.xm5,
        R.drawable.xm6,
        R.drawable.xm7,
        R.drawable.xm8,
        R.drawable.xm9,
        R.drawable.xxg1,
        R.drawable.xxg2,
        R.drawable.xxg3,
    };
}
