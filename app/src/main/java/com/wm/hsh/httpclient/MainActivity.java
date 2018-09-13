package com.wm.hsh.httpclient;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.wemind.net.client.Http;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private Disposable disposable;

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

//        Disposable disposable = new Http.RequestBuilder()
//                .get(MyModel.class)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<MyModel>() {
//                    @Override
//                    public void accept(MyModel myModel) throws Exception {
//                        System.out.println("success");
//                    }
//                });

        System.out.println(getExternalCacheDir().getAbsolutePath());
        disposable = new Http.RequestBuilder()
                .destDir(getExternalCacheDir().getAbsolutePath())
                .download("http://mirrors.hust.edu.cn/apache/tomcat/tomcat-7/v7.0.90/bin/apache-tomcat-7.0.90.tar.gz", "apache-tomcat-7.0.90.tar.gz")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        TextView progress = findViewById(R.id.progress);
                        progress.setText("progress:" + integer);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                });

    }

    public void Dispose(View view) {
        disposable.dispose();
    }
}
