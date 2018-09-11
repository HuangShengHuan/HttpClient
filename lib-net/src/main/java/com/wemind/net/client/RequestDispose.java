package com.wemind.net.client;

import android.support.annotation.NonNull;

import io.reactivex.disposables.Disposable;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * Created by HSH on 18-9-11.
 */
public class RequestDispose implements Disposable {
    private Call<ResponseBody> call;
    private boolean isDispose;
    public RequestDispose(@NonNull Call<ResponseBody> call) {
        this.call = call;
    }

    @Override
    public void dispose() {
        if (call != null && !call.isCanceled()) {
            call.cancel();
            isDispose = true;
        }
    }

    @Override
    public boolean isDisposed() {
        return isDispose;
    }
}
