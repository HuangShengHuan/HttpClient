package com.wemind.net.client;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;
import android.util.Pair;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.wemind.net.BaseModel;
import com.wemind.net.Config;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Multipart;

/**
 * Created by HSH on 18-9-10.
 */
public class RetrofitHttpClient implements HttpClient {

    private Retrofit retrofit;
    private Http.Builder initBuilder;
    private Gson gson;

    @Override
    public void install(Http.Builder builder) {
        this.initBuilder = builder;
        gson = new GsonBuilder().serializeNulls().create();
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(Config.CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(Config.READ_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(Config.WRITE_TIMEOUT, TimeUnit.SECONDS)
                .retryOnConnectionFailure(Config.RETRY_CONNECTED)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(builder.baseUrl)
//                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().serializeNulls().create()))
                .client(okHttpClient)
                .build();
    }

    @SuppressWarnings({"unchecked"})
    private <T extends BaseModel> T parse(@NonNull Reader reader, @NonNull Type type) throws IOException, JsonIOException {
        JsonReader jsonReader = gson.newJsonReader(reader);
        TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
        T result = (T) adapter.read(jsonReader);
        if (jsonReader.peek() != JsonToken.END_DOCUMENT) {
            throw new JsonIOException("JSON document was not fully consumed.");
        }
        return result;
    }

    @Override
    public <T extends BaseModel> Observable<T> get(final Http.RequestBuilder builder) {
        checkRequest();
        return Observable.create(new ObservableOnSubscribe<T>() {
            @Override
            @SuppressWarnings({"unchecked"})
            public void subscribe(ObservableEmitter<T> e) throws Exception {
                final Call<ResponseBody> call = retrofit.create(RetrofitService.class).get(builder.headers, parseUrl(builder));
                e.setDisposable(new RequestDispose(call));
                Response<ResponseBody> response = call.execute();

                ResponseBody body = response.body();
                if (body != null) {
                    try {
                        T model = parse(body.charStream(), builder.type);
                        e.onNext(model);
                    } finally {
                        body.close();
                        e.onComplete();
                    }
                } else {
                    BaseModel model = (BaseModel) builder.type.newInstance();
                    model.code = response.code();
                    model.msg = response.message();
                    model.state = 0;
                    e.onNext((T) model);
                    e.onComplete();
                }
            }
        }).subscribeOn(Schedulers.io());
    }

    @Override
    public <T extends BaseModel> Observable<T> post(final Http.RequestBuilder builder) {
        checkRequest();
        return Observable.create(new ObservableOnSubscribe<T>() {
            @Override
            @SuppressWarnings({"unchecked"})
            public void subscribe(ObservableEmitter<T> e) throws Exception {

                Call<ResponseBody> call = retrofit.create(RetrofitService.class)
                        .post(builder.headers, builder.path, builder.params);
                e.setDisposable(new RequestDispose(call));
                Response<ResponseBody> response = call.execute();

                ResponseBody body = response.body();
                if (body != null) {
                    try {
                        T model = parse(body.charStream(), builder.type);
                        e.onNext(model);
                    } finally {
                        body.close();
                        e.onComplete();
                    }
                } else {
                    BaseModel model = (BaseModel) builder.type.newInstance();
                    model.code = response.code();
                    model.msg = response.message();
                    model.state = 0;
                    e.onNext((T) model);
                    e.onComplete();
                }
            }
        }).subscribeOn(Schedulers.io());
    }

    @Override
    public <T extends BaseModel> Observable<T> upload(final Http.RequestBuilder builder) {
        checkRequest();

        if (builder.files.size() == 0) {
            throw new RuntimeException("Upload file can not be empty!");
        }

        Pair<String, String> info = builder.files.keyAt(0);
        File file = builder.files.valueAt(0);

        final RequestBody description = RequestBody.create(MediaType.parse(info.first), info.second);

        RequestBody requestFile = RequestBody.create(MediaType.parse("application/otcet-stream"), file);
        final MultipartBody.Part body = MultipartBody.Part.createFormData("aFile", file.getName(), requestFile);

        return Observable.create(new ObservableOnSubscribe<T>() {
            @Override
            @SuppressWarnings({"unchecked"})
            public void subscribe(ObservableEmitter<T> e) throws Exception {
                Call<ResponseBody> call = retrofit.create(RetrofitService.class)
                        .upload(builder.headers, builder.path, description, body);
                e.setDisposable(new RequestDispose(call));
                Response<ResponseBody> response = call.execute();

                ResponseBody body = response.body();
                if (body != null) {
                    try {
                        T model = parse(body.charStream(), builder.type);
                        e.onNext(model);
                    } finally {
                        body.close();
                        e.onComplete();
                    }
                }
            }
        }).subscribeOn(Schedulers.io());
    }

    @Override
    public <T extends BaseModel> Observable<T> multiUpload(final Http.RequestBuilder builder) {
        checkRequest();

        if (builder.files.size() == 0) {
            throw new RuntimeException("Upload file can not be empty!");
        }

        MultipartBody.Builder partBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);

        for (Map.Entry<String, String> entry : builder.params.entrySet()) {
            partBuilder.addFormDataPart(entry.getKey(), entry.getValue());
        }

        for (Map.Entry<Pair<String, String>, File> entry : builder.files.entrySet()) {
            partBuilder.addFormDataPart(entry.getKey().second, entry.getValue().getName(),
                    RequestBody.create(MediaType.parse(entry.getKey().first), entry.getValue()));
        }

        final MultipartBody multipartBody = partBuilder.build();

        return Observable.create(new ObservableOnSubscribe<T>() {
            @Override
            @SuppressWarnings({"unchecked"})
            public void subscribe(ObservableEmitter<T> e) throws Exception {
                Call<ResponseBody> call = retrofit.create(RetrofitService.class)
                        .multiUpload(builder.headers, builder.path, multipartBody);
                e.setDisposable(new RequestDispose(call));
                Response<ResponseBody> response = call.execute();

                ResponseBody body = response.body();
                if (body != null) {
                    try {
                        T model = parse(body.charStream(), builder.type);
                        e.onNext(model);
                    } finally {
                        body.close();
                        e.onComplete();
                    }
                }
            }
        }).subscribeOn(Schedulers.io());
    }

    private void checkRequest() {
        if (retrofit == null) {
            throw new RuntimeException("retrofit client can not be null!");
        }

        if (initBuilder == null || TextUtils.isEmpty(initBuilder.baseUrl)) {
            throw new RuntimeException("Base Url can not be null!");
        }
    }

    private String parseUrl(Http.RequestBuilder builder) {
        StringBuilder sb = new StringBuilder(initBuilder.baseUrl);
        if (sb.toString().endsWith("/")) {
            sb.deleteCharAt(sb.length() - 1);
        }
        ArrayMap<String, String> params = builder.params;
        if (params.size() > 0) {
            sb.append("?");
            for (Map.Entry<String, String> entry : params.entrySet()) {
                sb.append(entry.getKey());
                sb.append("=");
                sb.append(entry.getValue());
                sb.append("&");
            }
            sb.deleteCharAt(sb.length() - 1);
        }

        return sb.toString();
    }
}
