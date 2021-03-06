package com.liuchad.zhuangbility.network.api;

import com.liuchad.zhuangbility.vo.RemoteImage;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RemoteImageApi {
    @GET("search")
    Observable<List<RemoteImage>> search(@Query("q") String query);

    @GET("search")
    Single<List<RemoteImage>> search1(@Query("q") String query);
}
