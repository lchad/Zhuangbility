package com.liuchad.zhuangbility.util;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class RxUtil {

    public static <T> ObservableTransformer<T, T> applyAsySchedulers() {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

//    /**
//     * 对 List<?> 列表中的元素进行过滤
//     *
//     * @param predicate 检查器
//     * @param <T>       列表中的元素类型
//     * @return 原列表类型
//     */
//    public static <T> ObservableTransformer<List<T>, List<T>> filter(final Predicate<T> predicate) {
//        return new ObservableTransformer<List<T>, List<T>>() {
//            @Override
//            public ObservableSource<List<T>> apply(Observable<List<T>> upstream) {
//                return upstream.flatMap(new Function<List<T>, ObservableSource<?>>() {
//                    @Override
//                    public ObservableSource<?> apply(List<T> ts) throws Exception {
//                        return Observable.fromIterable(ts);
//                    }
//                }).filter(predicate).toList();
//            }
//        };
//    }
//
//    /**
//     * 过滤 List<String> 中的空字符串
//     */
//    public static ObservableTransformer<List<String>, List<String>> filterEmptyString() {
//        return filter(new Func1<String, Boolean>() {
//            @Override
//            public Boolean call(String s) {
//                return !TextUtils.isEmpty(s);
//            }
//        });
//    }

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
