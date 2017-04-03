package com.liuchad.zhuangbility.presenter;

import com.liuchad.zhuangbility.base.BasePresenter;
import com.liuchad.zhuangbility.network.Network;
import com.liuchad.zhuangbility.util.RxUtil;
import com.liuchad.zhuangbility.view.IElementaryView;
import com.liuchad.zhuangbility.vo.RemoteImage;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Predicate;

/**
 * Created by lchad on 2017/3/21.
 * Github: https://github.com/lchad
 */

public class ElementaryPresenter<T extends IElementaryView> extends BasePresenter implements IElementaryPresenter {
    private CompositeDisposable mCompositeDisposable;
    private IElementaryView mElementaryView;

    public ElementaryPresenter(IElementaryView view) {
        mCompositeDisposable = new CompositeDisposable();
        mElementaryView = view;
    }

    @Override
    public void search(String key) {
        mElementaryView.setRefresh(true);
        Network.getService()
                .search(key)
                .compose(RxUtil.<List<RemoteImage>>applyObservableSchedulers())
                .compose(RxUtil.filterObservable(new Predicate<RemoteImage>() {
                    @Override
                    public boolean test(RemoteImage remoteImage) throws Exception {
                        return !remoteImage.image_url.contains("gif");//暂时过滤掉gif图片.
                    }
                }))
                .subscribe(new Observer<List<RemoteImage>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mCompositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(List<RemoteImage> value) {
                        mElementaryView.setData(value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mElementaryView.setRefresh(false);
                    }

                    @Override
                    public void onComplete() {
                        mElementaryView.setRefresh(false);
                    }
                });
    }

    public void handleDestroy() {
        if (!mCompositeDisposable.isDisposed()) {
            mCompositeDisposable.dispose();
        }
    }
}
