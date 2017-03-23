package com.liuchad.zhuangbility.util;

import android.text.TextUtils;

import org.reactivestreams.Publisher;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

public class RxUtil {
    /**
     * 封装切换线程的函数(Observable)
     */
    public static <T> ObservableTransformer<T, T> applyObservableSchedulers() {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    /**
     * 封装切换线程的函数(Flowable)
     */
    public static <T>FlowableTransformer<T, T> applyFlowableSchedulers() {
        return new FlowableTransformer<T, T>() {
            @Override
            public Publisher<T> apply(Flowable<T> upstream) {
                return upstream.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    /**
     * 对 List<?> 列表中的元素进行过滤(Observable)
     *
     * @param predicate 检查器
     * @param <T>       列表中的元素类型
     * @return 原列表类型
     */
    public static <T> ObservableTransformer<List<T>, List<T>> filterObservable(final Predicate<T> predicate) {
        return new ObservableTransformer<List<T>, List<T>>() {
            @Override
            public ObservableSource<List<T>> apply(Observable<List<T>> upstream) {
                return upstream.flatMap(new Function<List<T>, ObservableSource<T>>() {
                    @Override
                    public ObservableSource<T> apply(List<T> ts) throws Exception {
                        return Observable.fromIterable(ts);
                    }
                }).filter(predicate).toList().toObservable();
            }
        };
    }

    /**
     * 对 List<?> 列表中的元素进行过滤(Flowable)
     *
     * @param predicate 检查器
     * @param <T>       列表中的元素类型
     * @return 原列表类型
     */
    public static <T> FlowableTransformer<List<T>, List<T>> filterFlowable(final Predicate<T> predicate) {
        return new FlowableTransformer<List<T>, List<T>>() {
            @Override
            public Publisher<List<T>> apply(Flowable<List<T>> upstream) {
                return upstream.flatMap(new Function<List<T>, Publisher<T>>() {
                    @Override
                    public Publisher<T> apply(List<T> ts) throws Exception {
                        return Flowable.fromIterable(ts);
                    }
                }).filter(predicate).toList().toFlowable();
            }
        };
    }

    /**
     * 过滤 List<String> 中的空字符串(Observable)
     */
    public static ObservableTransformer<List<String>, List<String>> filterObservableEmptyString() {
        return filterObservable(new Predicate<String>() {
            @Override
            public boolean test(String s) throws Exception {
                return !TextUtils.isEmpty(s);
            }
        });
    }

    /**
     * 过滤 List<String> 中的空字符串(Flowable)
     */
    public static FlowableTransformer<List<String>, List<String>> filterFlowableEmptyString() {
        return filterFlowable(new Predicate<String>() {
            @Override
            public boolean test(String s) throws Exception {
                return !TextUtils.isEmpty(s);
            }
        });
    }

//    /**
//     * 常用的组合
//     */
//    public static <T> ObservableTransformer<T, T> common(final IBaseView view) {
//
//        return new ObservableTransformer<T, T>() {
//            @Override
//            public Observable<T> call(Observable<T> observable) {
//                return observable.compose(RxUtil.<T>applyAsySchedulers())
//                    .compose(RxUtil.<T>loadingView(view))
//                    .compose(RxUtil.<T>error(view));
//            }
//        };
//    }
}
