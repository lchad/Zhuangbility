package com.liuchad.zhuangbility.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.liuchad.zhuangbility.R;
import com.liuchad.zhuangbility.viewholder.LicenceViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lchad on 2017/3/4.
 */

public class LicenceAdapter extends RecyclerView.Adapter<LicenceViewHolder> {

    private List<LicenceModel> mList = new ArrayList<>();
    private Context mContext;

    public LicenceAdapter(Context context) {
        mList.add(new LicenceModel("https://github.com/workarounds/bundler", "Bundler", "Apache License, Version 2.0"));
        mList.add(new LicenceModel("https://github.com/JakeWharton/butterknife", "ButterKnife", "Apache License, Version 2.0"));
        mList.add(new LicenceModel("https://github.com/square/okhttp", "OkHttp", "Apache License, Version 2.0"));
        mList.add(new LicenceModel("https://github.com/bumptech/glide", "Glide", "Apache License, Version 2.0"));
        mList.add(new LicenceModel("https://github.com/ReactiveX/RxAndroid", "RxAndroid", "Apache License, Version 2.0"));
        mList.add(new LicenceModel("https://github.com/koral--/android-gif-drawable", "android-gif-drawable", "Apache License, Version 2.0"));
        mList.add(new LicenceModel("https://github.com/square/leakcanary", "leakcanary", "Apache License, Version 2.0"));
        mList.add(new LicenceModel("https://github.com/facebook/stetho", "stetho", "BSD-licensed"));
        mList.add(new LicenceModel("https://github.com/square/retrofit", "retrofit", "Apache License, Version 2.0"));
        mList.add(new LicenceModel("https://github.com/ItsPriyesh/chroma", "chroma", "Apache License, Version 2.0"));
        mContext = context;
    }

    @Override
    public LicenceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new LicenceViewHolder(View.inflate(mContext, R.layout.item_licence, null));
    }

    @Override
    public void onBindViewHolder(LicenceViewHolder holder, int position) {
        holder.bind(mList.get(position));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class LicenceModel {
        public String url;
        public String name;
        public String licence;

        public LicenceModel(String url, String name, String licence) {
            this.url = url;
            this.name = name;
            this.licence = licence;
        }
    }
}
