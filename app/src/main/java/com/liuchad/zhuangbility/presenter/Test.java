package com.liuchad.zhuangbility.presenter;

import android.util.Log;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.liuchad.zhuangbility.TestActivity;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.AsyncSubject;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.ReplaySubject;
import io.reactivex.subjects.Subject;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by lchad on 2017/3/22.
 * Github: https://github.com/lchad
 */

public class Test {
    public static final String TAG = "RXJAVA";

    /**
     * concat() 连接操作
     */
    public static void testConcat() {
        Observable.concat(Observable.just(1, 2, 3, 4), Observable.just(5, 6, 7, 8))
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        System.out.println(TAG + " " + integer + "");
                    }
                });
    }

    /**
     * concat和merge的区别:concat不会交叉,merge可能会交叉
     */
    public static void testMerge() {
        Observable.merge(Observable.just(1, 2), Observable.just(3, 4)).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                System.out.println(TAG + " " + integer);
            }
        });
    }

    /**
     * 去重操作
     */
    public static void testDistinct() {
        Observable.just(1, 2, 3, 3, 3, 4, 4, 5, 6)
                .distinct()
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        System.out.println(TAG + " " + integer + "");
                    }
                });
    }

    /**
     * 筛选操作
     */
    public static void testFilter() {
        Observable.just(1, 2, 3, 4, 5, 6, 7, 8, 9)
                .filter(new Predicate<Integer>() {
                    @Override
                    public boolean test(Integer integer) throws Exception {
                        return integer > 5;
                    }
                }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                System.out.println(TAG + " " + integer + "");
            }
        });
    }

    /**
     * buffer() 每组三个,步长为2
     */
    public static void testBuffer() {
        Observable.just(1, 2, 3, 4, 5, 6, 7, 8, 9)
                .buffer(3, 2)
                .subscribe(new Consumer<List<Integer>>() {
                    @Override
                    public void accept(List<Integer> integers) throws Exception {
                        System.out.println(TAG + " " + integers.toString());
                    }
                });
    }

    /**
     * debounce() 仅在过了一段指定的时间还没发射数据时才发射一个数据
     */
    public static void testDebounce() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
