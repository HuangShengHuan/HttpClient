package com.wm.hsh.httpclient;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.wemind.net.client.Http;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Http.builder().baseUrl("https://api.github.com/login/").create();
    }

    public void test(View view) {
//        new Http.RequestBuilder()
//                .addParam("page","1")
//                .post(Model2.class)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<Model2>() {
//                    @Override
//                    public void accept(Model2 myModel) throws Exception {
//                        System.out.println("success");
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(Throwable throwable) throws Exception {
//
//                    }
//                });

        new Http.RequestBuilder()
                .get(MyModel.class)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<MyModel>() {
                    @Override
                    public void accept(MyModel myModel) throws Exception {
                        System.out.println("success");
                    }
                });

    }
}
