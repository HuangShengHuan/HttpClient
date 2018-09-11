package com.wemind.net.client;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.ArrayMap;
import android.util.Pair;

import com.wemind.net.BaseModel;
import com.wemind.net.Config;

import java.io.File;
import java.util.HashMap;

import io.reactivex.Observable;

/**
 * Created by HSH on 18-9-10.
 */
public class Http {

    private static volatile Http instance;

    private HttpClient client;

    Http(Builder builder) {
        this.client = builder.client;
        install(builder);
    }

    public static Builder builder(){
        return new Builder();
    }

    private void install(Builder builder) {
        if (client == null) {
            throw new RuntimeException("HttpClient can not be null!");
        }
        client.install(builder);
        synchronized (Http.class) {
            instance = this;
        }
    }

    public static Http getInstance(){
        if (instance == null) {
            synchronized (Http.class) {
                if (instance == null) {
                    instance = new Http(new Builder());
                }
            }
        }
        return instance;
    }

    public synchronized <T extends BaseModel> Observable<T> get(RequestBuilder builder) {
        return client.get(builder);
    }

    public synchronized <T extends BaseModel> Observable<T> post(RequestBuilder builder) {
        return client.post(builder);
    }


    public synchronized <T extends BaseModel> Observable<T> upload(RequestBuilder builder) {
        return client.upload(builder);
    }

    public synchronized <T extends BaseModel> Observable<T> multiUpload(RequestBuilder builder) {
        return client.multiUpload(builder);
    }

    public synchronized Observable<Integer> download(RequestBuilder builder) {
        return client.download(builder);
    }

    public static class Builder{
        String baseUrl = Config.BASE_URL;

        HttpClient client = new RetrofitHttpClient();

        HashMap<String, String> headers = new HashMap<>();

        int connectTimeout = Config.CONNECT_TIMEOUT;

        int readTimeout = Config.READ_TIMEOUT;

        int writeTimeout = Config.WRITE_TIMEOUT;

        boolean retryOnFail = Config.RETRY_CONNECTED;

        public Builder baseUrl(@NonNull String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public Builder client(@NonNull HttpClient client) {
            this.client = client;
            return this;
        }

        public Builder headers(@NonNull String header, @NonNull String value) {
            headers.put(header, value);
            return this;
        }

        public Builder connectTimeout(int connectTimeout) {
            this.connectTimeout = connectTimeout;
            return this;
        }

        public Builder readTimeout(int readTimeout) {
            this.readTimeout = readTimeout;
            return this;
        }

        public Builder writeTimeout(int writeTimeout) {
            this.writeTimeout = writeTimeout;
            return this;
        }

        public Builder retryOnFail(boolean retryOnFail) {
            this.retryOnFail = retryOnFail;
            return this;
        }

        public Http create() {
            return new Http(this);
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static class RequestBuilder{

        String baseUrl = Config.BASE_URL;

        String path = "";

        ArrayMap<String, String> params = new ArrayMap<>();

        ArrayMap<String, String> headers = new ArrayMap<>();

        ArrayMap<Pair<String, String>, File> files = new ArrayMap<>();

        Class<?> type;

        String downloadUrl;
        String fileName;

        String downDestDir = Config.DOWNLOAD_DEST_DIR;

        public RequestBuilder baseUrl(@NonNull String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public RequestBuilder path(@NonNull String path) {
            this.path = path;
            return this;
        }

        public RequestBuilder destDir(@NonNull String downDestDir) {
            this.downDestDir = downDestDir;
            return this;
        }

        public RequestBuilder addParam(@NonNull String param, @NonNull String value) {
            params.put(param, value);
            return this;
        }

        public RequestBuilder addHeader(@NonNull String header, @NonNull String value) {
            headers.put(header, value);
            return this;
        }

        public RequestBuilder addFile(@NonNull String name, @NonNull String mediaType, @NonNull File file) {
            files.put(new Pair<>(mediaType, name), file);
            return this;
        }

        public <T extends BaseModel> Observable<T> get(Class<T> clazz) {
            type = clazz;
            System.out.println(clazz);
            return Http.getInstance().get(this);
        }

        public <T extends BaseModel> Observable<T> post(Class<T> clazz) {
            type = clazz;
            return Http.getInstance().post(this);
        }

        public <T extends BaseModel> Observable<T> upload(Class<T> clazz) {
            type = clazz;
            return Http.getInstance().upload(this);
        }

        public <T extends BaseModel> Observable<T> multiUpload(Class<T> clazz) {
            type = clazz;
            return Http.getInstance().multiUpload(this);
        }

        public Observable<Integer> download(String downloadUrl, String fileName) {
            this.downloadUrl = downloadUrl;
            this.fileName = fileName;
            return Http.getInstance().download(this);
        }
    }


}
