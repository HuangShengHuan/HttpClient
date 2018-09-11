package com.wemind.net.client;

import com.wemind.net.BaseModel;

import io.reactivex.Observable;

/**
 * Created by HSH on 18-9-11.
 */
public class OkHttpClient implements HttpClient {
    @Override
    public void install(Http.Builder builder) {

    }

    @Override
    public <T extends BaseModel> Observable<T> get(Http.RequestBuilder builder) {
        return null;
    }

    @Override
    public <T extends BaseModel> Observable<T> post(Http.RequestBuilder builder) {
        return null;
    }

    @Override
    public <T extends BaseModel> Observable<T> upload(Http.RequestBuilder builder) {
        return null;
    }

    @Override
    public <T extends BaseModel> Observable<T> multiUpload(Http.RequestBuilder builder) {
        return null;
    }
}