//                e.onNext(1);
//                Thread.sleep(1000);
//                e.onNext(2);
//                Thread.sleep(400);
//                e.onNext(3);
//                Thread.sleep(1000);
//                e.onNext(4);
//                Thread.sleep(400);
//                e.onNext(5);
//                Thread.sleep(400);
//                e.onNext(6);
//                Thread.sleep(1000);

                for (int i = 0; i < 50; i++) {
                    e.onNext(i);
                    Thread.sleep(600);
                }

                e.onComplete();
            }
        })
                .debounce(500, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        System.out.println(TAG + " " + integer);
                    }
                });

    }

    /**
     * defer
     */
    public void testDefer() {
        Observable<Integer> observable = Observable.defer(new Callable<ObservableSource<Integer>>() {
            @Override
            public ObservableSource<Integer> call() throws Exception {
                return Observable.just(1, 2, 3);
            }
        });
        observable.subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                System.out.println(TAG + " " + integer);
            }
        });

        observable.subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                System.out.println(TAG + " " + integer);
            }
        });
    }

    /**
     * interval() 定时发射
     */
    public static void testInterval() {
        Observable.interval(1000, 500, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        System.out.println(TAG + " " + aLong);
                    }
                });
    }

    /**
     * last 取最后一个,没有值的话 默认值为0
     */
    public static void testLast() {
        Observable.just(1, 2, 4, 5).last(0).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                System.out.println(TAG + " " + integer);
            }
        });
    }

    /**
     * lastOrError 取最后一个,没有默认值
     */
    public static void testLastOrError() {
        Observable.just(1, 2, 4, 5).lastOrError().subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                System.out.println(TAG + " " + integer);
            }
        });
    }

    /**
     * reduce,和js里的reduce一样,依次用一个方法来处理
     */
    public void testReduce() {
        Observable.just(1, 2).reduce(new BiFunction<Integer, Integer, Integer>() {
            @Override
            public Integer apply(Integer integer, Integer integer2) throws Exception {
                return integer * integer2;
            }
        }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                System.out.println(TAG + " " + integer);
            }
        });
    }

    /**
     * scan() 和reduce类似,但是reduce只打印最终结果,scan()会打印所有中间过程的值.
     */
    public void testScan() {
        Observable.just(1, 2).scan(new BiFunction<Integer, Integer, Integer>() {
            @Override
            public Integer apply(Integer integer, Integer integer2) throws Exception {
                return integer * integer2;
            }
        }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                System.out.println(TAG + " " + integer);
            }
        });
    }
    //???replay() && cache()

    /**
     * skip() 跳过前面的 指定个数的值,也可以跳过时间
     */
    public void testSkip() {
        //跳过个数
        Observable.just(1, 2, 3, 4)
                .skip(2)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        System.out.println(TAG + " " + integer);
                    }
                });
        //跳过时间
        Observable.just(1, 2, 3, 4)
                .skip(1000, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        System.out.println(TAG + " " + integer);
                    }
                });
    }

    /**
     * take() takeWhile() takeUtil() takeLast() 取前几个,前一段时间的几个
     */
    public void testTake() {
        Observable.just(1, 2, 3, 4)
                .take(2)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        System.out.println(TAG + " " + integer);
                    }
                });
        Observable.just(1, 2, 3, 4)
                .takeLast(2)
                .takeUntil(new Predicate<Integer>() {
                    @Override
                    public boolean test(Integer integer) throws Exception {
                        return false;
                    }
                })
                .takeWhile(new Predicate<Integer>() {
                    @Override
                    public boolean test(Integer integer) throws Exception {
                        return false;
                    }
                })
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        System.out.println(TAG + " " + integer);
                    }
                });
        Observable.just(1, 2, 3, 4)
                .take(1000, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        System.out.println(TAG + " " + integer);
                    }
                });

    }

    /**
     * throttleFirst() 一段时间之内的第一个
     */
    public static void testThrottleFirst() {
        Observable.just(1, 2).throttleFirst(100, TimeUnit.MILLISECONDS).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                System.out.println(TAG + " " + integer);
            }
        });
    }
    // thrittlelast()源码里调用的就是sample()

    /**
     * timer() 定时任务
     */
    public static void testTimer() {
        Observable.timer(1, TimeUnit.SECONDS, AndroidSchedulers.mainThread()).subscribe(new Consumer<Long>() {
            @Override
            public void accept(Long aLong) throws Exception {
                System.out.println(TAG + " " + aLong);
            }
        });
    }

    /**
     * window() 按照时间划分窗口, 将数据发送给不同的Observable
     */
    public static void testWindow() {
        Observable.interval(9, TimeUnit.SECONDS)
                .take(9)
                .window(3, TimeUnit.SECONDS)
                .subscribe(new Consumer<Observable<Long>>() {
                    @Override
                    public void accept(Observable<Long> longObservable) throws Exception {
                        longObservable.subscribe(new Consumer<Long>() {
                            @Override
                            public void accept(Long aLong) throws Exception {
                                System.out.println(TAG + " " + aLong);
                            }
                        });
                    }
                });
    }

    /**
     * zip() 压合, 成对儿的,如果不能成对就丢弃
     */
    public static void testZip() {
        Observable.zip(Observable.just(1, 2, 3), Observable.just("a", "b", "c", "d"), new BiFunction<Integer, String, String>() {
            @Override
            public String apply(Integer integer, String s) throws Exception {
                return integer + s;
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                System.out.println(TAG + " " + s);
            }
        });
    }

    /**
     * PublishSubject 每个观察者都会收到通知
     */
    public static void testPublishSubject() {
        PublishSubject<Integer> publishSubject = PublishSubject.create();
        publishSubject.subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
                System.out.println(TAG + " " + "onSubscribe()");
            }

            @Override
            public void onNext(Integer value) {
                System.out.println(TAG + " " + value);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                System.out.println(TAG + " " + "onComplete()");
            }
        });
        publishSubject.onNext(0);

        publishSubject.subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
                System.out.println(TAG + " " + "onSubscribe()");
            }

            @Override
            public void onNext(Integer value) {
                System.out.println(TAG + " " + value);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                System.out.println(TAG + " " + "onComplete()");
            }
        });

        publishSubject.onNext(1);

        publishSubject.onComplete();
    }

    /**
     * Completable
     */
    public static void testCompletable() {
        Completable completable = Completable.timer(1, TimeUnit.SECONDS);
        completable.subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onComplete() {

            }

            @Override
            public void onError(Throwable e) {

            }
        });
    }

    public static void testRetryWhen() {
        Observable.just(0)
                .retryWhen(new Function<Observable<? extends Throwable>, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(Observable<? extends Throwable> throwableObservable) throws Exception {
                        return throwableObservable.flatMap(new Function<Throwable, ObservableSource<?>>() {
                            @Override
                            public ObservableSource<?> apply(Throwable throwable) throws Exception {
                                if (throwable instanceof IllegalArgumentException) {
                                    return Observable.just(1)
                                            .doOnNext(new Consumer<Integer>() {
                                                @Override
                                                public void accept(Integer integer) throws Exception {

                                                }
                                            });
                                }
                                return Observable.error(throwable);
                            }
                        });
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {

                    }
                });
    }

    /**
     * Subject:既可以是Observable也可以是Observer.
     * AsyncSubject 在 调用 onComplete() 之前，除了 subscribe() 其它的操作都会被缓存，在调用 onComplete() 之后只有最后一个 onNext() 会生效
     * PublishSubject 只会把在订阅发生的时间点之后来自原始Observable的数据发射给观察者
     * ReplaySubject 会发射所有来自原始Observable的数据给观察者，无论它们是何时订阅的
     * BehaviorSubject 会发射订阅之前最近的一个数据,之后一直发送直到onComplete().
     */
    public static void testAsyncSubject() {
        AsyncSubject<Integer> asyncSubject = AsyncSubject.create();
        asyncSubject.onNext(1);
        asyncSubject.onNext(2);
        asyncSubject.onNext(3);
        asyncSubject.onNext(4);

        asyncSubject.subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer value) throws Exception {
                System.out.println(TAG + " " + value);
            }
        });
        asyncSubject.onNext(5);
        asyncSubject.onNext(6);
        asyncSubject.onNext(7);
        asyncSubject.onNext(8);
        asyncSubject.onComplete();
    }

    public static void testPublishSubject2() {
        PublishSubject<Integer> publishSubject = PublishSubject.create();
        publishSubject.onNext(1);
        publishSubject.onNext(2);
        publishSubject.onNext(3);

        publishSubject.subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                System.out.println(TAG + " " + integer);
            }
        });
        publishSubject.onNext(4);
        publishSubject.onNext(5);
        publishSubject.onNext(6);
        publishSubject.onComplete();
    }

    public static void testReplaySubject() {
        ReplaySubject<Integer> replaySubject = ReplaySubject.create();
        replaySubject.onNext(1);
        replaySubject.onNext(2);
        replaySubject.onNext(3);
        replaySubject.onNext(4);

        replaySubject.subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                System.out.println(TAG + " " + integer);
            }
        });

        replaySubject.onNext(5);
        replaySubject.onNext(6);
        replaySubject.onNext(7);
        replaySubject.onComplete();
    }

    public static void testBehaviorSubject() {
        BehaviorSubject<Integer> behaviorSubject = BehaviorSubject.create();
        behaviorSubject.onNext(1);
        behaviorSubject.onNext(2);
        behaviorSubject.onNext(3);

        behaviorSubject.subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                System.out.println(TAG + " " + integer);
            }
        });

        behaviorSubject.onNext(4);
        behaviorSubject.onNext(5);
        behaviorSubject.onNext(6);
        behaviorSubject.subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer value) throws Exception {
                System.out.println(TAG + " " + value);
            }
        });

        behaviorSubject.onNext(7);
        behaviorSubject.onComplete();
    }

    public static void main(String[] args) {
        OkHttpClient okHttpClient = new OkHttpClient();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.account.ipalmap.com")
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Api api = retrofit.create(Api.class);
        api.search("palmap_open", "password", "799505946@qq.com", "zhiluji123123")
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String value) {
                        Log.d(TAG, value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "complete");
                    }
                });
    }

    interface Api {
        @GET("auth/realms/master/protocol/openid-connect/token")
        Observable<String> search(@Query("client_id") String clientId,
                                  @Query("grant_type") String pass,
                                  @Query("username") String username,
                                  @Query("password") String password);
    }

    //Rxjava 2.0之后不允许传递null了.

}
