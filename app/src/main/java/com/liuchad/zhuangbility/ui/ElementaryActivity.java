package com.liuchad.zhuangbility.ui;

import android.app.SearchManager;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.liuchad.zhuangbility.R;
import com.liuchad.zhuangbility.adapter.ZhuangbiListAdapter;
import com.liuchad.zhuangbility.base.BaseActivity;
import com.liuchad.zhuangbility.event.SelectRemotePicEvent;
import com.liuchad.zhuangbility.presenter.ElementaryPresenter;
import com.liuchad.zhuangbility.view.IElementaryView;
import com.liuchad.zhuangbility.vo.RemoteImage;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import in.workarounds.bundler.annotations.RequireBundler;

@RequireBundler
public class ElementaryActivity extends BaseActivity implements IElementaryView {
    private ElementaryPresenter mElementaryPresenter;

    @BindView(R.id.swipeRefreshLayout) SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.gridRv) RecyclerView mRecyclerView;
    @BindView(R.id.toolbar) Toolbar mToolbar;

    ZhuangbiListAdapter adapter = new ZhuangbiListAdapter();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_elementary;
    }

    @Override
    protected void initInjector() {
        mElementaryPresenter = new ElementaryPresenter(this);
    }

    @Override
    protected void initView() {
        mRecyclerView.setLayoutManager(new GridLayoutManager(ElementaryActivity.this, 2));
        mRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new ZhuangbiListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String url) {
                EventBus.getDefault().post(new SelectRemotePicEvent(url));
                finish();
            }
        });
        mSwipeRefreshLayout.setColorSchemeColors(Color.BLUE, Color.GREEN, Color.RED, Color.YELLOW);
        mSwipeRefreshLayout.setEnabled(false);

        mToolbar.setBackgroundColor(getResources().getColor(R.color.theme_light));
        setSupportActionBar(mToolbar);
    }

    @Override
    protected void initData() {
        mElementaryPresenter.search("装逼");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mElementaryPresenter.handleDestroy();
    }

    @Override
    public void setData(List<RemoteImage> value) {
        adapter.setData(value);
    }

    @Override
    public void setRefresh(boolean b) {
        mSwipeRefreshLayout.setRefreshing(b);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_elementary, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);

        SearchManager searchManager = (SearchManager) ElementaryActivity.this.getSystemService(Context.SEARCH_SERVICE);

        SearchView searchView = null;
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(ElementaryActivity.this.getComponentName()));

            searchView.setOnCloseListener(new SearchView.OnCloseListener() {
                @Override
                public boolean onClose() {
                    return false;
                }
            });
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    mElementaryPresenter.search(query);
                    mSwipeRefreshLayout.setRefreshing(true);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });
        }
        return super.onCreateOptionsMenu(menu);
    }
}
