package com.liuchad.zhuangbility.ui;

import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.liuchad.zhuangbility.R;
import com.liuchad.zhuangbility.adapter.ZhuangbiListAdapter;
import com.liuchad.zhuangbility.base.BaseActivity;
import com.liuchad.zhuangbility.network.Network;
import com.liuchad.zhuangbility.vo.RemoteImage;

import java.util.List;

import butterknife.BindView;
import in.workarounds.bundler.annotations.RequireBundler;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

@RequireBundler
public class ElementaryActivity extends BaseActivity {

    @BindView(R.id.swipeRefreshLayout) SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.gridRv) RecyclerView mRecyclerView;
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.search_view) SearchView mSearchView;

    ZhuangbiListAdapter adapter = new ZhuangbiListAdapter();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_elementary;
    }

    @Override
    protected void initInjector() {
    }

    @Override
    protected void initView() {
        mRecyclerView.setLayoutManager(new GridLayoutManager(ElementaryActivity.this, 2));
        mRecyclerView.setAdapter(adapter);
        mSwipeRefreshLayout.setColorSchemeColors(Color.BLUE, Color.GREEN, Color.RED, Color.YELLOW);
        mSwipeRefreshLayout.setEnabled(false);

        mToolbar.setBackgroundColor(getResources().getColor(R.color.theme_light));
        setSupportActionBar(mToolbar);
        mSearchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                return false;
            }
        });
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                search(query);
                mSwipeRefreshLayout.setRefreshing(true);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        mSearchView.setVisibility(View.VISIBLE);
    }

    @Override
    protected void initData() {
        search("装逼");
    }

    private void search(String key) {
        Network.getService()
                .search(key)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<RemoteImage>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(List<RemoteImage> zhuangbiImages) {
                        Observable.fromIterable(zhuangbiImages).filter(new Predicate<RemoteImage>() {
                            @Override
                            public boolean test(RemoteImage zhuangbiImage) throws Exception {
                                return !zhuangbiImage.image_url.contains("gif");
                            }
                        }).toList().subscribe(new Consumer<List<RemoteImage>>() {
                            @Override
                            public void accept(List<RemoteImage> zhuangbiImages) throws Exception {
                                adapter.setData(zhuangbiImages);
                            }
                        });
                    }

                    @Override
                    public void onError(Throwable e) {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onComplete() {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                });
    }
}
