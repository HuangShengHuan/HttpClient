package com.wemind.net.client;

import com.wemind.net.BaseModel;

import io.reactivex.Observable;
import okhttp3.ResponseBody;

/**
 * Created by HSH on 18-9-10.
 */
public interface HttpClient {
    void install(Http.Builder builder);

    <T extends BaseModel> Observable<T> get(Http.RequestBuilder builder);

    <T extends BaseModel> Observable<T> post(Http.RequestBuilder builder);

    <T extends BaseModel> Observable<T> upload(Http.RequestBuilder builder);

    <T extends BaseModel> Observable<T> multiUpload(Http.RequestBuilder builder);

    Observable<Integer> download(Http.RequestBuilder builder);
}
