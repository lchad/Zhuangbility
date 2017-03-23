package com.liuchad.zhuangbility.base;

/**
 * Created by lchad on 2017/2/26.
 */

public class BasePresenter<T extends IBaseView> implements IPresenter {
    //private final BehaviorSubject<PresenterEvent> lifecycleSubject = BehaviorSubject.create();
    protected T mView;

    public T getView() {
        return mView;
    }

    public void setView(T t) {
        mView = t;
        if (mView == null) {
            throw new RuntimeException("mView cant be null");
        }
        if ((!(mView instanceof BaseActivity) && !(mView instanceof BaseFragment))) {
            throw new RuntimeException("mView must be extends BaseActivity or BaseFragment");
        }
    }

    /**
     * Method that control the lifecycle of the view. It should be called in the view's
     * (Activity or Fragment) onCreate() / onCreateView() method.
     */
    public void create() {
        //lifecycleSubject.onNext(PresenterEvent.CREATE);
    }
}
